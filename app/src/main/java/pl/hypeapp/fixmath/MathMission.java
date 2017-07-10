package pl.hypeapp.fixmath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysgao on 09/07/2017.
 */

public class MathMission {
    private List<MathQuestion> mathQuestions = new ArrayList<>();
    private List<MathAnswer> mathAnswers = new ArrayList<>();

    public MathMission() {

    }

    public List<MathQuestion> getMathQuestions() {
        return mathQuestions;
    }

    public List<MathAnswer> getMathAnswers() {
        return mathAnswers;
    }

    public static class MathQuestion {
        private List<MathExpression> left = new ArrayList<>();
        private List<MathExpression> right = new ArrayList<>();
    }

    public static abstract class MathExpression {

    }

    public static class ConstValueExpression extends MathExpression {
        private int value;
    }

    public static class SymbolExpression extends MathExpression {
        private String code;
    }

    public static class VariableExpression extends MathExpression {
        private String code;
    }

    public static class MathAnswer {
        private String varName;
        private int value;
    }
}
