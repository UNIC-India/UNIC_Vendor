package com.unic.unic_vendor_final_1.views.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
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
import com.unic.unic_vendor_final_1.views.helpers.LocationSelector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddShop extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddShopBinding addShopBinding;
    private AddShopViewModel addShopViewModel;

    private static final int GALLERY_INTENT = 1001;
    private static final int LOCATION_SELECTOR = 1002;

    private Shop shop;
    private Bitmap imageBitmap;

    private boolean userWantsImage = true;
    private boolean imageSelectSuccessful = false;

    private Map<String,Double> location = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addShopBinding = ActivityAddShopBinding.inflate(getLayoutInflater());
        setContentView(addShopBinding.getRoot());
        addShopBinding.btnAddShopImage.setOnClickListener(this);
        addShopBinding.addShopStep2.setOnClickListener(this);
        addShopBinding.shopLocationSelect.setOnClickListener(this);
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

                if(imageBitmap==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setMessage("No shop Image selected. Do you want to select one?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    userWantsImage = true;
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(intent,GALLERY_INTENT);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    userWantsImage = false;
                                }
                            });
                }
                if(userWantsImage) {

                    if (addShopBinding.etAddShopName.getText().toString().trim().length()==0 || (addShopBinding.etAddShopAddressLine1.getText().toString().trim().length()==0 && addShopBinding.etAddShopAddressLine2.getText().toString().trim().length()==0) || addShopBinding.etShopAddLocality.getText().toString().trim().length()==0||addShopBinding.etShopAddCity.getText().toString().trim().length()==0) {
                        Toast.makeText(AddShop.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(location.size()==0){
                        Toast.makeText(AddShop.this, "Please select location on map", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    shop = new Shop(addShopBinding.etAddShopName.getText().toString().trim(),
                            addShopBinding.etAddShopAddressLine1.getText().toString().trim() + " " +
                                    addShopBinding.etAddShopAddressLine2.getText().toString().trim(),
                            addShopBinding.etShopAddLocality.getText().toString().trim(),
                            addShopBinding.etShopAddCity.getText().toString().trim(),location);

                    addShopViewModel.getShop().setValue(shop);

                    addShopViewModel.saveShop();
                }
                break;
            case R.id.btn_add_shop_image:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
                break;
            case R.id.shop_location_select:
                Intent locationIntent = new Intent(AddShop.this, LocationSelector.class);
                if(addShopBinding.etShopAddCity.getText().toString().trim().length()>0){
                    locationIntent.putExtra("type",Integer.valueOf(1));
                    if (addShopBinding.etShopAddLocality.getText().toString().trim().length()>0)
                        locationIntent.putExtra("address",addShopBinding.etShopAddLocality.getText().toString().trim()+" "+addShopBinding.etShopAddCity.getText().toString().trim());
                    else
                        locationIntent.putExtra("address",addShopBinding.etShopAddCity.getText().toString().trim());
                }
                else
                    locationIntent.putExtra("type",Integer.valueOf(0));
                startActivityForResult(locationIntent,LOCATION_SELECTOR);
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
                Intent intent = new Intent(this, SetShopStructure.class);
                intent.putExtra("shopId",shop.getId());
                intent.putExtra("template",Integer.valueOf(1));
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

        if(resultCode == RESULT_OK && data!=null) {

            switch (requestCode){
                case GALLERY_INTENT:
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
                    break;
                case LOCATION_SELECTOR:
                    location.put("latitude",data.getDoubleExtra("latitude",0.0));
                    location.put("longitude",data.getDoubleExtra("longitude",0.0));
                    break;
            }

        }
    }}
