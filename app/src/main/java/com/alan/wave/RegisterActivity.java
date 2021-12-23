package com.alan.wave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    Button button;
    private FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword, editTextPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.registerBtn);
        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText)findViewById(R.id.rpassword);
        editTextPasswordConfirm = (EditText)findViewById(R.id.rpassword2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();

            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButtonRegister);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String ConfirmPassword = editTextPasswordConfirm.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if(ConfirmPassword.isEmpty()){
            editTextPasswordConfirm.setError("Confirm password!");
            editTextPasswordConfirm.requestFocus();
            return;
        }

        if(!(ConfirmPassword.equals(password))){
            editTextPasswordConfirm.setError("Passwords don't match!");
            editTextPasswordConfirm.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){ //checks if user provides valid email
            editTextEmail.setError("Invalid Email!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.length() < 6) {  //firebase don't except passwords < 6
        editTextPassword.setError("Min password should be 6 characters");
        editTextPassword.requestFocus();
        return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){        //if the user has successfully been registered
                            User user = new User(email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "User has been registered", Toast.LENGTH_LONG).show();

                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
