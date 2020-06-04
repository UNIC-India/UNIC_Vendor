package com.unic.unic_vendor_final_1.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;

import com.unic.unic_vendor_final_1.databinding.ActivityAddShopBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.AddNewShopViewModel;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.LocationSelector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.unic.unic_vendor_final_1.commons.Helpers.buttonEffect;
import static com.unic.unic_vendor_final_1.commons.Helpers.enableDisableViewGroup;

public class AddShop extends AppCompatActivity implements View.OnClickListener{

    private AddNewShopViewModel addNewShopViewModel;
    private ActivityAddShopBinding addNewShopBinding;
    private Shop shop;

    private Uri imageUri,addressUri;

    private boolean userWantsImage = true;

    private View coverView;

    private int status;

    private static final int GALLERY_INTENT = 1001;
    private static final int CROP_IMAGE = 1002;
    private static final int LOCATION_SELECTOR = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addNewShopBinding = ActivityAddShopBinding.inflate(getLayoutInflater());

        setContentView(addNewShopBinding.getRoot());

        shop = new Shop();

        addNewShopViewModel = new ViewModelProvider(this).get(AddNewShopViewModel.class);

        addNewShopViewModel.getShop().setValue(shop);

        addNewShopViewModel.getStatus().observe(this, this::setStatus);
        addNewShopViewModel.getShop().observe(this,this::setShop);


        addNewShopBinding.addShopStep2.setOnClickListener(this);
        addNewShopBinding.addShopStep3.setOnClickListener(this);
        addNewShopBinding.btnAddShopImage.setOnClickListener(this);
        addNewShopBinding.addressView.setOnClickListener(this);
        addNewShopBinding.tvSelectAddress.setOnClickListener(this);
        buttonEffect(addNewShopBinding.addShopStep2);
        buttonEffect(addNewShopBinding.addShopStep3);



