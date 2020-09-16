package com.example.bookbrowser.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bookbrowser.R;
import com.example.bookbrowser.WelcomeActivity;

public class AdminCategoryActivity extends AppCompatActivity {

    private Button scienceFictionBtn,novelBtn,thrillerBtn;
    private Button dramaBtn,comicBtn,educationalBtn;
    private Button religiousBtn,biopicBtn;

    private Button LogoutBtn, CheckOrdersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

       // Toast.makeText(AdminCategoryActivity.this,"Welcome admins..",Toast.LENGTH_SHORT).show();


        LogoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);






        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });




        scienceFictionBtn =(Button) findViewById(R.id.science_fictionBtn);
        novelBtn =(Button) findViewById(R.id.novelBtn);
        thrillerBtn =(Button) findViewById(R.id.thrillerBtn);
        dramaBtn =(Button) findViewById(R.id.dramaBtn);
        comicBtn =(Button) findViewById(R.id.comicBtn);
        educationalBtn =(Button) findViewById(R.id.educationalBtn);
        religiousBtn =(Button) findViewById(R.id.religiousBtn);
        biopicBtn =(Button) findViewById(R.id.biopicBtn);


        scienceFictionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewBooksActivity.class);
                intent.putExtra("category","Science Fiction");
                startActivity(intent);

            }
        });



        novelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewBooksActivity.class);
                intent.putExtra("category","Novel");
                startActivity(intent);

            }
        });



        thrillerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewBooksActivity.class);
                intent.putExtra("category","Thriller");
                startActivity(intent);

            }
        });



        dramaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewBooksActivity.class);
                intent.putExtra("category","Drama");
                startActivity(intent);

            }
        });



        comicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewBooksActivity.class);
                intent.putExtra("category","Comic");
                startActivity(intent);

            }
        });


        educationalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewBooksActivity.class);
                intent.putExtra("category","Educational");
                startActivity(intent);

            }
        });


       religiousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewBooksActivity.class);
                intent.putExtra("category","Religious");
                startActivity(intent);

            }
        });



        biopicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewBooksActivity.class);
                intent.putExtra("category","Biopic");
                startActivity(intent);

            }
        });


    }
}
