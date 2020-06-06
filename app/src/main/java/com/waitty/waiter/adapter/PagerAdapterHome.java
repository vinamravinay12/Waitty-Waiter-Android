package com.waitty.waiter.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.waitty.waiter.fragment.MenuHomeFragment;
import com.waitty.waiter.fragment.ProfileHomeFragment;
import com.waitty.waiter.fragment.StatusHomeFragment;

public class PagerAdapterHome extends FragmentPagerAdapter {

    private Context appContext;
    public StatusHomeFragment statusHomeFragment;
    public MenuHomeFragment menuHomeFragment;
    public ProfileHomeFragment profileHomeFragment;

    // Class constructor
    public PagerAdapterHome(FragmentManager fm, Context context) {
        super(fm);
        appContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        try {
            if (position == 0) {
                statusHomeFragment = new StatusHomeFragment();
                return statusHomeFragment;
            } else if (position == 1) {
                menuHomeFragment = new MenuHomeFragment();
                return menuHomeFragment;
            } else if (position == 2) {
                profileHomeFragment = new ProfileHomeFragment();
                return profileHomeFragment;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        if (position == 0) {
            statusHomeFragment = (StatusHomeFragment) createdFragment;
            return statusHomeFragment;
        } else if (position == 1) {
            menuHomeFragment = (MenuHomeFragment) createdFragment;
            return menuHomeFragment;
        } else if (position == 2) {
            profileHomeFragment = (ProfileHomeFragment) createdFragment;
            return profileHomeFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}