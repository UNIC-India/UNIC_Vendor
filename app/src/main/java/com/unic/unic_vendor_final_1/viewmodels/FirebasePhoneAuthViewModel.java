package com.unic.unic_vendor_final_1.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.unic.unic_vendor_final_1.commons.FirebaseRepository;

public class FirebasePhoneAuthViewModel extends ViewModel {

    public FirebasePhoneAuthViewModel() {
    }

    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private MutableLiveData<Integer> status = new MutableLiveData<>();

    private MutableLiveData<String> SMSCode = new MutableLiveData<>();

    private String mVerificationId;

    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private FirebaseRepository firebaseRepository = new FirebaseRepository();

    public FirebasePhoneAuthViewModel(Context context) {
        this.context = context;
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            signInWithPhoneAuthCredential(phoneAuthCredential);
            SMSCode.setValue(phoneAuthCredential.getSmsCode());

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException)
                status.setValue(2);
            else if (e instanceof FirebaseTooManyRequestsException)
                status.setValue(3);
            else
                status.setValue(-1);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            status.setValue(1);
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

            // ...
        }
    };

    public void verifyPhoneNumber(String phoneNumber) {
        firebaseRepository.startPhoneNumberVerification(phoneNumber, mCallbacks);
    }

    public void verifyWithOTP(String code) {
        signInWithOTP(mVerificationId, code);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        status.setValue(4);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.setValue(-1);
                    }
                });
    }

    private void signInWithOTP(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    public void resendOTP(String phoneNumber) {
        firebaseRepository.resendVerificationCode(phoneNumber, mResendToken, mCallbacks);
    }

    public LiveData<Integer> getStatus() {
        return status;
    }

    public LiveData<String> getCode() {
        return SMSCode;
    }

    public void updateInstanceId(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                firebaseRepository.setInstanceId(mAuth.getUid(),token);
            }
        });
    }

    public void checkUserExists(String phoneNo){
        firebaseRepository.checkUser(phoneNo)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size()!=0){
                    status.setValue(5);
                }
                else status.setValue(-2);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                status.setValue(-2);
            }
        });
    }

}
