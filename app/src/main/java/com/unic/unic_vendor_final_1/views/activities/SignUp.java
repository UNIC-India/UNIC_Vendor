package com.unic.unic_vendor_final_1.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivitySignUpBinding;
import com.unic.unic_vendor_final_1.datamodels.User;
import com.unic.unic_vendor_final_1.viewmodels.FirebasePhoneAuthViewModel;
import com.unic.unic_vendor_final_1.viewmodels.FirestoreDataViewModel;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignUpBinding signUpBinding;

    private FirebasePhoneAuthViewModel signUpViewModel;
    private FirestoreDataViewModel firestoreDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());

        updateUI(0);

        signUpViewModel = ViewModelProviders.of(this).get(FirebasePhoneAuthViewModel.class);
        signUpViewModel.getStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                updateUI(integer);
            }
        });
        signUpViewModel.getCode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                signUpBinding.edtpin.setText(s);
            }
        });

        firestoreDataViewModel = ViewModelProviders.of(this).get(FirestoreDataViewModel.class);
        firestoreDataViewModel.getUserStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==-1){
                    updateUI(integer);
                }
                else if(integer==1){
                    startActivity(new Intent(SignUp.this,UserHome.class));
                    finish();
                }
            }
        });

        signUpBinding.btnconfirm.setOnClickListener(this);
        signUpBinding.btncontinue.setOnClickListener(this);
        signUpBinding.btnresend.setOnClickListener(this);
    }

    private void updateUI(int code){
        switch(code){
            case 0:
                signUpBinding.signupmain.setVisibility(View.VISIBLE);
                signUpBinding.signuppin.setVisibility(View.GONE);
                break;
            case 1:
                signUpBinding.signupmain.setVisibility(View.GONE);
                signUpBinding.signuppin.setVisibility(View.VISIBLE);
                break;
            case 2:
                signUpBinding.signupmain.setVisibility(View.VISIBLE);
                signUpBinding.signuppin.setVisibility(View.GONE);
                signUpBinding.edtphone.setError("Phone number might be incorrect");
                break;
            case 3:
                signUpBinding.signupmain.setVisibility(View.VISIBLE);
                signUpBinding.signuppin.setVisibility(View.GONE);
                signUpBinding.edtphone.setError("Verification limit exceeded for today");
                break;
            case 4:
                addUserToFirebase();
                break;
            case -1:
                signUpBinding.signupmain.setVisibility(View.VISIBLE);
                signUpBinding.signuppin.setVisibility(View.GONE);
                Toast.makeText(this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btncontinue:
                startAuth();
                break;
            case R.id.btnconfirm:
                authWithOTP();
                break;
            case R.id.btnresend:
                resendOTP();
                break;
        }
    }

    public void startAuth(){
        if(signUpBinding.edtphone.getText()==null||signUpBinding.edtemail.getText()==null||signUpBinding.edtfullname.getText()==null){
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show();
            return;
        }
        signUpViewModel.verifyPhoneNumber("+91"+signUpBinding.edtphone.getText().toString().trim());
    }

    public void authWithOTP(){
        if(signUpBinding.edtpin.getText().toString().trim().length()!=6){
            signUpBinding.edtpin.setError("Incorrect OTP Entered");
            return;
        }
        signUpViewModel.verifyWithOTP(signUpBinding.edtpin.getText().toString().trim());

    }

    private void resendOTP(){
        signUpViewModel.resendOTP("+91"+signUpBinding.edtphone.getText().toString().trim());
    }

    public void addUserToFirebase(){
        User user = new User(FirebaseAuth.getInstance().getUid(),signUpBinding.edtfullname.getText().toString().trim(),signUpBinding.edtemail.getText().toString().trim(),signUpBinding.edtphone.getText().toString().trim());
        firestoreDataViewModel.addUser(user);
    }

}
