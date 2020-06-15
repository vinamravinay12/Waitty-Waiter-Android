package com.waitty.waiter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.waitty.waiter.adapter.DishItemAdapter;
import com.waitty.waiter.databinding.FragmentSubmenuBinding;
import com.waitty.waiter.model.DishData;
import com.waitty.waiter.model.SubCategory;
import com.waitty.waiter.retrofit.API;
import com.waitty.waiter.utility.MyLoading;

import java.util.LinkedList;

public class SubmenuFragment extends Fragment {

    private ViewGroup root;
    private Context mContext;
    FragmentSubmenuBinding fragmentSubmenuBinding;
    private SubCategory subCategoryData;

    private MyLoading loader;
    private DishItemAdapter adapter;
    private LinearLayoutManager LinLayManager;
    private LinkedList<DishData> itemList=new LinkedList<DishData>();

    private int pastVisiblesItems, visibleItemCount, totalItemCount,pageNo=1;
    private boolean loading = true;

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      //  fragmentSubmenuBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_submenu, container, false);
        root = (ViewGroup) fragmentSubmenuBinding.getRoot();
        mContext = getContext();

        Bundle bundle = this.getArguments();
        if (bundle != null)
            try {
                subCategoryData = bundle.getParcelable(API.DATA);
            } catch (Exception e) {
                e.printStackTrace();
            }
      //  init();
        return root;
    }

    // Variable initialization
   /* private void init() {
        loader = new MyLoading(mContext);
        itemList.clear();

        LinLayManager=new LinearLayoutManager(mContext);
        LinLayManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentSubmenuBinding.rcvItem.setLayoutManager(LinLayManager);
        adapter = new DishItemAdapter(mContext,itemList);
        fragmentSubmenuBinding.rcvItem.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getDishItemDataAPI(false);

        fragmentSubmenuBinding.SwipeRefreshLayout.setColorSchemeResources(R.color.colorYallow);
        fragmentSubmenuBinding.SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utility.isNetworkAvailable(mContext)) {
                    pageNo=1;
                    itemList.clear();
                    adapter.notifyDataSetChanged();
                    getDishItemDataAPI(false);
                }else {
                    fragmentSubmenuBinding.SwipeRefreshLayout.setRefreshing(false);
                    Utility.ShowToast(mContext,getString(R.string.check_network),0);
                }
            }
        });

        fragmentSubmenuBinding.rcvItem.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = LinLayManager.getChildCount();
                totalItemCount = LinLayManager.getItemCount();
                pastVisiblesItems = LinLayManager.findFirstVisibleItemPosition();

                if(loading) {

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                        if (Utility.isNetworkAvailable(mContext)) {
                            loading=false;
                            getDishItemDataAPI(false);
                        } else
                            Utility.ShowToast(mContext,getString(R.string.check_network),0);

                    }
                }
            }
        });
    }

    // Get dish item API loadmore & pull to refresh
    private void getDishItemDataAPI(boolean loaderShow) {
        try{
            if(loaderShow)
                loader.show(getString(R.string.wait));

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.SUBCATEGORY_ID, subCategoryData.getId());
            jsonObject.addProperty(API.RESTAURANT_ID, ((HomeActivity)mContext).userInformation.getParentId());
            jsonObject.addProperty(API.LIMIT, 50);
            jsonObject.addProperty(API.PAGE,pageNo);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.getDish(jsonObject);
            new APICall(mContext).Server_Interaction(call,this ,API.GET_DISH);

        }catch (Exception e){
            e.printStackTrace();
            if(fragmentSubmenuBinding.SwipeRefreshLayout.isRefreshing()) {
                adapter.notifyDataSetChanged();
                fragmentSubmenuBinding.SwipeRefreshLayout.setRefreshing(false);
            }else
                loader.dismiss();

        }
    }

    @Override
    public void onSuccess(JSONObject OBJ, String msg, String typeAPI) {

        try {
            loading=true;
            adapter.notifyDataSetChanged();
            if(fragmentSubmenuBinding.SwipeRefreshLayout.isRefreshing())
                fragmentSubmenuBinding.SwipeRefreshLayout.setRefreshing(false);
            else
                loader.dismiss();

            if(OBJ.length()>0 && OBJ!=null){

                switch (typeAPI) {
                    case API.GET_DISH:
                        if(OBJ.getJSONArray(API.DATA).length()>0 && OBJ.getJSONArray(API.DATA)!=null){
                            pageNo++;

                            Type type = new TypeToken<LinkedList<DishData>>() { }.getType();
                            LinkedList<DishData> TempList = new Gson().fromJson(OBJ.getJSONArray(API.DATA).toString(), type);

                            itemList.addAll(TempList);
                            adapter.notifyDataSetChanged();
                        }else
                            loading=false;

                        if(itemList.size()==0)
                            fragmentSubmenuBinding.txtNoRecord.setVisibility(View.VISIBLE);
                        else
                            fragmentSubmenuBinding.txtNoRecord.setVisibility(View.GONE);

                        break;
                }
            }

        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void onFailed(String msg, String typeAPI) {
        loading=true;
        adapter.notifyDataSetChanged();
        if(fragmentSubmenuBinding.SwipeRefreshLayout.isRefreshing())
            fragmentSubmenuBinding.SwipeRefreshLayout.setRefreshing(false);
        else
            loader.dismiss();

        if(itemList.size()==0)
            fragmentSubmenuBinding.txtNoRecord.setVisibility(View.VISIBLE);
        else
            fragmentSubmenuBinding.txtNoRecord.setVisibility(View.GONE);

        if(!msg.isEmpty()){
            switch (typeAPI) {
                case API.GET_DISH:
                    Utility.ShowSnackbar(mContext, fragmentSubmenuBinding.Cordinator,msg);
                    break;
            }
        }
    }*/

}