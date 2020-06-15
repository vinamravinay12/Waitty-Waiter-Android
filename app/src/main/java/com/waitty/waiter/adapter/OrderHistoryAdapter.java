package com.waitty.waiter.adapter;

public class OrderHistoryAdapter {

//    private LinkedList<OrderDetails> dataList;
//    private Context mContext;
//    private LayoutInflater layoutInflater;
//    private int orderType;
//
//    // Class constructor
//    public OrderHistoryAdapter(Context context, LinkedList<OrderDetails> data, int typeID) {
//        this.dataList = data;
//        orderType=typeID;
//        this.mContext = context;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        if (layoutInflater == null) {
//            layoutInflater = LayoutInflater.from(parent.getContext());
//        }
//       // AdapterNewOrderBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_new_order, parent, false);
//        return new ViewHolder();
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        final OrderDetails data=dataList.get(position);
//
//        holder.adapterNewOrderBinding.txtName.setText(data.getUser().getName());
//        holder.adapterNewOrderBinding.txtDate.setText(Utility.ChangeDateFormate(constant.dateFormaterServer,Utility.chageUTC_ToLocalDate(constant.dateFormaterServer,data.getCreatedAt().trim(),constant.dateFormaterServer),constant.dateFormaterServedOrder));
//        holder.adapterNewOrderBinding.txtServed.setText("");
//
//        if(orderType==3) {
//            holder.adapterNewOrderBinding.txtOrderNo.setText(data.getOrderIdDisplay());
//            holder.adapterNewOrderBinding.txtOrderNo.setTextColor(mContext.getResources().getColor(R.color.colorValidation));
//
//            if(data.getOrderStatus().getId()==constant.ORDER_DELIVERED) {
//                holder.adapterNewOrderBinding.txtServed.setText(R.string.txt_served);
//                holder.adapterNewOrderBinding.txtServed.setTextColor(mContext.getResources().getColor(R.color.colorOrderReady));
//            }else {
//                holder.adapterNewOrderBinding.txtServed.setText(R.string.txt_rejected);
//                holder.adapterNewOrderBinding.txtServed.setTextColor(mContext.getResources().getColor(R.color.colorValidation));
//            }
//
//        }else {
//            holder.adapterNewOrderBinding.txtOrderNo.setText(mContext.getString(R.string.txt_order_no) + data.getOrderIdDisplay());
//            holder.adapterNewOrderBinding.txtOrderNo.setTextColor(mContext.getResources().getColor(R.color.colorSkipWelcome));
//        }
//
//        if(data.getTable().getName().contains(mContext.getString(R.string.txt_table_no)))
//            holder.adapterNewOrderBinding.txtTableNo.setText(mContext.getString(R.string.txt_t)+" "+data.getTable().getName().replace(mContext.getString(R.string.txt_table_no),"").trim());
//        else
//            holder.adapterNewOrderBinding.txtTableNo.setText(mContext.getString(R.string.txt_t)+" "+data.getTable().getName().replace(mContext.getString(R.string.txt_take_away),"").trim());
//
//        int Quantity=0;
//        for(int i=0;i<data.getOrderItems().size();i++){
//            Quantity=Quantity+data.getOrderItems().get(i).getQuantity();
//        }
//        holder.adapterNewOrderBinding.txtQuantity.setText(mContext.getString(R.string.txt_cross_sign)+" "+Quantity);
//
//        holder.adapterNewOrderBinding.txtOrderType.setText(data.getOrderType());
//        if(data.getOrderType().contains(mContext.getString(R.string.txt_dinein)))
//            holder.adapterNewOrderBinding.txtOrderType.setBackgroundResource(R.drawable.round_selected_submenu);
//        else
//            holder.adapterNewOrderBinding.txtOrderType.setBackgroundResource(R.drawable.round_take_away);
//
//        if(orderType==2){
//            holder.adapterNewOrderBinding.viewLine.setVisibility(View.VISIBLE);
//            holder.adapterNewOrderBinding.layRelBottom.setVisibility(View.VISIBLE);
//
//            if(data.getOrderStatus().getId()== constant.ORDER_PLACED || data.getOrderStatus().getId()== constant.ORDER_PREPARING) {
//                holder.adapterNewOrderBinding.txtOrderOnColor.setBackgroundResource(R.drawable.round_orderkitchen_color);
//                holder.adapterNewOrderBinding.txtOrderOn.setText(R.string.txt_kitchen);
//            }else {
//                holder.adapterNewOrderBinding.txtOrderOnColor.setBackgroundResource(R.drawable.round_orderready_color);
//                holder.adapterNewOrderBinding.txtOrderOn.setText(R.string.txt_ready);
//            }
//        }else{
//            holder.adapterNewOrderBinding.viewLine.setVisibility(View.GONE);
//            holder.adapterNewOrderBinding.layRelBottom.setVisibility(View.GONE);
//        }
//
//       /* holder.adapterNewOrderBinding.layLinDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(Utility.isNetworkAvailable(mContext))
//                    mContext.startActivity(new Intent(mContext, ProcessingOrderDetailsActivity.class).putExtra(API.DATA,data));
//                else
//                    Utility.ShowToast(mContext,mContext.getString(R.string.check_network),0);
//            }
//        });*/
//
////        holder.adapterNewOrderBinding.cardViewMain.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if(orderType==1){
////                    if(Utility.isNetworkAvailable(mContext))
////                        mContext.startActivity(new Intent(mContext, NewOrderDetailsActivity.class).putExtra(API.DATA, data));
////                    else
////                        Utility.ShowToast(mContext,mContext.getString(R.string.check_network),0);
////                }else if(orderType==2) {
////                    if(Utility.isNetworkAvailable(mContext))
////                        mContext.startActivity(new Intent(mContext, ProcessingOrderDetailsActivity.class).putExtra(API.DATA,data));
////                    else
////                        Utility.ShowToast(mContext,mContext.getString(R.string.check_network),0);
////                }else if(orderType==3)
////                    mContext.startActivity(new Intent(mContext, ServedOrderDetailsActivity.class).putExtra(API.DATA,data));
////            }
////        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return dataList.size();
//    }
//
//    // View holder
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        private AdapterNewOrderBinding adapterNewOrderBinding;
//
//        public ViewHolder(AdapterNewOrderBinding itemBinding) {
//            super(itemBinding.getRoot());
//            this.adapterNewOrderBinding = itemBinding;
//        }
//    }
}
