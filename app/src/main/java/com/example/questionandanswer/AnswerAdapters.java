package com.example.questionandanswer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AnswerAdapters extends RecyclerView.Adapter<AnswerAdapters.viewholder> {
    List<AnswerDetail> answerDetails;
    Context context;


    public AnswerAdapters(List<AnswerDetail> answerDetails, Context context) {
        this.answerDetails = answerDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_add_answer,parent,false);
        return new viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
    holder.answer.setText(answerDetails.get(position).getAnswer());
    holder.username.setText(answerDetails.get(position).getUsername());
    holder.username2.setText("@"+answerDetails.get(position).getUsername());
        Picasso.get().load(answerDetails.get(position).getImageurl()).transform(new CropCircleTransformation()).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (!answerDetails.isEmpty()){
            return answerDetails.size();
        }
        return 0;
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView answer,username,username2;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.answered_image);
            answer=itemView.findViewById(R.id.answer);
            username=itemView.findViewById(R.id.answer_user);
            username2=itemView.findViewById(R.id.answer_user2);
        }
    }
}
