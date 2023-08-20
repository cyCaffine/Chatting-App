package com.menkashah.whatschattingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.menkashah.whatschattingapp.Model.user;
import com.menkashah.whatschattingapp.databinding.ActivitySignInBinding;

public class SignIn extends AppCompatActivity {

    private ActivitySignInBinding signInBinding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        signInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(signInBinding.getRoot());


       auth =  FirebaseAuth.getInstance();
       database = FirebaseDatabase.getInstance();


        progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setTitle(" Login");
        progressDialog.setMessage("Login to Your Account");

//        configure google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        signInBinding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                auth.signInWithEmailAndPassword(
                        signInBinding.editTextSignEmailAddress.getText().toString(),
                        signInBinding.editTextSignNumberPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Intent i = new Intent(SignIn.this, MainActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(SignIn.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                });


            }
        });
        signInBinding.googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });
        signInBinding.btnsingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this,SignUp.class);
                startActivity(i);
            }
        });
        if(auth.getCurrentUser()!=null){
            Intent i = new Intent(SignIn.this,MainActivity.class);
            startActivity(i);
        }
    }

    int Rc_sign_in = 1;
    private  void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,Rc_sign_in);
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Rc_sign_in){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithAccount" + account.getId());
                firebaseAuthWithAccount(account.getIdToken());
            }catch(ApiException e){
                Log.w("TAG","Google sign in failed",e);
            }

        }
    }
    protected  void firebaseAuthWithAccount(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser users = auth.getCurrentUser();
                            user user = new user();
                             user.setUserId(users.getUid());
                             user.getUserName(users.getDisplayName());
                             user.getProfilePic(users.getPhotoUrl().toString());
                             database.getReference().child("Users").child(users.getUid()).setValue(user);                                                                                                                                                                                                                                                                                                                                                                                          
                             Intent i = new Intent(SignIn.this,MainActivity.class);
                             startActivity(i);
                            Toast.makeText(SignIn.this, "sign in with google", Toast.LENGTH_SHORT).show();

//                            updateUI(user);
                        }else{
                              // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            Snackbar.make(signInBinding.mainLayout,"authentication failed",Snackbar.LENGTH_SHORT);
//                            updateUI(null);
                        }
                    }
                });
    }
}







