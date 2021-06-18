package com.e.eleos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import static com.e.eleos.utils.Constants.*;

import com.e.eleos.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean(ISLOGGEDIN,false)){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
            finish();
        }
    }
}