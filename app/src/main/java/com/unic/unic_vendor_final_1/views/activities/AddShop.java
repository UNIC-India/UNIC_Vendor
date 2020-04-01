package com.unic.unic_vendor_final_1.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivityAddShopBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.AddShopViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddShop extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddShopBinding addShopBinding;
    private AddShopViewModel addShopViewModel;

    private static final int GALLERY_INTENT = 2;

    private Shop shop;
    private Bitmap imageBitmap;

    private boolean userWantsImage = true;
    private boolean imageSelectSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addShopBinding = ActivityAddShopBinding.inflate(getLayoutInflater());
        setContentView(addShopBinding.getRoot());
        addShopBinding.btnAddShopImage.setOnClickListener(this);
        addShopBinding.addShopStep2.setOnClickListener(this);
        addShopViewModel = ViewModelProviders.of(this).get(AddShopViewModel.class);
        addShopViewModel.getShopAddStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                statusUpdate(integer);
            }
        });
        addShopViewModel.getShop().observe(this, new Observer<Shop>() {
            @Override
            public void onChanged(Shop shop) {
                updateShop(shop);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_shop_step_2:
                if (addShopBinding.etAddShopName.getText().toString().trim() == null || (addShopBinding.etAddShopAddressLine1.getText().toString().trim() == null && addShopBinding.etAddShopAddressLine2.getText().toString().trim() == null && addShopBinding.etAddShopAddressLine3.getText().toString().trim() == null) || addShopBinding.etShopAddLocality.getText().toString().trim() == null) {
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                shop = new Shop(addShopBinding.etAddShopName.getText().toString().trim(),
                                        addShopBinding.etAddShopAddressLine1.getText().toString().trim() + " " +
                                        addShopBinding.etAddShopAddressLine2.getText().toString().trim() + " " +
                                        addShopBinding.etAddShopAddressLine3.getText().toString().trim(),
                                        addShopBinding.etShopAddLocality.getText().toString().trim());

                addShopViewModel.getShop().setValue(shop);

                addShopViewModel.saveShop();
                break;
            case R.id.btn_add_shop_image:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
                break;
        }
    }

    private void statusUpdate(int i){
        switch(i){
            case 1:
                Toast.makeText(this, "Shop saved", Toast.LENGTH_SHORT).show();
                addShopViewModel.setShopId();
                break;

            case 2:
                if(!userWantsImage){
                    Intent intent = new Intent(this,SetShopStructure.class);
                    intent.putExtra("shopId",shop.getId());
                    startActivity(intent);
                }
                else{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] data = baos.toByteArray();
                    addShopViewModel.saveShopImage(data);
                }
                break;
            case 3:
                addShopViewModel.setShopImageLink();
                break;

            case 4:
                Intent intent = new Intent(this,SetShopStructure.class);
                intent.putExtra("shopId",shop.getId());
                startActivity(intent);
                break;
        }
    }

    private void updateShop(Shop shop){
        this.shop = shop;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                addShopBinding.btnAddShopImage.setImageBitmap(bitmap);
                imageSelectSuccessful = true;
                imageBitmap = bitmap;

                // Log.d(TAG, String.valueOf(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }}
