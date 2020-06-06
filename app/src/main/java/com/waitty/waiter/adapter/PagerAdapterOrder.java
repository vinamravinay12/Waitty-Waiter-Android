package com.waitty.waiter.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.waitty.waiter.fragment.NewOrderListFragment;
import com.waitty.waiter.fragment.ProcessingOrderListFragment;
import com.waitty.waiter.fragment.ServedOrderListFragment;

public class PagerAdapterOrder extends FragmentPagerAdapter {

    private Context mContext;
    public NewOrderListFragment newOrderListFragment;
    public ProcessingOrderListFragment processingOrderListFragment;
    public ServedOrderListFragment servedOrderListFragment;

    // Class constructor
    public PagerAdapterOrder(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        try {

            if (position == 0) {
                newOrderListFragment = new NewOrderListFragment();
                return newOrderListFragment;
            } else if (position == 1) {
                processingOrderListFragment = new ProcessingOrderListFragment();
                return processingOrderListFragment;
            } else if (position == 2) {
                servedOrderListFragment = new ServedOrderListFragment();
                return servedOrderListFragment;
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
        if (position == 0) {
            newOrderListFragment = (NewOrderListFragment) createdFragment;
            return newOrderListFragment;
        } else if (position == 1) {
            processingOrderListFragment = (ProcessingOrderListFragment) createdFragment;
            return processingOrderListFragment;
        } else if (position == 2) {
            servedOrderListFragment = (ServedOrderListFragment) createdFragment;
            return servedOrderListFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}