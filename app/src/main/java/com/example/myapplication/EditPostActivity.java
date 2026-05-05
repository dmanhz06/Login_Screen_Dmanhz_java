package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class EditPostActivity extends AppCompatActivity {

    private TextInputEditText etEditContent;
    private Button btnSave, btnCancel;
    private int postPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        etEditContent = findViewById(R.id.etEditContent);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        Intent intent = getIntent();
        if (intent != null) {
            String currentContent = intent.getStringExtra("POST_CONTENT");
            postPosition = intent.getIntExtra("POST_POSITION", -1);
            etEditContent.setText(currentContent);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedContent = etEditContent.getText().toString().trim();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("UPDATED_CONTENT", updatedContent);
                resultIntent.putExtra("POST_POSITION", postPosition);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}