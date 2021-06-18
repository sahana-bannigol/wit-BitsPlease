package com.e.eleos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudant.sync.documentstore.AttachmentException;
import com.cloudant.sync.documentstore.ConflictException;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class SignupActivity extends AppCompatActivity {

    EditText et_email, et_password, et_username;
    Button btn_signup;
    DocumentRevision documentRevision;
    DocumentStore documentStore = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        documentRevision = new DocumentRevision();
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_username = (EditText) findViewById(R.id.et_username);
        btn_signup = (Button) findViewById(R.id.btn);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String username = et_username.getText().toString().trim();
                Map<String, Object> data = new HashMap<>();
                data.put("email", email);
                data.put("password", password);
                data.put("username", username);
                DocumentRevision documentRevision = new DocumentRevision();
                File location = getDir("users", Context.MODE_PRIVATE);
                DocumentStore documentStore = null;
                try {
                    documentStore = DocumentStore.getInstance(new File(location, "users"));
                    URI url = new URI("https://apikey-v2-5yle01i6o8cl2goo7oyc509u9bw5olu9mompdlpbjze:b9b0b4175077ff81254308988b69dfd5@c260a3aa-fd10-4833-941c-49b6cf0026dc-bluemix.cloudantnosqldb.appdomain.cloud/users");
                    List<DocumentRevision> list = documentStore.database().read(0, documentStore.database().getDocumentCount(), true);
                    Boolean exists = false;
                    for (DocumentRevision documentRevision1 : list) {
                        Map<String, Object> eachdata = documentRevision1.getBody().asMap();
                        Log.d("Signup",eachdata.toString());
                        if (eachdata.size() !=0 && eachdata.get("email").toString().equals(email)) {
                            Toast.makeText(SignupActivity.this, "Already Registered Please login", Toast.LENGTH_SHORT).show();
                            exists = true;
                            break;
                        }

                    }
                    if (!exists) {
                        documentRevision.setBody(DocumentBodyFactory.create(data));
                        documentStore.database().create(documentRevision);
                        Replicator rep1 = ReplicatorBuilder.push().from(documentStore).to(url).build();
                        rep1.start();
                        Toast.makeText(SignupActivity.this, "Successfully Registered, please go back to login", Toast.LENGTH_SHORT).show();
                    }
                } catch (DocumentStoreNotOpenedException e) {
                    e.printStackTrace();
                } catch (DocumentStoreException e) {
                    e.printStackTrace();
                } catch (AttachmentException e) {
                    e.printStackTrace();
                } catch (ConflictException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e){
                        e.printStackTrace();

                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}