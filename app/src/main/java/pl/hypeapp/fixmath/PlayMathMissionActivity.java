package pl.hypeapp.fixmath;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.hypeapp.fixmath.base.BaseGameActivity;
import pl.hypeapp.fixmath.factory.ChildMathFactory;
import pl.hypeapp.fixmath.model.Figures;
import pl.hypeapp.fixmath.model.MathMission;

//import com.nineoldandroids.animation.Animator;


public class PlayMathMissionActivity extends BaseGameActivity {
    private static class AnswerInfo {
        public final TextView view;
        public int currentValue;
        public final MathMission.MathAnswer answer;

        public AnswerInfo(MathMission.MathAnswer answer, TextView view) {
            this.answer = answer;
            this.view = view;
        }
    }
    private Map<String, AnswerInfo> answers = new HashMap<>();

    private Map<String, List<TextView>> variableViewDiction = new HashMap<>();

    private MathMission mathMission;

    public int ClickOn, calcLineSwitch, Up, Down, LineID;
    public View BlankPointer = null;
    public boolean clicked;
    private boolean isKeyboradOpen = false;
    public ArrayList<TextView> TextViews;
    Keyboard2 keyboard2;

    public Logic logic;
    ArrayList<Integer> clickAbleLine = new ArrayList<>();
    int howManyLines = 0, howManyLinesCorrectLines = 0;
    private int passedLinesIndexer = 0;
    public int TextViewX, TextViewY;
    public boolean theSameLine = false;
    public int parentId;
    private int[] calcID = new int[5];
    private Level level;

    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    SFXManager sfxManager;
    MyProgress myProgress;
    int levelActual1;
    ImageUtil imageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_math);

//        ImageView background = (ImageView) findViewById(R.id.play_background_image);
        imageUtil = (ImageUtil) getApplication();
//        imageUtil.setImageSecond(background, R.drawable.arcade_background);
        YoYo.with(Techniques.FadeIn).duration(500).playOn(findViewById(R.id.PlayAct));


        Bundle bundle = getIntent().getExtras();
        levelActual1 = bundle.getInt("LEVEL", 1);

        level = new Level(levelActual1);
        mathMission = new ChildMathFactory().createMisson(levelActual1);
        initAnswers(mathMission);

        getGameHelper().setConnectOnStart(false);
//        googleApiClient = getApiClient();
//        googleApiClient.registerConnectionCallbacks(this);
//        googleApiClient.registerConnectionFailedListener(this);

//        TextViews = new ArrayList<>();
//        View calucationsAll = findViewById(R.id.calculations);
//        setClickableRecursive(calucationsAll, false);




//        for(int i = 0; i < level.HowManyLines ; i++){
//            Calculations calculations = new Calculations(
//                    level.GetLineStart(i),
//                    level.GetColumnStart(i),
//                    level.GetVariables(i),
//                    level.GetSymbols(i),
//                    level.GetResult(i));
//            FillCalculationInLine(calculations);
//            SetClickAbleBlanks(calculations, level.GetFigures(i));
//        }
//
//        SharedPreferences sharedPref = getSharedPreferences("SOUNDS", MODE_PRIVATE);
//        sfxManager = new SFXManager(this, sharedPref.getBoolean("ISMUTE", false));
//
//
//
//        LinesSet();
//        SetResetKeyboard();
//        SetUpDown(level.GetUpLine(), level.GetDownLine());

        keyboard2 = new Keyboard2(this, level.GetUpLine(), level.GetDownLine(), sfxManager);
