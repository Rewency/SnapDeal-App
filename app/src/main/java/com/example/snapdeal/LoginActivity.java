package com.example.snapdeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snapdeal.model.Users;
import com.example.snapdeal.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPhoneNumber,inputPassword;
    private Button loginButton;
    private ProgressDialog loadinBar;
    private String parentDbName = "Users";
    private CheckBox chckboxRememberMe;
    private TextView AdminLink,NotAdminLink,ForgetPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton=(Button)findViewById(R.id.login_btn);
        inputPhoneNumber=(EditText)findViewById(R.id.login_phonenumber_input);
        inputPassword=(EditText)findViewById(R.id.login_password_input);
        loadinBar= new ProgressDialog(this);
        AdminLink =(TextView)findViewById(R.id.admin_panel_link);
        NotAdminLink=(TextView)findViewById(R.id.not_admin_panel_link);
        chckboxRememberMe=(CheckBox)findViewById(R.id.remember_me_chkb);
        ForgetPasswordLink=(TextView)findViewById(R.id.forgot_password_link);
        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginUser();
            }
        });

        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(LoginActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setText("Login Admin");
                AdminLink.setVisibility(view.INVISIBLE);
                NotAdminLink.setVisibility(view.VISIBLE);
                parentDbName="Admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginButton.setText("Login ");
                AdminLink.setVisibility(view.VISIBLE);
                NotAdminLink.setVisibility(view.INVISIBLE);
                parentDbName="Users";

            }
        });

    }

    private void loginUser()
    {
        String phone= inputPhoneNumber.getText().toString();
        String password= inputPassword.getText().toString();

         if(TextUtils.isEmpty(phone))
         {
        Toast.makeText(this,"Please Write your Phone Number",Toast.LENGTH_SHORT).show();
         }
         else if(TextUtils.isEmpty(password))
         {
        Toast.makeText(this,"Please Write your Password",Toast.LENGTH_SHORT).show();
         }
         else
         {
             loadinBar.setTitle("Login Account");
             loadinBar.setMessage("Please Wait");
             loadinBar.setCanceledOnTouchOutside(false);
             loadinBar.show();
             
             AccessAccount(phone,password);
         }
    }
    private void AccessAccount(final String phone, final String password)
    {
        if(chckboxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserphoneKey,phone);
            Paper.book().write(Prevalent.UserpasswordKey,password);
        }
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersdata=dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersdata.getPhone().equals(phone))
                    {
                        if(usersdata.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this,"logged in!",Toast.LENGTH_SHORT).show();
                                loadinBar.dismiss();

                                Intent intent=new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if(parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this,"logged in!",Toast.LENGTH_SHORT).show();
                                loadinBar.dismiss();

                                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                Prevalent.CurrOnlineUser=usersdata;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Incorrect Password!",Toast.LENGTH_SHORT).show();
                            loadinBar.dismiss();

                        }
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Account with this"+ phone +" not exist!",Toast.LENGTH_SHORT).show();
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
