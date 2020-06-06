package com.waitty.waiter.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import com.waitty.waiter.R;
import com.waitty.waiter.adapter.PagerAdapterOrder;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.FragmentStatusHomeBinding;
import com.waitty.waiter.utility.Utility;

public class StatusHomeFragment extends Fragment {

    private ViewGroup root;
    private Context mContext;
    FragmentStatusHomeBinding fragmentStatusHomeBinding;
    public PagerAdapterOrder adapter;
    private ClickHandler mclickHandler;
    private Typeface type,typeBold;
    private int currentTab;

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentStatusHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_status_home, container, false);
        root = (ViewGroup) fragmentStatusHomeBinding.getRoot();
        mContext = getContext();
        init();
        return root;
    }

    // Variable initialization
    private void init() {
        type = Typeface.createFromAsset(mContext.getAssets(), "p_medium.TTF");
        typeBold = Typeface.createFromAsset(mContext.getAssets(), "p_semibold.TTF");
        mclickHandler = new ClickHandler();
        fragmentStatusHomeBinding.setClickEvent(mclickHandler);
        setPager();
    }

    // Click event handler
    public class ClickHandler {

        public ClickHandler( ) {
        }

        public void txtNewOrderClick(View view) {
            currentTab=0;
            newBadgeAction(false);
            Utility.setSharedPreferencesInteger(mContext, constant.NOTIFICATION_COUNT_NEW,0);
            fragmentStatusHomeBinding.pager.setCurrentItem(0,false);
            fragmentStatusHomeBinding.txtNewOrder.setBackgroundResource(R.drawable.round_selected_submenu);
            fragmentStatusHomeBinding.txtNewOrder.setTypeface(typeBold);

            fragmentStatusHomeBinding.txtProcessingOrder.setBackgroundResource(R.color.colorTransparent);
            fragmentStatusHomeBinding.txtProcessingOrder.setTypeface(type);

            fragmentStatusHomeBinding.txtServedOrder.setBackgroundResource(R.color.colorTransparent);
            fragmentStatusHomeBinding.txtServedOrder.setTypeface(type);
            adapter.newOrderListFragment.checkListSize();
        }

        public void txtProcessingOrderClick(View view) {
            currentTab=1;
            processingBadgeAction(false);
            Utility.setSharedPreferencesInteger(mContext, constant.NOTIFICATION_COUNT_PROCESSING,0);
            fragmentStatusHomeBinding.pager.setCurrentItem(1,false);
            fragmentStatusHomeBinding.txtNewOrder.setBackgroundResource(R.color.colorTransparent);
            fragmentStatusHomeBinding.txtNewOrder.setTypeface(type);

            fragmentStatusHomeBinding.txtProcessingOrder.setBackgroundResource(R.drawable.round_selected_submenu);
            fragmentStatusHomeBinding.txtProcessingOrder.setTypeface(typeBold);

            fragmentStatusHomeBinding.txtServedOrder.setBackgroundResource(R.color.colorTransparent);
            fragmentStatusHomeBinding.txtServedOrder.setTypeface(type);
            adapter.processingOrderListFragment.checkListSize();
        }

        public void txtServedOrderClick(View view) {
            currentTab=2;
            fragmentStatusHomeBinding.pager.setCurrentItem(2,false);
            fragmentStatusHomeBinding.txtNewOrder.setBackgroundResource(R.color.colorTransparent);
            fragmentStatusHomeBinding.txtNewOrder.setTypeface(type);

            fragmentStatusHomeBinding.txtProcessingOrder.setBackgroundResource(R.color.colorTransparent);
            fragmentStatusHomeBinding.txtProcessingOrder.setTypeface(type);

            fragmentStatusHomeBinding.txtServedOrder.setBackgroundResource(R.drawable.round_selected_submenu);
            fragmentStatusHomeBinding.txtServedOrder.setTypeface(typeBold);
            adapter.servedOrderListFragment.checkListSize();
        }

    }

    // Set tab and pager data
    private void setPager() {
        currentTab=0;
        adapter=new PagerAdapterOrder(getChildFragmentManager(),mContext);
        fragmentStatusHomeBinding.pager.setAdapter(adapter);
        fragmentStatusHomeBinding.pager.setCurrentItem(0);
        fragmentStatusHomeBinding.pager.setOffscreenPageLimit(3);
        adapter.notifyDataSetChanged();

        fragmentStatusHomeBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                fragmentStatusHomeBinding.pager.setCurrentItem(i,false);
                currentTab=i;
                if(i==0){
                    newBadgeAction(false);
                    Utility.setSharedPreferencesInteger(mContext, constant.NOTIFICATION_COUNT_NEW,0);
                    fragmentStatusHomeBinding.txtNewOrder.setBackgroundResource(R.drawable.round_selected_submenu);
                    fragmentStatusHomeBinding.txtNewOrder.setTypeface(typeBold);

                    fragmentStatusHomeBinding.txtProcessingOrder.setBackgroundResource(R.color.colorTransparent);
                    fragmentStatusHomeBinding.txtProcessingOrder.setTypeface(type);

                    fragmentStatusHomeBinding.txtServedOrder.setBackgroundResource(R.color.colorTransparent);
                    fragmentStatusHomeBinding.txtServedOrder.setTypeface(type);
                    adapter.newOrderListFragment.checkListSize();
                }else if(i==1){
                    processingBadgeAction(false);
                    Utility.setSharedPreferencesInteger(mContext, constant.NOTIFICATION_COUNT_PROCESSING,0);
                    fragmentStatusHomeBinding.txtNewOrder.setBackgroundResource(R.color.colorTransparent);
                    fragmentStatusHomeBinding.txtNewOrder.setTypeface(type);

                    fragmentStatusHomeBinding.txtProcessingOrder.setBackgroundResource(R.drawable.round_selected_submenu);
                    fragmentStatusHomeBinding.txtProcessingOrder.setTypeface(typeBold);

                    fragmentStatusHomeBinding.txtServedOrder.setBackgroundResource(R.color.colorTransparent);
                    fragmentStatusHomeBinding.txtServedOrder.setTypeface(type);
                    adapter.processingOrderListFragment.checkListSize();
                }else{
                    fragmentStatusHomeBinding.txtNewOrder.setBackgroundResource(R.color.colorTransparent);
                    fragmentStatusHomeBinding.txtNewOrder.setTypeface(type);

                    fragmentStatusHomeBinding.txtProcessingOrder.setBackgroundResource(R.color.colorTransparent);
                    fragmentStatusHomeBinding.txtProcessingOrder.setTypeface(type);

                    fragmentStatusHomeBinding.txtServedOrder.setBackgroundResource(R.drawable.round_selected_submenu);
                    fragmentStatusHomeBinding.txtServedOrder.setTypeface(typeBold);
                    adapter.servedOrderListFragment.checkListSize();
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    // change tab position
    public void chageTab(int position){
        fragmentStatusHomeBinding.pager.setCurrentItem(position,false);
    }

    // Show and hide new notification badge
    public void newBadgeAction(boolean status){
        if(status)
            fragmentStatusHomeBinding.layLinCountNew.setVisibility(View.VISIBLE);
        else
            fragmentStatusHomeBinding.layLinCountNew.setVisibility(View.GONE);

    }

    // Show and hide processing notification badge
    public void processingBadgeAction(boolean status){
        if(status)
            fragmentStatusHomeBinding.layLinCountProcessing.setVisibility(View.VISIBLE);
        else
            fragmentStatusHomeBinding.layLinCountProcessing.setVisibility(View.GONE);

    }

    // Change tab screen background color
    public void changeBackgroundColor(int myTab,boolean listItem){
        if(currentTab==myTab){
            if(listItem) {
                fragmentStatusHomeBinding.Cordinator.setBackgroundColor(getResources().getColor(R.color.colorStatusBackground));
                fragmentStatusHomeBinding.tabLayoutMain.setBackgroundResource(R.drawable.status_tab_background);
            }else {
                fragmentStatusHomeBinding.Cordinator.setBackgroundColor(getResources().getColor(R.color.colorCardProfile));
                fragmentStatusHomeBinding.tabLayoutMain.setBackgroundResource(R.drawable.status_tab_background_empty);
            }
        }

    }

}