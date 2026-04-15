package com.example.myapplication;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignIn;
    private TextView linkRegister;

    String registeredName;
    String registeredEmail;
    String registeredPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        linkRegister = findViewById(R.id.linkRegister);

        Intent registerIntent = getIntent();

        if (registerIntent != null) {
            registeredName = registerIntent.getStringExtra("KEY_NAME");
            registeredEmail = registerIntent.getStringExtra("KEY_EMAIL");
            registeredPassword = registerIntent.getStringExtra("KEY_PASSWORD");

            if (registeredEmail == null) registeredEmail = "";
            if (registeredPassword == null) registeredPassword = "";
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = etEmail.getText().toString().trim();
                String loginPassword = etPassword.getText().toString().trim();

                if (loginEmail.isEmpty() || loginPassword.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill all information!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (loginEmail.equals(registeredEmail) && loginPassword.equals(registeredPassword)) {
                    Intent intent = new Intent(Login.this, Profile.class);
                    intent.putExtra("KEY_NAME", registeredName);
                    intent.putExtra("KEY_EMAIL", registeredEmail);
                    intent.putExtra("KEY_PASSWORD", registeredPassword);
                    Toast.makeText(Login.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Login.this, "Your email or password is wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}