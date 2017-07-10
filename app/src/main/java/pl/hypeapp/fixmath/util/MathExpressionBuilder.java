package pl.hypeapp.fixmath.util;

import java.util.ArrayList;
import java.util.List;

import pl.hypeapp.fixmath.model.MathMission;

/**
 * Created by ysgao on 10/07/2017.
 */

public class MathExpressionBuilder {
    private List<MathMission.MathExpression> expressions = new ArrayList<>();

    public MathExpressionBuilder value(int value) {
        expressions.add(new MathMission.ConstValueExpression(value));
        return this;
    }
    public MathExpressionBuilder symbol(String value) {
        expressions.add(new MathMission.SymbolExpression(value));
        return this;
    }
    public MathExpressionBuilder var(String value) {
        expressions.add(new MathMission.VariableExpression(value));
        return this;
    }

    public List<MathMission.MathExpression> build() {
        return expressions;
    }
}
