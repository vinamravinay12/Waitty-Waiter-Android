package com.waitty.waiter.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.waitty.waiter.R;
import com.waitty.waiter.fragment.MenuFragment;
import com.waitty.waiter.model.MenuData;
import com.waitty.waiter.retrofit.API;

public class PagerAdapterMenu extends FragmentPagerAdapter {

    private Context mContext;
    private MenuData menuDataList;
    private MenuFragment myMenu;
    private Typeface type,typeBold;

    // Class constructor
    public PagerAdapterMenu(FragmentManager fm, Context context, MenuData data) {
        super(fm);
        mContext = context;
        menuDataList =data;
        type = Typeface.createFromAsset(context.getAssets(), "p_medium.TTF");
        typeBold = Typeface.createFromAsset(context.getAssets(), "p_bold.TTF");
    }

    @Override
    public Fragment getItem(int position) {
        try {

            myMenu = new MenuFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList(API.DATA, menuDataList.getCategory().get(position).getSubCategories());
            myMenu.setArguments(args);
            return myMenu;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        myMenu = (MenuFragment) createdFragment;
        return myMenu;
    }

    @Override
    public int getCount() {
        return menuDataList.getCategory().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        try {
            return menuDataList.getCategory().get(position).getName();
        } catch (Exception e) {e.printStackTrace();}

        return null;
    }

    // Custom menu item
    public View getTabView(int position, int selectedItemPosition) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab_layout_menu, null);
        TextView tv = (TextView) v.findViewById(R.id.txtMenu);
        tv.setText(menuDataList.getCategory().get(position).getName());
        if(position==selectedItemPosition){
            tv.setTypeface(typeBold);
        }else{
            tv.setTypeface(type);
        }

        return v;
    }

}