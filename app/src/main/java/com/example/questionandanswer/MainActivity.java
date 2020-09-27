package com.example.questionandanswer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView=findViewById(R.id.bottom_nav);
        SelectedItem(R.id.questions);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                SelectedItem(item.getItemId());
                return true;
            }
        });
    }
    public boolean SelectedItem(int item){
        Fragment fragment=null;
     switch (item){
         case R.id.questions:
             fragment=new QuestionFragment();
             break;
         case R.id.myquestions:
             fragment=new MyQuestions();
             break;
         case R.id.profile_user:
             fragment=new ProfileFragment();
             break;
         default:
     }
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
     transaction.replace(R.id.frame,fragment);
     transaction.commit();
     return true;
    }
}