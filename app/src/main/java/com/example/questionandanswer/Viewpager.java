package com.example.questionandanswer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

public class Viewpager extends AppCompatActivity {
    TabLayout tabLayout;
    Button skip,next;
    ViewPager pager;
    ViewpageAdapter viewpageAdapter;
    Prefmang prefmang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        tabLayout = findViewById(R.id.tabs);
        skip=findViewById(R.id.skip);
        next=findViewById(R.id.next);
        pager=findViewById(R.id.viewpagernew);
        viewpageAdapter = new ViewpageAdapter(getApplicationContext());
        pager.setAdapter(viewpageAdapter);
        tabLayout.setupWithViewPager(pager);
        prefmang = new Prefmang(getApplicationContext());
        if (!prefmang.FirstLaunch()){
            starthomeactivity();

        }


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = getItem(+1);
                if (i<viewpageAdapter.getCount()){
                    pager.setCurrentItem(i);
                }else{
                    starthomeactivity();
                }

            }
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position==viewpageAdapter.getCount()-1){
                    skip.setVisibility(View.INVISIBLE);
                    next.setText("Get Started");
                }else{
                    skip.setVisibility(View.VISIBLE);
                    next.setText("Next");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }
    private void starthomeactivity() {
        prefmang.setFirstTimeLaunch(false);
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
    private int getItem(int i) {
        return pager.getCurrentItem() + i;
    }


}