package com.unic.unic_vendor_final_1.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivityLoginBinding;
import com.unic.unic_vendor_final_1.viewmodels.FirebasePhoneAuthViewModel;

import static com.unic.unic_vendor_final_1.commons.Helpers.enableDisableViewGroup;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebasePhoneAuthViewModel loginViewModel;

    private ActivityLoginBinding loginBinding;

    private View coverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        updateUI(0);

        loginViewModel = new ViewModelProvider(this).get(FirebasePhoneAuthViewModel.class);
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
                enableDisableViewGroup((ViewGroup)loginBinding.getRoot(),true);
                ((ViewGroup)loginBinding.getRoot()).removeView(coverView);
                loginBinding.loginProgressBar.setVisibility(View.GONE);
                loginBinding.details.setVisibility(View.GONE);
                loginBinding.loginOTP.setVisibility(View.VISIBLE);
                break;
            case 2:
                enableDisableViewGroup((ViewGroup)loginBinding.getRoot(),true);
                ((ViewGroup)loginBinding.getRoot()).removeView(coverView);
                loginBinding.loginProgressBar.setVisibility(View.GONE);
                loginBinding.details.setVisibility(View.VISIBLE);
                loginBinding.loginOTP.setVisibility(View.GONE);
                loginBinding.etphone.setError("Phone number might be incorrect");
                break;
            case 3:
                enableDisableViewGroup((ViewGroup)loginBinding.getRoot(),true);
                ((ViewGroup)loginBinding.getRoot()).removeView(coverView);
                loginBinding.loginProgressBar.setVisibility(View.GONE);
                loginBinding.details.setVisibility(View.VISIBLE);
                loginBinding.loginOTP.setVisibility(View.GONE);
                loginBinding.etphone.setError("Verification limit exceeded for today");
                break;
            case 4:
                enableDisableViewGroup((ViewGroup)loginBinding.getRoot(),true);
                ((ViewGroup)loginBinding.getRoot()).removeView(coverView);
                loginBinding.loginProgressBar.setVisibility(View.GONE);
                startActivity(new Intent(this,UserHome.class));
                finish();
                break;
            case -1:
                enableDisableViewGroup((ViewGroup)loginBinding.getRoot(),true);
                ((ViewGroup)loginBinding.getRoot()).removeView(coverView);
                loginBinding.loginProgressBar.setVisibility(View.GONE);
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
                coverView = new View(this);
                coverView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                coverView.setBackgroundResource(R.color.gray_1);
                coverView.setAlpha(0.5f);
                ((ViewGroup)loginBinding.getRoot()).addView(coverView);
                loginBinding.loginProgressBar.setVisibility(View.VISIBLE);
                loginBinding.loginProgressBar.bringToFront();
                enableDisableViewGroup((ViewGroup)loginBinding.getRoot(),false);
                startAuth();
                break;
            case R.id.btnconf:
                coverView = new View(this);
                coverView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                coverView.setBackgroundResource(R.color.gray_1);
                coverView.setAlpha(0.5f);
                ((ViewGroup)loginBinding.getRoot()).addView(coverView);
                loginBinding.loginProgressBar.setVisibility(View.VISIBLE);
                loginBinding.loginProgressBar.bringToFront();
                enableDisableViewGroup((ViewGroup)loginBinding.getRoot(),false);
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
        if(loginBinding.etphone.getText().toString().trim().length()==0) {
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
