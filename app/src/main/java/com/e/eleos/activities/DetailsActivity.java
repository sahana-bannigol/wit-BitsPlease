package com.e.eleos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.e.eleos.R;

import static com.e.eleos.utils.Constants.ADDRESS;
import static com.e.eleos.utils.Constants.APPNAME;
import static com.e.eleos.utils.Constants.PHONENUMBER;

public class DetailsActivity extends AppCompatActivity {
    Button btn_proceed;
    EditText et_phonenumber,et_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        et_phonenumber = (EditText) findViewById(R.id.et_phoneno);
        et_address = (EditText) findViewById(R.id.et_address);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenumebr = et_phonenumber.getText().toString().trim();
                String address = et_address.getText().toString().trim();
                SharedPreferences sharedPreferences = getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PHONENUMBER,phonenumebr);
                editor.putString(ADDRESS,address);
                editor.apply();
                Intent intent = new Intent(DetailsActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}