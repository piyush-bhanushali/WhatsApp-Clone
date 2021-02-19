package com.piyush.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.piyush.whatsapp.databinding.ActivityManageOtpBinding;

import java.util.concurrent.TimeUnit;

public class ManageOtpActivity extends AppCompatActivity {

    ActivityManageOtpBinding binding;

    String phonenumber;
    String otpid;
    FirebaseAuth mAuth;
    String TAG = "ManageOtpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityManageOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phonenumber = getIntent().getStringExtra("mobile");
        mAuth = FirebaseAuth.getInstance();

        initiateOtp(phonenumber);

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etOtp.getText().toString().isEmpty()){
                    Toast.makeText(ManageOtpActivity.this, "Field is blank", Toast.LENGTH_SHORT).show();
                    binding.etOtp.setError("Enter OTP");
                    return;
                }else if(binding.etOtp.getText().toString().length()!=6){
                    Toast.makeText(ManageOtpActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    binding.etOtp.setError("Enter Valid OTP");
                    return;
                }else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, binding.etOtp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

    }

    private void initiateOtp(String phoneNumber) {

        //**********
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(ManageOtpActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpid = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(ManageOtpActivity.this, TAG + " : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        //**********

//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phonenumber,                                // Phone number to verify
//                60,                                      // Timeout duration
//                TimeUnit.SECONDS,                           // Unit of timeout
//                this,                               // Activity (for callback binding)
//                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//                    @Override
//                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                        otpid = s;
//                    }
//
//                    @Override
//                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                        signInWithPhoneAuthCredential(phoneAuthCredential);
//                    }
//
//                    @Override
//                    public void onVerificationFailed(@NonNull FirebaseException e) {
//                        Toast.makeText(ManageOtpActivity.this, TAG + " : "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }); // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(ManageOtpActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(ManageOtpActivity.this, MainActivity.class));
                            //finish();

                            //FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {

                            Toast.makeText(ManageOtpActivity.this, TAG + " : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}