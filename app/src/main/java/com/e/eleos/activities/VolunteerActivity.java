package com.e.eleos.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.eleos.R;
import com.e.eleos.adapters.VolunteerFoodAdapter;
import com.e.eleos.models.DonateFood;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class VolunteerActivity extends AppCompatActivity {

    RecyclerView recyclerView,recyclerView2;
    VolunteerFoodAdapter adapter,adapter1;
    FirebaseFirestore firebaseFirestore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView1);
        recyclerView2 = (RecyclerView)findViewById(R.id.recyclerView2);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("fooddonate").whereEqualTo("isdelivered",false);
        FirestoreRecyclerOptions<DonateFood> donateFood = new FirestoreRecyclerOptions.Builder<DonateFood>().setQuery(query,DonateFood.class).build();
        adapter = new VolunteerFoodAdapter(this,donateFood);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Query query2 = firebaseFirestore.collection("ewastedonate").whereEqualTo("isdelivered",false);
        FirestoreRecyclerOptions<DonateFood> ewaste = new FirestoreRecyclerOptions.Builder<DonateFood>().setQuery(query2,DonateFood.class).build();
        adapter1 = new VolunteerFoodAdapter(this,ewaste);
        recyclerView2.setHasFixedSize(false);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(adapter1);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        adapter1.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter1.stopListening();
    }
}
