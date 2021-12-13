package ac.umn.id.nulis.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ac.umn.id.nulis.HelperClass.User;
import ac.umn.id.nulis.R;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextInputLayout regName, regEmail, regPass;
    Button linkToLogin, signupBtn;
    ProgressBar regProgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        linkToLogin = findViewById(R.id.link_login_btn);
        signupBtn = findViewById(R.id.signup_btn);
        regName = findViewById(R.id.regis_name);
        regEmail = findViewById(R.id.regis_email);
        regPass = findViewById(R.id.regis_password);
        regProgBar = findViewById(R.id.regis_progressBar);


        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        String email = regEmail.getEditText().getText().toString().trim();
        String password = regPass.getEditText().getText().toString().trim();
        String name = regName.getEditText().getText().toString().trim();

        //Check if inputs are empty
        if(name.isEmpty()){
            regName.setError("Full name is required!");
            regName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            regEmail.setError("Email is required!");
            regEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            regPass.setError("Password is required!");
            regPass.requestFocus();
            return;
        }

        //Check if email and password is valid
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            regEmail.setError("Please provide a valid email!");
            regEmail.requestFocus();
            return;
        }
        if(password.length() < 6){
            regPass.setError("Password must be at least 6 characters");
            regPass.requestFocus();
            return;
        }

        regProgBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){
                        User user = new User(name, email);

                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/");
                        DatabaseReference myRef = database.getReference("Users");

                        myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Toast.makeText(Register.this, "User has been created", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(Register.this, "Failed to create user", Toast.LENGTH_LONG).show();
                                    }
                            regProgBar.setVisibility(View.GONE);
                        });
                    }else{
                        Toast.makeText(Register.this, "Failed to create user", Toast.LENGTH_LONG).show();
                        regProgBar.setVisibility(View.GONE);
                    }
                });
    }
}