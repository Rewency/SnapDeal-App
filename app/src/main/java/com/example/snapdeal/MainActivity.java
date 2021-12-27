package com.example.snapdeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.snapdeal.model.Users;
import com.example.snapdeal.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog loadinBar;
    private Button joinNowButton,loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         joinNowButton=(Button)findViewById(R.id.main_join_now_btn);
         loginButton=(Button)findViewById(R.id.main_login_btn);
         loadinBar=new ProgressDialog(this);
         Paper.init(this);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        String UserphoneKey=Paper.book().read(Prevalent.UserphoneKey);
        String UserpasswordKey=Paper.book().read(Prevalent.UserpasswordKey);
        if(UserphoneKey != ""  && UserpasswordKey != "")
        {
            if(!TextUtils.isEmpty(UserphoneKey) && !TextUtils.isEmpty(UserpasswordKey))
            {
                AllowAccess(UserphoneKey,UserpasswordKey);
                loadinBar.setTitle("Already Login");
                loadinBar.setMessage("Please Wait");
                loadinBar.setCanceledOnTouchOutside(false);
                loadinBar.show();
            }
        }
    }

    private void AllowAccess(final String phone, final String password)
    {
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersdata=dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersdata.getPhone().equals(phone))
                    {
                        if(usersdata.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this,"logged in!",Toast.LENGTH_SHORT).show();
                            loadinBar.dismiss();

                            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                            Prevalent.CurrOnlineUser= usersdata;
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Incorrect Password!",Toast.LENGTH_SHORT).show();
                            loadinBar.dismiss();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Account with this"+ phone +" not exist!",Toast.LENGTH_SHORT).show();
                    loadinBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
}
