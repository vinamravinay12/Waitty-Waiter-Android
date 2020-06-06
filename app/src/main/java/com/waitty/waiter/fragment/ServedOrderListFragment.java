package com.waitty.waiter.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.waitty.waiter.R;
import com.waitty.waiter.activity.HomeActivity;
import com.waitty.waiter.adapter.OrderHistoryAdapter;
import com.waitty.waiter.appinterface.getResponseData;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.FragmentNeworderListBinding;
import com.waitty.waiter.model.OrderDetails;
import com.waitty.waiter.retrofit.API;
import com.waitty.waiter.retrofit.APICall;
import com.waitty.waiter.retrofit.ApiClient;
import com.waitty.waiter.retrofit.ApiInterface;
import com.waitty.waiter.utility.MyLoading;
import com.waitty.waiter.utility.Utility;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.LinkedList;
import retrofit2.Call;

public class ServedOrderListFragment extends Fragment implements getResponseData {
    private ViewGroup root;
    private Context mContext;
    FragmentNeworderListBinding fragmentNeworderListBinding;

    private MyLoading loader;
    private OrderHistoryAdapter adapter;
    private LinearLayoutManager LinLayManager;
    private LinkedList<OrderDetails> itemList=new LinkedList<>();

    private int pastVisiblesItems, visibleItemCount, totalItemCount,pageNo=1;
    private boolean loading = true;

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        if(Utility.getSharedPreferencesBoolean(mContext, constant.SERVED_ORDER_RELOAD)){
            Utility.setSharedPreferencesBoolean(mContext,constant.SERVED_ORDER_RELOAD,false);
            if (Utility.isNetworkAvailable(mContext))
                refreshList(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentNeworderListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_neworder_list, container, false);
        root = (ViewGroup) fragmentNeworderListBinding.getRoot();
        mContext = getContext();
        init();
        return root;
    }

    // Variable initialization
    private void init() {
        Utility.setSharedPreferencesBoolean(mContext,constant.SERVED_ORDER_RELOAD,false);
        loader = new MyLoading(mContext);
        itemList.clear();

        LinLayManager=new LinearLayoutManager(mContext);
        LinLayManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentNeworderListBinding.rcvItem.setLayoutManager(LinLayManager);
        adapter = new OrderHistoryAdapter(mContext,itemList,3);
        fragmentNeworderListBinding.rcvItem.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getOrderItemDataAPI(false);

        fragmentNeworderListBinding.SwipeRefreshLayout.setColorSchemeResources(R.color.colorYallow);
        fragmentNeworderListBinding.SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utility.isNetworkAvailable(mContext))
                    refreshList(false);
                else {
                    fragmentNeworderListBinding.SwipeRefreshLayout.setRefreshing(false);
                    Utility.ShowToast(mContext,getString(R.string.check_network),0);
                }
            }
        });

        fragmentNeworderListBinding.rcvItem.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = LinLayManager.getChildCount();
                totalItemCount = LinLayManager.getItemCount();
                pastVisiblesItems = LinLayManager.findFirstVisibleItemPosition();

                if(loading) {

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                        if (Utility.isNetworkAvailable(mContext)) {
                            loading=false;
                            getOrderItemDataAPI(true);
                        } else
                            Utility.ShowToast(mContext,getString(R.string.check_network),0);

                    }
                }
            }
        });

        fragmentNeworderListBinding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkAvailable(mContext))
                    refreshList(true);
                else
                    Utility.ShowToast(mContext,getString(R.string.check_network),0);
            }
        });

        fragmentNeworderListBinding.layLinFocus.setFocusableInTouchMode(true);
    }

    // Refresh list
    public void refreshList(boolean loader){
        pageNo=1;
        itemList.clear();
        adapter.notifyDataSetChanged();
        getOrderItemDataAPI(loader);
    }

    // Get order history item API loadmore & pull to refresh
    private void getOrderItemDataAPI(boolean loaderShow) {
        try{
            if(loaderShow)
                loader.show(getString(R.string.wait));

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.LIMIT, 20);
            jsonObject.addProperty(API.PAGE,pageNo);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.getServedOrder(jsonObject,Utility.getSharedPreferencesString(mContext, constant.USER_SECURITY_TOKEN));
            new APICall(mContext).Server_Interaction(call,this ,API.GET_SERVED_ORDER);

        }catch (Exception e){
            e.printStackTrace();
            adapter.notifyDataSetChanged();
            if(fragmentNeworderListBinding.SwipeRefreshLayout.isRefreshing())
                fragmentNeworderListBinding.SwipeRefreshLayout.setRefreshing(false);
            else
                loader.dismiss();
        }
    }

    @Override
    public void onSuccess(JSONObject OBJ, String msg, String typeAPI) {

        try {
            loading=true;
            adapter.notifyDataSetChanged();
            if(fragmentNeworderListBinding.SwipeRefreshLayout.isRefreshing())
                fragmentNeworderListBinding.SwipeRefreshLayout.setRefreshing(false);
            else
                loader.dismiss();

            if(OBJ.length()>0 && OBJ!=null){
                switch (typeAPI) {
                    case API.GET_SERVED_ORDER:

                        if(OBJ.getJSONArray(API.DATA).length()>0 && OBJ.getJSONArray(API.DATA)!=null){
                            pageNo++;

                            Type type = new TypeToken<LinkedList<OrderDetails>>() { }.getType();
                            LinkedList<OrderDetails> TempList = new Gson().fromJson(OBJ.getJSONArray(API.DATA).toString(), type);

                            itemList.addAll(TempList);
                            adapter.notifyDataSetChanged();
                        }else
                            loading=false;

                        if(itemList.size()==0) {
                            fragmentNeworderListBinding.txtNoRecord.setVisibility(View.VISIBLE);
                            fragmentNeworderListBinding.txtNoRecord.setText(R.string.txt_no_servedorder);
                            fragmentNeworderListBinding.btnRefresh.setVisibility(View.VISIBLE);
                        }else {
                            fragmentNeworderListBinding.txtNoRecord.setVisibility(View.GONE);
                            fragmentNeworderListBinding.btnRefresh.setVisibility(View.GONE);
                        }
                        checkListSize();
                        break;
                }

            }

        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void onFailed(String msg, String typeAPI) {
        loading=true;
        adapter.notifyDataSetChanged();
        if(fragmentNeworderListBinding.SwipeRefreshLayout.isRefreshing())
            fragmentNeworderListBinding.SwipeRefreshLayout.setRefreshing(false);
        else
            loader.dismiss();

        if(itemList.size()==0) {
            fragmentNeworderListBinding.txtNoRecord.setVisibility(View.VISIBLE);
            fragmentNeworderListBinding.txtNoRecord.setText(R.string.txt_no_servedorder);
            fragmentNeworderListBinding.btnRefresh.setVisibility(View.VISIBLE);
        }else {
            fragmentNeworderListBinding.txtNoRecord.setVisibility(View.GONE);
            fragmentNeworderListBinding.btnRefresh.setVisibility(View.GONE);
        }

        checkListSize();
        if(!msg.isEmpty()){
            switch (typeAPI) {
                case API.GET_SERVED_ORDER:
                    Utility.ShowSnackbar(mContext, fragmentNeworderListBinding.Cordinator,msg);
                    break;
            }
        }
    }

    public void checkListSize(){
        if(itemList.size()==0)
            ((HomeActivity)mContext).pagerAdapterHome.statusHomeFragment.changeBackgroundColor(2,false);
        else
            ((HomeActivity)mContext).pagerAdapterHome.statusHomeFragment.changeBackgroundColor(2,true);
    }
}
