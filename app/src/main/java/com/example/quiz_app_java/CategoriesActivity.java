package com.example.quiz_app_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycleView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        List<CategoryModel> list = new ArrayList<>();
        list.add(new CategoryModel("", "Category1"));
        list.add(new CategoryModel("", "Category1"));
        list.add(new CategoryModel("", "Category1"));
        list.add(new CategoryModel("", "Category1"));
        list.add(new CategoryModel("", "Category1"));
        list.add(new CategoryModel("", "Category1"));
        list.add(new CategoryModel("", "Category1"));
        list.add(new CategoryModel("", "Category1"));
        list.add(new CategoryModel("", "Category1"));
        CategoryAdapter adapter = new CategoryAdapter(list);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}