package umn.ac.id.nulis.Location;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

import umn.ac.id.nulis.Adapter.LocationAdapter;
import umn.ac.id.nulis.Dialog.AddDialogLocation;
import umn.ac.id.nulis.HelperClass.Location;
import umn.ac.id.nulis.R;

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
                Log.d("CARD CLICK", list.get(position).getTitle());
            }
            @Override
            public void onDeleteClick(int position) {
                Log.d("click", "click delete");
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Location loc = new Location(dataSnapshot.child("title").getValue().toString(),
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
                openDialog();
            }
        });
    }

    private void openDialog() {
        AddDialogLocation addDialog = new AddDialogLocation(bookId);
        addDialog.show(getSupportFragmentManager(), "add location");
    }

}
