package com.example.quiz_app_java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class QuestionsActivityAdmin extends AppCompatActivity {

    private Button add,excel;
    private RecyclerView recyclerView;
    private QuestionsAdapter adapter;
    public static List<QuestionModelAdmin> list;
    private Dialog loadingadminDialog;
    private DatabaseReference myRef;
    private String categoryName;
    private TextView loadingadminText;
    private String setId;
    public static final int CELL_COUNT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);

        myRef = FirebaseDatabase.getInstance().getReference();

        loadingadminDialog = new Dialog(this);
        loadingadminDialog.setContentView(R.layout.loadingadmin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingadminDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        }
        loadingadminDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingadminDialog.setCancelable(false);
        loadingadminText = loadingadminDialog.findViewById(R.id.textView);


        setSupportActionBar(toolbar);

        categoryName = getIntent().getStringExtra("category");
        setId = getIntent().getStringExtra("setId");
        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add = findViewById(R.id.add_btn);
        excel = findViewById(R.id.excel_btn);
        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new QuestionsAdapter(list, categoryName, new QuestionsAdapter.DeleteListener() {
            @Override
            public void onLongClick(int position, final String id) {
                new AlertDialog.Builder(QuestionsActivityAdmin.this)
                        .setTitle("Delete Question")
                        .setMessage("Are you sure, you want to delete this question?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadingadminDialog.show();
                                myRef.child("SETS").child(setId).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            list.remove(position);
                                            adapter.notifyItemRemoved(position);
                                        }
                                        else {
                                            Toast.makeText(QuestionsActivityAdmin.this, "Failed to delete",Toast.LENGTH_LONG).show();
                                        }
                                        loadingadminDialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("cencel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);

        getData(categoryName,setId);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addquestion = new Intent(QuestionsActivityAdmin.this,AddQuestionActivity.class);
                addquestion.putExtra("catgoryName", categoryName);
                addquestion.putExtra("setId", setId);
                startActivity(addquestion);
            }
        });


        excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(QuestionsActivityAdmin.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    selectFile();

                }
                else {
                    ActivityCompat.requestPermissions(QuestionsActivityAdmin.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectFile();
            }
            else {
                Toast.makeText(this, "Please Grant permissions!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void selectFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,"Select File"), 102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102){
            if (resultCode == RESULT_OK){
                String filePath = data.getData().getPath();
                if (filePath.endsWith("." +
                        "xlsx")){
                    readFile(data.getData());
                }
                else {
                    Toast.makeText(this, "Please choose an excel file!", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData(String categoryName, final String setId){
        loadingadminDialog.show();
        myRef
                .child("SETS").child(setId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            String id = dataSnapshot1.getKey();
                            String question = dataSnapshot1.child("question").getValue().toString();
                            String a = dataSnapshot1.child("optionA").getValue().toString();
                            String b = dataSnapshot1.child("optionB").getValue().toString();
                            String c = dataSnapshot1.child("optionC").getValue().toString();
                            String d = dataSnapshot1.child("optionD").getValue().toString();
                            String correctANS = dataSnapshot1.child("correctANS").getValue().toString();

                            list.add(new QuestionModelAdmin(id,question,a,b,c,d,correctANS,setId));
                        }
                        loadingadminDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QuestionsActivityAdmin.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                        loadingadminDialog.dismiss();
                        finish();
                    }
                });
    }

    private void readFile(Uri fileUri){
        loadingadminText.setText("Scanning Questions....");
        loadingadminDialog.show();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                HashMap<String,Object> parentMap = new HashMap<>();
                List<QuestionModelAdmin> tempList = new ArrayList<>();

                try {
                    InputStream inputStream = getContentResolver().openInputStream(fileUri);
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

                    int rowsCount = sheet.getPhysicalNumberOfRows();

                    if (rowsCount > 0){
                        for (int r = 0;r < rowsCount;r++){
                            Row row = sheet.getRow(r);

                            if (row.getPhysicalNumberOfCells() == CELL_COUNT){

                                String question = getCellData(row,0,formulaEvaluator);
                                String a = getCellData(row,1,formulaEvaluator);
                                String b = getCellData(row,2,formulaEvaluator);
                                String c = getCellData(row,3,formulaEvaluator);
                                String d = getCellData(row,4,formulaEvaluator);
                                String correctANS = getCellData(row,5,formulaEvaluator);

                                if (correctANS.equals(a) || correctANS.equals(b) || correctANS.equals(c) || correctANS.equals(d)){

                                    HashMap<String,Object> questionMap = new HashMap<>();
                                    questionMap.put("question",question);
                                    questionMap.put("optionA", a);
                                    questionMap.put("optionB", b);
                                    questionMap.put("optionC", c);
                                    questionMap.put("optionD", d);
                                    questionMap.put("correctANS", correctANS);
                                    questionMap.put("setId", setId);

                                    String id = UUID.randomUUID().toString();

                                    parentMap.put(id,questionMap);

                                    tempList.add(new QuestionModelAdmin(id,question,a,b,c,d,correctANS,setId));

                                }
                                else{
                                    int finalR = r;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadingadminText.setText("Loading...");
                                            loadingadminDialog.dismiss();
                                            Toast.makeText(QuestionsActivityAdmin.this, "Row no. "+(finalR +1)+" has no correct option", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    return;
                                }

                            }
                            else {
                                int finalR1 = r;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingadminText.setText("Loading...");
                                        loadingadminDialog.dismiss();
                                        Toast.makeText(QuestionsActivityAdmin.this, "Row no. "+(finalR1 +1)+" has incorrect data", Toast.LENGTH_LONG).show();
                                    }
                                });
                                return;
                            }

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingadminText.setText("Uploading....");

                                FirebaseDatabase.getInstance().getReference()
                                        .child("SETS").child(setId).updateChildren(parentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    list.addAll(tempList);
                                                    adapter.notifyDataSetChanged();

                                                }
                                                else {
                                                    loadingadminText.setText("Loading...");
                                                    Toast.makeText(QuestionsActivityAdmin.this,"Something went wrong!",Toast.LENGTH_LONG).show();
                                                }
                                                loadingadminDialog.dismiss();
                                            }
                                        });

                            }
                        });

                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingadminText.setText("Loading...");
                                loadingadminDialog.dismiss();
                                Toast.makeText(QuestionsActivityAdmin.this, "File is empty!", Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingadminText.setText("Loading...");
                            loadingadminDialog.dismiss();
                            Toast.makeText(QuestionsActivityAdmin.this, e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingadminText.setText("Loading...");
                            loadingadminDialog.dismiss();
                            Toast.makeText(QuestionsActivityAdmin.this, e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }


    private String getCellData(Row row,int cellPostion,FormulaEvaluator formulaEvaluator){

        String value = "";

        Cell cell = row.getCell(cellPostion);

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return value + cell.getBooleanCellValue();

            case Cell.CELL_TYPE_NUMERIC:
                return value + cell.getNumericCellValue();

            case Cell.CELL_TYPE_STRING:
                return value + cell.getStringCellValue();

            default:
                return value;

        }


    }
}
