package com.example.quiz_app_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button startBtn,bookmarkBtn,adminBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.start_btn);
        bookmarkBtn = findViewById(R.id.bookmarks_btn);
        adminBtn = findViewById(R.id.admin_btn);


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent catagoryIntent = new Intent(MainActivity.this, com.example.quiz_app_java.CategoriesActivity.class);
                startActivity(catagoryIntent);
            }
        });

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookmarksIntent = new Intent(MainActivity.this, com.example.quiz_app_java.BookmarkActivity.class);
                startActivity(bookmarksIntent);
            }
        });
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookmarksIntent = new Intent(MainActivity.this, com.example.quiz_app_java.MainActivityAdmin.class);
                startActivity(bookmarksIntent);
            }
        });
    }

}