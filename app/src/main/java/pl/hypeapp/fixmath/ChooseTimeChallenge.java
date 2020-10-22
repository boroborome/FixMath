package pl.hypeapp.fixmath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pl.hypeapp.fixmath.base.BaseGameActivity;
import pl.hypeapp.fixmath.base.BaseGameUtils;

public class ChooseTimeChallenge extends BaseGameActivity {
    private final int
            ONE_MINUTE = 60,
            THREE_MINUTES = 180,
            FIVE_MINTUES = 300;

    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mExplicitSignOut;
    SFXManager sfxManager;

    ImageUtil imageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time_challenge);

        ImageView background = (ImageView)findViewById(R.id.choose_time_background_image);
        imageUtil = (ImageUtil)getApplication();
        imageUtil.setImageFirst(background, R.drawable.level_background);

        SharedPreferences scorePref = getSharedPreferences("SCORE", MODE_PRIVATE);


        TextView oneMinuteBestScore = (TextView) findViewById(R.id.score1);
        TextView threeMinuteBestScore = (TextView) findViewById(R.id.score3);
        TextView fiveMinuteBestScore = (TextView) findViewById(R.id.score5);

        oneMinuteBestScore.setText(scorePref.getString("BEST_SCORE_STRING" + ONE_MINUTE, "0"));
        threeMinuteBestScore.setText(scorePref.getString("BEST_SCORE_STRING" + THREE_MINUTES, "0"));
        fiveMinuteBestScore.setText(scorePref.getString("BEST_SCORE_STRING" + FIVE_MINTUES, "0"));

        getGameHelper().setConnectOnStart(false);

        SharedPreferences soundsPref = getSharedPreferences("SOUNDS", MODE_PRIVATE);
        sfxManager = new SFXManager(this, soundsPref.getBoolean("ISMUTE", false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        imageUtil.unbindDrawables(findViewById(R.id.choose_time_layout));
        System.gc();
    }


    public void backToMenu(View view){
        Intent i = new Intent(ChooseTimeChallenge.this, MenuActivity.class);
        startActivity(i);
        sfxManager.KeyboardClickPlay(true);
        finish();
    }

    public void FiveMinChallenge(View view) {
        Intent i = new Intent(ChooseTimeChallenge.this, TimeAttackActivity.class);
        i.putExtra("challenge", FIVE_MINTUES);
        sfxManager.SameLineFigureClickPlay();
        startActivity(i);
        finish();
    }

    public void ThreeMinChallenge(View view) {
        Intent i = new Intent(ChooseTimeChallenge.this, TimeAttackActivity.class);
        i.putExtra("challenge", THREE_MINUTES);
        sfxManager.SameLineFigureClickPlay();
        startActivity(i);
        finish();
    }

    public void OneMinChallenge(View view) {
        Intent i = new Intent(ChooseTimeChallenge.this, TimeAttackActivity.class);
        i.putExtra("challenge", ONE_MINUTE);
        sfxManager.SameLineFigureClickPlay();
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ChooseTimeChallenge.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    public void openLeaderboard(View view) {
        sfxManager.KeyboardClickPlay(true);
        SharedPreferences sharedPref = getSharedPreferences("LOGGING", MODE_PRIVATE);
//        if(!sharedPref.getBoolean("SIGN_STATUS", true)) {
//            mSignInClicked = true;
//            mGoogleApiClient.connect();
//        }else{
//            mSignInClicked = true;
//            mGoogleApiClient.connect();
//            startActivityForResult(
//            Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient), 1);
//        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        SharedPreferences sharedPref = getSharedPreferences("LOGGING", MODE_PRIVATE);
//        if(sharedPref.getBoolean("SIGN_STATUS", true)){
//            mGoogleApiClient.connect();
//        }
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        mGoogleApiClient.disconnect();
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
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
}
