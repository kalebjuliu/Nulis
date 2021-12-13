package ac.umn.id.nulis.Character;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ac.umn.id.nulis.HelperClass.Character;
import ac.umn.id.nulis.R;

public class AddCharacterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String bookId, selectedRole;
    TextInputLayout addTitle, addDescription, addGoal, addOutcome, addStrength, addWeak, addSkill;
    Button addCharacter, uploadCharacter, selectCharacter;

    Uri imgUri;
    String uploadedImgUri;
    ImageView previewIv;

    StorageReference reference = FirebaseStorage.getInstance("gs://nulis-d3354.appspot.com").getReference();

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

        selectCharacter = findViewById(R.id.selectCharacter);
        previewIv = findViewById(R.id.characterFaceIv);
        selectCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        addCharacter.setOnClickListener(new View.OnClickListener() {
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
                                    uploadedImgUri = uri.toString();
                                    Log.d("URL", uploadedImgUri);
                                    Toast.makeText(AddCharacterActivity.this, "Upload success", Toast.LENGTH_SHORT).show();
                                    previewIv.setImageResource(R.drawable.ic_baseline_face_24);

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
                                            uploadedImgUri
                                    );

                                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Character").push().setValue(character);

                                    finish();
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            Toast.makeText(AddCharacterActivity.this, "Upload on progress", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddCharacterActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(AddCharacterActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
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
            previewIv.setImageURI(imgUri);
        }
    }
}