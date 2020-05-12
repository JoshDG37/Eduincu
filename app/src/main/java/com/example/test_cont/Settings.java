package com.example.test_cont;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {
    private EditText username;
    private Button send, but_logout;
    private ImageView profile_image;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;
    private DatabaseReference rootRef;
    private static final int GalleryPick=1;
    private StorageReference userProfileImageRef;
    public Settings() {
        // Required empty public constructor
    }
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_settings, container, false);

        mAuth =FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        rootRef= FirebaseDatabase.getInstance().getReference();
        userProfileImageRef= FirebaseStorage.getInstance().getReference().child("Profile Images");

        InitializaFields();


        //boton de mandar el usuario a firebase
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }


        });

        //Recuperar info del usuario
        RetrieveUserInfo();

        //Imagen de perfil
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);
            }
        });

        //boton de cerrar sesion

        currentUser=mAuth.getCurrentUser();
        but_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent logingIntent=new Intent(rootView.getContext(),MainActivity.class);
                startActivity(logingIntent);


                //----------------------------------------------------------------hacer para importar metodos del padre o alguna forma para poder hacer el logout

            }
        });


        return rootView;
    }

    private void InitializaFields() {
        but_logout= (Button) rootView.findViewById(R.id.but_logout);
        send=(Button) rootView.findViewById(R.id.send);
        username=(EditText) rootView.findViewById(R.id.username);
        profile_image=(ImageView) rootView.findViewById(R.id.profile_image);
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //
//        if(requestCode==GalleryPick && resultCode==RESULT_OK){
//            Uri ImageUri=data.getData();
//
//            CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(getContext(), this);
//        }
        //meter la imagen en storage
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//            if(resultCode==RESULT_OK){
//                Uri resultUri=result.getUri();
//
//                StorageReference filePath=userProfileImageRef.child(currentUserID + ".jpg");
//
//                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(getActivity(),"Imagen de Perfil",Toast.LENGTH_SHORT).show();
//                            //meter la imagen en database
//                            final String downloaedUrl=task.getResult().getStorage().getDownloadUrl().toString();
//
//                            rootRef.child("Users").child(currentUserID).child("image").setValue(downloaedUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        Toast.makeText(getActivity(),"Imagen salvada",Toast.LENGTH_SHORT).show();
//                                    }
//                                    else{
//                                        String message=task.getException().toString();
//                                        Toast.makeText(getActivity(),"Error: "+message,Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//
//                        }else{
//                            String message=task.getException().toString();
//                            Toast.makeText(getActivity(),"Error: "+message,Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                });
//            }
//
//        }

   // }

    private void UpdateSettings() {
        String setUserName=username.getText().toString();

        if(TextUtils.isEmpty(setUserName)){
            Toast.makeText(getActivity(),"Escribe tu nombre y apellido",Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String,String> profileMap=new HashMap<>();
            profileMap.put("uid",currentUserID);
            profileMap.put("name",setUserName);
            rootRef.child("Users").child(currentUserID).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(),"Hecho",Toast.LENGTH_SHORT).show();
                    }else{
                        String message=task.getException().toString();
                        Toast.makeText(getActivity(),"Error: "+message,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void RetrieveUserInfo() {
        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))){
                    String retrieveUserName=dataSnapshot.child("name").getValue().toString();
//                    String retrieveProfile=dataSnapshot.child("image").getValue().toString();

                    username.setText(retrieveUserName);
//                    Picasso.get().load(retrieveProfile).into(profile_image);
                }
                else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){
                    String retrieveUserName=dataSnapshot.child("name").getValue().toString();
//                    String retrieveProfile=dataSnapshot.child("image").getValue().toString();

                    username.setText(retrieveUserName);
//                    Picasso.get().load(retrieveProfile).into(profile_image);
                }
                else{
                    Toast.makeText(getActivity(),"Por favor actualiza tu perfil",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
