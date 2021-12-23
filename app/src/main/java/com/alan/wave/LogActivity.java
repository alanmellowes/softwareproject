package com.alan.wave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    EditText TextEmail, TextPassword;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);


        button = (Button) findViewById(R.id.loginButton2);
        button.setOnClickListener(this);

        TextEmail = (EditText) findViewById(R.id.email);
        TextPassword = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.floatingActionButtonRegister:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerBtn:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.loginButton2:
                userLogin();
                break;

        }
    }

    private void userLogin(){
        String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();

        if(email.isEmpty()){
            TextEmail.setError("Email is required!");
            TextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            TextPassword.setError("Password is required!");
            TextPassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){ //checks if user provides valid email
            TextEmail.setError("Invalid Email!");
            TextEmail.requestFocus();
            return;
        }

        if(password.length() < 6) {  //firebase don't except passwords < 6
            TextPassword.setError("Min password should be 6 characters");
            TextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LogActivity.this, featuresActivity.class));
                }else{
                    Toast.makeText(LogActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

