

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class OfferViewPager extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    public OfferViewPager(FragmentManager fm, List<Fragment> fragments) {

        super(fm);

        this.fragments = fragments;
        notifyDataSetChanged();

    }

    @Override

    public Fragment getItem(int position) {

        return this.fragments.get(position);

    }

    @Override

    public int getCount() {

        return this.fragments.size();

    }

    @Override
    public float getPageWidth(int position) {
        return 0.93f;
    }
}
