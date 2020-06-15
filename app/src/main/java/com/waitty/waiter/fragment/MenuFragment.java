package com.waitty.waiter.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import com.waitty.waiter.adapter.PagerAdapterSubmenu;
import com.waitty.waiter.databinding.FragmentMenuBinding;
import com.waitty.waiter.model.SubCategory;
import com.waitty.waiter.retrofit.API;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private ViewGroup root;
    private Context mContext;
    FragmentMenuBinding fragmentMenuBinding;
    private PagerAdapterSubmenu adapter;
    private ArrayList<SubCategory> subCategoryDataList;
    private Typeface type,typeBold;

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       // fragmentMenuBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
        root = (ViewGroup) fragmentMenuBinding.getRoot();
        mContext = getContext();

        Bundle bundle = this.getArguments();
        if (bundle != null)
            try {
                subCategoryDataList = bundle.getParcelableArrayList(API.DATA);
            } catch (Exception e) {
                e.printStackTrace();
            }
       // init();
        return root;
    }

    // Variable initialization
    /*private void init() {
        type = Typeface.createFromAsset(mContext.getAssets(), "p_regular.TTF");
        typeBold = Typeface.createFromAsset(mContext.getAssets(), "p_semibold.TTF");
        if(subCategoryDataList!=null && subCategoryDataList.size()>0)
            setPager();
        else {
            fragmentMenuBinding.tabLayout.setVisibility(View.GONE);
            fragmentMenuBinding.pager.setVisibility(View.GONE);
            fragmentMenuBinding.txtNoRecord.setVisibility(View.VISIBLE);
        }
    }

    // Set tab and pager data
    private void setPager() {
        adapter=new PagerAdapterSubmenu(getChildFragmentManager(),mContext, subCategoryDataList);
        fragmentMenuBinding.pager.setAdapter(adapter);
        fragmentMenuBinding.pager.setCurrentItem(0);
        fragmentMenuBinding.tabLayout.setupWithViewPager(fragmentMenuBinding.pager);
        fragmentMenuBinding.pager.setOffscreenPageLimit(fragmentMenuBinding.tabLayout.getTabCount());
        adapter.notifyDataSetChanged();

        for (int i = 0; i < fragmentMenuBinding.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = fragmentMenuBinding.tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i,0));
        }

        fragmentMenuBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //do stuff here
                fragmentMenuBinding.pager.setCurrentItem(tab.getPosition(),false);
                View view=tab.getCustomView();
                ((TextView)view.findViewById(R.id.txtSubmenu)).setBackgroundResource(R.drawable.round_selected_submenu);
                ((TextView)view.findViewById(R.id.txtSubmenu)).setTypeface(typeBold);
                ((TextView)view.findViewById(R.id.txtSubmenu)).setTextColor(mContext.getResources().getColor(R.color.colorNextWelcome));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view=tab.getCustomView();
                ((TextView)view.findViewById(R.id.txtSubmenu)).setBackgroundResource(R.drawable.round_unselected_submenu);
                ((TextView)view.findViewById(R.id.txtSubmenu)).setTypeface(type);
                ((TextView)view.findViewById(R.id.txtSubmenu)).setTextColor(mContext.getResources().getColor(R.color.colorSubmenuUnselected));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }*/
}