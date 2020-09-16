package com.example.bookbrowser.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbrowser.Interface.ItemClickListener;
import com.example.bookbrowser.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtBookName, txtBookPrice, txtBookQuantity;
    private ItemClickListener itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);


        txtBookName = itemView.findViewById(R.id.cart_book_name);
        txtBookPrice = itemView.findViewById(R.id.cart_book_price);
        txtBookQuantity = itemView.findViewById(R.id.cart_book_quantity);
    }

    @Override
    public void onClick(View v)
    {

        this.itemClickListner = itemClickListner;

    }
}
