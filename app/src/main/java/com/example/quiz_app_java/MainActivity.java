package com.example.quiz_app_java;

//import static com.example.quiz_app_java.R.id.bookmarks_btn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private  Button startBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startBtn = findViewById(R.id.startBtn);
        Button bookmarkBtn = findViewById(R.id.bookmarks_btn);

        startBtn.setOnClickListener(v -> {
            Intent categoriesIntent= new Intent(MainActivity.this, CategoriesActivity.class);
            startActivity(categoriesIntent);
        });
        bookmarkBtn.setOnClickListener(v -> {
            Intent bookmarksIntent = new Intent(MainActivity.this, BookmarkActivity.class);
            startActivity(bookmarksIntent);
        });
    }
}
