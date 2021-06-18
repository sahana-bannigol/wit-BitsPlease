package com.e.eleos.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.e.eleos.R;
import com.e.eleos.models.DonateFood;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class VolunteerFoodAdapter extends FirestoreRecyclerAdapter<DonateFood,VolunteerFoodAdapter.DonateViewHolder> {

    Context context;
    public VolunteerFoodAdapter(Context context, FirestoreRecyclerOptions<DonateFood> donateFood){
        super(donateFood);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull VolunteerFoodAdapter.DonateViewHolder holder, int position, @NonNull DonateFood model) {
        String order = model.getUsername()+" has booked a service";
        holder.setIsRecyclable(false);
        holder.tv_order.setText(order);
        if(model.isdelivered){
            holder.delivered.setText("Delivered");
        }
        else{
            holder.delivered.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference docRef = firebaseFirestore.collection("fooddonate").document(model.documentId);
                    docRef.update("isdelivered",true);
                    holder.delivered.setText("Delivered");
                }
            });
        }
        holder.tv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = "google.navigation:q="+(model.getLatitude()+"")+","+(model.getLongitude()+"");
                Uri gmmIntentUri = Uri.parse(loc);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                }
            }
        });
    }

    @NonNull
    @Override
    public VolunteerFoodAdapter.DonateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donate_container,parent,false);
        return new DonateViewHolder(view);

    }
    public class DonateViewHolder extends RecyclerView.ViewHolder{
        TextView tv_order;
        Button delivered;
        public DonateViewHolder(View itemView){
            super(itemView);
            tv_order = (TextView) itemView.findViewById(R.id.textView1);
            delivered = (Button) itemView.findViewById(R.id.button2);
        }
    }
}
