package com.example.pincat;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.pincat.adapter.CatImageAdapter;
import com.example.pincat.viewmodel.CatViewModel;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private CatViewModel viewModel;
    private CatImageAdapter adapter;
    private TextInputEditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация UI
        searchInput = findViewById(R.id.searchEditText);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        adapter = new CatImageAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Инициализация ViewModel
        viewModel = new ViewModelProvider(this).get(CatViewModel.class);
        viewModel.fetchAllBreeds();

        // Наблюдатели
        viewModel.getCatImages().observe(this, images ->
                adapter.setCatImages(images)
        );

        viewModel.getErrorMessage().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        );

        // Обработчик поиска
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = v.getText().toString().trim();
                if(!query.isEmpty()) {
                    viewModel.searchByBreedName(query, 10);
                }
                return true;
            }
            return false;
        });

        // Бесконечная прокрутка
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                StaggeredGridLayoutManager layoutManager =
                        (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

                int[] lastPositions = layoutManager.findLastVisibleItemPositions(null);
                int lastVisible = Math.max(lastPositions[0], lastPositions[1]);

                if(lastVisible >= adapter.getItemCount() - 5) {
                    viewModel.loadMoreImages(10);
                }
            }
        });

        // Первоначальная загрузка
        viewModel.loadInitialData(10);
    }
}