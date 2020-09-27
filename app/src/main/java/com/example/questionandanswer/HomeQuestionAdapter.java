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

public class HomeQuestionAdapter extends RecyclerView.Adapter<HomeQuestionAdapter.viewadapter> {
    List<MyQuestion> list;
    Context context;

    public HomeQuestionAdapter(List<MyQuestion> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_myquestions,parent,false);
        return new viewadapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewadapter holder, int position) {
        holder.question.setText(list.get(position).getQuestion());
        holder.type.setText(list.get(position).getType());
        holder.time.setText(list.get(position).getTime());
        holder.username.setText(list.get(position).getUsername());
        Picasso.get().load(list.get(position).getImageurl()).transform(new CropCircleTransformation()).fit().into(holder.imageView);
        holder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quest=holder.question.getText().toString();
                String typenew=holder.type.getText().toString();
                String timeadded=holder.time.getText().toString();
                Intent intent=new Intent(view.getContext(),Addanswer.class);
                intent.putExtra("Questiion",quest);
                intent.putExtra("Type",typenew);
                intent.putExtra("Time",timeadded);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (!list.isEmpty()){
            return list.size();
        }
        return 0;
    }

    public class viewadapter extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView question,type,username,time;
        public viewadapter(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.view_profile_pic);
            question=itemView.findViewById(R.id.question_ofuser);
            type=itemView.findViewById(R.id.question_type);
            username=itemView.findViewById(R.id.question_username);
            time=itemView.findViewById(R.id.question_addedtime);
        }
    }
}
