package com.example.questionandanswer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AnswerView extends AppCompatActivity {
    TextView question,username,username2,count_answer;
    RecyclerView recyclerView;
    ImageView imageView;
    List<AnswerDetail> answerDetailList=new ArrayList<>();
    AnswerAdapters answerAdapters;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerview);
        question=findViewById(R.id.question_oftheselected);
        final String quest=getIntent().getStringExtra("Questiion");
        imageView=findViewById(R.id.imagefor);
        username=findViewById(R.id.username1);
        username2=findViewById(R.id.username2);
        count_answer=findViewById(R.id.answersize);
        question.setText(quest);
        recyclerView=findViewById(R.id.recycle_answer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference().child("Question");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    final String keykey=snapshot1.getKey();
                    Query query=databaseReference.child(keykey).orderByChild("question").equalTo(quest);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot3:snapshot.getChildren()){
                                String key2=snapshot3.getKey();
                                DatabaseReference databaseReference4=databaseReference.child(keykey).child(key2).child("Answer");
                                databaseReference4.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        answerDetailList.clear();
                                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                            AnswerDetail answerDetail=dataSnapshot.getValue(AnswerDetail.class);
                                            answerDetailList.add(answerDetail);
                                        }
                                        answerAdapters=new AnswerAdapters(answerDetailList,getApplicationContext());
                                        recyclerView.setAdapter(answerAdapters);
                                        answerAdapters.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                DatabaseReference answeradded=databaseReference.child(keykey).child(key2).child("answersize");
                                answeradded.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        count_answer.setText(snapshot.getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                DatabaseReference username1=databaseReference.child(keykey).child(key2).child("username");
                                username1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        username.setText(snapshot.getValue().toString());
                                        username2.setText(String.format("@%s", snapshot.getValue().toString()));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                DatabaseReference imageurl2=databaseReference.child(keykey).child(key2).child("imageurl");
                                 imageurl2.addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                                         Picasso.get().load(snapshot.getValue().toString()).transform(new CropCircleTransformation()).fit().into(imageView);
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
    }



}
