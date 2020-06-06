package com.waitty.waiter.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.waitty.waiter.R;
import com.waitty.waiter.adapter.OrderHistoryItemAdapter;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.ActivityNeworderDetailsBinding;
import com.waitty.waiter.model.OrderDetails;
import com.waitty.waiter.retrofit.API;
import com.waitty.waiter.utility.Utility;

public class ServedOrderDetailsActivity extends AppCompatActivity {

    private Context mContext;
    private ActivityNeworderDetailsBinding activityNeworderDetailsBinding;
    private OrderDetails orderData;
    private Typeface typeMedium,typeLight;

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

            typeMedium = Typeface.createFromAsset(mContext.getAssets(), "p_medium.TTF");
            typeLight = Typeface.createFromAsset(mContext.getAssets(), "p_light.TTF");

            Utility.setToolbar(mContext, activityNeworderDetailsBinding.toolbarActionbar);
            orderData = (OrderDetails) getIntent().getSerializableExtra(API.DATA);

            activityNeworderDetailsBinding.txtHeading.setText(getString(R.string.txt_order_detail) + "- " + orderData.getOrderIdDisplay());
            activityNeworderDetailsBinding.txtName.setText(orderData.getUser().getName());
            activityNeworderDetailsBinding.txtOrderNo.setText(mContext.getString(R.string.txt_order_no) + orderData.getOrderIdDisplay());
            activityNeworderDetailsBinding.txtOrderNo.setTypeface(typeMedium);
            activityNeworderDetailsBinding.txtOrderNo.setTextColor(getResources().getColor(R.color.colorValidation));

            activityNeworderDetailsBinding.layRelETA.setVisibility(View.VISIBLE);
            activityNeworderDetailsBinding.txtETAHint.setVisibility(View.GONE);
            activityNeworderDetailsBinding.txtETA.setPadding(0, 0, 0, 0);
            activityNeworderDetailsBinding.txtETA.setText(Utility.ChangeDateFormate(constant.dateFormaterServer,Utility.chageUTC_ToLocalDate(constant.dateFormaterServer,orderData.getCreatedAt().trim(),constant.dateFormaterServer),constant.dateFormaterServedOrder));
            activityNeworderDetailsBinding.txtETA.setTypeface(typeLight);
            activityNeworderDetailsBinding.txtETA.setTextColor(getResources().getColor(R.color.colorSkipWelcome));
            activityNeworderDetailsBinding.txtETA.setBackgroundColor(getResources().getColor(R.color.colorTransparent));

            if(orderData.getOrderStatus().getId()==constant.ORDER_DELIVERED)
                activityNeworderDetailsBinding.txtServed.setText(R.string.txt_served);
            else {
                activityNeworderDetailsBinding.txtServed.setText(R.string.txt_rejected);
                activityNeworderDetailsBinding.txtServed.setTextColor(getResources().getColor(R.color.colorValidation));
            }

            if (orderData.getTable().getName().contains(mContext.getString(R.string.txt_table_no)))
                activityNeworderDetailsBinding.txtTableNo.setText(mContext.getString(R.string.txt_t) + " " + orderData.getTable().getName().replace(mContext.getString(R.string.txt_table_no), "").trim());
            else
                activityNeworderDetailsBinding.txtTableNo.setText(mContext.getString(R.string.txt_t) + " " + orderData.getTable().getName().replace(mContext.getString(R.string.txt_take_away), "").trim());

            int Quantity = 0;
            for (int i = 0; i < orderData.getOrderItems().size(); i++) {
                Quantity = Quantity + orderData.getOrderItems().get(i).getQuantity();
            }
            activityNeworderDetailsBinding.txtQuantity.setText(mContext.getString(R.string.txt_cross_sign) + " " + Quantity);

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
            OrderHistoryItemAdapter adapter = new OrderHistoryItemAdapter(mContext, orderData.getOrderItems(), orderData.getPaymentSymbol(), 3);
            activityNeworderDetailsBinding.rcvItem.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            activityNeworderDetailsBinding.tableView.setVisibility(View.GONE);
            activityNeworderDetailsBinding.tableViewServed.setVisibility(View.VISIBLE);

            if (orderData.getOrderItems().size() > 1)
                activityNeworderDetailsBinding.txtItemTotalHintServed.setText(R.string.txt_items_total);
            else
                activityNeworderDetailsBinding.txtItemTotalHintServed.setText(R.string.txt_item_total);

            activityNeworderDetailsBinding.txtItemTotalServed.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getTotal()));
            activityNeworderDetailsBinding.txtTaxServed.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getTax()));
            activityNeworderDetailsBinding.txtTotalAmountServed.setText(orderData.getPaymentSymbol() + " " + constant.FORMATOR_DECIMAL.format(orderData.getOrderAmount()));

            activityNeworderDetailsBinding.layLinAction.setVisibility(View.GONE);
            activityNeworderDetailsBinding.txtName.setFocusableInTouchMode(true);
        }catch (Exception e){e.printStackTrace();}
    }
}
