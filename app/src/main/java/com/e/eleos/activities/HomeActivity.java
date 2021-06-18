package com.e.eleos.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.e.eleos.R;
import com.google.android.material.navigation.NavigationView;

import static com.e.eleos.utils.Constants.APPNAME;
import static com.e.eleos.utils.Constants.ISLOGGEDIN;

public class HomeActivity extends AppCompatActivity {
    CardView cv_ewaste,cv_donatefood;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cv_ewaste = (CardView) findViewById(R.id.cv_ewaste);
        cv_donatefood =(CardView) findViewById(R.id.cv_donatefood);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,
                        R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.logout) {
                    Log.d("TAG", "logout clicked: ");
                    SharedPreferences sharedPreferences = getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ISLOGGEDIN,false);
                    editor.apply();
                    Intent intent = new Intent(HomeActivity.this,SigninActivity.class);
                    startActivity(intent);
                    finish();
//                    ((ActivityManager) HomeActivity.this.getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        cv_ewaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start ewaste form
                Intent intent = new Intent(HomeActivity.this,EwasteActivity.class);
                startActivity(intent);
            }
        });
        cv_donatefood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start donate food form
                Intent intent = new Intent(HomeActivity.this,DonatefoodActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            switch (item.getItemId()){
                case R.id.logout:
                    Log.d("TAG", "onOptionsItemSelected: ");


            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}