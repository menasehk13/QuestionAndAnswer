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
import androidx.core.view.ViewCompat;
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
        holder.username.setText(list.get(position).getUsername());
        holder.size.setText(list.get(position).getAnswersize());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String quest=holder.question.getText().toString();
                Intent intent=new Intent(view.getContext(),Addanswer.class);
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
        if (!list.isEmpty()){
            return list.size();
        }
        return 0;
    }

    public class viewadapter extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView cardView;
        TextView question,size,username,time;
        public viewadapter(@NonNull View itemView) {
            super(itemView);
            question=itemView.findViewById(R.id.question_ofuser);
            username=itemView.findViewById(R.id.question_username);
            cardView=itemView.findViewById(R.id.cardviewtrans);
            size=itemView.findViewById(R.id.answer_size);
        }
    }
}