        addNewShopBinding.addShopPage1.setVisibility(View.VISIBLE);
        addNewShopBinding.addShopPage2.setVisibility(View.GONE);

    }

    public void nextStep(){
        shop.setName(addNewShopBinding.etAddShopName.getText().toString());
        addNewShopBinding.addShopPage1.setVisibility(View.GONE);
        addNewShopBinding.addShopPage2.setVisibility(View.VISIBLE);
    }

    public void setStatus(int status) {
        this.status = status;

        if(status==2){

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] data = baos.toByteArray();

                addNewShopViewModel.uploadShopImage(data);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else if (status==5){
            enableDisableViewGroup((ViewGroup)addNewShopBinding.getRoot(),true);
            ((ViewGroup)addNewShopBinding.getRoot()).removeView(coverView);
            addNewShopBinding.addShopProgressBar.setVisibility(View.GONE);
            Intent intent = new Intent(this, SetShopStructure.class);
            intent.putExtra("shopId",shop.getId());
            intent.putExtra("template",Integer.valueOf(1));
            startActivity(intent);
            finish();
        }
        else if(status==-1){
            enableDisableViewGroup((ViewGroup)addNewShopBinding.getRoot(),true);
            ((ViewGroup)addNewShopBinding.getRoot()).removeView(coverView);
            addNewShopBinding.addShopProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==addNewShopBinding.addShopStep2.getId()){
            if(addNewShopBinding.etAddShopName.getText().length()==0){
                addNewShopBinding.etAddShopName.setError("This field is mandatory");
            }
            else if(imageUri==null&&userWantsImage){
                AlertDialog.Builder builder = new AlertDialog.Builder(AddShop.this);
                builder.setMessage("Are you sure you don't want to set a shop image?")
                        .setPositiveButton("YES",(dialog, which) -> {
                            userWantsImage = false;
                            nextStep();
                        })
                        .setNegativeButton("GO BACK",(dialog, which) -> dialog.dismiss());
                Dialog dialog = builder.create();
                dialog.show();
            }
            else{
                addNewShopViewModel.getShopImageUri().setValue(imageUri);
                nextStep();
            }
        }
        else if(v.getId()==addNewShopBinding.btnAddShopImage.getId()){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERY_INTENT);
        }
        else if(v.getId()==addNewShopBinding.addressView.getId()||v.getId()==addNewShopBinding.tvAddShopAddress.getId()){

            Intent locationIntent = new Intent(AddShop.this, LocationSelector.class);
            if(addNewShopBinding.etShopAddCity.getText().toString().trim().length()>0){
                locationIntent.putExtra("type",Integer.valueOf(1));
                if (addNewShopBinding.etShopAddLocality.getText().toString().trim().length()>0)
                    locationIntent.putExtra("address",addNewShopBinding.etShopAddLocality.getText().toString().trim()+" "+addNewShopBinding.etShopAddCity.getText().toString().trim());
                else
                    locationIntent.putExtra("address",addNewShopBinding.etShopAddCity.getText().toString().trim());
            }
            else
                locationIntent.putExtra("type",Integer.valueOf(0));
            startActivityForResult(locationIntent,LOCATION_SELECTOR);
        }
        else if (v.getId()==addNewShopBinding.addShopStep3.getId()){
            boolean done = true;

            if(addNewShopBinding.etShopAddCity.getText().length()==0){
                addNewShopBinding.etShopAddCity.setError("This field is mandatory");
                done = false;
            }
            else
                shop.setCity(addNewShopBinding.etShopAddCity.getText().toString());

            if (addNewShopBinding.etShopAddLocality.getText().length()==0){
                addNewShopBinding.etShopAddLocality.setError("This field is mandatory");
                done = false;
            }
            else
                shop.setLocality(addNewShopBinding.etShopAddLocality.getText().toString());

            if(addNewShopBinding.etAddShopAddressLine1.getText().length()==0&&addNewShopBinding.etAddShopAddressLine2.getText().length()==0){
                addNewShopBinding.etAddShopAddressLine1.setError("This filed is mandatory");
                done = false;
            }
            else
                shop.setAddress(addNewShopBinding.etAddShopAddressLine1.getText().toString()+", "+addNewShopBinding.etAddShopAddressLine2.getText().toString());

            if(addressUri==null){
                done = false;
                Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show();
            }

            if(done){
                addNewShopViewModel.getShop().setValue(shop);
                coverView = new View(this);
                coverView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                coverView.setBackgroundResource(R.color.gray_1);
                coverView.setAlpha(0.5f);
                ((ViewGroup)addNewShopBinding.getRoot()).addView(coverView);
                addNewShopBinding.addShopProgressBar.setVisibility(View.VISIBLE);
                addNewShopBinding.addShopProgressBar.bringToFront();
                addNewShopViewModel.uploadShopData();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data!=null) {

            switch (requestCode){
                case GALLERY_INTENT:
                    Uri uri = data.getData();
                    cropImage(uri);
                    break;
                case LOCATION_SELECTOR:
                    Map<String,Double> location = new HashMap<>();
                    location.put("latitude",data.getDoubleExtra("latitude",0.0));
                    location.put("longitude",data.getDoubleExtra("longitude",0.0));
                    shop.setLocation(location);
                    addNewShopViewModel.getShop().setValue(shop);
                    addressUri = data.getData();
                    addNewShopViewModel.getAddressImageUri().setValue(addressUri);
                    addNewShopBinding.addressView.setAlpha(1f);
                    Glide
                            .with(this)
                            .load(addressUri)
                            .into(addNewShopBinding.addressView);
                    addNewShopBinding.tvSelectAddress.setVisibility(View.GONE);
                    break;
                case CROP_IMAGE:
                    Glide
                            .with(this)
                            .load(imageUri)
                            .into(addNewShopBinding.btnAddShopImage);

                    addNewShopViewModel.getShopImageUri().setValue(imageUri);
            }

        }
    }

    private void cropImage(Uri uri){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("return-data", true);
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "new-shop-image-" + ".jpg");
        imageUri = Uri.fromFile(file);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cropIntent.putExtra("output",imageUri);
        startActivityForResult(cropIntent, CROP_IMAGE);
        Toast.makeText(this, "Cropping image", Toast.LENGTH_SHORT).show();
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
