package com.example.snapdeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snapdeal.prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity
{
    private CircleImageView profileImageView;
    private EditText fullnameEditText,UserPhoneEditText,AddressEditText;
    private TextView proflieChangetextBtn,closeTextBtn,saveTextBtn,birthdayTxt;
    private Uri imageUri;
    private String myUri="";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicRef;
    private String checker="";
    private Button  securityQuestionBtn;
    private DatePickerDialog dpd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePicRef= FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        profileImageView=(CircleImageView)findViewById(R.id.settings_profile_image);
        fullnameEditText=(EditText)findViewById(R.id.settings_full_name);
        UserPhoneEditText=(EditText)findViewById(R.id.settings_phone_number);
        AddressEditText=(EditText)findViewById(R.id.settings_address);
        proflieChangetextBtn=(TextView)findViewById(R.id.profile_image_change);
        closeTextBtn=(TextView)findViewById(R.id.close_settings);
        saveTextBtn=(TextView)findViewById(R.id.updateSettingsBtn);
        birthdayTxt=(TextView)findViewById(R.id.birthday_txt);
        securityQuestionBtn=(Button)findViewById(R.id.security_questions_btn);

        userInfoDisplay(profileImageView,fullnameEditText,UserPhoneEditText,AddressEditText,birthdayTxt);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(SettingsActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","settings");
                startActivity(intent);
            }
        });

        saveTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        proflieChangetextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker="clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
;        });
        birthdayTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(SettingsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day)
                    {
                        String date=  day+ "/" + (month+1) + "/"+year;
                        birthdayTxt.setText(date);
                    }
                },day,month,year);
                dpd.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error! try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullnameEditText, final EditText userPhoneEditText, final EditText addressEditText,final TextView birthdayTxt)
    {
        DatabaseReference UsersRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrOnlineUser.getPhone());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image=dataSnapshot.child("image").getValue().toString();
                        String name=dataSnapshot.child("name").getValue().toString();
                        String phone=dataSnapshot.child("phone").getValue().toString();
                        String address=dataSnapshot.child("address").getValue().toString();
                        String birthday=dataSnapshot.child("birthday").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullnameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);
                        birthdayTxt.setText(birthday);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
    private void userInfoSaved()
    {
        if(TextUtils.isEmpty(fullnameEditText.getText().toString()))
        {
            Toast.makeText(this, "please add user name first", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(AddressEditText.getText().toString()))
        {
            Toast.makeText(this, "please add address first", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(UserPhoneEditText.getText().toString()))
        {
            Toast.makeText(this, "please add phone number ", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }

    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri !=null)
        {
            final StorageReference fileRef=storageProfilePicRef.child(Prevalent.CurrOnlineUser.getPhone() + ".jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(! task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        Uri downloadUri=task.getResult();
                        myUri=downloadUri.toString();
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object>usermap=new HashMap<>();
                        usermap.put("name",fullnameEditText.getText().toString());
                        usermap.put("address",AddressEditText.getText().toString());
                        usermap.put("phoneOrder",UserPhoneEditText.getText().toString());
                        usermap.put("image",myUri);
                        usermap.put("birthday",birthdayTxt.getText().toString());
                        ref.child(Prevalent.CurrOnlineUser.getPhone()).updateChildren(usermap);

                        progressDialog.dismiss();
                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
        else
        {
            Toast.makeText(this, "Error image not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOnlyUserInfo()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object>usermap=new HashMap<>();
        usermap.put("name",fullnameEditText.getText().toString());
        usermap.put("address",AddressEditText.getText().toString());
        usermap.put("phoneOrder",UserPhoneEditText.getText().toString());
        usermap.put("birthday",birthdayTxt.getText().toString());

        ref.child(Prevalent.CurrOnlineUser.getPhone()).updateChildren(usermap);

        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
        finish();
    }

}
