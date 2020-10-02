package com.example.questionandanswer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewpageAdapter extends PagerAdapter {
    LayoutInflater layoutInflater;
    Context context;

    public ViewpageAdapter(Context context) {
        this.context = context;
    }

    public int[] layout = {
            R.layout.intro1,
            R.layout.intro2,
            R.layout.intro3,
            R.layout.intro4
    };

    @Override
    public int getCount() {
        return layout.length;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(layout[position],container,false);
        container.addView(view);
        return view;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view=(View) object;
        container.removeView(view);
    }
}