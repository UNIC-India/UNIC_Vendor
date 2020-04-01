package com.unic.unic_vendor_final_1.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivityLoginBinding;
import com.unic.unic_vendor_final_1.viewmodels.FirebasePhoneAuthViewModel;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebasePhoneAuthViewModel loginViewModel;

    private ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        updateUI(0);

        loginViewModel = ViewModelProviders.of(this).get(FirebasePhoneAuthViewModel.class);
        loginViewModel.getStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                updateUI(integer);
            }
        });

        loginViewModel.getCode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                loginBinding.etOTP.setText(s);
            }
        });

        loginBinding.btnconf.setOnClickListener(this);
        loginBinding.btnlogin.setOnClickListener(this);
        loginBinding.btnres.setOnClickListener(this);
        loginBinding.signupLink.setOnClickListener(this);
    }


    void updateUI(int code){
        switch(code){
            case 0:
                loginBinding.details.setVisibility(View.VISIBLE);
                loginBinding.loginOTP.setVisibility(View.GONE);
                break;
            case 1:
                loginBinding.details.setVisibility(View.GONE);
                loginBinding.loginOTP.setVisibility(View.VISIBLE);
                break;
            case 2:
                loginBinding.details.setVisibility(View.VISIBLE);
                loginBinding.loginOTP.setVisibility(View.GONE);
                loginBinding.etphone.setError("Phone number might be incorrect");
                break;
            case 3:
                loginBinding.details.setVisibility(View.VISIBLE);
                loginBinding.loginOTP.setVisibility(View.GONE);
                loginBinding.etphone.setError("Verification limit exceeded for today");
                break;
            case 4:
                startActivity(new Intent(this,UserHome.class));
                finish();
                break;
            case -1:
                loginBinding.details.setVisibility(View.VISIBLE);
                loginBinding.loginOTP.setVisibility(View.GONE);
                Toast.makeText(this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnlogin:
                startAuth();
                break;
            case R.id.btnconf:
                authWithOTP();
                break;
            case R.id.btnres:
                resendOTP();
                break;
            case R.id.signupLink:
                startActivity(new Intent(this,SignUp.class));
        }
    }

    private void startAuth(){
        if(loginBinding.etphone.getText().toString().trim()==null) {
            loginBinding.etphone.setError("Enter phone number");
            return;
        }
        loginViewModel.verifyPhoneNumber("+91"+loginBinding.etphone.getText().toString().trim());
    }
    private void authWithOTP(){
        if(loginBinding.etOTP.getText().toString().trim().length()!=6){
            loginBinding.etOTP.setError("Incorrect OTP entered");
            return;
        }
        loginViewModel.verifyWithOTP(loginBinding.etOTP.getText().toString().trim());
    }

    private void resendOTP(){
        loginViewModel.resendOTP("+91"+loginBinding.etphone.getText().toString().trim());
    }
}
