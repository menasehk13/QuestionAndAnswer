package com.example.questionandanswer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Addanswer extends AppCompatActivity {
    TextView questionof,name,name2,size,seen;
    Button send;
    EditText addanswer;
    RecyclerView recyclerView;
    ImageView prof;
    List<AnswerDetail> answerDetails=new ArrayList<>();
    AnswerAdapters answerAdapters;
    String answersizeof;
    String uid;
    String key;
    String user;
    String imageurl;
    String keyof;
    String Token,username,userimage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    private ApiInterface apiInterface;
    private static String TAG="VALUESSS";
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answeradd);
        questionof = findViewById(R.id.question_oftoanswer);
        addanswer = findViewById(R.id.addanswer);
        name=findViewById(R.id.usernameofquestion1);
        name2=findViewById(R.id.usernameofquestion2);
        size=findViewById(R.id.answersize);

        prof=findViewById(R.id.image_oftheselectedquestion);
        recyclerView = findViewById(R.id.addanswer_Recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(false);
        send = findViewById(R.id.button_send);
        size.setText(String.valueOf(answerDetails.size()));
        final String ques = getIntent().getStringExtra("Questiion");
        final String timequestion = getIntent().getStringExtra("Time");
        final String typequestion = getIntent().getStringExtra("Type");
        questionof.setText(ques);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Question");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    final String keykey=snapshot1.getKey();
                    Query query=databaseReference.child(keykey).orderByChild("question").equalTo(ques);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot3:snapshot.getChildren()){
                                String key2=snapshot3.getKey();
                                DatabaseReference databaseReference4=databaseReference.child(keykey).child(key2).child("Answer");
                                DatabaseReference nameof=databaseReference.child(keykey).child(key2).child("username");
                                DatabaseReference imageurl=databaseReference.child(keykey).child(key2).child("imageurl");
                                final DatabaseReference answersize=databaseReference.child(keykey).child(key2).child("answersize");
                                imageurl.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        userimage=snapshot.getValue().toString();
                                        Picasso.get().load(userimage).transform(new CropCircleTransformation()).fit().into(prof);
                                        Log.d(TAG, "onDataImage: "+userimage);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                nameof.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        username=snapshot.getValue().toString();
                                        Log.d(TAG, "onDataname: "+username);
                                        name.setText(username);
                                        name2.setText("@"+username);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                databaseReference4.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        answerDetails.clear();
                                      for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                          AnswerDetail answerDetail=dataSnapshot.getValue(AnswerDetail.class);
                                         answerDetails.add(answerDetail);
                                          Log.d(TAG, "onDataSize: "+answerDetails.size());
                                          answersizeof=String.valueOf(answerDetails.size());
                                          size.setText(answersizeof);
                                          answersize.setValue(answersizeof);
                                      }
                                      answerAdapters=new AnswerAdapters(answerDetails,getApplicationContext());
                                      recyclerView.setAdapter(answerAdapters);
                                      answerAdapters.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            final String key=dataSnapshot.getKey();
                            Log.d(TAG, "onKEY: "+key);
                            Query query=databaseReference.child(key).orderByChild("question").equalTo(ques);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                    for (DataSnapshot snapshot1:snapshot2.getChildren()){
                                        final String innerkey=snapshot1.getKey();
                                        DatabaseReference Databse2=databaseReference.child(key).child(innerkey).child("token");
                                        Databse2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                               Token=snapshot.getValue().toString();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        DatabaseReference imageurl=databaseReference.child(key).child(innerkey).child("imageurl");
                                        imageurl.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                userimage=snapshot.getValue().toString();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        DocumentReference documentReference=FirebaseFirestore.getInstance().collection("Users").document(user);
                                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot documentSnapshot=task.getResult();
                                                if (documentSnapshot.exists()) {
                                                    final String answerusername,imageusrl,answer,timeofanswer;
                                                    answerusername=documentSnapshot.get("FirstName").toString();
                                                    imageusrl=documentSnapshot.get("ImageUrl").toString();
                                                    answer=addanswer.getText().toString();
                                                    if (answer.isEmpty()){
                                                        addanswer.setError("Please Add your Answer");
                                                        addanswer.setFocusable(true);
                                                        send.setClickable(false);
                                                        return;
                                                    }
                                                    timeofanswer=DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
                                                   DatabaseReference databaseReference2=databaseReference.child(key).child(innerkey).child("Answer");
                                                   String id=databaseReference2.push().getKey();
                                                   AnswerDetail answerDetail=new AnswerDetail(answer,answerusername,timeofanswer,imageusrl);
                                                   databaseReference2.child(id).setValue(answerDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(Addanswer.this, "Added Answer", Toast.LENGTH_SHORT).show();
                                                            addanswer.getText().clear();
                                                            String title=ques;
                                                            SendChatNotification(Token,title,answer);
                                                        }
                                                       }
                                                   });
                                                }
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
    public void SendChatNotification(String to,String title,String body){
        DataModel dataModel=new DataModel(title,body);
        RequestNotification requestNotification=new RequestNotification(to,dataModel);
        apiInterface=ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall=apiInterface.SendChatNotification(requestNotification);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200){
                    try {
                        Log.d(TAG, "onResponse: "+response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onResponse: "+response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
