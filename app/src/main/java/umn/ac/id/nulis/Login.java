package umn.ac.id.nulis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextInputLayout logEmail, logPassword;
    Button linkToRegister, loginBtn;
    ProgressBar logProgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        linkToRegister = findViewById(R.id.link_signup_btn);
        loginBtn = findViewById(R.id.login_btn);
        logEmail = findViewById(R.id.email);
        logPassword = findViewById(R.id.password);
        logProgBar = findViewById(R.id.log_progressBar);

//      Shared preferences untuk login credential
        reLogin();

        linkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    //relogin account after first time login
    private void reLogin() {
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);

        String savedEmail = sp1.getString("email", null);
        String savedPassword = sp1.getString("password", null);
        if (savedEmail != null && savedPassword != null) {
            logEmail.getEditText().setText(savedEmail);
            logPassword.getEditText().setText(savedPassword);

            mAuth.signInWithEmailAndPassword(savedEmail, savedPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //to home page
                    startActivity(new Intent(Login.this, Dashboard.class));
                    finish();
                }
            });
        }
    }

    private void loginUser() {
        String email = logEmail.getEditText().getText().toString().trim();
        String password = logPassword.getEditText().getText().toString().trim();

        //Check if inputs are empty
        if (email.isEmpty()) {
            logEmail.setError("Email is required!");
            logEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            logPassword.setError("Password is required!");
            logPassword.requestFocus();
            return;
        }

        //Check if email and password is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            logEmail.setError("Please provide a valid email!");
            logEmail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            logPassword.setError("Password must be at least 6 characters");
            logPassword.requestFocus();
            return;
        }

        logProgBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Save Login Credential
                SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("email", email);
                Ed.putString("password", password);
                Ed.apply();
                //notes : to logout just set shared preferences (email, password) to null

                //to home page
                startActivity(new Intent(Login.this, Dashboard.class));
                finish();
            } else {
                Toast.makeText(Login.this, "Failed to login, please check your credentials", Toast.LENGTH_LONG).show();
                logProgBar.setVisibility(View.GONE);
            }
        });


    }
}