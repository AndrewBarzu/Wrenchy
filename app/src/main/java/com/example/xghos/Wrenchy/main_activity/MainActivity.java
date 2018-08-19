package com.example.xghos.Wrenchy.main_activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.helpers_extras.Helper;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
//    private ArrayList<User> list;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (CurrentUser.getStatus().equals("2")) {
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new ChangePW()).commit();

        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new NavigationFragment()).commit();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionbar.setTitle("");
        mDrawerLayout = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = View.inflate(this, R.layout.drawer_header_layout, null);
        TextView username = headerView.findViewById(R.id.username_drawer_header);
        username.setText(CurrentUser.getUserName());

        ImageView profilePic = headerView.findViewById(R.id.profilePic_drawer_header);
        profilePic.setImageBitmap(Helper.getINSTANCE().getBitmapFromString(CurrentUser.getAvatar()));
        navigationView.addHeaderView(headerView);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.new_offer:
                                Toast.makeText(MainActivity.this, "new offer", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.favorites:
                                Toast.makeText(MainActivity.this, "favorites", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.settings:
                                Toast.makeText(MainActivity.this, "settings", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.support:
                                Toast.makeText(MainActivity.this, "support", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.logout:
                                finish();
                        }

                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                });
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof NavigationFragment) {
            NavigationFragment navigationFragment = (NavigationFragment) fragment;
            if (navigationFragment.viewPager.getCurrentItem() != 0) {
                navigationFragment.viewPager.setCurrentItem(0);
            } else {
                super.onBackPressed();
            }
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CurrentUser.setTabindex(0);
    }
}