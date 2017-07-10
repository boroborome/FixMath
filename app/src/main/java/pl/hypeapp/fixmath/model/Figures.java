package pl.hypeapp.fixmath.model;

import java.util.HashMap;
import java.util.Map;

import pl.hypeapp.fixmath.R;

/**
 * Created by ysgao on 09/07/2017.
 */

public final class Figures {
    public static class FigureInfo {
        public final String code;
        public final int id;
        public final String name;
        public final int backgroundId;
        public final int frameFigure;

        public FigureInfo(String code, int id, String name, int backgroundId, int frameFigure) {
            this.code = code;
            this.id = id;
            this.name = name;
            this.backgroundId = backgroundId;
            this.frameFigure = frameFigure;
        }
    }

    public static final FigureInfo[] allFigures = new FigureInfo[] {
            new FigureInfo("k", R.drawable.kwadrat, "", R.drawable.kwadrat, R.drawable.kwadrat_ramka),
            new FigureInfo("o", R.drawable.okrag, "", R.drawable.okrag, R.drawable.okrag_ramka),
            new FigureInfo("r", R.drawable.romb, "", R.drawable.romb, R.drawable.romb_ramka),
            new FigureInfo("s", R.drawable.skat, "", R.drawable.skat, R.drawable.skat_ramka),
            new FigureInfo("rf", R.drawable.romb_f, "", R.drawable.romb_f, R.drawable.romb_f_ramka),
            new FigureInfo("oz", R.drawable.okrag_z, "", R.drawable.okrag_z, R.drawable.okrag_ramka),
            new FigureInfo("ok", R.drawable.okat, "", R.drawable.okat, R.drawable.okat_ramka),
            new FigureInfo("q", R.drawable.question, "", R.drawable.question, R.drawable.question_ramka),
            new FigureInfo("kf", R.drawable.kwadrat_f, "", R.drawable.kwadrat_f, R.drawable.kwadrat_ramka),
            new FigureInfo("kb", R.drawable.kwadrat_blue, "", R.drawable.kwadrat_blue, R.drawable.kwadrat_ramka),
            new FigureInfo("rg", R.drawable.romb_green, "", R.drawable.romb_green, R.drawable.romb_ramka),
    };

    private static Map<String, FigureInfo> diction;
    public static FigureInfo getFigure(String code) {
        Map<String, FigureInfo> d = diction;
        if (d == null) {
            d = initDiction();
        }
        return d.get(code);
    }

    private static Map<String,FigureInfo> initDiction() {
        Map<String, FigureInfo> d = new HashMap<>();
        for (FigureInfo info : allFigures) {
            d.put(info.code, info);
        }
        diction = d;
        return d;
    }
}
