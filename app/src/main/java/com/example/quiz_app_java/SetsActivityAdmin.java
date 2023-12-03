package com.example.quiz_app_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;

public class SetsActivityAdmin extends AppCompatActivity {

    private GridView gridView;
    private Dialog loadingadminDialog;
    private GridAdaptorAdmin adaptor;
    private String categoryName;
    private DatabaseReference myRef;
    private List<String> sets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadingadminDialog = new Dialog(this);
        loadingadminDialog.setContentView(R.layout.loadingadmin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingadminDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        }
        loadingadminDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingadminDialog.setCancelable(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoryName = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(categoryName);

        gridView = findViewById(R.id.gridview);
        myRef = FirebaseDatabase.getInstance().getReference();

        sets = CategoryActivityAdmin.list.get(getIntent().getIntExtra("position", 0)).getSets();
        adaptor = new GridAdaptorAdmin(sets,getIntent().getStringExtra("title"), new GridAdaptorAdmin.GridListner() {
            @Override
            public void addSet() {

                loadingadminDialog.show();

                String id = UUID.randomUUID().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference().child("Categories").child(getIntent().getStringExtra("key")).child("sets").child(id).setValue("SET ID").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            sets.add(id);
                            adaptor.notifyDataSetChanged();
                        } else {
                            Toast.makeText(SetsActivityAdmin.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                        loadingadminDialog.dismiss();
                    }
                });

            }

            @Override
            public void onLongCLick(final String setId, int position) {

                new AlertDialog.Builder(SetsActivityAdmin.this)
                        .setTitle("Delete SET "+ position)
                        .setMessage("Are you sure, you want to delete this SET?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadingadminDialog.show();
                                myRef
                                        .child("SETS").child(setId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    myRef.child("Categories").child(CategoryActivityAdmin.list.get(getIntent().getIntExtra("position", 0)).getKey())
                                                            .child("sets").child(setId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        sets.remove(setId);
                                                                        adaptor.notifyDataSetChanged();
                                                                    }
                                                                    else {
                                                                        Toast.makeText(SetsActivityAdmin.this,"Something went wrong",Toast.LENGTH_LONG).show();
                                                                    }
                                                                    loadingadminDialog.dismiss();
                                                                }
                                                            });

                                                } else {
                                                    Toast.makeText(SetsActivityAdmin.this,"Something went wrong", Toast.LENGTH_LONG).show();
                                                    loadingadminDialog.dismiss();
                                                }
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("cencel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        gridView.setAdapter(adaptor);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
