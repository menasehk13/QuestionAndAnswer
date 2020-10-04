package com.example.questionandanswer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class ProfileFragment extends Fragment {
EditText name,email,birthdate;
ImageView proimage;
Button logout;
    FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       name=view.findViewById(R.id.view_username);
       email=view.findViewById(R.id.viewemal);
       birthdate=view.findViewById(R.id.viewbirthday);
       proimage=view.findViewById(R.id.profile_image);
       logout=view.findViewById(R.id.logout);
       email.setClickable(false);
       email.setInputType(InputType.TYPE_NULL);
       name.setInputType(InputType.TYPE_NULL);
       name.setClickable(false);
       birthdate.setInputType(InputType.TYPE_NULL);
       birthdate.setClickable(false);
         auth=FirebaseAuth.getInstance();
        String userid=auth.getCurrentUser().getUid();
        final DocumentReference documentReference= FirebaseFirestore.getInstance().collection("Users").document(userid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
              DocumentSnapshot documentSnapshot=task.getResult();
              if (documentSnapshot.exists()){
               name.setText(String.format("%s %s", documentSnapshot.get("FirstName").toString(), documentSnapshot.get("LastName").toString()));
               email.setText(documentSnapshot.get("Email").toString());
               birthdate.setText(documentSnapshot.get("BirthDay").toString());
                  Picasso.get().load(documentSnapshot.get("ImageUrl").toString()).transform(new CropCircleTransformation()).fit().into(proimage);
              }else{
                  SweetAlertDialog alertDialog=new SweetAlertDialog(getContext());
                  alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                  alertDialog.setConfirmButton("SignUp", new SweetAlertDialog.OnSweetClickListener() {
                      @Override
                      public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(getContext(),PhoneVerification.class));
                        getActivity().finish();
                      }
                  });
              }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              final  SweetAlertDialog sweetAlertDialog2=new SweetAlertDialog(getContext());
                sweetAlertDialog2.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog2.setTitle("Warning");
                sweetAlertDialog2.show();
                sweetAlertDialog2.setContentText("Do you Want to Sign Out");
                sweetAlertDialog2.setConfirmButton("Logout", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        auth.signOut();
                        startActivity(new Intent(getContext(),PhoneVerification.class));
                    }
                });
                sweetAlertDialog2.setCancelButton("Canel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog2.dismiss();
                    }
                });
            }
        });
    }
}