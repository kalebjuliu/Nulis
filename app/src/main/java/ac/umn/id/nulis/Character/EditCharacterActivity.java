package ac.umn.id.nulis.Character;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import ac.umn.id.nulis.R;

public class EditCharacterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String bookId, charaId,selectedRole;
    TextInputLayout editTitle, editDescription, editGoal, editOutcome, editStrength, editWeak, editSkill;
    Button editCharacter, selectCharacter;

    DatabaseReference database;
    Query getCharaData;

    ImageView editCharaIv;
    String uploadedImgUri, oldImgUrl;
    Uri imgUri;

    FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://nulis-d3354.appspot.com");

    StorageReference reference = FirebaseStorage.getInstance("gs://nulis-d3354.appspot.com").getReference();

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

        editCharaIv = findViewById(R.id.characterFaceIv_Edit);
        selectCharacter = findViewById(R.id.selectCharacter_Edit);
        selectCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

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

                        Glide.with(getApplicationContext()).load(dataSnapshot.child("imgUrl").getValue().toString()).into(editCharaIv);
                        oldImgUrl = dataSnapshot.child("imgUrl").getValue().toString();
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
                if(imgUri != null){
                    StorageReference fileRef = reference.child(System.currentTimeMillis() + '.' + getFileExtension(imgUri));
                    fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    StorageReference oldImgRef = mStorage.getReferenceFromUrl(oldImgUrl);
                                    oldImgRef.delete();

                                    uploadedImgUri = uri.toString();
                                    Log.d("URL", uploadedImgUri);
                                    Toast.makeText(EditCharacterActivity.this, "Upload success", Toast.LENGTH_SHORT).show();

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
                                    postValues.put("imgUrl", uploadedImgUri);

                                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Character").child(charaId).updateChildren(postValues);

                                    Toast.makeText(getApplicationContext(), "Data berhasil di edit", Toast.LENGTH_LONG).show();

                                    editCharaIv.setImageResource(R.drawable.ic_baseline_face_24);
                                    finish();
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            Toast.makeText(EditCharacterActivity.this, "Upload on progress", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditCharacterActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(EditCharacterActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }

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
    private String getFileExtension(Uri imgUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imgUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imgUri = data.getData();
            editCharaIv.setImageURI(imgUri);
        }
    }
}
