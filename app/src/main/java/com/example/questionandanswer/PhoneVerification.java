package com.example.questionandanswer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class PhoneVerification extends AppCompatActivity implements View.OnClickListener {
CountryCodePicker countryCodePicker;
EditText Phone;
Button sendPhone;
ImageView imageView;
TextView textView;
Animation topanim;
FirebaseAuth auth;
FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        countryCodePicker=findViewById(R.id.countrypicker);
        Phone=findViewById(R.id.phone);
        sendPhone=findViewById(R.id.sendphone);
        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.textView);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if (user!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        topanim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        imageView.setAnimation(topanim);
        textView.setAnimation(topanim);
        sendPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String phonenum=Phone.getText().toString();
        if (phonenum.isEmpty()){
            return;
        }
        String phone=countryCodePicker.getSelectedCountryCode()+phonenum;
        Intent intent=new Intent(getApplicationContext(),VerifyPhone.class);
        intent.putExtra("Phone",phone);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}