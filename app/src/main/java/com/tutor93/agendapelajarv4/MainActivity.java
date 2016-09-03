/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tutor93.agendapelajarv4;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.tutor93.agendapelajarv4.fragment.MyPostsFragment;
import com.tutor93.agendapelajarv4.fragment.MyTopPostsFragment;
import com.tutor93.agendapelajarv4.fragment.RecentPostsFragment;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    private ResideMenu resideMenu;

    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemSchedule;


   /* private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemChat;
    private ResideMenuItem itemTask;
    private ResideMenuItem itemExams;
*/

    private FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpMenu();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();




        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new RecentPostsFragment(),
                    new MyPostsFragment(),
                    new MyTopPostsFragment(),
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_posts),
                    getString(R.string.heading_my_top_posts)
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Button launches NewPostActivity
        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewPostActivity.class));
            }
        });

        //setUpMenu();
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);

        //resideMenu.setMenuListener(menuListener);
        //scaling activity slide ke menu
        resideMenu.setScaleValue(0.7f);

        itemHome = new ResideMenuItem(this, R.drawable.ic_home, "Home");
        itemProfile = new ResideMenuItem(this, R.drawable.ic_profile, "Profile");
        //itemCalendar = new ResideMenuItem(this, R.drawable.ic_calendar, "Calendar");
        itemSchedule = new ResideMenuItem(this, R.drawable.ic_schedule, "Schedule");
        //itemExams = new ResideMenuItem(this, R.drawable.ic_exams, "Exams");
        //itemSettings = new ResideMenuItem(this, R.drawable.ic_setting, "Settings");
        //itemChat = new ResideMenuItem(this, R.drawable.ic_chat, "Chat");
        //itemTask = new ResideMenuItem(this, R.drawable.ic_task, "Task");


        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
       // resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSchedule, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(itemExams, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(itemTask, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(itemChat, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);

        // matikan slide ke kanan
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        itemHome.setOnClickListener(this);
        itemSchedule.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        //itemSettings.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == itemHome) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (view == itemSchedule) {
            startActivity(new Intent(this, ScheduleActivity.class));
        } else if (view == itemProfile) {
            startActivity(new Intent(this, ProfileActivity.class));
        }


    }


}
