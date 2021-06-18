package com.e.eleos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudant.sync.documentstore.DocumentBodyFactory;
import com.cloudant.sync.documentstore.DocumentRevision;
import com.cloudant.sync.documentstore.DocumentStore;
import com.cloudant.sync.documentstore.DocumentStoreException;
import com.cloudant.sync.documentstore.DocumentStoreNotOpenedException;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.replication.ReplicatorBuilder;
import com.e.eleos.R;
import com.e.eleos.models.Listener;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static com.e.eleos.utils.Constants.APPNAME;
import static com.e.eleos.utils.Constants.EMAIL;
import static com.e.eleos.utils.Constants.ISLOGGEDIN;
import static com.e.eleos.utils.Constants.LOGGEDINUSERNAME;

public class SigninActivity extends AppCompatActivity {
    EditText et_email, password;
    Button signin, signup,volunteer;
    DocumentStore documentStore = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        et_email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        signin = (Button) findViewById(R.id.btn_signin);
        signup = (Button) findViewById(R.id.btn_signup);
        volunteer = (Button)findViewById(R.id.volunteer);
        File location = getDir("users", Context.MODE_PRIVATE);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String passwd = password.getText().toString().trim();
                Map<String, Object> user = new HashMap<String, Object>();
                user.put("email", email);
                user.put("password", passwd);
                try {
                    documentStore = DocumentStore.getInstance(new File(location, "users"));
                    URI url = new URI("https://apikey-v2-5yle01i6o8cl2goo7oyc509u9bw5olu9mompdlpbjze:b9b0b4175077ff81254308988b69dfd5@c260a3aa-fd10-4833-941c-49b6cf0026dc-bluemix.cloudantnosqldb.appdomain.cloud/users");
                    Replicator rep = ReplicatorBuilder.pull().from(url).to(documentStore).build();
                    CountDownLatch latch = new CountDownLatch(1);
                    Listener listener = new Listener(latch);
                    rep.getEventBus().register(listener);
                    rep.start();
                    latch.await();
                    rep.getEventBus().unregister(listener);
                    if (rep.getState() != Replicator.State.COMPLETE) {
                        Log.d("TAG", "Error replicating FROM remote");
                    } else {
                        Log.d("TAG", (String.format("Replicated %d documents in %d batches",
                                listener.documentsReplicated, listener.batchesReplicated)));
                        boolean exists = false;
                        List<DocumentRevision> list = documentStore.database().read(0, documentStore.database().getDocumentCount(), true);
                        for (DocumentRevision documentRevision : list) {
                            Map<String, Object> eachdata = documentRevision.getBody().asMap();
                            Log.d("Signin",eachdata.toString());
                            if (eachdata.get("email").toString().equals(email) && eachdata.get("password").toString().equals(passwd)) {
                                SharedPreferences sharedPreferences = getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(EMAIL, email);
                                editor.putBoolean(ISLOGGEDIN, true);
                                editor.putString(LOGGEDINUSERNAME, eachdata.get("username").toString());
                                editor.apply();
                                exists = true;
                                Intent intent = new Intent(SigninActivity.this, DetailsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        if (!exists) {
                            Toast.makeText(SigninActivity.this, " User Doesn't exist or credentials doesn't match", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (DocumentStoreNotOpenedException | URISyntaxException | InterruptedException e) {
                    e.printStackTrace();
                } catch (DocumentStoreException e) {
                    e.printStackTrace();
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SigninActivity.this, "Signed in as a Volunteer", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SigninActivity.this, VolunteerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}