package com.example.snapdeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccount;
    private EditText inputName,inputPhoneNumber,inputPassword;
    private ProgressDialog loadinBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


         createAccount=(Button)findViewById(R.id.register_btn);
         inputName=(EditText)findViewById(R.id.register_username_input);
         inputPhoneNumber=(EditText)findViewById(R.id.register_phonenumber_input);
         inputPassword=(EditText)findViewById(R.id.register_password_input);
         loadinBar= new ProgressDialog(this);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAcc();
            }
        });
    }
    private void createAcc()
    {
        String name= inputName.getText().toString();
        String phone= inputPhoneNumber.getText().toString();
        String password= inputPassword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please Write your Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please Write your Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Write your Password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadinBar.setTitle("Create Account");
            loadinBar.setMessage("Please Wait");
            loadinBar.setCanceledOnTouchOutside(false);
            loadinBar.show();

            validatePhoneNumber(name,phone,password);
        }
    }

    private void validatePhoneNumber(final String name, final String phone, final String password)
    {
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> userdataMap=new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    Rootref.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {

                                        Toast.makeText(RegisterActivity.this,"Your account has been Created!",Toast.LENGTH_SHORT).show();
                                        loadinBar.dismiss();

                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadinBar.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Network Error! Pleae Try Again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"This" + phone +"already exists",Toast.LENGTH_SHORT).show();
                    loadinBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Please Try Again with another Phone Number",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {


            }
        });


    }
}
