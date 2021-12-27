package com.example.snapdeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snapdeal.ViewHolder.ProductViewHolder;
import com.example.snapdeal.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SearchProductActivity extends AppCompatActivity
{
    private Button searchBtn,qrBtn,voiceBtn;
    private EditText inputTexe;
    private RecyclerView searchList;
    private String searchInput;
    private String voiceTxt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        inputTexe=(EditText)findViewById(R.id.Search_product_name);
        searchBtn=(Button)findViewById(R.id.search_btn);
        searchList=(RecyclerView)findViewById(R.id.search_list);
        qrBtn=(Button)findViewById(R.id.qr_btn);
        voiceBtn=(Button)findViewById(R.id.voice_btn);

        voiceTxt=getIntent().getStringExtra("voice");
        inputTexe.setText(voiceTxt);

        searchList.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                searchInput=inputTexe.getText().toString();
                onStart();
            }
        });
        qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SearchProductActivity.this,BarcodeActivity.class);
                startActivity(intent);
            }
        });
        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(SearchProductActivity.this,VoiceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("Pname").startAt(searchInput),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products)
            {
                productViewHolder.txtProductName.setText(products.getPname());
                productViewHolder.txtProductDesc.setText(products.getDescription());
                productViewHolder.txtProductPrice.setText("Price = " + products.getPrice()+ "$");
                Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(SearchProductActivity.this,ProductDetailsActivity.class);
                        intent.putExtra("Pid",products.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;
            }
        };

        searchList.setAdapter(adapter);
        adapter.startListening();
    }

}
