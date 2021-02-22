package com.piyush.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.piyush.whatsapp.databinding.ActivityPhoneSignInBinding;

public class PhoneSignInActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private ActivityPhoneSignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPhoneSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ccp.registerCarrierNumberEditText(binding.etPhone);
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneSignInActivity.this, ManageOtpActivity.class);
                intent.putExtra("mobile",binding.ccp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(PhoneSignInActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("About")
                        .setMessage("SignUp/SignIn with the number is the process for make your login easy and friendly.\nEnter your phone number to register or login in the WhatsApp")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                break;
        }
        return true;
    }
}