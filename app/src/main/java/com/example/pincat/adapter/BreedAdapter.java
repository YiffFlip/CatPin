package com.example.pincat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.pincat.model.CatImage;
import java.util.List;

public class BreedAdapter extends ArrayAdapter<CatImage.Breed> {
    public BreedAdapter(Context context, List<CatImage.Breed> breeds) {
        super(context, android.R.layout.simple_dropdown_item_1line, breeds);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        CatImage.Breed breed = getItem(position);
        if (breed != null) {
            textView.setText(breed.getName());
        }
        return convertView;
    }
}
