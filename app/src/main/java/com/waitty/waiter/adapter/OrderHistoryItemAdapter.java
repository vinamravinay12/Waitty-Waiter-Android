package com.waitty.waiter.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.waitty.waiter.R;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.AdapterOrderItemBinding;
import com.waitty.waiter.model.OrderDetails;
import java.util.List;

public class OrderHistoryItemAdapter extends RecyclerView.Adapter<OrderHistoryItemAdapter.ViewHolder>{

    private List<OrderDetails.OrderItem> dataList;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private String paymentSymbol;
    private int orderType;
    private Typeface typeLight,typeSemiBold,typeRegular,typeMedium;

    // Class constructor
    public OrderHistoryItemAdapter(Context context, List<OrderDetails.OrderItem> orderItems, String symbol,int tab) {
        this.dataList = orderItems;
        this.paymentSymbol = symbol;
        this.orderType=tab;
        this.mContext = context;
        typeRegular = Typeface.createFromAsset(mContext.getAssets(), "p_regular.TTF");
        typeMedium = Typeface.createFromAsset(mContext.getAssets(), "p_medium.TTF");
        typeLight = Typeface.createFromAsset(mContext.getAssets(), "p_light.TTF");
        typeSemiBold = Typeface.createFromAsset(mContext.getAssets(), "p_semibold.TTF");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterOrderItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_order_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final OrderDetails.OrderItem data=dataList.get(position);

        int left= (int) mContext.getResources().getDimension(R.dimen._15sdp);
        int top=(int) mContext.getResources().getDimension(R.dimen._7sdp);

        if(orderType==3){
            holder.adapterOrderItemBinding.layRelQuantity.setBackgroundColor(mContext.getResources().getColor(R.color.colorCardProfile));
            holder.adapterOrderItemBinding.layRelQuantity.setPadding(left,top,left,top);
        }else{
            holder.adapterOrderItemBinding.layRelQuantity.setBackgroundColor(mContext.getResources().getColor(R.color.colorTransparent));
            holder.adapterOrderItemBinding.layRelQuantity.setPadding(left,0,left,0);
        }

        if(orderType==1){
            holder.adapterOrderItemBinding.txtName.setTypeface(typeMedium);
            holder.adapterOrderItemBinding.txtQuantity.setTypeface(typeMedium);
            holder.adapterOrderItemBinding.txtPrice.setTypeface(typeRegular);
        }else{
            holder.adapterOrderItemBinding.txtName.setTypeface(typeSemiBold);
            holder.adapterOrderItemBinding.txtQuantity.setTypeface(typeMedium);
            holder.adapterOrderItemBinding.txtPrice.setTypeface(typeLight);
        }

        holder.adapterOrderItemBinding.txtName.setText(data.getDishDetails().getName());
        holder.adapterOrderItemBinding.txtQuantity.setText(String.valueOf(data.getQuantity()));
        holder.adapterOrderItemBinding.txtPrice.setText(paymentSymbol+" "+ constant.FORMATOR_DECIMAL.format(data.getQuantity()*data.getDishAmount()));

        if(data.getOrderItemCustomizations()!=null && data.getOrderItemCustomizations().size()>0){
            holder.adapterOrderItemBinding.txtCustomization1.setVisibility(View.GONE);
            holder.adapterOrderItemBinding.txtCustomization2.setVisibility(View.GONE);
            holder.adapterOrderItemBinding.txtCustomization3.setVisibility(View.GONE);

            for(int i=0;i<data.getOrderItemCustomizations().size();i++){
                if(i==0) {
                    String optionHint="";
                    if(data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().size()>0) {
                        for (int j = 0; j < data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().size();j++){
                            if(optionHint.isEmpty())
                                optionHint=data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().get(j).getOrderCustomizationOptionDetails().getName();
                            else
                                optionHint=optionHint+", "+data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().get(j).getOrderCustomizationOptionDetails().getName();
                        }
                    }

                    if(!optionHint.isEmpty()) {
                        holder.adapterOrderItemBinding.txtCustomization1.setVisibility(View.VISIBLE);
                        String text = "<font color=#777670>" + data.getOrderItemCustomizations().get(i).getOrderCustomizationDetails().getName() + ": " + "</font> <font color=#292323>" + optionHint + "</font>";
                        holder.adapterOrderItemBinding.txtCustomization1.setText(Html.fromHtml(text));
                    }
                }else if(i==1) {
                    String optionHint="";
                    if(data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().size()>0) {
                        for (int j = 0; j < data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().size();j++){
                            if(optionHint.isEmpty())
                                optionHint=data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().get(j).getOrderCustomizationOptionDetails().getName();
                            else
                                optionHint=optionHint+", "+data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().get(j).getOrderCustomizationOptionDetails().getName();
                        }
                    }

                    if(!optionHint.isEmpty()) {
                        holder.adapterOrderItemBinding.txtCustomization2.setVisibility(View.VISIBLE);
                        String text = "<font color=#777670>" + data.getOrderItemCustomizations().get(i).getOrderCustomizationDetails().getName() + ": " + "</font> <font color=#292323>" + optionHint + "</font>";
                        holder.adapterOrderItemBinding.txtCustomization2.setText(Html.fromHtml(text));
                    }
                }else if(i==2) {
                    String optionHint="";
                    if(data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().size()>0) {
                        for (int j = 0; j < data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().size();j++){
                            if(optionHint.isEmpty())
                                optionHint=data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().get(j).getOrderCustomizationOptionDetails().getName();
                            else
                                optionHint=optionHint+", "+data.getOrderItemCustomizations().get(i).getOrderItemCustomizationsOptions().get(j).getOrderCustomizationOptionDetails().getName();
                        }
                    }

                    if(!optionHint.isEmpty()) {
                        holder.adapterOrderItemBinding.txtCustomization3.setVisibility(View.VISIBLE);
                        String text = "<font color=#777670>" + data.getOrderItemCustomizations().get(i).getOrderCustomizationDetails().getName() + ": " + "</font> <font color=#292323>" + optionHint + "</font>";
                        holder.adapterOrderItemBinding.txtCustomization3.setText(Html.fromHtml(text));
                    }
                }
            }
        }else{
            holder.adapterOrderItemBinding.txtCustomization1.setVisibility(View.GONE);
            holder.adapterOrderItemBinding.txtCustomization2.setVisibility(View.GONE);
            holder.adapterOrderItemBinding.txtCustomization3.setVisibility(View.GONE);
        }

        if(data.getIn_stock()==0)
            holder.adapterOrderItemBinding.txtOutOfStock.setVisibility(View.VISIBLE);
        else
            holder.adapterOrderItemBinding.txtOutOfStock.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // View holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        private AdapterOrderItemBinding adapterOrderItemBinding;

        public ViewHolder(AdapterOrderItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.adapterOrderItemBinding = itemBinding;
        }
    }
}
