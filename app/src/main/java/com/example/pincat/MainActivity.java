package com.example.pincat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.pincat.adapter.CatImageAdapter;
import com.example.pincat.viewmodel.CatViewModel;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;

public class MainActivity extends AppCompatActivity {
    private CatViewModel catViewModel;
    private RecyclerView recyclerView;
    private CatImageAdapter catImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        catImageAdapter = new CatImageAdapter(new ArrayList<>());
        recyclerView.setAdapter(catImageAdapter);

        catViewModel = new ViewModelProvider(this).get(CatViewModel.class);
        catViewModel.getCatImages().observe(this, catImages -> {
            catImageAdapter.setCatImages(catImages);
        });

        catViewModel.fetchRandomCatImages(10);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int[] lastVisibleItems = layoutManager.findLastVisibleItemPositions(null);
                    int lastVisibleItem = Math.max(lastVisibleItems[0], lastVisibleItems[1]);

                    if (lastVisibleItem >= catImageAdapter.getItemCount() - 5) {
                        catViewModel.loadMoreCatImages(10);
                    }
                }
            }
        });
    }
}