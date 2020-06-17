package com.unic.unic_vendor_final_1.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivityAddNewProductBinding;
import com.unic.unic_vendor_final_1.datamodels.Product;
import com.unic.unic_vendor_final_1.viewmodels.AddNewProductViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.unic.unic_vendor_final_1.commons.Helpers.buttonEffect;
import static com.unic.unic_vendor_final_1.commons.Helpers.enableDisableViewGroup;

public class AddNewProduct extends AppCompatActivity implements View.OnClickListener {
    private String shopId;
    private AddNewProductViewModel addNewProductViewModel;
    private ActivityAddNewProductBinding addNewProductBinding;
    private Product product;


    private View coverView;

    private static final int GALLERY_INTENT = 1001;
    private static final int CROP_IMAGE = 1002;

    private Uri imageUri;

    private boolean userWantsImage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNewProductBinding=ActivityAddNewProductBinding.inflate(getLayoutInflater());
        addNewProductViewModel = new ViewModelProvider(this).get(AddNewProductViewModel.class);
        setContentView(addNewProductBinding.getRoot());

        shopId = getIntent().getStringExtra("shopId");
        Toast.makeText(AddNewProduct.this, shopId, Toast.LENGTH_SHORT).show();
        assert shopId != null;
        addNewProductBinding.btnAddProductImage.setOnClickListener(this);
        addNewProductBinding.btnConfirmProductAddition.setOnClickListener(this);
        addNewProductViewModel.setShopId(shopId);


        addNewProductViewModel.getProductStatus().observe(this, this::statusUpdate);
        addNewProductViewModel.getProduct().observe(this,this::setProduct);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==addNewProductBinding.btnAddProductImage.getId()){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERY_INTENT);
        }

        else if(v.getId()==addNewProductBinding.btnConfirmProductAddition.getId()){
            if(userWantsImage&&imageUri==null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("No product image selected. Do you want to select one?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            userWantsImage = true;
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,GALLERY_INTENT);
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            userWantsImage = false;
                            saveProduct();
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            else {
                saveProduct();
            }

        }

    }

    private void statusUpdate(int i){
        switch(i){

            case 2:
                if(imageUri!=null){

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                        bitmap = getResizedBitmap(bitmap, 300);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                        byte[] stream = baos.toByteArray();

                        addNewProductViewModel.uploadImage(stream);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else
                    finish();
                break;

            case 4:
                enableDisableViewGroup((ViewGroup)addNewProductBinding.getRoot(),true);
                ((ViewGroup)addNewProductBinding.getRoot()).removeView(coverView);
                addNewProductBinding.addShopProgressBar.setVisibility(View.GONE);
                finish();
                break;

            case -1:
                enableDisableViewGroup((ViewGroup)addNewProductBinding.getRoot(),true);
                ((ViewGroup)addNewProductBinding.getRoot()).removeView(coverView);
                addNewProductBinding.addShopProgressBar.setVisibility(View.GONE);
        }
    }

    public void saveProduct(){

        product = new Product(shopId);

        boolean done = true;

        if(addNewProductBinding.edtProductName.getText().length()==0){
            addNewProductBinding.edtProductName.setError("This field is mandatory");
            done = false;
        }
        else {
            product.setName(addNewProductBinding.edtProductName.getText().toString());
            List<String> keys = new ArrayList<>();
            for(String key : product.getName().split(" ")) {
                if (key.length() > 1)
                    keys.add(key.substring(0, 2).toLowerCase());
            }
            product.setNameKeywords(keys);

        }

        if(addNewProductBinding.edtProductCompany.getText().length()==0){
            addNewProductBinding.edtProductCompany.setError("This field is mandatory");
            done = false;
        }
        else
            product.setCompany(addNewProductBinding.edtProductCompany.getText().toString());

        if(addNewProductBinding.edtProductCategory.getText().length()==0){
            addNewProductBinding.edtProductCategory.setError("This field is mandatory");
            done = false;
        }
        else
            product.setCategory(addNewProductBinding.edtProductCategory.getText().toString());

        if(addNewProductBinding.edtProductPrice.getText().length()==0){
            addNewProductBinding.edtProductPrice.setError("This field is mandatory");
            done = false;
        }
        else
            product.setPrice(Double.parseDouble(addNewProductBinding.edtProductPrice.getText().toString()));

        if(addNewProductBinding.edtProductTags.getText().length()!=0)
            product.setTags(addNewProductBinding.edtProductTags.getText().toString());

        if(addNewProductBinding.edtProductSubCategory.getText().length()!=0){
            product.setSubcategory(addNewProductBinding.edtProductSubCategory.getText().toString());
        }

        if(addNewProductBinding.edtProductDesc.getText().length()!=0)
            product.setDesc(addNewProductBinding.edtProductDesc.getText().toString());
        if(addNewProductBinding.edtProductDiscount.getText().length()!=0)
            product.setDiscount(Double.parseDouble(addNewProductBinding.edtProductDiscount.getText().toString()));
        if(addNewProductBinding.edtProductExtrainfo1.getText().length()!=0)
            product.setExtraInfo1(addNewProductBinding.edtProductExtrainfo1.getText().toString());
        if(addNewProductBinding.edtProductExtrainfo2.getText().length()!=0)
            product.setExtraInfo2(addNewProductBinding.edtProductExtrainfo2.getText().toString());

        if(done){
            addNewProductViewModel.setProduct(product);
            coverView = new View(this);
            coverView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            coverView.setBackgroundResource(R.color.gray_1);
            coverView.setAlpha(0.5f);
            addNewProductBinding.getRoot().addView(coverView);
            addNewProductBinding.addShopProgressBar.setVisibility(View.VISIBLE);
            addNewProductBinding.addShopProgressBar.bringToFront();
            addNewProductViewModel.saveProduct();
        }

    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data!=null) {

            switch (requestCode){
                case GALLERY_INTENT:
                    Uri uri = data.getData();

                    cropImage(uri);
                    break;

                case CROP_IMAGE:

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);

                        addNewProductBinding.btnAddProductImage.setImageBitmap(bitmap);

                        /*Glide
                                .with(this)
                                .load(imageUri)
                                .into(addNewProductBinding.btnAddProductImage);*/

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


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
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "new-product-image" + ".jpg");
        imageUri = Uri.fromFile(file);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cropIntent.putExtra("output",imageUri);
        startActivityForResult(cropIntent, CROP_IMAGE);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
