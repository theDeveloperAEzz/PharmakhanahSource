package com.pharmakhanah.hp.pharmakhanahsource.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.pharmakhanah.hp.pharmakhanahsource.DB.DBHelper;
import com.pharmakhanah.hp.pharmakhanahsource.R;
import com.pharmakhanah.hp.pharmakhanahsource.fragment.ChatsFragment;
import com.pharmakhanah.hp.pharmakhanahsource.fragment.HomeFragment;
import com.pharmakhanah.hp.pharmakhanahsource.fragment.NotificationsFragment;
import com.pharmakhanah.hp.pharmakhanahsource.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main2Activity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        fab();
        final TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(3).setIcon(R.mipmap.ic_notifay4);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText() == "") {
                    tab.setIcon(R.mipmap.ic_notification);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getText() == "") {
                    tab.setIcon(R.mipmap.ic_notifay4);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

   public  void fab() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ProfileFragment.fabProfile.show();
                        HomeFragment.fabHome.hide();
                        break;
                    case 1:
                        ProfileFragment.fabProfile.hide();
                        HomeFragment.fabHome.show();
                        break;
                    case 2:
                        HomeFragment.fabHome.hide();
                        break;


                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

//    public void logout(View view) {
//        LoginManager.getInstance().logOut();
//        goLoginScreen();
//    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileFragment(), "Profile");
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new ChatsFragment(), "Chats");
        adapter.addFragment(new NotificationsFragment(), "");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            if (isOnline()) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                dbHelper.clearTable();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "check internet connection", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)
                Objects.requireNonNull(getApplicationContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
