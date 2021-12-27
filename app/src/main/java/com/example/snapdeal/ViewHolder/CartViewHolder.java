package com.example.snapdeal.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snapdeal.R;
import com.example.snapdeal.interfaces.itemClickListner;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductPrice,txtProductQuantity;
    private itemClickListner  itemClickListner;

    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.cart_Product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_Product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_Product_quantity);

    }

    @Override
    public void onClick(View view)
    {
        itemClickListner.onClick(view,getAdapterPosition(),false);

    }

    public void setItemClickListner(com.example.snapdeal.interfaces.itemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }
}
