package com.waitty.waiter.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.waitty.waiter.R;
import com.waitty.waiter.activity.SearchActivity;
import com.waitty.waiter.appinterface.getResponseData;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.AdapterSearchItemBinding;
import com.waitty.waiter.databinding.DialogLogoutBinding;
import com.waitty.waiter.model.LoginUser;
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

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> implements getResponseData {

    private LinkedList<OrderDetails> dataList;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private MyLoading loader;
    private AlertDialog dialogAction;
    private Typeface typeLight,typeSemiBold,typeMedium;
    Type type = new TypeToken<LoginUser>() { }.getType();

    // Class constructor
    public SearchItemAdapter(Context context, LinkedList<OrderDetails> data) {
        this.dataList = data;
        this.mContext = context;
        loader = new MyLoading(mContext);
        typeLight = Typeface.createFromAsset(mContext.getAssets(), "p_light.TTF");
        typeSemiBold = Typeface.createFromAsset(mContext.getAssets(), "p_semibold.TTF");
        typeMedium = Typeface.createFromAsset(mContext.getAssets(), "p_medium.TTF");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterSearchItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_search_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        try {

            final OrderDetails orderData=dataList.get(position);

            holder.adapterSearchItemBinding.txtName.setText(orderData.getUser().getName());
            holder.adapterSearchItemBinding.txtMobile.setText(orderData.getUser().getCountryCode()+" "+orderData.getUser().getMobile());
            holder.adapterSearchItemBinding.txtOrderNo.setText(mContext.getString(R.string.txt_order_no) + orderData.getOrderIdDisplay());

            if (orderData.getTable().getName().contains(mContext.getString(R.string.txt_table_no)))
                holder.adapterSearchItemBinding.txtTableNo.setText(mContext.getString(R.string.txt_t) + " " + orderData.getTable().getName().replace(mContext.getString(R.string.txt_table_no), "").trim());
            else
                holder.adapterSearchItemBinding.txtTableNo.setText(mContext.getString(R.string.txt_t) + " " + orderData.getTable().getName().replace(mContext.getString(R.string.txt_take_away), "").trim());

            int Quantity = 0;
            int outofStockCount = 0;
            for (int i = 0; i < orderData.getOrderItems().size(); i++) {
                Quantity = Quantity + orderData.getOrderItems().get(i).getQuantity();
                if(orderData.getOrderItems().get(i).getIn_stock()==0)
                    outofStockCount=outofStockCount+1;
            }
            holder.adapterSearchItemBinding.txtQuantity.setText(mContext.getString(R.string.txt_cross_sign) + " " + Quantity);

            holder.adapterSearchItemBinding.layLinAction.setVisibility(View.VISIBLE);
            if(outofStockCount==0){
                holder.adapterSearchItemBinding.txtAccept.setVisibility(View.VISIBLE);
                holder.adapterSearchItemBinding.txtReject.setVisibility(View.GONE);
            }else if(outofStockCount==orderData.getOrderItems().size()){
                holder.adapterSearchItemBinding.txtAccept.setVisibility(View.GONE);
                holder.adapterSearchItemBinding.txtReject.setVisibility(View.VISIBLE);
            }else{
                holder.adapterSearchItemBinding.txtAccept.setVisibility(View.VISIBLE);
                holder.adapterSearchItemBinding.txtReject.setVisibility(View.VISIBLE);
            }

            holder.adapterSearchItemBinding.txtOrderType.setText(orderData.getOrderType());
            if (orderData.getOrderType().contains(mContext.getString(R.string.txt_dinein)))
                holder.adapterSearchItemBinding.txtOrderType.setBackgroundResource(R.drawable.round_selected_submenu);
            else
                holder.adapterSearchItemBinding.txtOrderType.setBackgroundResource(R.drawable.round_take_away);

            if (!orderData.getComment().trim().isEmpty()) {
                holder.adapterSearchItemBinding.viewLineComment.setVisibility(View.VISIBLE);
                holder.adapterSearchItemBinding.txtComment.setVisibility(View.VISIBLE);
                holder.adapterSearchItemBinding.txtComment.setText(Html.fromHtml(orderData.getComment()));
            }

            int orderStatus;

            if(orderData.getOrderStatus().getId()==constant.ORDER_PLACED){
                orderStatus=1;
                if(orderData.getWaiterId()>0)
                    orderStatus=2;
            }else if(orderData.getOrderStatus().getId()==constant.ORDER_PREPARING || orderData.getOrderStatus().getId()==constant.ORDER_READY_SERVE){
                orderStatus=2;
            }else
                orderStatus=3;

            LinearLayoutManager LinLayManager = new LinearLayoutManager(mContext);
            LinLayManager.setOrientation(LinearLayoutManager.VERTICAL);
            holder.adapterSearchItemBinding.rcvItem.setLayoutManager(LinLayManager);
            OrderHistoryItemAdapter adapter = new OrderHistoryItemAdapter(mContext, orderData.getOrderItems(), orderData.getPaymentSymbol(), orderStatus);
            holder.adapterSearchItemBinding.rcvItem.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if(orderStatus==1 ||orderStatus==2) {

                if (orderData.getOrderItems().size() > 1)
                    holder.adapterSearchItemBinding.txtItemTotalHint.setText(R.string.txt_items_total);
                else
                    holder.adapterSearchItemBinding.txtItemTotalHint.setText(R.string.txt_item_total);

                holder.adapterSearchItemBinding.txtItemTotal.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getTotal()));
                holder.adapterSearchItemBinding.txtTax.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getTax()));
                holder.adapterSearchItemBinding.txtTotalAmount.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getOrderAmount()));

                if(orderStatus==2){
                    if(!orderData.getOrderArrivingTime().isEmpty()) {
                        holder.adapterSearchItemBinding.layRelETA.setVisibility(View.VISIBLE);
                        holder.adapterSearchItemBinding.txtETA.setText(Utility.ChangeDateFormate(constant.dateFormaterServer,Utility.chageUTC_ToLocalDate(constant.dateFormaterServer,orderData.getOrderArrivingTime(),constant.dateFormaterServer),constant.dateFormaterTrackOrder));
                    }

                    holder.adapterSearchItemBinding.txtItemTotalHint.setTypeface(typeLight);
                    holder.adapterSearchItemBinding.txtItemTotal.setTypeface(typeLight);
                    holder.adapterSearchItemBinding.txtTaxHint.setTypeface(typeLight);
                    holder.adapterSearchItemBinding.txtTax.setTypeface(typeLight);
                    holder.adapterSearchItemBinding.txtTotalHint.setTypeface(typeSemiBold);
                    holder.adapterSearchItemBinding.txtTotalAmount.setTypeface(typeSemiBold);

                    holder.adapterSearchItemBinding.layLinAction.setVisibility(View.GONE);
                    holder.adapterSearchItemBinding.layLinTracking.setVisibility(View.VISIBLE);

                    if(orderData.getOrderStatus().getId()==constant.ORDER_PLACED || orderData.getOrderStatus().getId()==constant.ORDER_PREPARING){
                        holder.adapterSearchItemBinding.imvPreparing.setBorderColor(mContext.getResources().getColor(R.color.colorOrderKitchen));
                        holder.adapterSearchItemBinding.txtimvPreparing.setTypeface(typeMedium);

                        holder.adapterSearchItemBinding.imvReady.setBorderColor(mContext.getResources().getColor(R.color.colorCardProfile));
                        holder.adapterSearchItemBinding.txtimvReady.setTypeface(typeLight);

                        holder.adapterSearchItemBinding.imvDeliverd.setBorderColor(mContext.getResources().getColor(R.color.colorCardProfile));
                        holder.adapterSearchItemBinding.txtimvDeliverd.setTypeface(typeLight);
                    }else if(orderData.getOrderStatus().getId()==constant.ORDER_READY_SERVE){
                        holder.adapterSearchItemBinding.imvPreparing.setBorderColor(mContext.getResources().getColor(R.color.colorCardProfile));
                        holder.adapterSearchItemBinding.txtimvPreparing.setTypeface(typeLight);

                        holder.adapterSearchItemBinding.imvReady.setBorderColor(mContext.getResources().getColor(R.color.colorOrderReady));
                        holder.adapterSearchItemBinding.txtimvReady.setTypeface(typeMedium);

                        holder.adapterSearchItemBinding.imvDeliverd.setBorderColor(mContext.getResources().getColor(R.color.colorCardProfile));
                        holder.adapterSearchItemBinding.txtimvDeliverd.setTypeface(typeLight);
                    }else{
                        holder.adapterSearchItemBinding.imvPreparing.setBorderColor(mContext.getResources().getColor(R.color.colorCardProfile));
                        holder.adapterSearchItemBinding.txtimvPreparing.setTypeface(typeLight);

                        holder.adapterSearchItemBinding.imvReady.setBorderColor(mContext.getResources().getColor(R.color.colorCardProfile));
                        holder.adapterSearchItemBinding.txtimvReady.setTypeface(typeLight);

                        holder.adapterSearchItemBinding.imvDeliverd.setBorderColor(mContext.getResources().getColor(R.color.colorYallow));
                        holder.adapterSearchItemBinding.txtimvDeliverd.setTypeface(typeMedium);
                    }
                }
            }else{
                holder.adapterSearchItemBinding.txtOrderNo.setTypeface(typeMedium);
                holder.adapterSearchItemBinding.txtOrderNo.setTextColor(mContext.getResources().getColor(R.color.colorValidation));

                holder.adapterSearchItemBinding.layRelETA.setVisibility(View.VISIBLE);
                holder.adapterSearchItemBinding.txtETAHint.setVisibility(View.GONE);
                holder.adapterSearchItemBinding.txtETA.setPadding(0, 0, 0, 0);
                holder.adapterSearchItemBinding.txtETA.setText(Utility.ChangeDateFormate(constant.dateFormaterServer,Utility.chageUTC_ToLocalDate(constant.dateFormaterServer,orderData.getCreatedAt().trim(),constant.dateFormaterServer),constant.dateFormaterServedOrder));
                holder.adapterSearchItemBinding.txtETA.setTypeface(typeLight);
                holder.adapterSearchItemBinding.txtETA.setTextColor(mContext.getResources().getColor(R.color.colorSkipWelcome));
                holder.adapterSearchItemBinding.txtETA.setBackgroundColor(mContext.getResources().getColor(R.color.colorTransparent));

                if(orderData.getOrderStatus().getId()==constant.ORDER_DELIVERED) {
                    holder.adapterSearchItemBinding.txtServed.setText(R.string.txt_served);
                    holder.adapterSearchItemBinding.txtServed.setTextColor(mContext.getResources().getColor(R.color.colorOrderReady));
                }else {
                    holder.adapterSearchItemBinding.txtServed.setText(R.string.txt_rejected);
                    holder.adapterSearchItemBinding.txtServed.setTextColor(mContext.getResources().getColor(R.color.colorValidation));
                }

                holder.adapterSearchItemBinding.tableView.setVisibility(View.GONE);
                holder.adapterSearchItemBinding.layLinAction.setVisibility(View.GONE);
                holder.adapterSearchItemBinding.layLinTracking.setVisibility(View.GONE);
                holder.adapterSearchItemBinding.tableViewServed.setVisibility(View.VISIBLE);

                if (orderData.getOrderItems().size() > 1)
                    holder.adapterSearchItemBinding.txtItemTotalHintServed.setText(R.string.txt_items_total);
                else
                    holder.adapterSearchItemBinding.txtItemTotalHintServed.setText(R.string.txt_item_total);

                holder.adapterSearchItemBinding.txtItemTotalServed.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getTotal()));
                holder.adapterSearchItemBinding.txtTaxServed.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getTax()));
                holder.adapterSearchItemBinding.txtTotalAmountServed.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getOrderAmount()));

            }

            holder.adapterSearchItemBinding.txtAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderActionDialog(1,orderData.getId());
                }
            });

            holder.adapterSearchItemBinding.txtReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderActionDialog(2,orderData.getId());
                }
            });

            holder.adapterSearchItemBinding.imvDeliverd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginUser userInformation = new Gson().fromJson(Utility.getSharedPreferencesString(mContext, constant.USER_INFORMATION), type);

                    if(orderData.getOrderStatus().getId()==constant.ORDER_READY_SERVE && userInformation.getId()==orderData.getWaiterId()){
                        if(Utility.isNetworkAvailable(mContext))
                            orderDeliverdAPI(orderData.getId());
                        else
                            Utility.ShowToast(mContext,mContext.getString(R.string.check_network),0);
                    }
                }
            });

            holder.adapterSearchItemBinding.txtimvDeliverd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginUser userInformation = new Gson().fromJson(Utility.getSharedPreferencesString(mContext, constant.USER_INFORMATION), type);

                    if(orderData.getOrderStatus().getId()==constant.ORDER_READY_SERVE && userInformation.getId()==orderData.getWaiterId()){
                        if(Utility.isNetworkAvailable(mContext))
                            orderDeliverdAPI(orderData.getId());
                        else
                            Utility.ShowToast(mContext,mContext.getString(R.string.check_network),0);
                    }
                }
            });

        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // View holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        private AdapterSearchItemBinding adapterSearchItemBinding;

        public ViewHolder(AdapterSearchItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.adapterSearchItemBinding = itemBinding;
        }
    }

    // Show dialog for order action
    public void orderActionDialog(final int actionType,final int orderID){
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
                        acceptOrderAPI(orderID);
                    else
                        rejectOrderAPI(orderID);
                }else
                    Utility.ShowToast(mContext,mContext.getString(R.string.check_network),0);

            }
        });
    }

    // Accept order API
    private void acceptOrderAPI(int orderID) {
        try{
            loader.show(mContext.getString(R.string.wait));

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.ORDER_ID, orderID);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.acceptOrder(jsonObject,Utility.getSharedPreferencesString(mContext, constant.USER_SECURITY_TOKEN));
            new APICall(mContext).Server_Interaction(call,this ,API.ACCEPT_ORDER);

        }catch (Exception e){
            e.printStackTrace();
            loader.dismiss();
        }
    }

    // Reject order API
    private void rejectOrderAPI(int orderID) {
        try{
            loader.show(mContext.getString(R.string.wait));

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.ORDER_ID, orderID);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.rejectOrder(jsonObject,Utility.getSharedPreferencesString(mContext, constant.USER_SECURITY_TOKEN));
            new APICall(mContext).Server_Interaction(call,this ,API.REJECT_ORDER);

        }catch (Exception e){
            e.printStackTrace();
            loader.dismiss();
        }
    }

    // Order deliverd API
    private void orderDeliverdAPI(int orderID) {
        try{
            loader.show(mContext.getString(R.string.wait));

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.ORDER_ID, orderID);

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
                        Utility.setSharedPreferencesBoolean(mContext,constant.NEW_ORDER_RELOAD,true);
                        Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_ORDER_RELOAD,true);
                        Utility.setSharedPreferencesBoolean(mContext,constant.SERVED_ORDER_RELOAD,true);
                        Utility.ShowToast(mContext,msg,0);
                        ((SearchActivity)mContext).getOrderStatusAPI();
                        break;
                    case API.ACCEPT_ORDER:
                        Utility.setSharedPreferencesBoolean(mContext,constant.NEW_ORDER_RELOAD,true);
                        Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_ORDER_RELOAD,true);
                        Utility.ShowToast(mContext,msg,0);
                        LoginUser userInformation = new Gson().fromJson(Utility.getSharedPreferencesString(mContext, constant.USER_INFORMATION), type);
                        dataList.get(0).setWaiterId(userInformation.getId());
                        notifyDataSetChanged();
                        break;
                    case API.REJECT_ORDER:
                        Utility.setSharedPreferencesBoolean(mContext,constant.NEW_ORDER_RELOAD,true);
                        Utility.setSharedPreferencesBoolean(mContext,constant.SERVED_ORDER_RELOAD,true);
                        Utility.ShowToast(mContext,msg,0);
                        ((SearchActivity)mContext).getOrderStatusAPI();
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
                    Utility.ShowToast(mContext,msg,0);
                    break;
                case API.ACCEPT_ORDER:
                    Utility.setSharedPreferencesBoolean(mContext,constant.NEW_ORDER_RELOAD,true);
                    Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_ORDER_RELOAD,true);
                    Utility.setSharedPreferencesBoolean(mContext,constant.SERVED_ORDER_RELOAD,true);
                    ((SearchActivity)mContext).getOrderStatusAPI();
                    Dialog.orderAlreadyAcceptedDialog(mContext,mContext.getString(R.string.txt_alert),msg,false);
                    break;
                case API.REJECT_ORDER:
                    Utility.setSharedPreferencesBoolean(mContext,constant.NEW_ORDER_RELOAD,true);
                    Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_ORDER_RELOAD,true);
                    Utility.setSharedPreferencesBoolean(mContext,constant.SERVED_ORDER_RELOAD,true);
                    ((SearchActivity)mContext).getOrderStatusAPI();
                    Dialog.orderAlreadyAcceptedDialog(mContext,mContext.getString(R.string.txt_alert),msg,false);
                    break;
            }
        }
    }
}
