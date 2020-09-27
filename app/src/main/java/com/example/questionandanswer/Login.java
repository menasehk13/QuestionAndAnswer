package com.example.questionandanswer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
     EditText email,password;
     Button Login;
     TextView register;
     FirebaseAuth auth;
     FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        Login=findViewById(R.id.login);
        register=findViewById(R.id.Register);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if (user!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memail,mpassword;
                memail=email.getText().toString();
                mpassword=password.getText().toString();
                if (memail.isEmpty()){
                    email.setError("Invalid Email");
                    email.setFocusable(true);
                    return;
                }
                if (mpassword.isEmpty() && mpassword.length()<=8){
                    password.setError("Password empty or password Incorrect");
                    password.setFocusable(true);
                    return;
                }
                auth.signInWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()){
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                     }
                    }
                });
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }
}