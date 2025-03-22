package com.example.pincat.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pincat.R;
import com.example.pincat.model.CatImage;

import java.util.List;

public class CatImageAdapter extends RecyclerView.Adapter<CatImageAdapter.CatImageViewHolder> {
    private List<CatImage> catImages;

    // Конструктор, принимающий список catImages
    public CatImageAdapter(List<CatImage> catImages) {
        this.catImages = catImages;
    }

    // Метод для обновления списка catImages
    public void setCatImages(List<CatImage> catImages) {
        this.catImages = catImages;
        notifyDataSetChanged(); // Обновляем RecyclerView
    }

    @NonNull
    @Override
    public CatImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cat_image, parent, false);
        return new CatImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatImageViewHolder holder, int position) {
        CatImage catImage = catImages.get(position);
        Glide.with(holder.itemView.getContext())
                .load(catImage.getUrl())
                .into(holder.catImageView);

        holder.catImageView.setOnClickListener(v -> {
            downloadImage(holder.itemView.getContext(), catImage.getUrl());
        });
    }

    @Override
    public int getItemCount() {
        return catImages.size();
    }

    private void downloadImage(Context context, String imageUrl) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Downloading Cat Image");
        request.setDescription("Downloading image from CatAPI");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "cat_image_" + System.currentTimeMillis() + ".jpg");

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

    public static class CatImageViewHolder extends RecyclerView.ViewHolder {
        ImageView catImageView;

        public CatImageViewHolder(@NonNull View itemView) {
            super(itemView);
            catImageView = itemView.findViewById(R.id.catImageView);
        }
    }
}
