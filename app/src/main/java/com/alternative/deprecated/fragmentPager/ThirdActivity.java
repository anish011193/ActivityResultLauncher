package com.alternative.deprecated.fragmentPager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.alternative.deprecated.BuildConfig;
import com.alternative.deprecated.Constant;
import com.alternative.deprecated.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ThirdActivity extends AppCompatActivity {

    //Var
    private String[] titles = {"Water", "Fire"};
    private ViewStateAdapter viewStateAdapter;
    private ViewPagerAdapter viewPagerAdapter;
    //UI Init
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        getSupportActionBar().hide();
        viewPager2 = findViewById(R.id.viewPager2);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        if(Constant.showDeprecated) { // OLD Way
            viewPager2.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);

            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setOffscreenPageLimit(2);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            for(String str: titles){
                tabLayout.addTab(tabLayout.newTab().setText(str));
            }
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        } else { // NEW WAY
            viewPager2.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            viewStateAdapter = new ViewStateAdapter(this);
            viewPager2.setAdapter(viewStateAdapter);
//            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
//                @Override
//                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                    tab.setText(titles[position]);
//                }
//            });
//            tabLayoutMediator.attach();
            new TabLayoutMediator(tabLayout,viewPager2,((tab, position) -> tab.setText(titles[position]))).attach();
        }

    }

    private class ViewStateAdapter extends FragmentStateAdapter {

        public ViewStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new WaterFragment();
                case 1:
                    return new FireFragment();
                default:
                    return new WaterFragment();
            }
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WaterFragment();
                case 1:
                    return new FireFragment();
                default:
                    return new WaterFragment();
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}