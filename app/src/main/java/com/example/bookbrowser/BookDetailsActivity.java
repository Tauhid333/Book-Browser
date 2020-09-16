package com.example.bookbrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bookbrowser.Model.Books;
import com.example.bookbrowser.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class BookDetailsActivity extends AppCompatActivity {


    private Button addToCartButton;
    private ImageView bookImage;
    private ElegantNumberButton numberButton;
    private TextView bookPrice, bookDescription, bookName;
    private String bookID = "", state = "Normal" ;
    private String categoryName;
    private DatabaseReference BooksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);


        bookID = getIntent().getStringExtra("bid");
        categoryName = getIntent().getStringExtra("category");
        BooksRef = FirebaseDatabase.getInstance().getReference().child("Books").child(categoryName);

        addToCartButton = (Button) findViewById(R.id.book_add_to_cart_button);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        bookImage = (ImageView) findViewById(R.id.book_image_details);
        bookName = (TextView) findViewById(R.id.book_name_details);
        bookDescription = (TextView) findViewById(R.id.book_description_details);
        bookPrice = (TextView) findViewById(R.id.book_price_details);


        getBookDetails(bookID);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(BookDetailsActivity.this, "you can add purchase more books, once your order is shipped or confirmed.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }

            }
        });

    }




    @Override
    protected void onStart()
    {
        super.onStart();

        CheckOrderState();
    }


    private void addingToCartList()
    {

        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss aa");
        saveCurrentTime = currentDate.format(calForDate.getTime());

         final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");


        final HashMap<String , Object> cartMap = new HashMap<>();
        cartMap.put("bid", bookID);
        cartMap.put("name", bookName.getText().toString());
        cartMap.put("price", bookPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");


        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Books").child(bookID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Books").child(bookID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(BookDetailsActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                                            }


                                        }
                                    });

                        }

                    }
                });

    }






    private void getBookDetails(String bookID )
    {



        BooksRef.child(bookID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if (snapshot.exists())
                {
                    Books books = snapshot.getValue(Books.class);

                    bookName.setText(books.getName());
                    bookPrice.setText(books.getPrice());
                    bookDescription.setText(books.getDescription());
                    Picasso.get().load(books.getImage()).into(bookImage);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }



    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
