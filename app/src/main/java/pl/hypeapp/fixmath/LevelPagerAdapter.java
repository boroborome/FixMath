package pl.hypeapp.fixmath;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by PrzemekEnterprise on 04.01.2016.
 */
public class LevelPagerAdapter extends FragmentPagerAdapter {
    public LevelPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new FragmentLevelPage(position * 16, Contants.MaxLevel);
    }

    @Override
    public int getCount() {
        return Contants.MaxLevel / 16 + (Contants.MaxLevel % 16 > 0 ? 1 : 0);
    }
}
