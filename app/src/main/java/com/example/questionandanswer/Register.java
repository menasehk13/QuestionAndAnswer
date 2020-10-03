package com.example.questionandanswer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register extends AppCompatActivity {
TextInputEditText firstname,lastname,email,password,birthdate;
RadioButton male,femlae;
Button register;
ImageView profile;
DatePickerDialog pickerDialog;
FirebaseAuth auth;
FirebaseFirestore firebaseFirestore;
FirebaseStorage storage;
DocumentReference reference;
StorageReference storageReference;
private int PICK_IMAGE=99;
RadioGroup group;
Uri uri;
SweetAlertDialog sweetAlertDialog;
String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.Lastname);
        email=findViewById(R.id.emailinput);
        password=findViewById(R.id.passwordnew);
        birthdate=findViewById(R.id.date);
        profile=findViewById(R.id.image_view);
        male=findViewById(R.id.male);
        femlae=findViewById(R.id.female);
        register=findViewById(R.id.registernew);
        group=findViewById(R.id.radiobutton);
        firebaseFirestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
               token=task.getResult().getToken();
            }
        });
        birthdate.setInputType(InputType.TYPE_NULL);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Your Profile Image"),PICK_IMAGE);
            }
        });
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int yy=calendar.get(Calendar.YEAR);
                int mm=calendar.get(Calendar.MONTH);
                int dd=calendar.get(Calendar.DAY_OF_MONTH);
                pickerDialog=new DatePickerDialog(Register.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                       birthdate.setText(i2+"/"+i1+"/"+i);
                    }
                },yy,mm,dd);
                pickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pickerDialog.show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uri!=null) {
                    sweetAlertDialog=new SweetAlertDialog(Register.this);
                    sweetAlertDialog.setTitle("Uploading Information Please Wiat");
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                    sweetAlertDialog.show();
                    final String fname, lname, memail, mpass, mbirthdate, mgender;
                    fname = firstname.getText().toString().trim();
                    lname = lastname.getText().toString().trim();
                    memail = email.getText().toString();
                    mpass = password.getText().toString();
                    mbirthdate = birthdate.getText().toString();
                    int getselctedid=group.getCheckedRadioButtonId();
                    RadioButton radioButton=findViewById(getselctedid);
                    mgender=radioButton.getText().toString();
                    if (fname.isEmpty() && lname.isEmpty()) {
                        firstname.setError("Please input your name");
                        firstname.setFocusable(true);
                        lastname.setError("Please Enter Your Lastname");
                        lastname.setFocusable(true);
                        return;
                    }
                    if (memail.isEmpty() && mpass.isEmpty() && mpass.length() < 8) {
                        email.setError("Please Enter Your Email ");
                        email.setFocusable(true);
                        password.setError("Enter Passward ...Password must be Greater than 8 ");
                        return;
                    }
                    if (mbirthdate.isEmpty()) {
                        birthdate.setError("Please enter Your Birth day");
                        birthdate.setFocusable(true);

                    }
                    auth.createUserWithEmailAndPassword(memail, mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String userId = auth.getCurrentUser().getUid();
                               storageReference=storage.getReference().child("Users Profile Picture"+System.currentTimeMillis()+"."+getDataExtenssion(uri));
                               storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                   @Override
                                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                       Toast.makeText(Register.this, "Profile Image Uploaded", Toast.LENGTH_SHORT).show();

                                       Task<Uri> task1=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                       task1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                           @Override
                                           public void onSuccess(Uri uri) {
                                             String imageurl;
                                             imageurl=uri.toString();
                                             reference=firebaseFirestore.collection("Users").document(userId);
                                               Map<String,Object> map=new HashMap<>();
                                               map.put("FirstName",fname);
                                               map.put("LastName",lname);
                                               map.put("Email",memail);
                                               map.put("BirthDay",mbirthdate);
                                               map.put("Gender",mgender);
                                               map.put("ImageUrl",imageurl);
                                               map.put("token",token);
                                               reference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                     if (task.isSuccessful()){
                                                         sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                         sweetAlertDialog.setConfirmButton("Continue", new SweetAlertDialog.OnSweetClickListener() {
                                                             @Override
                                                             public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                 startActivity(new Intent(getApplicationContext(),Viewpager.class));
                                                                 finish();

                                                             }
                                                         });

                                                      }
                                                   }
                                               });
                                           }
                                       });
                                   }
                               }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                   @Override
                                   public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                   double prgress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                                   sweetAlertDialog.setContentText("Uploaded"+" "+((int)prgress)+"%...");
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                      sweetAlertDialog.dismissWithAnimation();
                                       Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                   }
                               });

                            }
                        }
                    });
                }else{
                    Toast.makeText(Register.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE&&data!=null&&data.getData()!=null){
            uri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                profile.setImageBitmap(bitmap);
                Picasso.get().load(uri).fit().into(profile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public  String getDataExtenssion(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
}