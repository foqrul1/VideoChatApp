package com.example.videochatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private CountryCodePicker codePicker;
    private EditText phoneText, CodeText;
    private Button continueAndNextButton;
    private String checker = "", PhoneNumber = "";

    private RelativeLayout relativeLayout;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    FirebaseAuth mAuth;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken  resendingToken;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        phoneText = findViewById(R.id.phoneText);
        CodeText = findViewById(R.id.codeText);
        relativeLayout = findViewById(R.id.phoneAuth);
        continueAndNextButton = findViewById(R.id.continueNextButton);

        codePicker = findViewById(R.id.ccp);
        codePicker.registerCarrierNumberEditText(phoneText);

        continueAndNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(continueAndNextButton.getText().equals("Submit") || checker.equals("Code Sent")){
                    String VerificationCode = CodeText.getText().toString();
                    if(VerificationCode.equals("")){
                        Toast.makeText(RegisterActivity.this, "Please write Verified Code first", Toast.LENGTH_SHORT).show();
                    }else{
                        loadingBar.setTitle("Code Verification...");
                        loadingBar.setMessage("Please Wait we are verifying your phonr");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, VerificationCode);
                        SignInPhoneAuthCredential(phoneAuthCredential);
                    }

                }else{
                    PhoneNumber = codePicker.getFullNumberWithPlus();

                    if(!PhoneNumber.equals("")){
                        loadingBar.setTitle("Code Verification...");
                        loadingBar.setMessage("Please Wait we are verifying your phonr");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                PhoneNumber,
                                60,
                                TimeUnit.SECONDS,
                                RegisterActivity.this,
                                callbacks
                        );
                        
                    }else{
                        Toast.makeText(RegisterActivity.this, "Please Enter a Valid Code", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                SignInPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(RegisterActivity.this, "Sign in Failed", Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.VISIBLE);

            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken){
                super.onCodeSent(s, forceResendingToken);
                relativeLayout.setVisibility(View.GONE);
                mVerificationId = s;
                resendingToken = forceResendingToken;
                
                checker = "Code Sent";
                continueAndNextButton.setText("");
                CodeText.setVisibility(View.VISIBLE);
                Toast.makeText(RegisterActivity.this, "Code has been send, please check", Toast.LENGTH_SHORT).show();
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Intent homeIntent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }
    private void SignInPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            loadingBar.dismiss();
                            Log.d("TAG", "Sign in Suceess");
                            //FirebaseUser firebaseUser = task.getResult().getUser();
                            SendUserToMainActivity();
                        }
                        else{
                            loadingBar.dismiss();
                            String e = task.getException().toString();
                            Toast.makeText(RegisterActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    private void SendUserToMainActivity(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
