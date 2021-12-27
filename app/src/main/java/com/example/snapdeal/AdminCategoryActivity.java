package com.example.snapdeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView t_shirts,sports_t_shirts,female_dresses,sweaters;
    private ImageView glasses,purses_bags,hats,shoes;
    private ImageView  headphoness,laptops,watches,mobiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        t_shirts=(ImageView)findViewById(R.id.t_shirts);
        sports_t_shirts=(ImageView)findViewById(R.id.sports_t_shirts);
        female_dresses=(ImageView)findViewById(R.id.female_dresses);
        sweaters=(ImageView)findViewById(R.id.sweaters);
        glasses=(ImageView)findViewById(R.id.glasses);
        purses_bags=(ImageView)findViewById(R.id.purses_bags);
        hats=(ImageView)findViewById(R.id.hats);
        shoes=(ImageView)findViewById(R.id.shoes);
        headphoness=(ImageView)findViewById(R.id.headphoness);
        laptops=(ImageView)findViewById(R.id.laptops);
        watches=(ImageView)findViewById(R.id.watches);
        mobiles=(ImageView)findViewById(R.id.mobiles);


        t_shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "t_shirts");
                startActivity(intent);

            }
        });

        sports_t_shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "sports_t_shirts");
                startActivity(intent);
            }
        });
        female_dresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "female_dresses");
                startActivity(intent);
            }
        });
        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "sweaters");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "glasses");
                startActivity(intent);
            }
        });
        purses_bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "purses_bags");
                startActivity(intent);
            }
        });
        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "hats");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "shoes");
                startActivity(intent);
            }
        });
        headphoness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "headphoness");
                startActivity(intent);
            }
        });
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "laptops");
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "watches");
                startActivity(intent);
            }
        });
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminActivity.class);
                intent.putExtra("category", "mobiles");
                startActivity(intent);
            }
        });


    }
}
