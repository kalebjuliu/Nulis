package ac.umn.id.nulis.Character;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import ac.umn.id.nulis.Adapter.CharacterAdapter;
import ac.umn.id.nulis.HelperClass.Character;
import ac.umn.id.nulis.R;

public class CharacterActivity extends AppCompatActivity {

    String bookId;
    FloatingActionButton fabAddChar;
    RecyclerView recyclerView;
    CharacterAdapter characterAdapter;

    DatabaseReference database;
    ArrayList<Character> list;

    FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://nulis-d3354.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Characters");

        SharedPreferences sp1 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp1.getString("bId", null);

        fabAddChar = findViewById(R.id.fab_add_character);
        recyclerView = findViewById(R.id.rv_character);
        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Character");

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        characterAdapter = new CharacterAdapter(this, list);
        recyclerView.setAdapter(characterAdapter);
        characterAdapter.setOnItemClickListener(new CharacterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("CARD CLICK", list.get(position).getChId());
                SharedPreferences spLocationId = getSharedPreferences("Character Info", MODE_PRIVATE);
                SharedPreferences.Editor Ed = spLocationId.edit();
                Ed.putString("chId", list.get(position).getChId());
                Ed.putString("chTitle", list.get(position).getTitle());
                Ed.apply();

                Intent intent = new Intent(getApplicationContext(), CharacterDetailActivity.class);
                startActivity(intent);
            }
            @Override
            public void onDeleteClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CharacterActivity.this);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StorageReference oldImgRef = mStorage.getReferenceFromUrl(list.get(position).getImgUrl());
                        oldImgRef.delete();
                        database.child(list.get(position).getChId()).removeValue().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(CharacterActivity.this, "Character is deleted", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(CharacterActivity.this, "Failed to delete character", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage("Are you sure you want to delete this character?");
                builder.show();
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Character character = new Character(
                            dataSnapshot.child("title").getValue().toString(),
                            dataSnapshot.child("description").getValue().toString(),
                            dataSnapshot.child("role").getValue().toString(),
                            dataSnapshot.child("goal").getValue().toString(),
                            dataSnapshot.child("outcome").getValue().toString(),
                            dataSnapshot.child("strength").getValue().toString(),
                            dataSnapshot.child("weakness").getValue().toString(),
                            dataSnapshot.child("skills").getValue().toString(),
                            dataSnapshot.child("imgUrl").getValue().toString(),
                            dataSnapshot.getKey()
                    );
                    list.add(character);
                    Log.d("DEBUG", dataSnapshot.getKey());
                }
                characterAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fabAddChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddCharacterActivity.class);
                startActivity(intent);
            }
        });
    }

}