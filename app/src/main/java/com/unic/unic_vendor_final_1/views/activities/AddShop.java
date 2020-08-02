package com.unic.unic_vendor_final_1.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.ContentResolver;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.GeoPoint;
import com.theartofdev.edmodo.cropper.CropImage;
import com.unic.unic_vendor_final_1.R;

import com.unic.unic_vendor_final_1.databinding.ActivityAddShopBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.AddNewShopViewModel;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.LocationSelector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
        addNewShopBinding.btnAddShopImage.setOnClickListener(this);
        addNewShopBinding.shopLocationSelect.setOnClickListener(this);
        addNewShopBinding.btnCancel.setOnClickListener(this);

    }

    public void setStatus(int status) {
        this.status = status;

        if(status==2){

            if(imageUri!=null&&userWantsImage) {

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] data = baos.toByteArray();

                    addNewShopViewModel.uploadShopImage(data);
                } catch (IOException e) {
                    e.printStackTrace();
                    setStatus(5);
                }
            }
            else {
                setStatus(5);
            }
        }
        else if (status==5){
            addNewShopViewModel.buildSubscribeLink();
        }
        else if(status==6){
            enableDisableViewGroup(addNewShopBinding.getRoot(),true);
            ((ViewGroup)addNewShopBinding.getRoot()).removeView(coverView);
            addNewShopBinding.addShopProgressBar.setVisibility(View.GONE);
            Intent intent = new Intent(this, SetShopStructure.class);
            intent.putExtra("shopId",shop.getId());
            intent.putExtra("template",Integer.valueOf(1));
            startActivity(intent);
            finish();
        }
        else if(status==-1){
            enableDisableViewGroup(addNewShopBinding.getRoot(),true);
            ((ViewGroup)addNewShopBinding.getRoot()).removeView(coverView);
            addNewShopBinding.addShopProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==addNewShopBinding.btnAddShopImage.getId()){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERY_INTENT);
        }
        else if(v.getId()==addNewShopBinding.shopLocationSelect.getId()){

            Intent locationIntent = new Intent(AddShop.this, LocationSelector.class);
            if(addNewShopBinding.etAddShopCity.getText().toString().trim().length()>0){
                locationIntent.putExtra("type",Integer.valueOf(1));
                if (addNewShopBinding.etAddShopLocality.getText().toString().trim().length()>0)
                    locationIntent.putExtra("address",addNewShopBinding.etAddShopLocality.getText().toString().trim()+" "+addNewShopBinding.etAddShopCity.getText().toString().trim());
                else
                    locationIntent.putExtra("address",addNewShopBinding.etAddShopCity.getText().toString().trim());
            }
            else
                locationIntent.putExtra("type",Integer.valueOf(0));
            startActivityForResult(locationIntent,LOCATION_SELECTOR);
        }
        else if (v.getId()==addNewShopBinding.addShopStep2.getId()){
            boolean done = true;

            if(addNewShopBinding.etAddShopName.getText().length()==0){
                addNewShopBinding.etAddShopName.setError("This field is mandatory");
                done = false;
            }
            else {
                shop.setName(addNewShopBinding.etAddShopName.getText().toString());
            }

            if(imageUri==null&&userWantsImage){
                done = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddShop.this);
                builder.setMessage("Are you sure you don't want to set a shop image?")
                        .setPositiveButton("YES",(dialog, which) -> {
                            userWantsImage = false;
                        })
                        .setNegativeButton("GO BACK",(dialog, which) -> dialog.dismiss());
                Dialog dialog = builder.create();
                dialog.show();
            }
            else{
                addNewShopViewModel.getShopImageUri().setValue(imageUri);
            }

            if(addNewShopBinding.etAddShopCity.getText().length()==0){
                addNewShopBinding.etAddShopCity.setError("This field is mandatory");
                done = false;
            }
            else
                shop.setCity(addNewShopBinding.etAddShopCity.getText().toString());

            if (addNewShopBinding.etAddShopLocality.getText().length()==0){
                addNewShopBinding.etAddShopLocality.setError("This field is mandatory");
                done = false;
            }
            else
                shop.setLocality(addNewShopBinding.etAddShopLocality.getText().toString());

            if(addNewShopBinding.etAddShopAddressLine1.getText().length()==0){
                addNewShopBinding.etAddShopAddressLine1.setError("This filed is mandatory");
                done = false;
            }
            else
                shop.setAddress(addNewShopBinding.etAddShopAddressLine1.getText().toString());

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
                addNewShopBinding.getRoot().addView(coverView);
                addNewShopBinding.addShopProgressBar.setVisibility(View.VISIBLE);
                addNewShopBinding.addShopProgressBar.bringToFront();
                addNewShopViewModel.uploadShopData();
            }
        }

        else if (v.getId() == addNewShopBinding.btnCancel.getId()) {
            finish();
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
                    GeoPoint geoPoint = new GeoPoint(data.getDoubleExtra("latitude",0.0),data.getDoubleExtra("longitude",0.0));
                    shop.setGeoLocation(geoPoint);
                    shop.setLocation(location);
                    addNewShopViewModel.getShop().setValue(shop);
                    addressUri = data.getData();
                    addNewShopViewModel.getAddressImageUri().setValue(addressUri);
                    addNewShopBinding.shopLocationSelect.setAlpha(1f);
                    Glide
                            .with(this)
                            .load(addressUri)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(new RequestOptions().fitCenter())
                            .into(addNewShopBinding.shopLocationSelect);
                    break;
                case CROP_IMAGE:
                    Glide
                            .with(this)
                            .load(imageUri)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(new RequestOptions().fitCenter())
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
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "new-shop-image" + ".jpg");

        imageUri = Uri.fromFile(file);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cropIntent.putExtra("output",imageUri);

        if(cropIntent.resolveActivity(getPackageManager())!=null) {
            Toast.makeText(this, "Cropping image", Toast.LENGTH_SHORT).show();
            startActivityForResult(cropIntent, CROP_IMAGE);
        }
        else {
            Glide
                    .with(this)
                    .load(uri)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(new RequestOptions().fitCenter())
                    .into(addNewShopBinding.btnAddShopImage);

            addNewShopViewModel.getShopImageUri().setValue(uri);
        }

    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void copy(Uri src, File dst) throws IOException {
        InputStream in = this.getContentResolver().openInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
}
