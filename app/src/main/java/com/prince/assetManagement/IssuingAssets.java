package com.prince.assetManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IssuingAssets extends AppCompatActivity {
    TextView available_quantity_field, remaining_quantity_field;
    EditText department_name_field, room_field, issue_quantity_field, issued_to_field;
    Button next;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = IssuingAssets.class.getName();
//    ArrayList<String> document_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issuing_assets);
        available_quantity_field = findViewById(R.id.total_quantity_available);
        department_name_field = findViewById(R.id.department);
        room_field = findViewById(R.id.room_number);
        issue_quantity_field = findViewById(R.id.issue_quantity);
        remaining_quantity_field = findViewById(R.id.remaining_quantity);
        next = findViewById(R.id.next);
        issued_to_field = findViewById(R.id.issued_to);


        String document= getIntent().getStringExtra("document_id");
        final ArrayList<String> document_list = new ArrayList<>(Arrays.asList(document.split(",")));


        FirebaseApp.initializeApp(getApplicationContext());
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String id = getIntent().getStringExtra("id");

        final int total_quantity = getIntent().getIntExtra("totalQuantity",0);
        Log.e(TAG, "Total quantity getting is" + total_quantity );
        available_quantity_field.setText(String.valueOf(total_quantity));
        String department = department_name_field.getText().toString();
        String room_number = room_field.getText().toString();
//        int quantity_issued = Integer.parseInt(str_quantity_issued);


        issue_quantity_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    String str_quantity_issued = issue_quantity_field.getText().toString();
                    int quantity_issued = Integer.parseInt(str_quantity_issued);
                    int remaining_quantity = total_quantity - quantity_issued;
                    remaining_quantity_field.setText(String.valueOf(remaining_quantity));
                }
                catch (Exception e){
                    Log.e(TAG, "onTextChanged: Exception Occurred!" );
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
//        int remaining_quantity = total_quantity - quantity_issued;
//        remaining_quantity_field.setText(String.valueOf(remaining_quantity));

//        Toast.makeText(this, "Total Quantity is" + total_quantity, Toast.LENGTH_SHORT).show();

        final String detectedObject = getIntent().getStringExtra("detectedObject");
//        Toast.makeText(this, "Document list is " + document_list.toString(), Toast.LENGTH_SHORT).show();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IssuingAssets.this, GenerateQR.class);
                intent.putExtra("detectedObject", detectedObject);
                intent.putExtra("id", id);
                intent.putStringArrayListExtra("Document IDs",document_list);
                startActivity(intent);
            }
        });
    }
}
