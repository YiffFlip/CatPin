package com.example.pincat.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.pincat.R;
import com.example.pincat.model.CatImage;
import java.util.List;

public class CatImageAdapter extends RecyclerView.Adapter<CatImageAdapter.ViewHolder> {
    private List<CatImage> catImages;

    public CatImageAdapter(List<CatImage> catImages) {
        this.catImages = catImages;
    }

    public void setCatImages(List<CatImage> catImages) {
        this.catImages = catImages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cat_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CatImage catImage = catImages.get(position);

        Glide.with(holder.itemView.getContext())
                .load(catImage.getUrl())
                .into(holder.catImageView);

        if (catImage.getBreeds() != null && !catImage.getBreeds().isEmpty()) {
            holder.breedTextView.setText(catImage.getBreeds().get(0).getName());
        } else {
            holder.breedTextView.setText("Unknown breed");
        }

        holder.itemView.setOnClickListener(v -> downloadImage(v.getContext(), catImage.getUrl()));
    }

    @Override
    public int getItemCount() {
        return catImages != null ? catImages.size() : 0;
    }

    private void downloadImage(Context context, String imageUrl) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Downloading Cat Image");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                "cat_" + System.currentTimeMillis() + ".jpg"
        );

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView catImageView;
        TextView breedTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImageView = itemView.findViewById(R.id.catImageView);
            breedTextView = itemView.findViewById(R.id.breedTextView);
        }
    }
}