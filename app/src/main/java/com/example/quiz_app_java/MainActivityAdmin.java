package com.example.quiz_app_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityAdmin extends AppCompatActivity {

    private EditText email,password;
    private Button login,register;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register_btn);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = new Intent(this,CategoryActivityAdmin.class);

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(intent);
            finish();
            return;
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityAdmin.this);
                builder.setMessage("questkiddo@gmail.com");
                builder.setTitle("Contact owner to get credentials.        It takes atleast 24hr.");
                builder.setCancelable(false);
                builder.setPositiveButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    finish();
                });
                builder.setNegativeButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()){
                    email.setError("required");
                    return;
                }
                else{
                    email.setError(null);
                }

                if(password.getText().toString().isEmpty()){
                    password.setError("required");
                    return;
                }
                else{
                    password.setError(null);
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivityAdmin.this, "Failed!", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }
}
