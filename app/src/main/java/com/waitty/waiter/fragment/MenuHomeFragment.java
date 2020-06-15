package com.waitty.waiter.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import com.waitty.waiter.adapter.PagerAdapterMenu;
import com.waitty.waiter.databinding.FragmentMenuHomeBinding;
import com.waitty.waiter.model.MenuData;

public class MenuHomeFragment extends Fragment  {

    private ViewGroup root;
    private Context mContext;
    FragmentMenuHomeBinding fragmentMenuHomeBinding;
    private PagerAdapterMenu adapter;
    private MenuData menuDataList;
    private Typeface type,typeBold;

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       // fragmentMenuHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu_home, container, false);
        root = (ViewGroup) fragmentMenuHomeBinding.getRoot();
        mContext = getContext();
        init();
        return root;
    }

    // Variable initialization
    private void init() {
        type = Typeface.createFromAsset(mContext.getAssets(), "p_medium.TTF");
        typeBold = Typeface.createFromAsset(mContext.getAssets(), "p_bold.TTF");
      //  getMenuDataAPI();
    }

    // Restaurant menu data
   /* private void getMenuDataAPI() {
        try{
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.RESTAURANT_ID, ((HomeActivity)mContext).userInformation.getParentId());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.getCategory(jsonObject);
            new APICall(mContext).Server_Interaction(call,this ,API.GET_CATEGORY);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
*/
    // Set tab and pager data
   /* private void setPager() {

        adapter=new PagerAdapterMenu(getChildFragmentManager(),mContext, menuDataList);
        fragmentMenuHomeBinding.pager.setAdapter(adapter);
        fragmentMenuHomeBinding.pager.setCurrentItem(0);
        fragmentMenuHomeBinding.tabLayout.setupWithViewPager(fragmentMenuHomeBinding.pager);
        fragmentMenuHomeBinding.pager.setOffscreenPageLimit(fragmentMenuHomeBinding.tabLayout.getTabCount());
        adapter.notifyDataSetChanged();

        for (int i = 0; i < fragmentMenuHomeBinding.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = fragmentMenuHomeBinding.tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i,0));
        }

        fragmentMenuHomeBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //do stuff here
                fragmentMenuHomeBinding.pager.setCurrentItem(tab.getPosition(),false);
                View view=tab.getCustomView();
                ((TextView)view.findViewById(R.id.txtMenu)).setTypeface(typeBold);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view=tab.getCustomView();
                ((TextView)view.findViewById(R.id.txtMenu)).setTypeface(type);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onSuccess(JSONObject OBJ, String msg, String typeAPI) {

        try {
            if(OBJ.length()>0 && OBJ!=null){

                switch (typeAPI) {
                    case API.GET_CATEGORY:
                        if(OBJ.getJSONObject(API.DATA)!=null && OBJ.getJSONObject(API.DATA).length()>0){
                            Type type = new TypeToken<MenuData>() { }.getType();
                            menuDataList = new Gson().fromJson(OBJ.getJSONObject(API.DATA).toString(), type);

                            if(menuDataList.getCategory()!=null && menuDataList.getCategory().size()>0)
                                setPager();
                            else
                                noDataFound();
                        }else
                            noDataFound();
                        break;
                }
            }

        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void onFailed(String msg, String typeAPI) {
        noDataFound();
        if(!msg.isEmpty()){
            switch (typeAPI) {
                case API.GET_CATEGORY:
                    Utility.ShowSnackbar(mContext, fragmentMenuHomeBinding.Cordinator,msg);
                    break;
            }
        }
    }

    // Set no data view
    public void noDataFound(){
        fragmentMenuHomeBinding.tabLayout.setVisibility(View.GONE);
        fragmentMenuHomeBinding.pager.setVisibility(View.GONE);
        fragmentMenuHomeBinding.txtNoRecord.setVisibility(View.VISIBLE);
    }*/

}