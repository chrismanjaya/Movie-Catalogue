package com.example.moviecatalogue5.activity;

import android.os.Bundle;
import android.view.MenuItem;
import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.adapter.ViewPagerAdapter;
import com.example.moviecatalogue5.custom.CustomViewPager;
import com.example.moviecatalogue5.fragment.FavFragment;
import com.example.moviecatalogue5.fragment.MovieFragment;
import com.example.moviecatalogue5.fragment.TvFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

public class MainActivity extends AppCompatActivity {

    private CustomViewPager customViewPager;
    private MovieFragment movieFragment = new MovieFragment();
    private TvFragment tvFragment = new TvFragment();
    private FavFragment favFragment = new FavFragment();

    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.tab_movie:
                        customViewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_tv:
                        customViewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_fav:
                        customViewPager.setCurrentItem(2);
                        break;
                }

                return false;
            }
        });

        customViewPager = findViewById(R.id.film_pager);
        customViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // setOffscreenPageLimit: Total Page - 1. If view pager have 3 tab, so the page limit is 2.
        customViewPager.setOffscreenPageLimit(2);

        // setPagingEnabled to disable swipe in view pager. False = disable swipe and True = enable swipe.
        customViewPager.setPagingEnabled(false);

        setupViewPager(customViewPager);


    }

    private void setupViewPager(CustomViewPager customViewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(movieFragment);
        viewPagerAdapter.addFragment(tvFragment);
        viewPagerAdapter.addFragment(favFragment);
        customViewPager.setAdapter(viewPagerAdapter);
    }
}
