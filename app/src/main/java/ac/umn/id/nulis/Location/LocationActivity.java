package ac.umn.id.nulis.Location;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ac.umn.id.nulis.Adapter.LocationAdapter;
import ac.umn.id.nulis.HelperClass.Location;
import ac.umn.id.nulis.R;

public class LocationActivity extends AppCompatActivity {

    String bookId;
    FloatingActionButton fabAddLoc;
    RecyclerView recyclerView;
    LocationAdapter locAdapter;

    DatabaseReference database;
    ArrayList<Location> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Locations");

        SharedPreferences sp1 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp1.getString("bId", null);

        fabAddLoc = findViewById(R.id.fab_add_location);
        recyclerView = findViewById(R.id.rv_location);
        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Location");

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        locAdapter = new LocationAdapter(this, list);
        recyclerView.setAdapter(locAdapter);
        locAdapter.setOnItemClickListener(new LocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("CARD CLICK", list.get(position).getlId());
                SharedPreferences spLocationId = getSharedPreferences("Location Info", MODE_PRIVATE);
                SharedPreferences.Editor Ed = spLocationId.edit();
                Ed.putString("lId", list.get(position).getlId());
                Ed.putString("lTitle", list.get(position).getTitle());
                Ed.apply();

                Intent intent = new Intent(getApplicationContext(), LocationDetailActivity.class);
                startActivity(intent);
            }
            @Override
            public void onDeleteClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child(list.get(position).getlId()).removeValue().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(LocationActivity.this, "Location is deleted", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(LocationActivity.this, "Failed to delete location", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage("Are you sure you want to delete this location?");
                builder.show();
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Location loc = new Location(
                            dataSnapshot.child("title").getValue().toString(),
                            dataSnapshot.child("description").getValue().toString(),
                            dataSnapshot.child("history").getValue().toString(),
                            dataSnapshot.child("design").getValue().toString(),
                            dataSnapshot.getKey());
                    list.add(loc);
                    Log.d("DEBUG", dataSnapshot.getKey());
                }
                locAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fabAddLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddLocationActivity.class);
                startActivity(intent);
            }
        });
    }

}
