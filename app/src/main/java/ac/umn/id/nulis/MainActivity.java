package ac.umn.id.nulis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import ac.umn.id.nulis.Authentication.Login;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 2500;

    Animation topAnim, botAnim;
    ImageView nulis_splashImg;
    TextView nulis_splashTitle, nulis_splashSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        nulis_splashImg = findViewById(R.id.nulish_imagesplash);
        nulis_splashTitle = findViewById(R.id.nulis_title);
        nulis_splashSubTitle = findViewById(R.id.nulis_subtitle);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        nulis_splashImg.setAnimation(topAnim);
        nulis_splashTitle.setAnimation(botAnim);
        nulis_splashSubTitle.setAnimation(botAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}