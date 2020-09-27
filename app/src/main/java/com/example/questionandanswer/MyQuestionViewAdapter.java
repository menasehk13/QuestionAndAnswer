package com.example.questionandanswer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MyQuestionViewAdapter extends RecyclerView.Adapter<MyQuestionViewAdapter.viewholder> {
    Context context;
    List<MyQuestion> myQuestions;

    public MyQuestionViewAdapter(Context context, List<MyQuestion> myQuestions) {
        this.context = context;
        this.myQuestions = myQuestions;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_myquestions,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, int position) {
     holder.question.setText(myQuestions.get(position).getQuestion());
     holder.type.setText(myQuestions.get(position).getType());
     holder.time.setText(myQuestions.get(position).getTime());
     holder.username.setText(myQuestions.get(position).getUsername());
        Picasso.get().load(myQuestions.get(position).getImageurl()).transform(new CropCircleTransformation()).fit().into(holder.imageView);
        holder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String quest=holder.question.getText().toString();
                 String typenew=holder.type.getText().toString();
                 String timeadded=holder.time.getText().toString();
                Intent intent=new Intent(view.getContext(),AnswerView.class);
                intent.putExtra("Questiion",quest);
                intent.putExtra("Type",typenew);
                intent.putExtra("Time",timeadded);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (!myQuestions.isEmpty()){
            return myQuestions.size();
        }
        return 0;
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView question,type,username,time;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.view_profile_pic);
            question=itemView.findViewById(R.id.question_ofuser);
            type=itemView.findViewById(R.id.question_type);
            username=itemView.findViewById(R.id.question_username);
            time=itemView.findViewById(R.id.question_addedtime);
        }
    }
}
