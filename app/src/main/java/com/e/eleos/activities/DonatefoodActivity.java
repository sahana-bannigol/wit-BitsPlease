package com.e.eleos.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;

import com.e.eleos.R;
import com.e.eleos.models.DonateFood;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.e.eleos.utils.Constants.ADDRESS;
import static com.e.eleos.utils.Constants.APPNAME;
import static com.e.eleos.utils.Constants.EMAIL;
import static com.e.eleos.utils.Constants.LOGGEDINUSERNAME;
import static com.e.eleos.utils.Constants.PHONENUMBER;
import static com.e.eleos.utils.Constants.USERNAME;

public class DonatefoodActivity extends AppCompatActivity {

    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;
    private List<String> list;
    private Button button,submit;
    private Chip chip_rice,chip_chapati,chip_curry,chip_grocery,chip_other;
    private TextView tv_coordinates;
    private EditText et_noofpppl,et_cooktime;
    private ToggleButton toggle_reheat,toggle_pickup;
    private Double latitude,longitude;
    DonateFood donateFood;
    FirebaseFirestore firebaseFirestore;


    private Boolean flag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donatefood);
        firebaseFirestore = FirebaseFirestore.getInstance();

        //if you want to lock screen for always Portrait mode
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);

        tv_coordinates = (TextView)findViewById(R.id.coordinates);
        button = (Button) findViewById(R.id.button);
        chip_chapati = (Chip) findViewById(R.id.chip_chapati);
        chip_rice = (Chip) findViewById(R.id.chip_rice);
        chip_grocery = (Chip) findViewById(R.id.chip_groceries);
        chip_curry = (Chip) findViewById(R.id.chip_curry);
        chip_other = (Chip) findViewById(R.id.chip_other);
        et_cooktime = (EditText)findViewById(R.id.et_cooktime);
        et_noofpppl = (EditText)findViewById(R.id.et_noofppl);
        toggle_reheat = (ToggleButton)findViewById(R.id.btn_preheat);
        toggle_pickup = (ToggleButton)findViewById(R.id.togglepickup);
        submit = (Button)findViewById(R.id.btn_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = displayGpsStatus();
                if (flag) {

                    Log.v(TAG, "onClick");

                    tv_coordinates.setText("Please!! move your device to" +
                            " see the changes in coordinates." + "\nWait..");
                    locationListener = new MyLocationListener();

                    if (ActivityCompat.checkSelfPermission(DonatefoodActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DonatefoodActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationMangaer.requestLocationUpdates(LocationManager
                            .GPS_PROVIDER, 5000, 10, locationListener);

                } else {
                    alertbox("Gps Status!!", "Your GPS is: OFF");
                }

            }
        });

        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<>();
                Timestamp timestamp = new Timestamp(new Date());
                if(chip_chapati.isChecked()) list.add("chapati");
                if(chip_curry.isChecked()) list.add("curry");
                if (chip_grocery.isChecked()) list.add("grocery");
                if(chip_rice.isChecked()) list.add("rice");
                if (chip_other.isChecked()) list.add("other");
                String time = et_cooktime.getText().toString();
                String noofppl = et_noofpppl.getText().toString();
                Boolean togglepickup = toggle_pickup.isChecked();
                Boolean togglereheat = toggle_reheat.isChecked();
                donateFood = new DonateFood();
                SharedPreferences sharedPreferences = getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
                donateFood.setUsername(sharedPreferences.getString(LOGGEDINUSERNAME,""));
                donateFood.setEmail(sharedPreferences.getString(EMAIL,""));
                donateFood.setAddress(sharedPreferences.getString(ADDRESS,""));
                donateFood.setPhno(sharedPreferences.getString(PHONENUMBER,""));
                donateFood.setList(list);
                donateFood.setLatitude(latitude);
                donateFood.setLongitude(longitude);
                donateFood.setTime(time);
                donateFood.setQuantity(noofppl);
                donateFood.setPickup(togglepickup);
                donateFood.setReheat(togglereheat);
                donateFood.bookedOn = new Timestamp(new Date());
                donateFood.deliveredOn = null;
                donateFood.isdelivered = false;
                donateFood.documentId = "";
                Log.d("DonateFood:",donateFood.toString());
                firebaseFirestore.collection("fooddonate").add(donateFood)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.update("documentId",documentReference.getId());
                                AlertDialog.Builder alertBuilder2= new AlertDialog.Builder(DonatefoodActivity.this);
                                alertBuilder2.setMessage("The request has been placed. We'll get back to you")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        });
            }
        });

    }

    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    /*----------Method to create an AlertBox ------------- */
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disabled")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*----------Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {

            tv_coordinates.setText("");
            Toast.makeText(getBaseContext(),"Location changed : Lat: " +
                            loc.getLatitude()+ " Lng: " + loc.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            longitude = loc.getLongitude();
            Log.v(TAG, longitude.toString());
            latitude = loc.getLatitude();
            Log.v(TAG, latitude.toString());
            String s = "Longitude:"+longitude+"\n"+"Latitude:"+latitude +"\n";
            tv_coordinates.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
}