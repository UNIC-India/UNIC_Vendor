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

import com.google.firebase.auth.FirebaseAuth;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivitySignUpBinding;
import com.unic.unic_vendor_final_1.datamodels.User;
import com.unic.unic_vendor_final_1.viewmodels.FirebasePhoneAuthViewModel;
import com.unic.unic_vendor_final_1.viewmodels.FirestoreDataViewModel;

import static com.unic.unic_vendor_final_1.commons.Helpers.enableDisableViewGroup;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignUpBinding signUpBinding;

    private FirebasePhoneAuthViewModel signUpViewModel;
    private FirestoreDataViewModel firestoreDataViewModel;

    private View coverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());

        updateUI(0);

        signUpViewModel = new ViewModelProvider(this).get(FirebasePhoneAuthViewModel.class);
        signUpViewModel.getStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                updateUI(integer);
            }
        });
        signUpViewModel.getCode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s!=null&&s.length()!=0) {
                    signUpBinding.edtpin.setText(s);
                    coverView = new View(SignUp.this);
                    coverView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    coverView.setBackgroundResource(R.color.gray_1);
                    coverView.setAlpha(0.5f);
                    ((ViewGroup)signUpBinding.getRoot()).addView(coverView);
                    signUpBinding.signUpProgressBar.setVisibility(View.VISIBLE);
                    signUpBinding.signUpProgressBar.bringToFront();
                    enableDisableViewGroup((ViewGroup)signUpBinding.getRoot(),false);
                }
            }
        });

        firestoreDataViewModel = new ViewModelProvider(this).get(FirestoreDataViewModel.class);
        firestoreDataViewModel.getUserStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == -1) {
                    updateUI(integer);
                } else if (integer == 1) {
                    enableDisableViewGroup((ViewGroup)signUpBinding.getRoot(),true);
                    ((ViewGroup)signUpBinding.getRoot()).removeView(coverView);
                    signUpBinding.signUpProgressBar.setVisibility(View.GONE);
                    signUpViewModel.updateInstanceId();
                    startActivity(new Intent(SignUp.this, UserHome.class));
                    finish();
                }
            }
        });

        signUpBinding.btnconfirm.setOnClickListener(this);
        signUpBinding.btncontinue.setOnClickListener(this);
        signUpBinding.btnresend.setOnClickListener(this);
    }

    private void updateUI(int code) {
        switch (code) {
            case 0:
                signUpBinding.signupmain.setVisibility(View.VISIBLE);
                signUpBinding.signuppin.setVisibility(View.GONE);
                break;
            case 1:
                enableDisableViewGroup((ViewGroup)signUpBinding.getRoot(),true);
                ((ViewGroup)signUpBinding.getRoot()).removeView(coverView);
                signUpBinding.signUpProgressBar.setVisibility(View.GONE);
                signUpBinding.signupmain.setVisibility(View.GONE);
                signUpBinding.signuppin.setVisibility(View.VISIBLE);
                break;
            case 2:
                enableDisableViewGroup((ViewGroup)signUpBinding.getRoot(),true);
                ((ViewGroup)signUpBinding.getRoot()).removeView(coverView);
                signUpBinding.signUpProgressBar.setVisibility(View.GONE);
                signUpBinding.signupmain.setVisibility(View.VISIBLE);
                signUpBinding.signuppin.setVisibility(View.GONE);
                signUpBinding.edtphone.setError("Phone number might be incorrect");
                break;
            case 3:
                enableDisableViewGroup((ViewGroup)signUpBinding.getRoot(),true);
                ((ViewGroup)signUpBinding.getRoot()).removeView(coverView);
                signUpBinding.signUpProgressBar.setVisibility(View.GONE);
                signUpBinding.signupmain.setVisibility(View.VISIBLE);
                signUpBinding.signuppin.setVisibility(View.GONE);
                signUpBinding.edtphone.setError("Verification limit exceeded for today");
                break;
            case 4:
                addUserToFirebase();
                break;
            case -1:
                enableDisableViewGroup((ViewGroup)signUpBinding.getRoot(),true);
                ((ViewGroup)signUpBinding.getRoot()).removeView(coverView);
                signUpBinding.signUpProgressBar.setVisibility(View.GONE);
                signUpBinding.signupmain.setVisibility(View.VISIBLE);
                signUpBinding.signuppin.setVisibility(View.GONE);
                Toast.makeText(this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btncontinue:
                coverView = new View(this);
                coverView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                coverView.setBackgroundResource(R.color.gray_1);
                coverView.setAlpha(0.5f);
                ((ViewGroup)signUpBinding.getRoot()).addView(coverView);
                signUpBinding.signUpProgressBar.setVisibility(View.VISIBLE);
                signUpBinding.signUpProgressBar.bringToFront();
                enableDisableViewGroup((ViewGroup)signUpBinding.getRoot(),false);
                startAuth();
                break;
            case R.id.btnconfirm:
                coverView = new View(this);
                coverView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                coverView.setBackgroundResource(R.color.gray_1);
                coverView.setAlpha(0.5f);
                ((ViewGroup)signUpBinding.getRoot()).addView(coverView);
                signUpBinding.signUpProgressBar.setVisibility(View.VISIBLE);
                signUpBinding.signUpProgressBar.bringToFront();
                enableDisableViewGroup((ViewGroup)signUpBinding.getRoot(),false);
                authWithOTP();
                break;
            case R.id.btnresend:
                resendOTP();
                break;
        }
    }

    public void startAuth() {
        if (signUpBinding.edtphone.getText() == null || signUpBinding.edtemail.getText() == null || signUpBinding.edtfullname.getText() == null) {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show();
            return;
        }
        signUpViewModel.verifyPhoneNumber("+91" + signUpBinding.edtphone.getText().toString().trim());
    }

    public void authWithOTP() {
        if (signUpBinding.edtpin.getText().toString().trim().length() != 6) {
            signUpBinding.edtpin.setError("Incorrect OTP Entered");
            return;
        }
        signUpViewModel.verifyWithOTP(signUpBinding.edtpin.getText().toString().trim());

    }

    private void resendOTP() {
        signUpViewModel.resendOTP("+91" + signUpBinding.edtphone.getText().toString().trim());
    }

    public void addUserToFirebase() {
        User user = new User(FirebaseAuth.getInstance().getUid(), signUpBinding.edtfullname.getText().toString().trim(), signUpBinding.edtemail.getText().toString().trim(), signUpBinding.edtphone.getText().toString().trim());
        firestoreDataViewModel.addUser(user);
    }


}
