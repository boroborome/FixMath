package pl.hypeapp.fixmath;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

;

public class SplashScreenActivity extends AppCompatActivity {

    private static int TIME_RUNNABLE = 3000;
    ImageUtil imageUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageUtil = (ImageUtil) getApplication();

        ImageView splashScreen = (ImageView)findViewById(R.id.splash_screen_imageview);
        imageUtil.setImageFirst(splashScreen, R.drawable.splash_screen );

        YoYo.with(Techniques.Pulse)
                .delay(700)
                .duration(800)
                .playOn(splashScreen);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashScreenActivity.this, MenuActivity.class);
                startActivity(i);

                finish();


            }
        }, TIME_RUNNABLE);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageUtil.unbindDrawables(findViewById(R.id.splash_screen));
        System.gc();
    }



}

