package com.waitty.waiter.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.waitty.waiter.R;
import com.waitty.waiter.adapter.SearchItemAdapter;
import com.waitty.waiter.appinterface.getResponseData;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.ActivitySearchBinding;
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
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;

public class SearchActivity extends AppCompatActivity implements getResponseData {
    private Context mContext;
    private ActivitySearchBinding activitySearchBinding;
    private  MenuItem cancelItem,arrowItem;
    private MyLoading loader;

    private SearchItemAdapter adapter;
    private LinearLayoutManager LinLayManager;
    private LinkedList<OrderDetails> itemList=new LinkedList<OrderDetails>();

    private Handler handler;
    private Runnable update;
    private Timer timer;

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        activitySearchBinding= DataBindingUtil.setContentView(this, R.layout.activity_search);
        mContext = this;
        init();
    }

    // Variable initialization
    private void init() {

        loader = new MyLoading(mContext);
        setToolbar();

        itemList.clear();

        LinLayManager=new LinearLayoutManager(mContext);
        LinLayManager.setOrientation(LinearLayoutManager.VERTICAL);
        activitySearchBinding.rcvItem.setLayoutManager(LinLayManager);
        adapter = new SearchItemAdapter(mContext,itemList);
        activitySearchBinding.rcvItem.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        activitySearchBinding.edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (String.valueOf(editable).trim().isEmpty()) {
                    arrowItem.setVisible(false);
                    cancelItem.setVisible(true);
                    activitySearchBinding.txtNoRecord.setText("");
                }else {
                    arrowItem.setVisible(false);
                    cancelItem.setVisible(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        activitySearchBinding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == android.view.KeyEvent.KEYCODE_SEARCH)) || (actionId == EditorInfo.IME_ACTION_SEARCH))
                    searchOrder();
                return false;
            }
        });

        timer=new Timer();
        handler = new Handler();
        update = new Runnable() {
            public void run() {
                if(Utility.isNetworkAvailable(mContext) && itemList.size()>0)
                    getOrderStatusAPI();
            }
        };

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 12000, 12000);

    }

    // Set toolbar
    public void setToolbar() {
        setSupportActionBar(activitySearchBinding.toolbarActionbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        activitySearchBinding.toolbarActionbar.setNavigationIcon(R.mipmap.search_icon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cancel_customization, menu);
        cancelItem=menu.findItem(R.id.cancel);
        arrowItem=menu.findItem(R.id.arrow);
        arrowItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cancel)
            finish();
        else if (item.getItemId() == R.id.arrow)
            searchOrder();
        return super.onOptionsItemSelected(item);
    }

    // Search order by order ID
    private void searchOrder() {
        if(!activitySearchBinding.edtSearch.getText().toString().trim().isEmpty()){
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activitySearchBinding.edtSearch.getWindowToken(), 0);
            if(Utility.isNetworkAvailable(mContext)) {
                itemList.clear();
                adapter.notifyDataSetChanged();
                searchOrderAPI();
            }else
                Utility.ShowToast(mContext,getString(R.string.check_network),0);
        }
    }

    // Check order statusAPI
    public void getOrderStatusAPI() {
        try {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(API.ORDER_ID, itemList.get(0).getId());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.checkOrderStatus(jsonObject, Utility.getSharedPreferencesString(mContext, constant.USER_SECURITY_TOKEN));
            new APICall(mContext).Server_Interaction(call, this, API.ORDER_STATUS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get search order API
    private void searchOrderAPI() {
        try{
            loader.show(getString(R.string.wait));

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.ORDER_ID_DISPLAY, activitySearchBinding.edtSearch.getText().toString().trim());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.searchOrder(jsonObject,Utility.getSharedPreferencesString(mContext, constant.USER_SECURITY_TOKEN));
            new APICall(mContext).Server_Interaction(call,this ,API.SEARCH_ORDER);

        }catch (Exception e){
            e.printStackTrace();
            loader.dismiss();
        }
    }

    @Override
    public void onSuccess(JSONObject OBJ, String msg, String typeAPI) {

        try {
            loader.dismiss();
            adapter.notifyDataSetChanged();

            if(OBJ.length()>0 && OBJ!=null){

                switch (typeAPI) {
                    case API.SEARCH_ORDER:
                        if(OBJ.getJSONArray(API.DATA).length()>0 && OBJ.getJSONArray(API.DATA)!=null){
                            itemList.clear();
                            activitySearchBinding.Cordinator.setBackgroundColor(getResources().getColor(R.color.colorStatusBackground));
                            activitySearchBinding.txtNoRecord.setVisibility(View.GONE);
                            Type type = new TypeToken<LinkedList<OrderDetails>>() { }.getType();
                            LinkedList<OrderDetails> TempList = new Gson().fromJson(OBJ.getJSONArray(API.DATA).toString(), type);
                            itemList.add(TempList.get(0));
                            adapter.notifyDataSetChanged();
                            activitySearchBinding.edtSearch.setFocusableInTouchMode(true);
                        }else{
                            activitySearchBinding.Cordinator.setBackgroundColor(getResources().getColor(R.color.colorCardProfile));
                            activitySearchBinding.txtNoRecord.setText(R.string.txt_search_nodata);
                            activitySearchBinding.txtNoRecord.setVisibility(View.VISIBLE);
                        }

                        break;

                    case API.ORDER_STATUS:
                        if(OBJ.getJSONArray(API.DATA).length()>0 && OBJ.getJSONArray(API.DATA)!=null){
                            Type type = new TypeToken<LinkedList<OrderDetails>>() { }.getType();
                            LinkedList<OrderDetails> TempList = new Gson().fromJson(OBJ.getJSONArray(API.DATA).toString(), type);
                            OrderDetails orderDataTemp=TempList.get(0);

                            if(itemList.size()>0){
                                itemList.get(0).setOrderArrivingTime(orderDataTemp.getOrderArrivingTime());
                                itemList.get(0).setWaiterId(orderDataTemp.getWaiterId());
                                itemList.get(0).setOrderStatus(orderDataTemp.getOrderStatus());
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
            }

        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void onFailed(String msg, String typeAPI) {
        loader.dismiss();
        adapter.notifyDataSetChanged();

        if(!msg.isEmpty()){
            switch (typeAPI) {
                case API.SEARCH_ORDER:
                    activitySearchBinding.Cordinator.setBackgroundColor(getResources().getColor(R.color.colorCardProfile));
                    activitySearchBinding.txtNoRecord.setText(R.string.txt_search_nodata);
                    activitySearchBinding.txtNoRecord.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

}

