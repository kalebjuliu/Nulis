package umn.ac.id.nulis.Character;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import umn.ac.id.nulis.HelperClass.Character;
import umn.ac.id.nulis.R;

public class AddCharacterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String bookId, selectedRole;
    TextInputLayout addTitle, addDescription, addGoal, addOutcome, addStrength, addWeak, addSkill;
    Button addCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_character);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create character");

        SharedPreferences sp1 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp1.getString("bId", null);

        Spinner roleSpinner = findViewById(R.id.spinnerRole);
        ArrayAdapter<CharSequence> arrayAdapterRole = ArrayAdapter.createFromResource(this, R.array.role, R.layout.dropdown_item_role);
        arrayAdapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(arrayAdapterRole);
        roleSpinner.setOnItemSelectedListener(this);

        addTitle = findViewById(R.id.add_character_title);
        addDescription = findViewById(R.id.add_character_desc);
        addGoal = findViewById(R.id.add_character_goal);
        addOutcome = findViewById(R.id.add_character_outcome);
        addStrength = findViewById(R.id.add_character_strength);
        addWeak = findViewById(R.id.add_character_weakness);
        addSkill = findViewById(R.id.add_character_skills);
        addCharacter = findViewById(R.id.addCharacter);

        addCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addCharTitle = addTitle.getEditText().getText().toString().trim();
                String addCharDescription = addDescription.getEditText().getText().toString().trim();
                String addCharGoal= addGoal.getEditText().getText().toString().trim();
                String addCharOutcome = addOutcome.getEditText().getText().toString().trim();
                String addCharStrength= addStrength.getEditText().getText().toString().trim();
                String addCharWeakness = addWeak.getEditText().getText().toString().trim();
                String addCharSkill = addSkill.getEditText().getText().toString().trim();



                FirebaseDatabase database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("Users");

                Character character = new Character(
                        addCharTitle,
                        addCharDescription,
                        selectedRole,
                        addCharGoal,
                        addCharOutcome,
                        addCharStrength,
                        addCharWeakness,
                        addCharSkill,
                        "");

                myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Character").push().setValue(character);

                finish();
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