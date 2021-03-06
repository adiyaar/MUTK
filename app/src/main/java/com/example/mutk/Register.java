package com.example.mutk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mfullname,memail,mpassword;
    Button mcreate;
    FirebaseAuth fauth;
    FirebaseFirestore fStore;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mfullname = findViewById(R.id.editText7);
        memail = findViewById(R.id.editText8);
        mpassword = findViewById(R.id.editText9);
        mcreate = findViewById(R.id.button2);
        fauth = FirebaseAuth.getInstance();




            mcreate.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               Toast.makeText(Register.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                               startActivity(new Intent(getApplicationContext(), Home.class));

                                           }
                                       }
            );

            mcreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final  String fullname = mfullname.getText().toString();
                    final String email = memail.getText().toString().trim();
                    final String password = mpassword.getText().toString().trim();

                    if(TextUtils.isEmpty((email)))
                    {
                        memail.setError("Email Id missing");
                        return;
                    }
                    if(TextUtils.isEmpty((password)))
                    {
                        mpassword.setError("Email Id missing");
                        return;
                    }
                    if(password.length() < 6)
                    {
                        mpassword.setError("Password Must be >= 6 Characters");
                        return;
                    }
                    fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                // send verification link

                                FirebaseUser fuser = fauth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>()
                                {
                                    @Override
                                    public void onSuccess(Void aVoid)
                                    {
                                        Toast.makeText(Register.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                    }
                                });

                                Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                                userID = fauth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("fname",fullname);
                                user.put("email",email);

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));

                            }else {
                                Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }


                    });

    }}
);}}
