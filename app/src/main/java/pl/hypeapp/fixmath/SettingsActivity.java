package pl.hypeapp.fixmath;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import pl.hypeapp.fixmath.base.BaseGameActivity;


public class SettingsActivity extends BaseGameActivity {

    private boolean signStatusGoogleGames;

    private static int RC_SIGN_IN = 9002;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;
    TextView LogInText;
    Switch toggle;
    SFXManager sfxManager;
    MyProgress myProgress;
    ImageUtil imageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView background = (ImageView) findViewById(R.id.setting_background_image);
        imageUtil = (ImageUtil)getApplication();
        imageUtil.setImageFirst(background, R.drawable.time_attack_background_normal);

        getGameHelper().setConnectOnStart(false);



        SharedPreferences sharedPref = getSharedPreferences("LOGGING", MODE_PRIVATE);
        this.signStatusGoogleGames = sharedPref.getBoolean("SIGN_STATUS", true);

        LogInText = (TextView) findViewById(R.id.signInText);

        if(signStatusGoogleGames) {
            LogInText.setText("GOOGLE PLAY GAMES LOG OUT");
        }else {
            LogInText.setText("GOOGLE PLAY GAMES LOG IN");
        }

        toggle = (Switch) findViewById(R.id.sounds);
        final SharedPreferences sharedPreferences = getSharedPreferences("SOUNDS", MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("ISMUTE", false)){
            toggle.setChecked(true);
        }else{
            toggle.setChecked(false);
        }


        sfxManager = new SFXManager(this, sharedPreferences.getBoolean("ISMUTE", false));
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    editor.putBoolean("ISMUTE", false);
                    editor.commit();


                } else {
                    editor.putBoolean("ISMUTE", true);
                    editor.commit();

                }
            }
        });
        myProgress = MyProgress.getInstance();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageUtil.unbindDrawables(findViewById(R.id.settings_layout));
        System.gc();
    }

    public void backToMenu(View view) {
        sfxManager.KeyboardClickPlay(true);
        Intent i = new Intent(SettingsActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    public void signinGoogleGames(View view) {
        sfxManager.KeyboardClickPlay(true);
        if(signStatusGoogleGames){


            SharedPreferences scorePref = getSharedPreferences("LOGGING", MODE_PRIVATE);
            SharedPreferences.Editor editor = scorePref.edit();
            editor.putBoolean("SIGN_STATUS", false);
            signStatusGoogleGames = false;
            editor.commit();

            LogInText.setText("GOOGLE PLAY GAMES LOG IN");

        }else{
            mSignInClicked = true;
        }
    }

    public void resetGame(View view){
        SharedPreferences prefs = getSharedPreferences("LVL", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

        prefs = getSharedPreferences("LEVEL_COUNT", MODE_PRIVATE);
        editor = prefs.edit();
        editor.clear();
        editor.commit();

        prefs = getSharedPreferences("SCORE", MODE_PRIVATE);
        editor = prefs.edit();
        editor.clear();
        editor.commit();

        sfxManager.KeyboardClickPlay(true);

        Toast.makeText(this, getString(R.string.reset),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SettingsActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    public void setOnCheckedChangeListener(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("SOUNDS", MODE_PRIVATE);
        sfxManager = new SFXManager(this, sharedPreferences.getBoolean("ISMUTE", false));
    }
}
