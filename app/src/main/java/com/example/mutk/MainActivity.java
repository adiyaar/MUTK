 package com.example.mutk;

 import android.content.DialogInterface;
 import android.content.Intent;
 import android.content.res.AssetManager;
 import android.graphics.Typeface;
 import android.graphics.drawable.AnimationDrawable;
 import android.os.Bundle;
 import android.text.TextUtils;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.RelativeLayout;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AlertDialog;
 import androidx.appcompat.app.AppCompatActivity;

 import com.google.android.gms.tasks.OnCompleteListener;
 import com.google.android.gms.tasks.OnFailureListener;
 import com.google.android.gms.tasks.OnSuccessListener;
 import com.google.android.gms.tasks.Task;
 import com.google.firebase.auth.AuthResult;
 import com.google.firebase.auth.FirebaseAuth;


 import java.util.Objects;


 public class MainActivity extends AppCompatActivity {

     AnimationDrawable anim;
     AssetManager am;
     FirebaseAuth fauth;


     Typeface  typefaceArial;
     TextView lblHeader;
     TextView mreg;
     TextView forgetlink;

     EditText memail,mpassword;

     Button btnLogin;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         am = this.getApplicationContext().getAssets();
         RelativeLayout container = findViewById(R.id.container);
         anim = (AnimationDrawable) container.getBackground();
         anim.setEnterFadeDuration(100);
         anim.setExitFadeDuration(1000);
         fauth = FirebaseAuth.getInstance();
         forgetlink= findViewById(R.id.reset);
         lblHeader = findViewById(R.id.lblHeader);


         mpassword = findViewById(R.id.edtPassword);
         memail = findViewById(R.id.edtEmail);
         btnLogin = findViewById(R.id.btnLogin);
         mreg = findViewById(R.id.createtext);
         mpassword.setTypeface(typefaceArial);
         memail.setTypeface(typefaceArial);
         btnLogin.setTypeface(typefaceArial);

         btnLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String email = memail.getText().toString().trim();
                 String password = mpassword.getText().toString().trim();

                 if (TextUtils.isEmpty(email)) {
                     memail.setError("Email is Required.");
                     return;
                 }

                 if (TextUtils.isEmpty(password)) {
                     mpassword.setError("Password is Required.");
                     return;
                 }

                 if (password.length() < 6) {
                     mpassword.setError("Your password would be of length greater than 6 characters");
                     return;
                 }

                 fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(getApplicationContext(), Home.class));
                         } else {
                             Toast.makeText(MainActivity.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                         }

                     }
                 });
             }


         });

         mreg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(), Register.class));
             }
         });

         forgetlink.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 final EditText resetmail =  new EditText(v.getContext());
                 AlertDialog.Builder passwordreset = new AlertDialog.Builder(v.getContext());
                 passwordreset.setTitle("Reset password");
                 passwordreset.setMessage("Enter your registred email id");
                 passwordreset.setView(resetmail);

                 passwordreset.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                        //extract mail
                         String mail = resetmail.getText().toString();
                         fauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Reset Link sent", Toast.LENGTH_SHORT).show();
                             }
                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(MainActivity.this,"Link not sent" + e.getMessage(),  Toast.LENGTH_SHORT).show();
                             }
                         });
                     }
                 });
                passwordreset.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passwordreset.create().show();
             }
         });

     }}