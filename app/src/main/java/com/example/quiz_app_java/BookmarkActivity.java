package com.example.quiz_app_java;

import static com.example.quiz_app_java.R.id.toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
//import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookmarkActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.rv_bookmarks);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        List<QuestionModel> list = new ArrayList<>();
        list.add(new QuestionModel("what is your name?","","","","","Shivamanu",0));
        list.add(new QuestionModel("what is your name?","","","","","Shivamanu",0));
        list.add(new QuestionModel("what is your name?","","","","","Shivamanu",0));
        list.add(new QuestionModel("what is your name?","","","","","Shivamanu",0));
        list.add(new QuestionModel("what is your name?","","","","","Shivamanu",0));
        list.add(new QuestionModel("what is your name?","","","","","Shivamanu",0));
        list.add(new QuestionModel("what is your name?","","","","","Shivamanu",0));
        list.add(new QuestionModel("what is your name?","","","","","Shivamanu",0));


        BookmarksAdapter adapter = new BookmarksAdapter(list);
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