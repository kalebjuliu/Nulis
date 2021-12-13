package ac.umn.id.nulis.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ac.umn.id.nulis.Dashboard;
import ac.umn.id.nulis.R;

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

        //firebase auth
        mAuth = FirebaseAuth.getInstance();
        reLogin();

        linkToRegister = findViewById(R.id.link_signup_btn);
        loginBtn = findViewById(R.id.login_btn);
        logEmail = findViewById(R.id.email);
        logPassword = findViewById(R.id.password);
        logProgBar = findViewById(R.id.log_progressBar);

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

    //re-login account after first time login
    private void reLogin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //refresh id token for login (refresh session)
            mAuth.getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(token -> {
                        startActivity(new Intent(Login.this, Dashboard.class));
                        finish();
                    })
                    .addOnFailureListener(error -> Log.e("Error", error.getMessage()));
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