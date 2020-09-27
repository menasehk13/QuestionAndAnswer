package com.example.questionandanswer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class QuestionFragment extends Fragment {
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerview;
    HomeQuestionAdapter questionAdapter;
    List<MyQuestion> myQuestions=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview=view.findViewById(R.id.all_question_Recycleview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        refreshLayout=view.findViewById(R.id.refreshswipe);
        questionAdapter=new HomeQuestionAdapter(myQuestions,getContext());
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                questionAdapter.notifyDataSetChanged();
            }
        });
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Question");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                for (DataSnapshot snapshot1:dataSnapshot.getChildren()){
                   MyQuestion myQuestion=snapshot1.getValue(MyQuestion.class);
                   myQuestions.add(myQuestion);
                }
              }
              recyclerview.setAdapter(questionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}