package com.example.snapdeal;

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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    private String category_name,Description,Price,Pname,saveCurrentDate,saveCurrentTime;
    private Button addNewProductBtn;
    private ImageView InputProductImage;
    private EditText InputProductName,InputProductDescription,InputProductPrice;
    private static final int GalleryPick=1;
    private Uri imageUri;
    private String ProductRandomKey,DownloadImageURL;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loadinBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        category_name=getIntent().getExtras().get("category").toString();
        ProductImageRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef=FirebaseDatabase.getInstance().getReference().child("Products");

        addNewProductBtn=(Button)findViewById(R.id.add_new_product);
        InputProductImage=(ImageView)findViewById(R.id.select_product_image);
        InputProductName=(EditText)findViewById(R.id.product_name);
        InputProductDescription=(EditText)findViewById(R.id.product_description);
        InputProductPrice=(EditText)findViewById(R.id.product_price);
        loadinBar= new ProgressDialog(this);



        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });

        addNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                validateProduct();
            }
        });

    }

    private void OpenGallery()
    {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!= null)
        {
            imageUri=data.getData();
            InputProductImage.setImageURI(imageUri);
        }

    }
    private void validateProduct()
    {
        Description=InputProductDescription.getText().toString();
        Price=InputProductPrice.getText().toString();
        Pname=InputProductName.getText().toString();

        if(imageUri== null)
        {
            Toast.makeText(this, "Add Product Image First", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Add Description First", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Add Price First", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Add Product Name First", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInfo();
        }

    }

    private void StoreProductInfo()
    {
        loadinBar.setTitle("Adding New Product");
        loadinBar.setMessage("Please Wait");
        loadinBar.setCanceledOnTouchOutside(false);
        loadinBar.show();

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        ProductRandomKey=saveCurrentDate + saveCurrentTime;

        final StorageReference filePath=ProductImageRef.child(imageUri.getLastPathSegment() + ProductRandomKey + ".jpg");

        final UploadTask uploadTask=filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message=e.toString();
                Toast.makeText(AdminActivity.this, "Error: " + message , Toast.LENGTH_SHORT).show();
                loadinBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminActivity.this, "Product Image Uploaded ! ", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }
                        DownloadImageURL=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            DownloadImageURL=task.getResult().toString();
                            Toast.makeText(AdminActivity.this, "Saving Product Image !", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String,Object> ProductMap=new HashMap<>();
        ProductMap.put("Pid",ProductRandomKey);
        ProductMap.put("date",saveCurrentDate);
        ProductMap.put("time",saveCurrentTime);
        ProductMap.put("description",Description);
        ProductMap.put("image",DownloadImageURL);
        ProductMap.put("category",category_name);
        ProductMap.put("Price",Price);
        ProductMap.put("Pname",Pname);

        ProductRef.child(ProductRandomKey).updateChildren(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {

                    Intent intent=new Intent(AdminActivity.this,AdminCategoryActivity.class);
                    startActivity(intent);

                    loadinBar.dismiss();
                    Toast.makeText(AdminActivity.this, "Product Added !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadinBar.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(AdminActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
