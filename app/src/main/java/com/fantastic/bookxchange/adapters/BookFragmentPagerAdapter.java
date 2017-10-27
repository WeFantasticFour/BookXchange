package com.fantastic.bookxchange.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fantastic.bookxchange.fragments.ExchangeListFragment;
import com.fantastic.bookxchange.fragments.ShareListFragment;
import com.fantastic.bookxchange.fragments.WishListFragment;
import com.fantastic.bookxchange.utils.SmartFragmentStatePagerAdapter;

/**
 * Created by m3libea on 10/13/17.
 */

public class BookFragmentPagerAdapter extends SmartFragmentStatePagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Exchange", "Share", "Wishlist"};
    private Context context;

    public BookFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fm;
        switch (position) {
            case 0:
                fm = ExchangeListFragment.newInstance();
                break;
            case 1:
                fm = ShareListFragment.newInstance();
                break;
            case 2:
                fm = WishListFragment.newInstance();
                break;
            default:
                fm = null;
        }
        return fm;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
