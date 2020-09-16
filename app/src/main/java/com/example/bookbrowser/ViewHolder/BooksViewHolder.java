package com.example.bookbrowser.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbrowser.Interface.ItemClickListener;
import com.example.bookbrowser.R;

public class BooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView textBookName,textBookDescription,textBookPrice;
    public ImageView imageView;
    public ItemClickListener listener;

    public BooksViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.book_image);
        textBookName = (TextView) itemView.findViewById(R.id.book_name);
        textBookDescription = (TextView) itemView.findViewById(R.id.book_description);
        textBookPrice = (TextView) itemView.findViewById(R.id.book_price);
    }



    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;

    }



    @Override
    public void onClick(View v)
    {
        listener.onClick(v,getAdapterPosition(),false);

    }
}
