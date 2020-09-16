package com.example.bookbrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookbrowser.Model.Books;
import com.example.bookbrowser.ViewHolder.BooksViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class EducationalActivity extends AppCompatActivity {

    private DatabaseReference BooksRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educational);

        BooksRef = FirebaseDatabase.getInstance().getReference().child("Books").child("Educational");




        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Books> options =
                new FirebaseRecyclerOptions.Builder<Books>()
                        .setQuery(BooksRef, Books.class)
                        .build();


        FirebaseRecyclerAdapter<Books, BooksViewHolder> adapter =
                new FirebaseRecyclerAdapter<Books, BooksViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BooksViewHolder holder, int position, @NonNull final Books model)
                    {

                        holder.textBookName.setText(model.getName());
                        holder.textBookDescription.setText(model.getDescription());
                        holder.textBookPrice.setText("Price = "+model.getPrice() + "tk");
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {


                                Intent intent = new Intent(EducationalActivity.this, BookDetailsActivity.class);
                                intent.putExtra("bid",model.getBid());
                                intent.putExtra("category","Educational");
                                startActivity(intent);

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_layout,parent,false);
                        BooksViewHolder holder = new BooksViewHolder(view);
                        return holder;
                    }
                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
