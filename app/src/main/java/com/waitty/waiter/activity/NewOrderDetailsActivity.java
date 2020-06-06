package com.waitty.waiter.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
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
import com.waitty.waiter.databinding.DialogLogoutBinding;
import com.waitty.waiter.model.OrderDetails;
import com.waitty.waiter.retrofit.API;
import com.waitty.waiter.retrofit.APICall;
import com.waitty.waiter.retrofit.ApiClient;
import com.waitty.waiter.retrofit.ApiInterface;
import com.waitty.waiter.utility.Dialog;
import com.waitty.waiter.utility.MyLoading;
import com.waitty.waiter.utility.Utility;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.LinkedList;
import retrofit2.Call;

public class NewOrderDetailsActivity extends AppCompatActivity implements getResponseData {

    private Context mContext;
    private ActivityNeworderDetailsBinding activityNeworderDetailsBinding;
    private OrderDetails orderData;
    private MyLoading loader;
    private AlertDialog dialogAction;

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        try {

            loader = new MyLoading(mContext);
            Utility.setToolbar(mContext, activityNeworderDetailsBinding.toolbarActionbar);
            orderData = (OrderDetails) getIntent().getSerializableExtra(API.DATA);

            activityNeworderDetailsBinding.txtHeading.setText(getString(R.string.txt_accept) + "- " + orderData.getOrderIdDisplay());
            activityNeworderDetailsBinding.txtName.setText(orderData.getUser().getName());
            activityNeworderDetailsBinding.txtOrderNo.setText(mContext.getString(R.string.txt_order_no) + orderData.getOrderIdDisplay());

            if (orderData.getTable().getName().contains(mContext.getString(R.string.txt_table_no)))
                activityNeworderDetailsBinding.txtTableNo.setText(mContext.getString(R.string.txt_t) + " " + orderData.getTable().getName().replace(mContext.getString(R.string.txt_table_no), "").trim());
            else
                activityNeworderDetailsBinding.txtTableNo.setText(mContext.getString(R.string.txt_t) + " " + orderData.getTable().getName().replace(mContext.getString(R.string.txt_take_away), "").trim());

            int Quantity = 0;
            int outofStockCount = 0;
            for (int i = 0; i < orderData.getOrderItems().size(); i++) {
                Quantity = Quantity + orderData.getOrderItems().get(i).getQuantity();
                if(orderData.getOrderItems().get(i).getIn_stock()==0)
                    outofStockCount=outofStockCount+1;
            }
            activityNeworderDetailsBinding.txtQuantity.setText(mContext.getString(R.string.txt_cross_sign) + " " + Quantity);

            if(outofStockCount==0){
                activityNeworderDetailsBinding.txtAccept.setVisibility(View.VISIBLE);
                activityNeworderDetailsBinding.txtReject.setVisibility(View.GONE);
            }else if(outofStockCount==orderData.getOrderItems().size()){
                activityNeworderDetailsBinding.txtAccept.setVisibility(View.GONE);
                activityNeworderDetailsBinding.txtReject.setVisibility(View.VISIBLE);
            }else{
                activityNeworderDetailsBinding.txtAccept.setVisibility(View.VISIBLE);
                activityNeworderDetailsBinding.txtReject.setVisibility(View.VISIBLE);
            }

            activityNeworderDetailsBinding.txtOrderType.setText(orderData.getOrderType());
            if (orderData.getOrderType().contains(mContext.getString(R.string.txt_dinein)))
                activityNeworderDetailsBinding.txtOrderType.setBackgroundResource(R.drawable.round_selected_submenu);
            else
                activityNeworderDetailsBinding.txtOrderType.setBackgroundResource(R.drawable.round_take_away);

            if (!orderData.getComment().trim().isEmpty()) {
                activityNeworderDetailsBinding.viewLineComment.setVisibility(View.VISIBLE);
                activityNeworderDetailsBinding.txtComment.setVisibility(View.VISIBLE);
                activityNeworderDetailsBinding.txtComment.setText(Html.fromHtml(orderData.getComment()));
            }

            LinearLayoutManager LinLayManager = new LinearLayoutManager(mContext);
            LinLayManager.setOrientation(LinearLayoutManager.VERTICAL);
            activityNeworderDetailsBinding.rcvItem.setLayoutManager(LinLayManager);
            OrderHistoryItemAdapter adapter = new OrderHistoryItemAdapter(mContext, orderData.getOrderItems(), orderData.getPaymentSymbol(), 1);
            activityNeworderDetailsBinding.rcvItem.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if (orderData.getOrderItems().size() > 1)
                activityNeworderDetailsBinding.txtItemTotalHint.setText(R.string.txt_items_total);
            else
                activityNeworderDetailsBinding.txtItemTotalHint.setText(R.string.txt_item_total);

            activityNeworderDetailsBinding.txtItemTotal.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getTotal()));
            activityNeworderDetailsBinding.txtTax.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getTax()));
            activityNeworderDetailsBinding.txtTotalAmount.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getOrderAmount()));

            getOrderStatusAPI();
            activityNeworderDetailsBinding.txtAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderActionDialog(1);
                }
            });

            activityNeworderDetailsBinding.txtReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderActionDialog(2);
                }
            });

            activityNeworderDetailsBinding.txtName.setFocusableInTouchMode(true);
        }catch (Exception e){e.printStackTrace();}
    }

    // Show dialog for order action
    public void orderActionDialog(final int actionType){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        DialogLogoutBinding dialogLogoutBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_logout, null, false);
        builder.setView(dialogLogoutBinding.getRoot());

        if(actionType==1){
            dialogLogoutBinding.txtHeading.setText(R.string.txt_accept);
            dialogLogoutBinding.txtMSG.setText(R.string.accept_msg);
            dialogLogoutBinding.btnLogout.setText(R.string.txt_accept);
        }else{
            dialogLogoutBinding.txtHeading.setText(R.string.txt_reject);
            dialogLogoutBinding.txtMSG.setText(R.string.reject_msg);
            dialogLogoutBinding.btnLogout.setText(R.string.txt_reject);
        }

        dialogAction = builder.create();
        dialogAction.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAction.setCanceledOnTouchOutside(false);
        dialogAction.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAction.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogAction.show();

        dialogLogoutBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAction.dismiss();
            }
        });

        dialogLogoutBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.isNetworkAvailable(mContext)) {
                    dialogAction.dismiss();
                    if(actionType==1)
                        acceptOrderAPI();
                    else
                        rejectOrderAPI();
                }else
                    Utility.ShowToast(mContext,getString(R.string.check_network),0);

            }
        });
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

    // Accept order API
    private void acceptOrderAPI() {
        try{
            loader.show(getString(R.string.wait));

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.ORDER_ID, orderData.getId());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.acceptOrder(jsonObject,Utility.getSharedPreferencesString(mContext, constant.USER_SECURITY_TOKEN));
            new APICall(mContext).Server_Interaction(call,this ,API.ACCEPT_ORDER);

        }catch (Exception e){
            e.printStackTrace();
            loader.dismiss();
        }
    }

    // Reject order API
    private void rejectOrderAPI() {
        try{
            loader.show(getString(R.string.wait));

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.ORDER_ID, orderData.getId());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.rejectOrder(jsonObject,Utility.getSharedPreferencesString(mContext, constant.USER_SECURITY_TOKEN));
            new APICall(mContext).Server_Interaction(call,this ,API.REJECT_ORDER);

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
                    case API.ACCEPT_ORDER:
                        Utility.setSharedPreferencesBoolean(mContext,constant.NEW_ORDER_RELOAD,true);
                        Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_ORDER_RELOAD,true);
                        Utility.ShowToast(mContext,msg,0);
                        finish();
                        break;
                    case API.REJECT_ORDER:
                        Utility.setSharedPreferencesBoolean(mContext,constant.NEW_ORDER_RELOAD,true);
                        Utility.setSharedPreferencesBoolean(mContext,constant.SERVED_ORDER_RELOAD,true);
                        Utility.ShowToast(mContext,msg,0);
                        finish();
                        break;
                    case API.ORDER_STATUS:
                        if(OBJ.getJSONArray(API.DATA).length()>0 && OBJ.getJSONArray(API.DATA)!=null){
                            Type type = new TypeToken<LinkedList<OrderDetails>>() { }.getType();
                            LinkedList<OrderDetails> TempList = new Gson().fromJson(OBJ.getJSONArray(API.DATA).toString(), type);
                            OrderDetails orderDataTemp=TempList.get(0);
                            if(orderDataTemp.getWaiterId()>0){
                                Utility.setSharedPreferencesBoolean(mContext,constant.NEW_ORDER_RELOAD,true);
                                Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_ORDER_RELOAD,true);
                                Dialog.orderAlreadyAcceptedDialog(mContext,getString(R.string.txt_alert),getString(R.string.txt_order_already_accepted),true);
                            }else if(orderData.getOrderStatus().getId()==constant.ORDER_REFUND_INITIATED || orderData.getOrderStatus().getId()==constant.ORDER_AMOUNT_REFUNDED){
                                Utility.setSharedPreferencesBoolean(mContext,constant.NEW_ORDER_RELOAD,true);
                                Utility.setSharedPreferencesBoolean(mContext,constant.SERVED_ORDER_RELOAD,true);
                                Dialog.orderAlreadyAcceptedDialog(mContext,getString(R.string.txt_alert),getString(R.string.txt_order_already_rejected),true);
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
                case API.ACCEPT_ORDER:
                    Utility.setSharedPreferencesBoolean(mContext,constant.NEW_ORDER_RELOAD,true);
                    Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_ORDER_RELOAD,true);
                    Utility.setSharedPreferencesBoolean(mContext,constant.SERVED_ORDER_RELOAD,true);
                    Dialog.orderAlreadyAcceptedDialog(mContext,getString(R.string.txt_alert),msg,true);
                    break;
                case API.REJECT_ORDER:
                    Utility.setSharedPreferencesBoolean(mContext,constant.NEW_ORDER_RELOAD,true);
                    Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_ORDER_RELOAD,true);
                    Utility.setSharedPreferencesBoolean(mContext,constant.SERVED_ORDER_RELOAD,true);
                    Dialog.orderAlreadyAcceptedDialog(mContext,getString(R.string.txt_alert),msg,true);
                    break;
            }
        }
    }
}
