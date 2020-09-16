package com.example.bookbrowser.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookbrowser.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewBooksActivity extends AppCompatActivity {

    private String CategoryName,Description,Price,Bname,saveCurrentDate,saveCurrentTime;
    private Button AddNewBookButton;
    private ImageView InputBookImage;
    private EditText InputBookName, InputBookDescription, InputBookPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String BookRandomKey,downloadImageUrl;
    private StorageReference bookImageRef;
    private DatabaseReference BooksRef;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_books);



        CategoryName = getIntent().getExtras().get("category").toString();
        bookImageRef = FirebaseStorage.getInstance().getReference().child("Book Images");
        BooksRef = FirebaseDatabase.getInstance().getReference().child("Books").child(CategoryName);



        AddNewBookButton = (Button) findViewById(R.id.add_new_book);
        InputBookImage = (ImageView) findViewById(R.id.select_book_image);
        InputBookName =(EditText) findViewById(R.id.book_name);
        InputBookDescription =(EditText) findViewById(R.id.book_description);
        InputBookPrice =(EditText) findViewById(R.id.book_price);
        loadingBar = new ProgressDialog(this);



        InputBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OpenGallery();

            }
        });



        AddNewBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidateBookData();

            }
        });



    }





    private void OpenGallery()
    {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            InputBookImage.setImageURI(ImageUri);
        }
    }



    private void ValidateBookData()
    {
        Description = InputBookDescription.getText().toString();
        Price = InputBookPrice.getText().toString();
        Bname = InputBookName.getText().toString();


        if(ImageUri == null)
        {
            Toast.makeText(this,"Book image is required",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this,"Book description is required",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this,"Book price is required",Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreBookInformation();
        }

    }



    private void StoreBookInformation()
    {

        loadingBar.setTitle("Add New books");
        loadingBar.setMessage("Dear admin Please wait, while we are adding new books .");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();



        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat CurrentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = CurrentDate.format(calendar.getTime());

        SimpleDateFormat CurrentTime = new SimpleDateFormat("hh:mm:ss aa");
        saveCurrentTime = CurrentTime.format(calendar.getTime());


        BookRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = bookImageRef.child(ImageUri.getLastPathSegment() + " " +BookRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminAddNewBooksActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminAddNewBooksActivity.this,"Book image uploaded successfully",Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {

                        if(!task.isSuccessful())
                        {

                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {

                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewBooksActivity.this,"got the book image successfully",Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();

                        }


                    }
                });

            }
        });


    }

    private void SaveProductInfoToDatabase()
    {

        HashMap<String, Object> booktMap = new HashMap<>();
        booktMap.put("bid", BookRandomKey);
        booktMap.put("date", saveCurrentDate);
        booktMap.put("time", saveCurrentTime);
        booktMap.put("description", Description);
        booktMap.put("image", downloadImageUrl);
        booktMap.put("category", CategoryName);
        booktMap.put("price", Price);
        booktMap.put("name", Bname);



        BooksRef.child(BookRandomKey).updateChildren(booktMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {

                           Intent intent = new Intent(AdminAddNewBooksActivity.this, AdminAddNewBooksActivity.class);
                           startActivity(intent);



                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewBooksActivity.this,"Books is added successfully",Toast.LENGTH_SHORT).show() ;

                        }
                        else
                        {

                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewBooksActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }
}
