package com.example.snapdeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.snapdeal.model.Products;
import com.example.snapdeal.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity
{
    private FloatingActionButton addToCart;
    private ImageView ProductImage;
    private ElegantNumberButton numberButton;
    private TextView ProductPrice,ProductDescription,ProductName;
    private String ProductID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        ProductID=getIntent().getStringExtra("Pid");


        addToCart=(FloatingActionButton)findViewById(R.id.add_product_toCart_btn);
        numberButton=(ElegantNumberButton)findViewById(R.id.number_btn);
        ProductImage=(ImageView)findViewById(R.id.product_image_details);
        ProductPrice=(TextView)findViewById(R.id.Product_price_details);
        ProductDescription=(TextView)findViewById(R.id.Product_description_details);
        ProductName=(TextView)findViewById(R.id.Product_name_details);

        getProductDetails(ProductID);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                addingToCartList();
            }
        });
    }

    private void addingToCartList()
    {
        String saveCurrentTime,saveCurrentDate;
        Calendar callForDate= Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(callForDate.getTime());

        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("Pid",ProductID);
        cartMap.put("Pname",ProductName.getText().toString());
        cartMap.put("Price",ProductPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.CurrOnlineUser.getPhone()).child("Products").child(ProductID)
        .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.CurrOnlineUser.getPhone()).child("Products").child(ProductID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });

                        }


                    }
                });


    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Products products=dataSnapshot.getValue(Products.class);

                    ProductName.setText(products.getPname());
                    ProductDescription.setText(products.getDescription());
                    ProductPrice.setText(products.getPrice()+ "$");
                    Picasso.get().load(products.getImage()).into(ProductImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {


            }
        });
    }
}
