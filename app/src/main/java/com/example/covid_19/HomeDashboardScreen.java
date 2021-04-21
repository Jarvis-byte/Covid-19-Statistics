package com.example.covid_19;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.covid_19.Adapters.ViewPagerAdapter;
import com.example.covid_19.Fragments.NewsFragment;
import com.example.covid_19.Fragments.StatisticsFragment;
import com.example.covid_19.Fragments.VaccinationFragment;

public class HomeDashboardScreen extends AppCompatActivity {

    ImageView toolbar_image;
    MeowBottomNavigation bottom_navigation;
    NewsFragment newsFragment;
    StatisticsFragment statisticsFragment;
    VaccinationFragment vaccinationFragment;
    private Toolbar toolbar;
    private ViewPager2 fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard_screen);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar);
        toolbar_image = findViewById(R.id.toolbar_image);
//        ImageSlider imageSlider = findViewById(R.id.slider);
//        mChart = findViewById(R.id.lineChart);


        /** Add menu Item for bottom Nav*/
        fragment_container = findViewById(R.id.fragment_container);

        bottom_navigation.add(new MeowBottomNavigation.Model(0, R.drawable.ic_baseline_home_24)); //statistics
        bottom_navigation.add(new MeowBottomNavigation.Model(1, R.drawable.news));
        bottom_navigation.add(new MeowBottomNavigation.Model(2, R.drawable.syringe));


        bottom_navigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                switch (item.getId()) {
                    case 0:
                        fragment_container.setCurrentItem(0, false);
                        break;
                    case 1:
                        fragment_container.setCurrentItem(1, false);
                        break;
                    case 2:
                        fragment_container.setCurrentItem(2, false);
                        break;

                }
            }
        });

        fragment_container.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                switch (position) {
                    case 0:
                        bottom_navigation.getModels().get(0).getId();
                        bottom_navigation.show(0, true);
                        break;
                    case 1:
                        bottom_navigation.getModels().get(1).getId();
                        bottom_navigation.show(1, true);
                        break;
                    case 2:
                        bottom_navigation.getModels().get(2);
                        bottom_navigation.show(2, true);
                        break;

                }
            }

        });
        setupViewPager(fragment_container);
        bottom_navigation.show(0, true);
        bottom_navigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
//                Toast.makeText(HomeDashboardScreen.this, "You clicked"+item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        bottom_navigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
//                Toast.makeText(HomeDashboardScreen.this, "You clicked"+item.getId(), Toast.LENGTH_SHORT).show();
            }
        });


        /**=============== END ===========================*/


        configureToolbar(toolbar);
    }

    private void setupViewPager(ViewPager2 viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        statisticsFragment = new StatisticsFragment();
        newsFragment = new NewsFragment();
        vaccinationFragment = new VaccinationFragment();

        adapter.addFragment(statisticsFragment);
        adapter.addFragment(newsFragment);
        adapter.addFragment(vaccinationFragment);


        viewPager.setAdapter(adapter);
    }


    private void configureToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Covid-19 Statistics");
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
}