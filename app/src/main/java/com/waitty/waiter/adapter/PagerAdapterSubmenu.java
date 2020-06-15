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
import com.waitty.waiter.fragment.SubmenuFragment;
import com.waitty.waiter.model.SubCategory;
import com.waitty.waiter.retrofit.API;

import java.util.ArrayList;

public class PagerAdapterSubmenu extends FragmentPagerAdapter {

    private Context mContext;
    private SubmenuFragment subMenu;
    private ArrayList<SubCategory> subCategoryDataList;
    private Typeface type,typeBold;

    // Class constructor
    public PagerAdapterSubmenu(FragmentManager fm, Context context, ArrayList<SubCategory> data) {
        super(fm);
        mContext = context;
        subCategoryDataList =data;
        type = Typeface.createFromAsset(context.getAssets(), "p_regular.TTF");
        typeBold = Typeface.createFromAsset(context.getAssets(), "p_semibold.TTF");
    }

    @Override
    public Fragment getItem(int position) {
        try {

            subMenu = new SubmenuFragment();
            Bundle args = new Bundle();
            args.putParcelable(API.DATA, subCategoryDataList.get(position));
            subMenu.setArguments(args);
            return subMenu;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        subMenu = (SubmenuFragment) createdFragment;
        return subMenu;
    }

    @Override
    public int getCount() {
        return subCategoryDataList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        try {
            return subCategoryDataList.get(position).getName();
        } catch (Exception e) {e.printStackTrace();}

        return null;
    }

    // Custom submenu item
    public View getTabView(int position,int selectedItemPosition) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.txtSubmenu);
        tv.setText(subCategoryDataList.get(position).getName());
        if(position==selectedItemPosition){
            tv.setBackgroundResource(R.drawable.round_selected_submenu);
            tv.setTypeface(typeBold);
            tv.setTextColor(mContext.getResources().getColor(R.color.colorNextWelcome));
        }else{
            tv.setBackgroundResource(R.drawable.round_unselected_submenu);
            tv.setTypeface(type);
            tv.setTextColor(mContext.getResources().getColor(R.color.colorSubmenuUnselected));
        }

        return v;
    }

}