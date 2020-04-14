package com.unic.unic_vendor_final_1.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Notification;

public class SetNotification extends AppCompatActivity {
    Button btnSend, btnCancel;
    EditText etTitle,etMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        etTitle=findViewById(R.id.etTitle);
        etMessage=findViewById(R.id.etMessage);
        btnSend=findViewById(R.id.btnsend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db=FirebaseFirestore.getInstance();
                Notification n=new Notification();
                n.setTitle(etTitle.getText().toString());
                n.setDescription(etMessage.getText().toString());
                db.collection("notifications").add(n);
            }
        });
    }

}
