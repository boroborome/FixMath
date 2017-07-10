package pl.hypeapp.fixmath.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import pl.hypeapp.fixmath.model.Figures;
import pl.hypeapp.fixmath.model.MathMission;
import pl.hypeapp.fixmath.util.MathExpressionBuilder;

/**
 * Created by ysgao on 10/07/2017.
 */

public class ChildMathFactory {
    private static final Random numberGenerator = new Random();

    public MathMission createMisson(int level) {
        /*
        1-20:1 question
        20-30:
         */

        int questionCount = level / 20 + 1;
        if (questionCount > 3) {
            questionCount = 3;
        }

        int maxNumber = level / 20 + 5;

        MathMission mission = new MathMission(level);
        for (int questionIndex = 0; questionIndex < questionCount; questionIndex++) {
            int a = newNumber(maxNumber);
            int b = newNumber(maxNumber);

            String resultVar = "v" + questionIndex;
            mission.getMathQuestions().add(new MathMission.MathQuestion(
                    new MathExpressionBuilder()
                        .value(a)
                        .symbol("+")
                        .value(b)
                        .build(),
                    new MathExpressionBuilder()
                            .var(resultVar)
                            .build()
                    ));

            mission.getMathAnswers().add(new MathMission.MathAnswer(resultVar, a + b));
            String figureId = Figures.getRandomFigure(mission.varFigure);
            mission.varFigure.put(resultVar, figureId);
        }

        return mission;

    }



    private int newNumber(int maxNumber) {
        int half = maxNumber / 2;
        while(half > 0) {
            int value = numberGenerator.nextInt(maxNumber);
            if (value > half) {
                return value;
            }
            half = half / 2;
        }
        return numberGenerator.nextInt(maxNumber);
    }
}
