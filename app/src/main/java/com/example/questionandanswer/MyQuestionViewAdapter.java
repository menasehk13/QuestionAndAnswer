package com.example.questionandanswer;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
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
     holder.username.setText(myQuestions.get(position).getUsername());
     holder.answersize.setText(myQuestions.get(position).getAnswersize());
        holder.question.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                 String quest=holder.question.getText().toString();
                Intent intent=new Intent(view.getContext(),AnswerView.class);
                intent.putExtra("Questiion",quest);
                Pair[] pairs=new Pair[2];
                pairs[0]=new Pair<View,String>(holder.cardView,"cardview");
                pairs[1]=new Pair<View,String>(holder.question,"questiontrns");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),pairs);
                view.getContext().startActivity(intent,options.toBundle());

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
        CardView cardView;
        TextView question,type,username,answersize;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            question=itemView.findViewById(R.id.question_ofuser);
            username=itemView.findViewById(R.id.question_username);
            answersize=itemView.findViewById(R.id.answer_size);
            cardView=itemView.findViewById(R.id.cardviewtrans);
        }
    }
}
