package com.devsh.babi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.devsh.babi.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private Handler sliderHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(mBinding.getRoot());

        List<String> sliderItems = new ArrayList<>();
        sliderItems.add("1");
        sliderItems.add("2");
        sliderItems.add("3");
        sliderItems.add("4");
        sliderItems.add("5");
        sliderItems.add("6");


        mBinding.vpImageSlider.setAdapter(new SliderAdapter(this, mBinding.vpImageSlider, sliderItems));

        mBinding.vpImageSlider.setClipToPadding(false);
        mBinding.vpImageSlider.setClipChildren(false);
        mBinding.vpImageSlider.setOffscreenPageLimit(3);
        mBinding.vpImageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        float offsetBetweenPages = getResources().getDimensionPixelOffset(R.dimen.offsetBetweenPages);
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * - (2 * offsetBetweenPages);
                if (position < - 1) {
                    page.setTranslationX(-myOffset);
                }
                else if (position <= 1) {
                    float f = 1 - Math.abs(position);
                    page.setScaleY(0.8f + f * 0.2f);
                    page.setAlpha(0.8f + f * 0.2f);
                    page.setTranslationX(myOffset);
                }
                else {
                    page.setAlpha(0f);
                    page.setTranslationX(myOffset);
                }
            }
        });

        mBinding.vpImageSlider.setPageTransformer(compositePageTransformer);

        mBinding.vpImageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            mBinding.vpImageSlider.setCurrentItem(mBinding.vpImageSlider.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }

}