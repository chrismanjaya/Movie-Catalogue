package com.example.moviecatalogue5.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.adapter.TabAdapter;
import com.example.moviecatalogue5.custom.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

public class FavFragment extends Fragment {

    private Context context;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_fav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.favPager);
        tabLayout = view.findViewById(R.id.tabLayoutFav);

        FragmentManager fragmentManager = getChildFragmentManager();
        tabAdapter = new TabAdapter(fragmentManager, context);
        tabAdapter.addFragment(new FavMovieFragment(), getResources().getString(R.string.tab_movie));
        tabAdapter.addFragment(new FavTvFragment(), getResources().getString(R.string.tab_tv));

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setPagingEnabled(true);
        viewPager.setOffscreenPageLimit(1);

        highLightCurrentTab(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void highLightCurrentTab(int position) {
        for (int i=0; i<tabLayout.getTabCount(); i++) {
            TabLayout.Tab tabs = tabLayout.getTabAt(i);
            assert tabs != null;
            tabs.setCustomView(null);
            tabs.setCustomView(tabAdapter.getTabView(i));
        }

        TabLayout.Tab tabs = tabLayout.getTabAt(position);
        assert tabs != null;
        tabs.setCustomView(null);
        tabs.setCustomView(tabAdapter.getSelectedTabView(position));
    }
}
