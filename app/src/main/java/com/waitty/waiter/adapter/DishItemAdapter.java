package com.waitty.waiter.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.waitty.waiter.R;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.AdapterDishBinding;
import com.waitty.waiter.model.DishData;
import com.waitty.waiter.utility.Utility;
import java.util.LinkedList;

public class DishItemAdapter extends RecyclerView.Adapter<DishItemAdapter.ViewHolder>{

    private LinkedList<DishData> dataList;
    private Context mContext;
    private LayoutInflater layoutInflater;

    // Class constructor
    public DishItemAdapter(Context context, LinkedList<DishData> data) {
        this.dataList = data;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterDishBinding binding =DataBindingUtil.inflate(layoutInflater, R.layout.adapter_dish, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.adapterDishBinding.setDish(dataList.get(position));
        DishData data=dataList.get(position);

        if(data.getDishImage().trim().isEmpty())
            holder.adapterDishBinding.layRelImage.setVisibility(View.GONE);
        else
            holder.adapterDishBinding.layRelImage.setVisibility(View.VISIBLE);

        if(data.getIsSoldout())
            holder.adapterDishBinding.txtSoldOut.setVisibility(View.VISIBLE);
        else
            holder.adapterDishBinding.txtSoldOut.setVisibility(View.GONE);

        if(Utility.getDishTotalPrice(data)>0)
            holder.adapterDishBinding.txtPrice.setText(mContext.getString(R.string.rupess_sign)+" "+ constant.FORMATOR_DECIMAL.format(Utility.getDishTotalPrice(data)));
        else
            holder.adapterDishBinding.txtPrice.setText(mContext.getString(R.string.rupess_sign)+" 0.00");

        holder.adapterDishBinding.txtPrice.setPaintFlags( holder.adapterDishBinding.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if(Utility.getDishTotalSellingPrice(data)>0)
            holder.adapterDishBinding.txtSellPrice.setText(mContext.getString(R.string.rupess_sign)+" "+ constant.FORMATOR_DECIMAL.format(Utility.getDishTotalSellingPrice(data)));
        else
            holder.adapterDishBinding.txtSellPrice.setText(mContext.getString(R.string.rupess_sign)+" 0.00");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // View holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        private AdapterDishBinding adapterDishBinding;

        public ViewHolder(AdapterDishBinding itemBinding) {
            super(itemBinding.getRoot());
            this.adapterDishBinding = itemBinding;
        }

    }
}
