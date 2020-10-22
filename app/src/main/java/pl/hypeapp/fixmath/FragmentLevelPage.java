package pl.hypeapp.fixmath;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by PrzemekEnterprise on 04.01.2016.
 */
public class FragmentLevelPage extends Fragment {

    private final int maxLevel;
    private final int startLevel;

    public FragmentLevelPage() {
        this(0, 100);
    }
    @SuppressLint("ValidFragment")
    public FragmentLevelPage(int startLevel, int maxLevel) {
        this.startLevel = startLevel;
        this.maxLevel = maxLevel;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.level_page_fragment, container, false);
        LinearLayout containerView = (LinearLayout) rootView.findViewById(R.id.levelRows);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LVL", getActivity().MODE_PRIVATE);
        int userLevel = sharedPreferences.getInt("LEVEL_COUNT", 0);

        int currentLevel = startLevel;
        for (int row = 0; row < 4 && currentLevel < maxLevel; row++) {
            LinearLayout lineRow = createNewLine();
            containerView.addView(lineRow);

            for (int column = 0; column < 4 && currentLevel < maxLevel; column++, currentLevel++) {
                TextView levelButton = (TextView) inflater.inflate(R.layout.level_button_component, container, false);
                String text = String.valueOf(currentLevel + 1);
                levelButton.setText(text);
                levelButton.setTag(text);

                if (currentLevel <= userLevel) {
                    levelButton.setBackgroundResource(R.drawable.levelcorrect_button);
                    levelButton.setTextColor(getResources().getColor(R.color.correct_text));
                    levelButton.setEnabled(true);
                } else {
                    levelButton.setBackgroundResource(R.drawable.level_button);
                    levelButton.setTextColor(getResources().getColor(R.color.normal_text));
                }
                lineRow.addView(levelButton);
            }

        }

        return rootView;
    }

    @NonNull
    private LinearLayout createNewLine() {
        LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        lineLayoutParams.setMargins(0,10,0,0);

        LinearLayout lineRow = new LinearLayout(this.getContext());
        lineRow.setOrientation(LinearLayout.HORIZONTAL);
        lineRow.setLayoutParams(lineLayoutParams);
        lineRow.setGravity(Gravity.CENTER_HORIZONTAL);
        return lineRow;
    }
}
