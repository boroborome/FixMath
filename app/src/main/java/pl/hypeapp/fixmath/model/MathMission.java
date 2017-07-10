package pl.hypeapp.fixmath.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ysgao on 09/07/2017.
 */

public class MathMission {
    private int level;
    private List<MathQuestion> mathQuestions = new ArrayList<>();
    private List<MathAnswer> mathAnswers = new ArrayList<>();
    public final Map<String, String> varFigure = new HashMap<>();

    public MathMission(int level) {
        this.level = level;
    }

    public List<MathQuestion> getMathQuestions() {
        return mathQuestions;
    }

    public List<MathAnswer> getMathAnswers() {
        return mathAnswers;
    }

    public static class MathQuestion {
        public final List<MathExpression> left;
        public final List<MathExpression> right;

        public MathQuestion(List<MathExpression> left, List<MathExpression> right) {
            this.left = left;
            this.right = right;
        }
    }

    public static abstract class MathExpression {

    }

    public static class ConstValueExpression extends MathExpression {
        public final int value;

        public ConstValueExpression(int value) {
            this.value = value;
        }
    }

    public static class SymbolExpression extends MathExpression {
        public final String code;
        public SymbolExpression(String code) {
            this.code = code;
        }
    }

    public static class VariableExpression extends MathExpression {
        public final String code;
        public VariableExpression(String code) {
            this.code = code;
        }
    }

    public static class MathAnswer {
        public final String varName;
        public final int value;

        public MathAnswer(String varName, int value) {
            this.varName = varName;
            this.value = value;
        }
    }


}
