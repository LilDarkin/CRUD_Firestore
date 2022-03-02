package com.example.rawr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextInputLayout inputID = findViewById(R.id.txtUsername);
        TextInputLayout inputPass = findViewById(R.id.txtPassword);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String AdminID = inputID.getEditText().getText().toString().trim();
                String AdminPass = inputPass.getEditText().getText().toString().trim();

                if (AdminID.equals("") && AdminPass.equals("")) {
                    Toast.makeText(Login.this, "Verified Admin", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Login.this, MainActivity.class));
                } else {
                    Toast.makeText(Login.this, "Unverified! Who're you? Hmm.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}