//
//
//        logic = new Logic();
//        for(int i = 0, x = level.GetUpLine(); i < level.HowManyLines; i++, x++) {
//            String[] result = Arrays.copyOf(level.GetVariables(i), level.GetVariables(i).length);//new String[level.GetVariables(i).length];
//            String[] fixgure = level.GetFigures(i);
//            for (int index = 0; index < result.length; index++) {
//                if (fixgure[index] != "q") {
//                    result[index] = "";
//                }
//            }
//
//            logic.setLogic(level.GetUpLine(), x, result);
//        }
//        setCorrectFrameFigures();
//
//
//
//        myProgress = MyProgress.getInstance();
//
//        setAcutalLevelTextView(levelActual1);

        // setupIntersitialAds(levelActual1);

    }

    private void initAnswers(MathMission mathMission) {
        LinearLayout answerView = (LinearLayout) this.findViewById(R.id.answerView);
        for (MathMission.MathAnswer answer : mathMission.getMathAnswers()) {
            TextView view = (TextView) getLayoutInflater().inflate(R.layout.play_answer_component, null);
            int imageResId = Figures.getFigure(mathMission.varFigure.get(answer.varName)).id;
            view.setBackgroundResource(imageResId);
            answers.put(answer.varName, new AnswerInfo(answer, view));
            answerView.addView(view);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        imageUtil.unbindDrawables(findViewById(R.id.PlayAct));
        System.gc();

    }

    private void showNextLevelOrClose(int levelActual) {

        if(isNextLevel(levelActual)){

            Intent i = new Intent(PlayMathMissionActivity.this, PlayMathMissionActivity.class);
            i.putExtra("LEVEL", levelActual + 1);
            startActivity(i);
            finish();

        }else{

            Intent x = new Intent(PlayMathMissionActivity.this, LevelMenuActivity.class);
            startActivity(x);
            finish();
        }
    }

    boolean isNextLevel(int levelActual1){
        if(levelActual1 + 1 <= 100){
            return true;
        }else{
            return false;
        }
    }

    public void NextLevelBtnClick(View view){
        sfxManager.KeyboardClickPlay(true);
//        if(!showIntersitialAdOnNextLevel(intersitialAdOnNextLevel, levelActual1)) {
            showNextLevelOrClose(levelActual1);
//        }
    }

    void setAcutalLevelTextView(int acutalLevel){
        TextView textView = (TextView) findViewById(R.id.levelLine);
        textView.setText("LEVEL " + acutalLevel);
    }


    private void setCorrectFrameFigures(){
        TextView correctFigure;
        int correctFigures = this.level.CorrectFigures.size();

        for (int i = 0, x = 1; i < correctFigures ; i++, x++){
            String TextViewID = "a_correctFigure_" + x;
            int correctFigureID = getResources().getIdentifier(TextViewID, "id", getPackageName());
            correctFigure = (TextView) findViewById(correctFigureID);
            correctFigure.setVisibility(View.VISIBLE);
            setCorrectFrameFigure(correctFigure, i);
        }

    }

    private void setCorrectFrameFigure(TextView correctFrameFigure, int index){
        String code = level.GetTimeAttackCorrectFigures(index);
        int backgroundResId = Figures.getFigure(code).backgroundId;
        correctFrameFigure.setBackgroundResource(backgroundResId);
    }

    private static int[] correctFigureIds = new int[] {
            R.id.a_correctFigure_1,
            R.id.a_correctFigure_2,
            R.id.a_correctFigure_3,
            R.id.a_correctFigure_4,
            R.id.a_correctFigure_5
    };
    private void addCorrectFigure(){
        int correctFigureId = correctFigureIds[passedLinesIndexer - 1];

        TextView correctFigure = (TextView) findViewById(correctFigureId);
        correctFigure.setText(level.GetResultTexts(passedLinesIndexer - 1));
        setCorrectFigure(correctFigure);
    }

    public void setCorrectFigure(final TextView correctFigure){

        correctFigure.setVisibility(View.INVISIBLE);
        String code = level.GetTimeAttackCorrectFigures(passedLinesIndexer - 1);
        int backgroundResource = Figures.getFigure(code).backgroundId;

        correctFigure.setBackgroundResource(backgroundResource);
        blockGoodAnswerFigures(code);

        YoYo.with(Techniques.ZoomIn)
                .duration(500)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        correctFigure.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(correctFigure);


    }

    void animClickFigures(String figureType){

        for (int i = Up; i <= Down; i++) {
            for (int x = 0; x <= 4; x++) {
                String TextViewId = "var" + i + "x" + x;
                int id = getResources().getIdentifier(TextViewId, "id", getPackageName());
                TextView allTextContainer = (TextView) findViewById(id);
                if(allTextContainer.getTag() != null) {
                    String containerTags = allTextContainer.getTag().toString();

                    if (containerTags.equals(figureType)) {
                        YoYo.with(Techniques.RubberBand)
                                .duration(500)
                                .playOn(allTextContainer);
                    }
                }

            }
        }
    }

    void blockGoodAnswerFigures(String figureType){

            for (int i = Up; i <= Down; i++) {
                for (int x = 0; x <= 4; x++) {
                    String TextViewId = "var" + i + "x" + x;
                    int id = getResources().getIdentifier(TextViewId, "id", getPackageName());
                    TextView allTextContainer = (TextView) findViewById(id);
                    if(allTextContainer.getTag() != null) {
                        String containerTags = allTextContainer.getTag().toString();

                        if (containerTags.equals(figureType)) {
                            SetFiguresColor(allTextContainer, figureType);
                            allTextContainer.setEnabled(false);
                        }
                    }

                }
            }

    }

    public void SetFiguresColor(TextView textView, String figureType){

            switch(figureType){
                case "k":
                    textView.setTextColor(ContextCompat.getColor(this, R.color.kwadrat_txt_kolor));
                    break;
                case "rf":
                    textView.setTextColor(ContextCompat.getColor(this, R.color.rombf_txt_kolor));
                    break;
                case "oz":
                    textView.setTextColor(ContextCompat.getColor(this, R.color.skat_txt_kolor));
                    break;
                case "ok":
                    textView.setTextColor(ContextCompat.getColor(this, R.color.okat_txt_kolor));
                    break;
                case "o":
                    textView.setTextColor(ContextCompat.getColor(this, R.color.okrag_txt_kolor));
                    break;
                case "kb":
                    textView.setTextColor(ContextCompat.getColor(this, R.color.kwadrat_blue_txt_kolor));
                    break;
                case "s":
                    textView.setTextColor(ContextCompat.getColor(this, R.color.skat_txt_kolor));
                    break;
                case "rg":
                    textView.setTextColor(ContextCompat.getColor(this, R.color.romb_green_txt_kolor));
                    break;
                case "kf":
                    textView.setTextColor(ContextCompat.getColor(this, R.color.kwadrat_f_kolor_txt));
                    break;
                case "r":
                    textView.setTextColor(ContextCompat.getColor(this, R.color.romb_txt_color));
                    break;
                case "q":
                    textView.setTextColor(ContextCompat.getColor(this, R.color.question_txt_kolor));
                    break;
            }
    }

    public void FillCalculationInLine(Calculations calculations){
        int symbolID, resultID;
        TextView textView;

        for(int x = calculations.WhichColumnStart, i = 0; i < calculations.SymbolsCount; i++, x++){
            String textViewId = "symbol" + calculations.WhichLine + "x" + x;
            symbolID = getResources().getIdentifier(textViewId, "id", getPackageName());
            textView = (TextView) findViewById(symbolID);
            textView.setVisibility(View.VISIBLE);
            textView.setText(calculations.SymbolList.get(i));
        }

        int endStart = calculations.WhichColumnStart + calculations.SymbolsCount;
        String textViewId = "symbol" + calculations.WhichLine + "x" + endStart;
        symbolID = getResources().getIdentifier(textViewId, "id", getPackageName());
        textView = (TextView) findViewById(symbolID);
        textView.setVisibility(View.VISIBLE);
        textView.setText("=");

        String resultTextViewId = "var" + calculations.WhichLine + "x" + (endStart + 1);
        resultID = getResources().getIdentifier(resultTextViewId, "id", getPackageName());
        textView = (TextView) findViewById(resultID);
        textView.setVisibility(View.VISIBLE);

        if(calculations.Result.length() >= 2){
            textView.setTextSize(20);
        }
        if(calculations.Result.length() >= 4){
            textView.setTextSize(15);
        }
        if(calculations.Result.length() >= 5){
            textView.setTextSize(12);
        }
        textView.setText(calculations.Result);

        int idParent = ((View)textView.getParent()).getId();
        ArrayIdLinesFill();



        howManyLines++;


    }

    public void LinesSet(){
        if(howManyLines == 4){
            View v = findViewById(R.id.calculations);
            RelativeLayout.LayoutParams vParam = (RelativeLayout.LayoutParams) v.getLayoutParams();
            vParam.topMargin = 120;
            v.setLayoutParams(vParam);
        }
    }

    public void ArrayIdLinesFill(){
        View calcIdView = findViewById(R.id.Line1);
        calcID[0] = calcIdView.getId();
        calcIdView = findViewById(R.id.Line2);
        calcID[1] = calcIdView.getId();
        calcIdView = findViewById(R.id.Line3);
        calcID[2] = calcIdView.getId();
        calcIdView = findViewById(R.id.Line4);
        calcID[3] = calcIdView.getId();
        calcIdView = findViewById(R.id.Line5);
        calcID[4] = calcIdView.getId();
    }

    public void SetOffClickableLine(){

        if(clickAbleLine.contains(1)){
            View v1 = findViewById(R.id.Line1);
            setClickableRecursive(v1, false);
        }if(clickAbleLine.contains(2)){
            View v1 = findViewById(R.id.Line2);
            setClickableRecursive(v1, false);
        }if(clickAbleLine.contains(3)){
            View v1 = findViewById(R.id.Line3);
            setClickableRecursive(v1, false);
        }if(clickAbleLine.contains(4)){
            View v1 = findViewById(R.id.Line4);
            setClickableRecursive(v1, false);
        }if(clickAbleLine.contains(5)){
            View v1 = findViewById(R.id.Line5);
            setClickableRecursive(v1, false);
        }

    }

    public void KeyClick(View keyClick) {
        if (clicked) {
            int keyClickID = keyClick.getId();
            TextView textView = (TextView) findViewById(ClickOn);
            int[] ID = new int[12];


            for (int i = 0; i <= 11; i++) {
                String TextViewId = "k" + i;
                ID[i] = getResources().getIdentifier(TextViewId, "id", getPackageName());
                String txt = textView.getText().toString();
                if (keyClickID == ID[i]) {
                    if (i <= 9){

                        if (i == 0){
                            if(txt.equals("")){
                                ErrorKeyAnim();
                                return;
                            }
                        }


                        if(textView.getText().length() > 2){
                            ErrorKeyAnim();
                        }else{
                            keyboard2.PrintNumbersInFigures( i, textView);
                            WriteBlankAnim(textView);
                        }


                    }else if (i == 10) {

                        if(!txt.equals("")){
                            BackspaceBlankAnim(textView);
                        }
                        keyboard2.BackspaceNumbersInFigures(textView);


                    }
                    else{

                        KeyboardCloseAnimate();

                    }


                }
            }

            //LOGIKA

            int parentTextView = ((View)textView.getParent()).getId();

            if (calcID[0] == parentTextView){
                if(logic.checkFirstLine(keyboard2)){
                    sfxManager.CorrectLinePlay();
                    this.howManyLinesCorrectLines++;
                    View v = findViewById(parentTextView);
                    KeyboardCloseAnimate();
                    v.setClickable(false);
                    v = findViewById(R.id.transparent_line1);
                    v.setBackgroundResource(R.drawable.correct);
                    YoYo.with(Techniques.ZoomIn)
                            .duration(300)
                            .playOn(v);
                    clickAbleLine.add(1);
                    this.passedLinesIndexer = 1;
                    addCorrectFigure();

                }
            }
            else if (calcID[1] == parentTextView){
                if(logic.checkSecondLine(keyboard2)){
                    sfxManager.CorrectLinePlay();
                    this.howManyLinesCorrectLines++;
                    View v = findViewById(parentTextView);
                    KeyboardCloseAnimate();
                    v.setClickable(false);
                    v = findViewById(R.id.transparent_line2);
                    v.setBackgroundResource(R.drawable.correct);
                    YoYo.with(Techniques.ZoomIn)
                            .duration(300)
                            .playOn(v);
                    clickAbleLine.add(2);

                    if(this.level.GetUpLine() > 1){
                        this.passedLinesIndexer = 1;
                    }else {
                        this.passedLinesIndexer = 2;
                    }

                    addCorrectFigure();

                }
            }
            else if (calcID[2] == parentTextView){
               if(logic.checkThirdLine(keyboard2)){
                   sfxManager.CorrectLinePlay();
                   this.howManyLinesCorrectLines++;
                   View v = findViewById(parentTextView);
                   KeyboardCloseAnimate();
                   v.setClickable(false);
                   v = findViewById(R.id.transparent_line3);
                   v.setBackgroundResource(R.drawable.correct);
                   YoYo.with(Techniques.ZoomIn)
                           .duration(300)
                           .playOn(v);
                   clickAbleLine.add(3);
                   if(this.level.GetUpLine() > 1){
                       this.passedLinesIndexer = 2;
                   }else {
                       this.passedLinesIndexer = 3;
                   }
                   addCorrectFigure();

               }
            }
            else if (calcID[3] == parentTextView){
                if (logic.checkFourLine(keyboard2)){
                    sfxManager.CorrectLinePlay();
                    this.howManyLinesCorrectLines++;
                    View v = findViewById(parentTextView);
                    KeyboardCloseAnimate();
                    v.setClickable(false);
                    v = findViewById(R.id.transparent_line4);
                    v.setBackgroundResource(R.drawable.correct);
                    YoYo.with(Techniques.ZoomIn)
                            .duration(300)
                            .playOn(v);
                    clickAbleLine.add(4);
                    if(this.level.GetUpLine() > 1){
                        this.passedLinesIndexer = 3;
                    }else {
                        this.passedLinesIndexer = 4;
                    }
                    addCorrectFigure();

                }
            }
            else if (calcID[4] == parentTextView){
                if (logic.checkFiveLine(keyboard2)){
                    sfxManager.CorrectLinePlay();
                    this.howManyLinesCorrectLines++;
                    View v = findViewById(parentTextView);
                    KeyboardCloseAnimate();
                    v.setClickable(false);
                    v = findViewById(R.id.transparent_line5);
                    v.setBackgroundResource(R.drawable.correct);
                    YoYo.with(Techniques.ZoomIn)
                            .duration(300)
                            .playOn(v);
                    clickAbleLine.add(5);
                    this.passedLinesIndexer = 5;
                    addCorrectFigure();

                }
            }


            if(this.howManyLinesCorrectLines == this.level.GetHowManyLines()){
                ImageView winView = (ImageView) findViewById(R.id.arcade_level_complete);
                winView.setImageResource(R.drawable.arcade_level_complete_window);

                ImageView winChooseLevel = (ImageView) findViewById(R.id.choosinglevel_btn_complete);
                winChooseLevel.setImageResource(R.drawable.level_choosing_btn);
                winChooseLevel.setVisibility(View.VISIBLE);

                ImageView winAchievementList = (ImageView) findViewById(R.id.achievement_btn_complete);
                winAchievementList.setImageResource(R.drawable.achivement_small_button);
                winAchievementList.setVisibility(View.VISIBLE);

                ImageView winNextLevel = (ImageView) findViewById(R.id.nextlevel_btn);
                winNextLevel.setImageResource(R.drawable.nextlevel_btn);
                winNextLevel.setVisibility(View.VISIBLE);

                YoYo.with(Techniques.SlideInDown).playOn(findViewById(R.id.level_complete_layout));

                View backNormal = findViewById(R.id.back);
                backNormal.setVisibility(View.INVISIBLE);

                SharedPreferences sharedPreferences = getSharedPreferences("LVL", MODE_PRIVATE);

                myProgress.levelPassed(levelActual1, this);
                myProgress.unlockAchievement(this, sharedPreferences.getInt("LEVEL_COUNT", 0));
//                myProgress.updateProgress(googleApiClient, this);

            }


        }



    }

    public void ErrorKeyAnim(){
        YoYo.with(Techniques.Shake)
                .duration(400)
                .playOn(findViewById(R.id.keyboard));
        sfxManager.KeyboardErrorPlay();
    }

    public void BackspaceBlankAnim(View animBlank){
        YoYo.with(Techniques.Swing)
                .duration(800)
                .playOn(findViewById(animBlank.getId()));
        sfxManager.KeyboardBackspacePlay();
    }

    public void WriteBlankAnim(View animBlank){
        YoYo.with(Techniques.RubberBand)
                .duration(500)
                .playOn(findViewById(animBlank.getId()));

        sfxManager.KeyboardClickPlay(true);

        new ParticleSystem(this, 5, R.drawable.star_white, 800)
                .setScaleRange(0.7f, 1.3f)
                .setSpeedRange(0.1f, 0.25f)
                .setRotationSpeedRange(90, 180)
                .setFadeOut(200, new AccelerateInterpolator())
                .oneShot(animBlank, 100);
    }

    public void SetUpDown(int up, int down){
        this.Up = up;
        this.Down = down;

        RelativeLayout transparentLine;
        int transparentLineIDint;
        for(int i = Up; i <= Down ; i++){
            String transparentLineID = "transparent_line" + i;
            transparentLineIDint = getResources().getIdentifier(transparentLineID, "id", getPackageName());
            transparentLine = (RelativeLayout) findViewById(transparentLineIDint);
            transparentLine.setBackgroundResource(R.drawable.aaas);
        }


    }

    @Override
    public void onBackPressed(){
        if(isKeyboradOpen) {
            KeyboardCloseAnimate();
        }else{
            Intent i = new Intent(PlayMathMissionActivity.this, LevelMenuActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void CloseKeyboardNextTo(){

        View clsDown = findViewById(R.id.keyboard_cls_down);
        View clsUp = findViewById(R.id.keyboard_cls_up);
        RelativeLayout.LayoutParams closeKeyParamsDown = (RelativeLayout.LayoutParams) clsDown.getLayoutParams();
        RelativeLayout.LayoutParams closeKeyParamsUp = (RelativeLayout.LayoutParams) clsUp.getLayoutParams();
        //KEYBOARD CLOSE FIELD

        clsUp.setClickable(true);
        clsDown.setClickable(true);

        View[] line = new View[2];
        int[] idLine = new int[2];


        View v = findViewById(R.id.figure_color);
        v.setBackgroundResource(0);

        if(Up == 1) {
            line[0] = findViewById(R.id.Line1);
            idLine[0] = line[0].getId();
            closeKeyParamsUp.addRule(RelativeLayout.ABOVE, idLine[0]);
        }
        if(Up == 2){
            line[0] = findViewById(R.id.transparent_line2);
            idLine[0] = line[0].getId();
            closeKeyParamsUp.addRule(RelativeLayout.ABOVE, idLine[0]);
        }
        if(Up == 3){
            line[0] = findViewById(R.id.transparent_line3);
            idLine[0] = line[0].getId();
            closeKeyParamsUp.addRule(RelativeLayout.ABOVE, idLine[0]);
        }

        //DOWN

        if (Down == 5){

            line[1] = findViewById(R.id.transparent_line5);
            idLine[1] = line[1].getId();
            closeKeyParamsDown.addRule(RelativeLayout.BELOW, idLine[1]);

        }
        if(Down == 4){

            line[1] = findViewById(R.id.transparent_line4);
            idLine[1] = line[1].getId();
            closeKeyParamsDown.addRule(RelativeLayout.BELOW, idLine[1]);

        }
        if(Down == 3){

            line[1] = findViewById(R.id.transparent_line3);
            idLine[1] = line[1].getId();
            closeKeyParamsDown.addRule(RelativeLayout.BELOW, idLine[1]);

        }
        if(Down == 2){

            line[1] = findViewById(R.id.transparent_line2);
            idLine[1] = line[1].getId();
            closeKeyParamsDown.addRule(RelativeLayout.BELOW, idLine[1]);

        }

        clsUp.setVisibility(View.VISIBLE);
        clsDown.setVisibility(View.VISIBLE);


    }

    public void KeyboardCloseAnimate(){


        theSameLine = false;
        isKeyboradOpen = false;
        final View keyView = findViewById(R.id.keyboard);
        PointerAnimation();
        YoYo.with(Techniques.ZoomOut)
                .duration(400)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        SetVisibleLines();
                        VisibleAndAnimationLines(calcLineSwitch);
                        setClickableRecursive(keyView, false);
                    }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        SetClickAbleBlanksInGame();
                        View clsup = findViewById(R.id.keyboard_cls_up) ,
                                clsdown = findViewById(R.id.keyboard_cls_down);
                        clsup.setClickable(true);
                        clsdown.setClickable(true);

                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {}
                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                })
                .playOn(findViewById(R.id.keyboard));

        View clsDown = findViewById(R.id.keyboard_cls_down);
        View clsUp = findViewById(R.id.keyboard_cls_up);
        clsUp.setVisibility(View.INVISIBLE);
        clsDown.setVisibility(View.INVISIBLE);


    }

    public void KeyboardCloseAnimateField(View view){
        KeyboardCloseAnimate();
    }

    public void KeyboardCloseBtn(View view){
        sfxManager.KeybordClosePlay();
        KeyboardCloseAnimate();
    }

    public void SetClickAbleBlanks(Calculations calculations, String figures[]) {
        int variable = calculations.WhichColumnStart;
        int ID;
        TextView textView;
        for (int i = 0; i < calculations.VariablesCount; i++) {
            String TextViewId = "var" + calculations.WhichLine + "x" + variable;
            ID = getResources().getIdentifier(TextViewId, "id", getPackageName());
            variable++;
            textView = (TextView) findViewById(ID);
            TextViews.add(textView);
            textView.setClickable(figures[i] == "q");
            textView.setVisibility(View.VISIBLE);
            textView.setText(calculations.VariableList.get(i));
            if (figures[i].equals("k")){
                textView.setBackgroundResource(R.drawable.kwadrat);
                textView.setTag("k");
            }else if(figures[i].equals("o")){
                textView.setBackgroundResource(R.drawable.okrag);
                textView.setTag("o");
            }else if(figures[i].equals("r")){
                textView.setBackgroundResource(R.drawable.romb);
                textView.setTag("r");
            }else if(figures[i].equals("s")){
                textView.setBackgroundResource(R.drawable.skat);
                textView.setTag("s");
            }else  if(figures[i].equals("rf")){
                textView.setBackgroundResource(R.drawable.romb_f);
                textView.setTag("rf");
            }else  if(figures[i].equals("oz")){
                textView.setBackgroundResource(R.drawable.okrag_z);
                textView.setTag("oz");
            }else  if(figures[i].equals("ok")){
                textView.setBackgroundResource(R.drawable.okat);
                textView.setTag("ok");
            }else if(figures[i].equals("q")){
                textView.setBackgroundResource(R.drawable.question);
                textView.setText("?");
                textView.setTag("q");
            }else  if(figures[i].equals("kf")){
                textView.setBackgroundResource(R.drawable.kwadrat_f);
                textView.setTag("kf");
            }else  if(figures[i].equals("kb")){
                textView.setBackgroundResource(R.drawable.kwadrat_blue);
                textView.setTag("kb");
            }else  if(figures[i].equals("rg")){
                textView.setBackgroundResource(R.drawable.romb_green);
                textView.setTag("rg");
            }

        }


    }

    public void SetClickAbleBlanksInGame(){
        for (int i = 0; i < TextViews.size(); i++){
            TextViews.get(i).setClickable(true);
        }
        SetOffClickableLine();
    }

    public void SameLineKeyAnim(View calcClik){
        int parentId2 = ((View) calcClik.getParent()).getId();
        if(!theSameLine) {

            parentId = ((View) calcClik.getParent()).getId();
            theSameLine = true;
            YoYo.with(Techniques.FlipInX)
                    .duration(500)
                    .playOn(findViewById(R.id.keyboard));
            sfxManager.NewLineFigureClickPlay();
            return;
        }

        if (parentId == parentId2){
            YoYo.with(Techniques.Pulse)
                    .duration(500)
                    .playOn(findViewById(R.id.keyboard));
            sfxManager.SameLineFigureClickPlay();
        }else{
            YoYo.with(Techniques.FlipInX)
                    .duration(500)
                    .playOn(findViewById(R.id.keyboard));
            parentId = ((View) calcClik.getParent()).getId();
            sfxManager.NewLineFigureClickPlay();

        }
    }

    public void CalcClick(View view){
        clicked = true;
        isKeyboradOpen = true;
        ClickOn = view.getId();
        //FIELD TO CLOSE KEYBORAD
        CloseKeyboardNextTo();

        //FIGURE ANIMATION


        animClickFigures(view.getTag().toString());



        //      KEYBOARD
        RelativeLayout keyobard1 = (RelativeLayout)findViewById(R.id.keyboard);
        View key = findViewById(R.id.keyboard);
        setClickableRecursive(key, true);

        //Keyboard animation
        SameLineKeyAnim(view);

        RelativeLayout.LayoutParams keyboardParams = (RelativeLayout.LayoutParams)keyobard1.getLayoutParams();

        //RESET KEYBOARD
        keyboardParams.addRule(RelativeLayout.BELOW);
        keyboardParams.addRule(RelativeLayout.ABOVE);
        keyobard1.setLayoutParams(keyboardParams);



        SetVisibleKeyboard();

        //COLOR KEYBOARD
        SetKeyboardView(view);

        int ID, on;

        outerloop:
        for (int i = 1; i <= 5; i++){
            for (int z = 0; z <= 4; z++) {
                String TextViewId = "var" + i + "x" + z;
                ID = getResources().getIdentifier(TextViewId, "id", getPackageName());
                TextView xt = (TextView) findViewById(ID);
                on = xt.getId();

                if (ClickOn == on){

                    TextViewX = i;
                    TextViewY = z;
                    break outerloop;
                }
            }
        }


    }

    public void SetKeyboardView(View clickOn){

        View[] calcLine = new View[5];
        View transprentLine;

        RelativeLayout keyobard1 = (RelativeLayout)findViewById(R.id.keyboard);
        TextView figureColor = (TextView)findViewById(R.id.figure_color);


        //SETTINGS VISIBLE AND CALCS
        for (int i = 0; i <=4; i++){
            switch (i) {
                case 0:
                    transprentLine = findViewById(R.id.transparent_line1);
                    transprentLine.setVisibility(View.VISIBLE);

                    calcLine[i] = findViewById(R.id.Line1);
                    calcID[i] = calcLine[i].getId();
                    calcLine[i].setVisibility(View.VISIBLE);
                    break;
                case 1:
                    transprentLine = findViewById(R.id.transparent_line2);
                    transprentLine.setVisibility(View.VISIBLE);

                    calcLine[i] = findViewById(R.id.Line2);
                    calcID[i] = calcLine[i].getId();
                    calcLine[i].setVisibility(View.VISIBLE);
                    break;
                case 2:
                    transprentLine = findViewById(R.id.transparent_line3);
                    transprentLine.setVisibility(View.VISIBLE);

                    calcLine[i] = findViewById(R.id.Line3);
                    calcID[i] = calcLine[i].getId();
                    calcLine[i].setVisibility(View.VISIBLE);
                    break;
                case 3:
                    transprentLine = findViewById(R.id.transparent_line4);
                    transprentLine.setVisibility(View.VISIBLE);

                    calcLine[i] = findViewById(R.id.Line4);
                    calcID[i] = calcLine[i].getId();
                    calcLine[i].setVisibility(View.VISIBLE);
                    break;
                case 4:
                    transprentLine = findViewById(R.id.transparent_line5);
                    transprentLine.setVisibility(View.VISIBLE);

                    calcLine[i] = findViewById(R.id.Line5);
                    calcID[i] = calcLine[i].getId();
                    calcLine[i].setVisibility(View.VISIBLE);
                    break;
            }

        }
        for (int i = 0; i <= 10; i++) {
            switch (i){
                case 0:
                    //kwadrat - color
                    figureColor.setBackgroundResource(R.drawable.kwadrat);
                    if(clickOn.getBackground().getConstantState().equals(figureColor.getBackground().getConstantState())) {
                        //keyboard COLOR
                        keyobard1.setBackgroundResource(R.color.kwadrat_kolor);
                        //POINTER COLOR
                        SetSettingsKeyPointer(calcID, calcLine, clickOn, i);
                    }
                    break;
                case 1:
                    //romb_f - color
                    figureColor.setBackgroundResource(R.drawable.romb_f);
                    if (clickOn.getBackground().getConstantState().equals(figureColor.getBackground().getConstantState())) {
                        //KEYBOARD COLOR
                        keyobard1.setBackgroundResource(R.color.rombf_kolor);
                        //POINTER COLOR
                        SetSettingsKeyPointer(calcID, calcLine, clickOn, i);
                    }
                    break;
                case 2:
                    //okrag - color
                    figureColor.setBackgroundResource(R.drawable.okrag);
                    if (clickOn.getBackground().getConstantState().equals(figureColor.getBackground().getConstantState())) {
                        //KEYBOARD COLOR
                        keyobard1.setBackgroundResource(R.color.okrag_kolor);
//                        POINTER COLOR
                        SetSettingsKeyPointer(calcID, calcLine, clickOn, i);
                    }
                    break;
                case 3:
                    //romb - color
                    figureColor.setBackgroundResource(R.drawable.romb);
                    if (clickOn.getBackground().getConstantState().equals(figureColor.getBackground().getConstantState())) {
                        //KEYBOARD COLOR
                        keyobard1.setBackgroundResource(R.color.romb_kolor);
                        //POINTER COLOR
                        SetSettingsKeyPointer(calcID, calcLine, clickOn, i);
                    }
                    break;
                case 4:
                    //skat - color
                    figureColor.setBackgroundResource(R.drawable.skat);
                    if (clickOn.getBackground().getConstantState().equals(figureColor.getBackground().getConstantState())) {
                        //KEYBOARD COLOR
                        keyobard1.setBackgroundResource(R.color.skat_kolor);
                        //POINTER COLOR
                       SetSettingsKeyPointer(calcID,calcLine,clickOn, i);
                    }
                    break;
                case 5:
                    //okat - color
                    figureColor.setBackgroundResource(R.drawable.okat);
                    if (clickOn.getBackground().getConstantState().equals(figureColor.getBackground().getConstantState())) {
                        //KEYBOARD COLOR
                        keyobard1.setBackgroundResource(R.color.okat_kolor);
                        //POINTER COLOR
                        SetSettingsKeyPointer(calcID,calcLine,clickOn, i);
                    }
                    break;
                case 6:
                    //okrag_z - color
                    figureColor.setBackgroundResource(R.drawable.okrag_z);
                    if (clickOn.getBackground().getConstantState().equals(figureColor.getBackground().getConstantState())) {
                        //KEYBOARD COLOR
                        keyobard1.setBackgroundResource(R.color.okrag_z_kolor);
                        //POINTER COLOR
                        SetSettingsKeyPointer(calcID,calcLine,clickOn, i);
                    }
                    break;
                case 7:
                    //okrag_z - color
                    figureColor.setBackgroundResource(R.drawable.question);
                    if (clickOn.getBackground().getConstantState().equals(figureColor.getBackground().getConstantState())) {
                        //KEYBOARD COLOR
                        keyobard1.setBackgroundResource(R.color.question_kolor);
                        //POINTER COLOR
                        SetSettingsKeyPointer(calcID,calcLine,clickOn, i);
                    }
                    break;
                case 8:
                    //okrag_z - color
                    figureColor.setBackgroundResource(R.drawable.romb_green);
                    if (clickOn.getBackground().getConstantState().equals(figureColor.getBackground().getConstantState())) {
                        //KEYBOARD COLOR
                        keyobard1.setBackgroundResource(R.color.romb_green_kolor);
                        //POINTER COLOR
                        SetSettingsKeyPointer(calcID,calcLine,clickOn, i);
                    }
                    break;
                case 9:
                    //okrag_z - color
                    figureColor.setBackgroundResource(R.drawable.kwadrat_blue);
                    if (clickOn.getBackground().getConstantState().equals(figureColor.getBackground().getConstantState())) {
                        //KEYBOARD COLOR
                        keyobard1.setBackgroundResource(R.color.kwadrat_blue_kolor);
                        //POINTER COLOR
                        SetSettingsKeyPointer(calcID,calcLine,clickOn, i);
                    }
                    break;
                case 10:
                    //okrag_z - color
                    figureColor.setBackgroundResource(R.drawable.kwadrat_f);
                    if (clickOn.getBackground().getConstantState().equals(figureColor.getBackground().getConstantState())) {
                        //KEYBOARD COLOR
                        keyobard1.setBackgroundResource(R.color.kwadrat_f_kolor);
                        //POINTER COLOR
                        SetSettingsKeyPointer(calcID,calcLine,clickOn, i);
                    }
                    break;
            }

        }


    }

    public void setClickableRecursive(View view, boolean isClickable){
        if(view instanceof ViewGroup){
            ViewGroup group = (ViewGroup) view;
            for(int i = 0; i < group.getChildCount(); i++){
                setClickableRecursive(group.getChildAt(i), isClickable);
            }
        }else{
            view.setClickable(isClickable);
        }
    }

    public void VisibleAndAnimationLines(int calcLineSwitch){

        View calcLine;

        switch (calcLineSwitch) {
            case 0:
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.Line2));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.Line3));


                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.transparent_line2));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.transparent_line3));


                break;

            case 1:
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.Line3));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.Line4));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.transparent_line3));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.transparent_line4));
                break;

            case 2:
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.Line4));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.Line5));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.transparent_line4));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.transparent_line5));
                break;
            case 3:
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.Line3));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.Line2));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.transparent_line3));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.transparent_line2));
                break;
            case 4:
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.Line4));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.Line3));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.transparent_line4));
                YoYo.with(Techniques.ZoomIn)
                        .duration(350)
                        .playOn(findViewById(R.id.transparent_line3));
                break;
        }


        calcLine = findViewById(R.id.keyboard);
        new ParticleSystem(this, 5, R.drawable.star_white, 800)
                .setScaleRange(0.7f, 1.3f)
                .setSpeedRange(0.1f, 0.25f)
                .setRotationSpeedRange(90, 180)
                .setFadeOut(200, new AccelerateInterpolator())
                .oneShot(calcLine, 100);
        calcLine = findViewById(R.id.calculations);
        calcLine.clearAnimation();
        setClickableRecursive(calcLine, false);

    }

    public void SetSettingsKeyPointer(int[] calcID, View[] calcLine, View clickOn, int pointerFigure){
        View transparentLine;
        int parentKeyboard = ((View)clickOn.getParent()).getId();
        View blankPointer;
        RelativeLayout keyobard1 = (RelativeLayout)findViewById(R.id.keyboard);
        RelativeLayout.LayoutParams keyboardParams = (RelativeLayout.LayoutParams)keyobard1.getLayoutParams();
        RelativeLayout.LayoutParams pointerParams;

        blankPointer = findViewById(R.id.pointer_line1);
        blankPointer.setVisibility(View.INVISIBLE);
        blankPointer = findViewById(R.id.pointer_line2);
        blankPointer.setVisibility(View.INVISIBLE);
        blankPointer = findViewById(R.id.pointer_line3);
        blankPointer.setVisibility(View.INVISIBLE);
        blankPointer = findViewById(R.id.pointer_line4);
        blankPointer.setVisibility(View.INVISIBLE);
        blankPointer = findViewById(R.id.pointer_line5);
        blankPointer.setVisibility(View.INVISIBLE);

        if (parentKeyboard == calcID[0]) {
            //SETTING POINTER
            blankPointer = findViewById(R.id.pointer_line1);
            blankPointer.setVisibility(View.VISIBLE);
            this.BlankPointer = blankPointer;

            //Pointer settings
            pointerParams = (RelativeLayout.LayoutParams) blankPointer.getLayoutParams();
            pointerParams.addRule(RelativeLayout.ALIGN_RIGHT, ClickOn);
            pointerParams.addRule(RelativeLayout.ALIGN_LEFT, ClickOn);
            pointerParams.addRule(RelativeLayout.BELOW, ClickOn);
            blankPointer.setLayoutParams(pointerParams);

            //Keyboard settings
            calcLineSwitch = 0;
            calcLine[1].setVisibility(View.INVISIBLE);
            calcLine[2].setVisibility(View.INVISIBLE);
            keyboardParams.addRule(RelativeLayout.BELOW, parentKeyboard);
            keyobard1.setLayoutParams(keyboardParams);

            //Transparent Line
            transparentLine = findViewById(R.id.transparent_line2);
            transparentLine.setVisibility(View.INVISIBLE);
            transparentLine = findViewById(R.id.transparent_line3);
            transparentLine.setVisibility(View.INVISIBLE);

        }else if(parentKeyboard == calcID[1]){
            //SETTING POINTER
            blankPointer = findViewById(R.id.pointer_line2);
            blankPointer.setVisibility(View.VISIBLE);
            this.BlankPointer = blankPointer;


            //Pointer settings
            pointerParams = (RelativeLayout.LayoutParams) blankPointer.getLayoutParams();
            pointerParams.addRule(RelativeLayout.ALIGN_RIGHT, ClickOn);
            pointerParams.addRule(RelativeLayout.ALIGN_LEFT, ClickOn);
            pointerParams.addRule(RelativeLayout.BELOW, ClickOn);
            blankPointer.setLayoutParams(pointerParams);

            //Keyboard settings
            calcLineSwitch = 1;
            calcLine[2].setVisibility(View.INVISIBLE);
            calcLine[3].setVisibility(View.INVISIBLE);
            keyboardParams.addRule(RelativeLayout.BELOW, parentKeyboard);
            keyobard1.setLayoutParams(keyboardParams);

            //Transparent Line
            transparentLine = findViewById(R.id.transparent_line3);
            transparentLine.setVisibility(View.INVISIBLE);
            transparentLine = findViewById(R.id.transparent_line4);
            transparentLine.setVisibility(View.INVISIBLE);
        }else if(parentKeyboard == calcID[2]){
            //SETTING POINTER
            blankPointer = findViewById(R.id.pointer_line3);
            blankPointer.setVisibility(View.VISIBLE);
            this.BlankPointer = blankPointer;


            //Pointer settings
            pointerParams = (RelativeLayout.LayoutParams) blankPointer.getLayoutParams();
            pointerParams.addRule(RelativeLayout.ALIGN_RIGHT, ClickOn);
            pointerParams.addRule(RelativeLayout.ALIGN_LEFT, ClickOn);
            pointerParams.addRule(RelativeLayout.BELOW, ClickOn);
            blankPointer.setLayoutParams(pointerParams);

            //Keyboard settings
            calcLineSwitch = 2;
            calcLine[3].setVisibility(View.INVISIBLE);
            calcLine[4].setVisibility(View.INVISIBLE);
            keyboardParams.addRule(RelativeLayout.BELOW, parentKeyboard);
            keyobard1.setLayoutParams(keyboardParams);

            //Transparent Line
            transparentLine = findViewById(R.id.transparent_line4);
            transparentLine.setVisibility(View.INVISIBLE);
            transparentLine = findViewById(R.id.transparent_line5);
            transparentLine.setVisibility(View.INVISIBLE);

        }else if(parentKeyboard == calcID[3]){
            //SETTING POINTER
            blankPointer = findViewById(R.id.pointer_line4);
            blankPointer.setVisibility(View.VISIBLE);
            this.BlankPointer = blankPointer;


            //Pointer settings
            pointerParams = (RelativeLayout.LayoutParams) blankPointer.getLayoutParams();
            pointerParams.addRule(RelativeLayout.ALIGN_RIGHT, ClickOn);
            pointerParams.addRule(RelativeLayout.ALIGN_LEFT, ClickOn);
            pointerParams.addRule(RelativeLayout.ABOVE, ClickOn);
            blankPointer.setLayoutParams(pointerParams);
            pointerFigure += 20;

            //Keyboard settings
            calcLineSwitch = 3;
            View line4Key = findViewById(R.id.Line4Key);
            int line4key;
            line4key = line4Key.getId();
            calcLine[1].setVisibility(View.INVISIBLE);
            calcLine[2].setVisibility(View.INVISIBLE);
            keyboardParams.addRule(RelativeLayout.ABOVE, line4key);
            keyobard1.setLayoutParams(keyboardParams);

            //Transparent Line
            transparentLine = findViewById(R.id.transparent_line2);
            transparentLine.setVisibility(View.INVISIBLE);
            transparentLine = findViewById(R.id.transparent_line3);
            transparentLine.setVisibility(View.INVISIBLE);

        }else if(parentKeyboard == calcID[4]){
            //SETTING POINTER
            blankPointer = findViewById(R.id.pointer_line5);
            blankPointer.setVisibility(View.VISIBLE);
            this.BlankPointer = blankPointer;


            //Pointer settings
            pointerParams = (RelativeLayout.LayoutParams) blankPointer.getLayoutParams();
            pointerParams.addRule(RelativeLayout.ALIGN_RIGHT, ClickOn);
            pointerParams.addRule(RelativeLayout.ALIGN_LEFT, ClickOn);
            pointerParams.addRule(RelativeLayout.ABOVE, ClickOn);
            blankPointer.setLayoutParams(pointerParams);
            pointerFigure += 20;

            //Keyboard settings
            calcLineSwitch = 4;
            calcLine[2].setVisibility(View.INVISIBLE);
            calcLine[3].setVisibility(View.INVISIBLE);
            keyboardParams.addRule(RelativeLayout.ABOVE, parentKeyboard);
            keyobard1.setLayoutParams(keyboardParams);

            //Transparent Line
            transparentLine = findViewById(R.id.transparent_line3);
            transparentLine.setVisibility(View.INVISIBLE);
            transparentLine = findViewById(R.id.transparent_line4);
            transparentLine.setVisibility(View.INVISIBLE);

        }

        switch (pointerFigure){
            case 0:
                blankPointer.setBackgroundResource(R.drawable.pointer_kwadrat);
                break;
            case 1:
                blankPointer.setBackgroundResource(R.drawable.pointer_rombf);
                break;
            case 2:
                blankPointer.setBackgroundResource(R.drawable.pointer_okrag);
                break;
            case 3:
                blankPointer.setBackgroundResource(R.drawable.pointer_romb);
                break;
            case 4:
                blankPointer.setBackgroundResource(R.drawable.pointer_skat);
                break;
            case 5:
                blankPointer.setBackgroundResource(R.drawable.pointer_okat);
                break;
            case 6:
                blankPointer.setBackgroundResource(R.drawable.pointer_skat);
                break;
            case 7:
                blankPointer.setBackgroundResource(R.drawable.pointer_question);
                break;
            case 8:
                blankPointer.setBackgroundResource(R.drawable.pointer_romb_green);
                break;
            case 9:
                blankPointer.setBackgroundResource(R.drawable.pointer_kwadrat_blue);
                break;
            case 10:
                blankPointer.setBackgroundResource(R.drawable.pointer_kwadrat_f);
                break;
            case 20:
                blankPointer.setBackgroundResource(R.drawable.pointer_kwadrat_rotated);
                break;
            case 21:
                blankPointer.setBackgroundResource(R.drawable.pointer_rombf_rotated);
                break;
            case 22:
                blankPointer.setBackgroundResource(R.drawable.pointer_okrag_rotated);
                break;
            case 23:
                blankPointer.setBackgroundResource(R.drawable.pointer_romb_rotated);
                break;
            case 24:
                blankPointer.setBackgroundResource(R.drawable.pointer_skat_rotated);
                break;
            case 25:
                blankPointer.setBackgroundResource(R.drawable.pointer_okat_rotated);
                break;
            case 26:
                blankPointer.setBackgroundResource(R.drawable.pointer_skat_rotated);
                break;
            case 27:
                blankPointer.setBackgroundResource(R.drawable.pointer_question_rotated);
                break;
            case 28:
                blankPointer.setBackgroundResource(R.drawable.pointer_romb_green_rotated);
                break;
            case 29:
                blankPointer.setBackgroundResource(R.drawable.pointer_kwadrat_blue_rotated);
                break;
            case 30:
                blankPointer.setBackgroundResource(R.drawable.pointer_kwadrat_f_rotated);
                break;

        }

        OpenPointerAnimation();
    }

    public void SetResetKeyboard() {
        RelativeLayout keyobardRL = (RelativeLayout)findViewById(R.id.keyboard);
        keyobardRL.setVisibility(View.INVISIBLE);
    }

    public void SetVisibleKeyboard(){
        RelativeLayout keyobardRL = (RelativeLayout)findViewById(R.id.keyboard);
        keyobardRL.setVisibility(View.VISIBLE);
    }

    public void OpenPointerAnimation(){
        if(BlankPointer!= null) {
            ImageView pointer = (ImageView) findViewById(BlankPointer.getId());
            YoYo.with(Techniques.FlipInX)
                    .duration(500)
                    .playOn(pointer);
        }
    }

    public void PointerAnimation(){
        if(BlankPointer != null) {
            ImageView pointer = (ImageView) findViewById(BlankPointer.getId());
            YoYo.with(Techniques.ZoomOut)
                    .duration(200)
                    .playOn(pointer);
        }
    }

    public void SetVisibleLines(){
        View[] calcLine = new View[5];
        View transprentLine;
        //SETTINGS VISIBLE AND CALCS
        for (int i = 0; i <=4; i++){
            switch (i) {
                case 0:
                    calcLine[i] = findViewById(R.id.Line1);
                    calcLine[i].setVisibility(View.VISIBLE);
                    transprentLine = findViewById(R.id.transparent_line1);
                    transprentLine.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    calcLine[i] = findViewById(R.id.Line2);
                    calcLine[i].setVisibility(View.VISIBLE);
                    transprentLine = findViewById(R.id.transparent_line2);
                    transprentLine.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    transprentLine = findViewById(R.id.transparent_line3);
                    transprentLine.setVisibility(View.VISIBLE);
                    calcLine[i] = findViewById(R.id.Line3);
                    calcLine[i].setVisibility(View.VISIBLE);
                    break;
                case 3:
                    transprentLine = findViewById(R.id.transparent_line4);
                    transprentLine.setVisibility(View.VISIBLE);
                    calcLine[i] = findViewById(R.id.Line4);
                    calcLine[i].setVisibility(View.VISIBLE);
                    break;
                case 4:
                    transprentLine = findViewById(R.id.transparent_line5);
                    transprentLine.setVisibility(View.VISIBLE);
                    calcLine[i] = findViewById(R.id.Line5);
                    calcLine[i].setVisibility(View.VISIBLE);
                    break;
            }

        }
    }

    public void backToLevelMenu(View view) {
        sfxManager.KeyboardClickPlay(true);
        Intent i = new Intent(PlayMathMissionActivity.this, LevelMenuActivity.class);
        startActivity(i);
        finish();
    }

    public void ChooseLevelToBack(View view) {
        sfxManager.KeyboardClickPlay(true);
        Intent i = new Intent(PlayMathMissionActivity.this, LevelMenuActivity.class);
        startActivity(i);
        finish();
    }

    public void intentAchievement(View view) {
        sfxManager.KeyboardClickPlay(true);
        SharedPreferences sharedPref = getSharedPreferences("LOGGING", MODE_PRIVATE);
//        if(!sharedPref.getBoolean("SIGN_STATUS", true)) {
//            mSignInClicked = true;
//            googleApiClient.connect();
//            myProgress.updateProgress(googleApiClient, this);
//        }else{
//            mSignInClicked = true;
//            googleApiClient.connect();
//            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 2);
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getSharedPreferences("LOGGING", MODE_PRIVATE);
//        if(sharedPref.getBoolean("SIGN_STATUS", true)){
//            googleApiClient.connect();
//        }
    }
}
