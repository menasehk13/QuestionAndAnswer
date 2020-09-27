package com.example.questionandanswer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddQuestion extends AppCompatActivity implements View.OnClickListener {
    EditText questionadd,type;
    Button addquestion;
    String question,typeadd,time,username,imageurl;
    SweetAlertDialog alertDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addquestions);
        questionadd=findViewById(R.id.addnewquestion);
        type=findViewById(R.id.type_Add);
        addquestion=findViewById(R.id.Add);
        addquestion.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        alertDialog=new SweetAlertDialog(AddQuestion.this);
        alertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        alertDialog.setTitle("Question Added");
        alertDialog.setContentText("Adding Question.....");
        alertDialog.show();
        question=questionadd.getText().toString();
        typeadd=type.getText().toString();
        time= DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final String user=firebaseAuth.getCurrentUser().getUid();
        final DocumentReference documentReference= FirebaseFirestore.getInstance().collection("Users").document(user);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
           DocumentSnapshot documentSnapshot=task.getResult();
           if (documentSnapshot.exists()){
               username=documentSnapshot.get("FirstName").toString()+documentSnapshot.get("LastName").toString();
               imageurl=documentSnapshot.get("ImageUrl").toString();
               DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Question").child(user);
             MyQuestion myQuestion=new MyQuestion(question,time,typeadd,imageurl,username);
             String id=databaseReference.push().getKey();
             databaseReference.child(id).setValue(myQuestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()){
                       alertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                       alertDialog.setContentText("Question Added");
                       alertDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                           @Override
                           public void onClick(SweetAlertDialog sweetAlertDialog) {
                             startActivity(new Intent(getApplicationContext(),MainActivity.class));
                             finish();
                           }
                       });

                   }
                 }
             });

           }
            }
        });
    }
}
