package umn.ac.id.nulis.Character;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import umn.ac.id.nulis.HelperClass.Character;
import umn.ac.id.nulis.Location.EditLocationActivity;
import umn.ac.id.nulis.Location.LocationDetailActivity;
import umn.ac.id.nulis.R;

public class EditCharacterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String bookId, charaId,selectedRole;
    TextInputLayout editTitle, editDescription, editGoal, editOutcome, editStrength, editWeak, editSkill;
    Button editCharacter;

    DatabaseReference database;
    Query getCharaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_character);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Character");

        SharedPreferences sp1 = this.getSharedPreferences("Character Info", MODE_PRIVATE);
        charaId = sp1.getString("chId", null);

        SharedPreferences sp2 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp2.getString("bId", null);

        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        getCharaData = database.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("Book").
                child(bookId).
                child("Character");

        Spinner roleSpinner = findViewById(R.id.spinnerRole);
        ArrayAdapter<CharSequence> arrayAdapterRole = ArrayAdapter.createFromResource(this, R.array.role, R.layout.dropdown_item_role);
        arrayAdapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(arrayAdapterRole);
        roleSpinner.setOnItemSelectedListener(this);

        editTitle = findViewById(R.id.edit_character_title);
        editDescription = findViewById(R.id.edit_character_desc);
        editGoal = findViewById(R.id.edit_character_goal);
        editOutcome = findViewById(R.id.edit_character_outcome);
        editStrength = findViewById(R.id.edit_character_strength);
        editWeak = findViewById(R.id.edit_character_weakness);
        editSkill = findViewById(R.id.edit_character_skills);
        editCharacter = findViewById(R.id.editCharacter);

        getCharaData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(charaId)){
                        Log.d("MATCH", "TRUE");
                        Log.d("TESTING", dataSnapshot.child("title").getValue().toString());
                        editTitle.getEditText().setText(dataSnapshot.child("title").getValue().toString());
                        editDescription.getEditText().setText(dataSnapshot.child("description").getValue().toString());
                        editGoal.getEditText().setText(dataSnapshot.child("goal").getValue().toString());
                        editOutcome.getEditText().setText(dataSnapshot.child("outcome").getValue().toString());
                        editStrength.getEditText().setText(dataSnapshot.child("strength").getValue().toString());
                        editWeak.getEditText().setText(dataSnapshot.child("weakness").getValue().toString());
                        editSkill.getEditText().setText(dataSnapshot.child("skills").getValue().toString());
                        selectedRole = dataSnapshot.child("role").getValue().toString();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editCharTitle = editTitle.getEditText().getText().toString().trim();
                String editCharDescription = editDescription.getEditText().getText().toString().trim();
                String editCharGoal= editGoal.getEditText().getText().toString().trim();
                String editCharOutcome = editOutcome.getEditText().getText().toString().trim();
                String editCharStrength= editStrength.getEditText().getText().toString().trim();
                String editCharWeakness = editWeak.getEditText().getText().toString().trim();
                String editCharSkill = editSkill.getEditText().getText().toString().trim();

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("Users");

                Map<String, Object> postValues = new HashMap<String,Object>();
                postValues.put("title", editCharTitle);
                postValues.put("description", editCharDescription);
                postValues.put("goal", editCharGoal);
                postValues.put("outcome", editCharOutcome);
                postValues.put("strength", editCharStrength);
                postValues.put("weakness", editCharWeakness);
                postValues.put("skills", editCharSkill);
                postValues.put("role", selectedRole);

                myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Character").child(charaId).updateChildren(postValues);

                Toast.makeText(EditCharacterActivity.this, "Data berhasil di edit", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), CharacterDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("SPINNER", parent.getItemAtPosition(position).toString());
        selectedRole = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
