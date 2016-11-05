package com.kevin.tech.vedioguidedemo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Kevin on 2016/10/13.
 */

public class MyViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private int[] mImageArr;

    public MyViewPagerAdapter(Context context, int[] imageArr) {
        this.mContext = context;
        this.mImageArr = imageArr;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        position %= mImageArr.length;
        if(position<0){
            position = mImageArr.length+position;
        }
        imageView.setImageResource(mImageArr[position]);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);//轮播图撑满布局
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }
}
