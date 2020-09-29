package com.example.questionandanswer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class AnswerView extends AppCompatActivity {
    TextView question,time,type;
    RecyclerView recyclerView;
    List<AnswerDetail> answerDetailList=new ArrayList<>();
    AnswerAdapters answerAdapters;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerview);
        question=findViewById(R.id.question_oftheselected);
        time=findViewById(R.id.time_Added);
        type=findViewById(R.id.type_ofthequestion);
        final String quest=getIntent().getStringExtra("Questiion");
        String typeof=getIntent().getStringExtra("Type");
        question.setText(quest);
        time.setText(getIntent().getStringExtra("Time"));
        type.setText(typeof);
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
