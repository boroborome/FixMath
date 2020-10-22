package pl.hypeapp.fixmath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import pl.hypeapp.fixmath.base.BaseGameUtils;



public class MenuActivity extends AppCompatActivity {

    ViewPager viewPager;

    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;
    private boolean mExplicitSignOut;
    ImageView googlePlayBtn;

    static SFXManager sfxManager;
    private MyProgress myProgress;
    ImageUtil imageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        imageUtil = (ImageUtil) getApplication();
        ImageView backgroundImage = (ImageView) findViewById(R.id.menu_background_image);
        imageUtil.setImageSecond(backgroundImage, R.drawable.menu_background);


        SharedPreferences sharedPref = getSharedPreferences("LOGGING", MODE_PRIVATE);
        mExplicitSignOut = sharedPref.getBoolean("SIGN_STATUS", true);

        googlePlayBtn = (ImageView) findViewById(R.id.sign_in_gp);
        if(mExplicitSignOut) {
            ImageView googlePlayBtn = (ImageView) findViewById(R.id.sign_in_gp);
            googlePlayBtn.setImageResource(R.drawable.play_button_loggin);
        }else{
            googlePlayBtn.setImageResource(R.drawable.play_button_to_loggin);
        }
        sharedPref = getSharedPreferences("SOUNDS", MODE_PRIVATE);

        sfxManager = new SFXManager(this, sharedPref.getBoolean("ISMUTE", false));

        ImageView v = (ImageView) findViewById(R.id.imageDot1);
        v.setImageResource(R.drawable.yellow_dot);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                manageDotToPage(position);
                manageTextAnimationToPage(position);
                sfxManager.NewLineFigureClickPlay();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(pagerAdapter);

        myProgress = MyProgress.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mExplicitSignOut) {
//            mGoogleApiClient.connect();
            googlePlayBtn.setImageResource(R.drawable.play_button_loggin);
        } else {
            googlePlayBtn.setImageResource(R.drawable.play_button_to_loggin);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
//                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_failure);

                SharedPreferences scorePref = getSharedPreferences("LOGGING", MODE_PRIVATE);
                SharedPreferences.Editor editor = scorePref.edit();
                editor.putBoolean("SIGN_STATUS", false);
                editor.commit();
            }
        } else {
            super.onActivityResult(requestCode, requestCode, intent);
        }
    }

    void manageDotToPage(int actualPage){
        switch (actualPage) {
            case 0:
                ImageView v = (ImageView) findViewById(R.id.imageDot1);
                v.setImageResource(R.drawable.yellow_dot);
                YoYo.with(Techniques.Landing)
                        .duration(1000)
                        .playOn(v);
                v = (ImageView) findViewById(R.id.imageDot2);
                v.setImageResource(R.drawable.gray_dot);
                v = (ImageView) findViewById(R.id.imageDot3);
                v.setImageResource(R.drawable.gray_dot);
                v = (ImageView) findViewById(R.id.imageDot4);
                v.setImageResource(R.drawable.gray_dot);
                break;
            case 1:
                v = (ImageView) findViewById(R.id.imageDot1);
                v.setImageResource(R.drawable.gray_dot);
                v = (ImageView) findViewById(R.id.imageDot2);
                v.setImageResource(R.drawable.yellow_dot);
                YoYo.with(Techniques.Landing)
                        .duration(1000)
                        .playOn(v);
                v = (ImageView) findViewById(R.id.imageDot3);
                v.setImageResource(R.drawable.gray_dot);
                v = (ImageView) findViewById(R.id.imageDot4);
                v.setImageResource(R.drawable.gray_dot);
                break;
            case 2:
                v = (ImageView) findViewById(R.id.imageDot1);
                v.setImageResource(R.drawable.gray_dot);
                v = (ImageView) findViewById(R.id.imageDot2);
                v.setImageResource(R.drawable.gray_dot);
                v = (ImageView) findViewById(R.id.imageDot3);
                v.setImageResource(R.drawable.yellow_dot);
                YoYo.with(Techniques.Landing)
                        .duration(1000)
                        .playOn(v);
                v = (ImageView) findViewById(R.id.imageDot4);
                v.setImageResource(R.drawable.gray_dot);
                break;
            case 3:
                v = (ImageView) findViewById(R.id.imageDot1);
                v.setImageResource(R.drawable.gray_dot);
                v = (ImageView) findViewById(R.id.imageDot2);
                v.setImageResource(R.drawable.gray_dot);
                v = (ImageView) findViewById(R.id.imageDot3);
                v.setImageResource(R.drawable.gray_dot);
                v = (ImageView) findViewById(R.id.imageDot4);
                v.setImageResource(R.drawable.yellow_dot);
                YoYo.with(Techniques.Landing)
                        .duration(1000)
                        .playOn(v);
                break;
        }
    }

    void manageTextAnimationToPage(int actualPage){

        switch (actualPage) {
            case 0:
                TextView v = (TextView) findViewById(R.id.arcade);
                YoYo.with(Techniques.Landing)
                        .duration(1000)
                        .playOn(v);
                break;
            case 1:
                v = (TextView) findViewById(R.id.time_attack);
                YoYo.with(Techniques.Landing)
                        .duration(1000)
                        .playOn(v);
                break;
            case 2:
                v = (TextView) findViewById(R.id.achivements_text);
                YoYo.with(Techniques.Landing)
                        .duration(1000)
                        .playOn(v);
                break;
            case 3:
                v = (TextView) findViewById(R.id.settings_text);
                YoYo.with(Techniques.Landing)
                        .duration(1000)
                        .playOn(v);
                break;
        }
    }

    public void openLevels(View view) {
        Intent i = new Intent(MenuActivity.this, LevelMenuActivity.class);
        startActivity(i);
        sfxManager.KeyboardClickPlay(true);
        finish();
    }

    public void openChallengeTimeChoose(View view){
        Intent i = new Intent(MenuActivity.this, ChooseTimeChallenge.class);
        startActivity(i);
        sfxManager.KeyboardClickPlay(true);
        finish();
    }

    public void openSettings(View view) {
        Intent i = new Intent(MenuActivity.this, SettingsActivity.class);
        startActivity(i);
        sfxManager.KeyboardClickPlay(true);
        finish();

    }

    public void openAchivementsList(View view) {
        sfxManager.KeyboardClickPlay(true);
        SharedPreferences sharedPref = getSharedPreferences("LOGGING", MODE_PRIVATE);
        if(!sharedPref.getBoolean("SIGN_STATUS", true)) {
            mSignInClicked = true;
//            mGoogleApiClient.connect();
        }else{
//            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), 1);
        }
    }

    public void share(View view) {

        sfxManager.KeyboardClickPlay(true);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share));
        startActivity(Intent.createChooser(intent, "Share"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageUtil.unbindDrawables(findViewById(R.id.menu_layout));
        System.gc();
    }
}


