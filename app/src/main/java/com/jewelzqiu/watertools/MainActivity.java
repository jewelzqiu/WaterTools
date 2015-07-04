package com.jewelzqiu.watertools;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

//    @Bind(R.id.toolbar)
//    Toolbar mToolbar;

    @Bind(R.id.tablayout)
    TabLayout mTabLayout;

    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        setSupportActionBar(mToolbar);

        setupTabs();
    }

    private void setupTabs() {
        mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), this));
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
