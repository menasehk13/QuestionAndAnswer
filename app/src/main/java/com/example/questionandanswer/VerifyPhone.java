package com.example.questionandanswer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VerifyPhone extends AppCompatActivity {
    EditText addcode;
    Button verfiy;
    FirebaseAuth auth;
    ImageView imageView;
    TextView textView;
    String Phonerecived,VerficationId;
    SweetAlertDialog sweetAlertDialog;
     public static final String TAG="Phone";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        auth=FirebaseAuth.getInstance();
        addcode=findViewById(R.id.code);
        verfiy=findViewById(R.id.toregister);
        textView=findViewById(R.id.phoneview);
        imageView=findViewById(R.id.imageView5);
        Phonerecived=getIntent().getStringExtra("Phone");
        SendVerficationCode(Phonerecived);
        Log.d(TAG, "onCreate: "+Phonerecived);
        sweetAlertDialog=new SweetAlertDialog(VerifyPhone.this);
        sweetAlertDialog.setContentText("Waiting For Code")
                        .setTitle("HighTechQuestion");
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.show();
        verfiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Phonerecived.isEmpty()||Phonerecived.length()<6){
                    addcode.setError("Enter The Code we Have Sent");
                    addcode.isFocusable();
                }else{

                    VerfiyCode(Phonerecived);

                }

            }
        });
    }
    public void SendVerficationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+"+number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBacks
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            VerficationId=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if (code!=null){
                addcode.setText(code);
                VerfiyCode(code);

            }
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    public  void VerfiyCode(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(VerficationId,code);
        SignInWithCredential(credential);
    }

    private void SignInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setContentText("Code Inserted")
                            .setConfirmButton("NEXT", new SweetAlertDialog.OnSweetClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent=new Intent(VerifyPhone.this,Register.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(VerifyPhone.this,imageView,"imageview");
                                    intent.putExtra("PhoneOfUser",Phonerecived);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }else{
                    Toast.makeText(VerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setContentText("Error!!"+task.getException().getMessage());
                    sweetAlertDialog.setCancelButton("CANCEL", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                          startActivity(new Intent(getApplicationContext(),PhoneVerification.class));
                          finish();
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}