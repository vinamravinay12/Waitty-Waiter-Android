package com.waitty.waiter.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.waitty.waiter.R;
import com.waitty.waiter.adapter.OrderHistoryItemAdapter;
import com.waitty.waiter.appinterface.getResponseData;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.ActivityNeworderDetailsBinding;
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

public class ProcessingOrderDetailsActivity extends AppCompatActivity implements getResponseData {

    private Context mContext;
    private ActivityNeworderDetailsBinding activityNeworderDetailsBinding;
    private MyLoading loader;
    private OrderDetails orderData;
    private Typeface typeLight,typeSemiBold,typeMedium;
    private int orderStatus;
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
        activityNeworderDetailsBinding= DataBindingUtil.setContentView(this, R.layout.activity_neworder_details);
        mContext = this;
        init();
    }

    // Variable initialization
    private void init() {
        try{

            loader = new MyLoading(mContext);
            typeLight = Typeface.createFromAsset(mContext.getAssets(), "p_light.TTF");
            typeSemiBold = Typeface.createFromAsset(mContext.getAssets(), "p_semibold.TTF");
            typeMedium = Typeface.createFromAsset(mContext.getAssets(), "p_medium.TTF");

            Utility.setToolbar(mContext,activityNeworderDetailsBinding.toolbarActionbar);
            orderData=(OrderDetails) getIntent().getSerializableExtra(API.DATA);

            orderStatus=orderData.getOrderStatus().getId();
            activityNeworderDetailsBinding.txtHeading.setText(getString(R.string.txt_processing)+"- "+orderData.getOrderIdDisplay());
            activityNeworderDetailsBinding.txtName.setText(orderData.getUser().getName());
            activityNeworderDetailsBinding.txtOrderNo.setText(mContext.getString(R.string.txt_order_no)+orderData.getOrderIdDisplay());

            if(orderData.getTable().getName().contains(mContext.getString(R.string.txt_table_no)))
                activityNeworderDetailsBinding.txtTableNo.setText(mContext.getString(R.string.txt_t)+" "+orderData.getTable().getName().replace(mContext.getString(R.string.txt_table_no),"").trim());
            else
                activityNeworderDetailsBinding.txtTableNo.setText(mContext.getString(R.string.txt_t)+" "+orderData.getTable().getName().replace(mContext.getString(R.string.txt_take_away),"").trim());

            int Quantity=0;
            for(int i=0;i<orderData.getOrderItems().size();i++){
                Quantity=Quantity+orderData.getOrderItems().get(i).getQuantity();
            }
            activityNeworderDetailsBinding.txtQuantity.setText(mContext.getString(R.string.txt_cross_sign)+" "+Quantity);

            activityNeworderDetailsBinding.txtOrderType.setText(orderData.getOrderType());
            if(orderData.getOrderType().contains(mContext.getString(R.string.txt_dinein)))
                activityNeworderDetailsBinding.txtOrderType.setBackgroundResource(R.drawable.round_selected_submenu);
            else
                activityNeworderDetailsBinding.txtOrderType.setBackgroundResource(R.drawable.round_take_away);

            if(!orderData.getComment().trim().isEmpty()){
                activityNeworderDetailsBinding.viewLineComment.setVisibility(View.VISIBLE);
                activityNeworderDetailsBinding.txtComment.setVisibility(View.VISIBLE);
                activityNeworderDetailsBinding.txtComment.setText(Html.fromHtml(orderData.getComment()));
            }

            if(!orderData.getOrderArrivingTime().isEmpty()) {
                activityNeworderDetailsBinding.layRelETA.setVisibility(View.VISIBLE);
                activityNeworderDetailsBinding.txtETA.setText(Utility.ChangeDateFormate(constant.dateFormaterServer,Utility.chageUTC_ToLocalDate(constant.dateFormaterServer,orderData.getOrderArrivingTime(),constant.dateFormaterServer),constant.dateFormaterTrackOrder));
            }

            LinearLayoutManager LinLayManager=new LinearLayoutManager(mContext);
            LinLayManager.setOrientation(LinearLayoutManager.VERTICAL);
            activityNeworderDetailsBinding.rcvItem.setLayoutManager(LinLayManager);
            OrderHistoryItemAdapter adapter = new OrderHistoryItemAdapter(mContext,orderData.getOrderItems(),orderData.getPaymentSymbol(),2);
            activityNeworderDetailsBinding.rcvItem.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            activityNeworderDetailsBinding.txtItemTotalHint.setTypeface(typeLight);
            activityNeworderDetailsBinding.txtItemTotal.setTypeface(typeLight);
            activityNeworderDetailsBinding.txtTaxHint.setTypeface(typeLight);
            activityNeworderDetailsBinding.txtTax.setTypeface(typeLight);
            activityNeworderDetailsBinding.txtTotalHint.setTypeface(typeSemiBold);
            activityNeworderDetailsBinding.txtTotalAmount.setTypeface(typeSemiBold);

            if(orderData.getOrderItems().size()>1)
                activityNeworderDetailsBinding.txtItemTotalHint.setText(R.string.txt_items_total);
            else
                activityNeworderDetailsBinding.txtItemTotalHint.setText(R.string.txt_item_total);

            activityNeworderDetailsBinding.txtItemTotal.setText(orderData.getPaymentSymbol()+" "+ constant.FORMATOR_DECIMAL.format(orderData.getTotal()));
            activityNeworderDetailsBinding.txtTax.setText(orderData.getPaymentSymbol()+" "+constant.FORMATOR_DECIMAL.format(orderData.getTax()));
            activityNeworderDetailsBinding.txtTotalAmount.setText(orderData.getPaymentSymbol()+" "+constant.FORMATOR_DECIMAL.format(orderData.getOrderAmount()));

            activityNeworderDetailsBinding.layLinAction.setVisibility(View.GONE);
            activityNeworderDetailsBinding.layLinTracking.setVisibility(View.VISIBLE);

            activityNeworderDetailsBinding.imvDeliverd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderDeliverdClick();
                }
            });

            activityNeworderDetailsBinding.txtimvDeliverd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderDeliverdClick();
                }
            });

            setTracking();

            getOrderStatusAPI();
            timer=new Timer();
            handler = new Handler();
            update = new Runnable() {
                public void run() {
                    if(Utility.isNetworkAvailable(mContext))
                        getOrderStatusAPI();
                }
            };

            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.post(update);
                }
            }, 12000, 12000);

            activityNeworderDetailsBinding.txtName.setFocusableInTouchMode(true);
        }catch (Exception e){e.printStackTrace();}
    }

    // Set order tracking
    private void setTracking() {
        if(orderStatus==constant.ORDER_PLACED || orderStatus==constant.ORDER_PREPARING){
            activityNeworderDetailsBinding.imvPreparing.setBorderColor(getResources().getColor(R.color.colorOrderKitchen));
            activityNeworderDetailsBinding.txtimvPreparing.setTypeface(typeMedium);

            activityNeworderDetailsBinding.imvReady.setBorderColor(getResources().getColor(R.color.colorCardProfile));
            activityNeworderDetailsBinding.txtimvReady.setTypeface(typeLight);

            activityNeworderDetailsBinding.imvDeliverd.setBorderColor(getResources().getColor(R.color.colorCardProfile));
            activityNeworderDetailsBinding.txtimvDeliverd.setTypeface(typeLight);
        }else if(orderStatus==constant.ORDER_READY_SERVE){
            activityNeworderDetailsBinding.imvPreparing.setBorderColor(getResources().getColor(R.color.colorCardProfile));
            activityNeworderDetailsBinding.txtimvPreparing.setTypeface(typeLight);

            activityNeworderDetailsBinding.imvReady.setBorderColor(getResources().getColor(R.color.colorOrderReady));
            activityNeworderDetailsBinding.txtimvReady.setTypeface(typeMedium);

            activityNeworderDetailsBinding.imvDeliverd.setBorderColor(getResources().getColor(R.color.colorCardProfile));
            activityNeworderDetailsBinding.txtimvDeliverd.setTypeface(typeLight);
        }else{
            activityNeworderDetailsBinding.imvPreparing.setBorderColor(getResources().getColor(R.color.colorCardProfile));
            activityNeworderDetailsBinding.txtimvPreparing.setTypeface(typeLight);

            activityNeworderDetailsBinding.imvReady.setBorderColor(getResources().getColor(R.color.colorCardProfile));
            activityNeworderDetailsBinding.txtimvReady.setTypeface(typeLight);

            activityNeworderDetailsBinding.imvDeliverd.setBorderColor(getResources().getColor(R.color.colorYallow));
            activityNeworderDetailsBinding.txtimvDeliverd.setTypeface(typeMedium);
        }

    }

    // Order deliverd click
    private void orderDeliverdClick() {
        if(orderStatus==constant.ORDER_READY_SERVE){
          if(Utility.isNetworkAvailable(mContext))
              orderDeliverdAPI();
          else
              Utility.ShowSnackbar(mContext,activityNeworderDetailsBinding.Cordinator,getString(R.string.check_network));
        }
    }

    // Check order statusAPI
    private void getOrderStatusAPI() {
        try {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(API.ORDER_ID, orderData.getId());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.checkOrderStatus(jsonObject, Utility.getSharedPreferencesString(mContext, constant.USER_SECURITY_TOKEN));
            new APICall(mContext).Server_Interaction(call, this, API.ORDER_STATUS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Order deliverd API
    private void orderDeliverdAPI() {
        try{
            loader.show(getString(R.string.wait));

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.ORDER_ID, orderData.getId());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.deliverdOrder(jsonObject,Utility.getSharedPreferencesString(mContext, constant.USER_SECURITY_TOKEN));
            new APICall(mContext).Server_Interaction(call,this ,API.DELIVERD_ORDER);

        }catch (Exception e){
            e.printStackTrace();
            loader.dismiss();
        }
    }

    @Override
    public void onSuccess(JSONObject OBJ, String msg, String typeAPI) {

        try {
            loader.dismiss();

            if(OBJ.length()>0 && OBJ!=null){
                switch (typeAPI) {
                    case API.DELIVERD_ORDER:
                        Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_ORDER_RELOAD,true);
                        Utility.setSharedPreferencesBoolean(mContext,constant.SERVED_ORDER_RELOAD,true);
                        Utility.ShowToast(mContext,msg,0);
                        finish();
                        break;
                    case API.ORDER_STATUS:
                        if(OBJ.getJSONArray(API.DATA).length()>0 && OBJ.getJSONArray(API.DATA)!=null){
                            Type type = new TypeToken<LinkedList<OrderDetails>>() { }.getType();
                            LinkedList<OrderDetails> TempList = new Gson().fromJson(OBJ.getJSONArray(API.DATA).toString(), type);
                            OrderDetails orderDataTemp=TempList.get(0);
                            if(orderStatus<orderDataTemp.getOrderStatus().getId())
                                Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_ORDER_RELOAD,true);

                            orderStatus=orderDataTemp.getOrderStatus().getId();
                            setTracking();
                            if(!orderDataTemp.getOrderArrivingTime().trim().isEmpty()){
                                orderData.setOrderArrivingTime(orderDataTemp.getOrderArrivingTime().trim());
                                activityNeworderDetailsBinding.layRelETA.setVisibility(View.VISIBLE);
                                activityNeworderDetailsBinding.txtETA.setText(Utility.ChangeDateFormate(constant.dateFormaterServer,Utility.chageUTC_ToLocalDate(constant.dateFormaterServer,orderData.getOrderArrivingTime(),constant.dateFormaterServer),constant.dateFormaterTrackOrder));
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
        if(!msg.isEmpty()){
            switch (typeAPI) {
                case API.DELIVERD_ORDER:
                    Utility.ShowSnackbar(mContext,activityNeworderDetailsBinding.Cordinator,msg);
                    break;
            }
        }
    }

}
