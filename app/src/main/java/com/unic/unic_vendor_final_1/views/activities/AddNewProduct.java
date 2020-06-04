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
    private boolean imageSelectSuccessful = false;

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


        addNewProductViewModel.getProductStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                statusUpdate(integer);
            }
        });
        addNewProductViewModel.getProduct().observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                updateProduct(product);
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==addNewProductBinding.btnAddProductImage.getId()){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERY_INTENT);
        }

        else if(v.getId()==addNewProductBinding.btnConfirmProductAddition.getId()){
            if(userWantsImage){

            }
            
        }

    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_shop_step_3:

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
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                if(userWantsImage) {

                   if (addNewProductBinding.edtProductName.getText().toString().trim().length()==0 || (addNewProductBinding.edtProductCategory.getText().toString().trim().length()==0 && addNewProductBinding.edtProductCompany.getText().toString().trim().length()==0) || addNewProductBinding.edtProductPrice.getText().toString().trim().length()==0) {
                        Toast.makeText(AddNewProduct.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    coverView = new View(this);
                    coverView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    coverView.setBackgroundResource(R.color.gray_1);
                    coverView.setAlpha(0.5f);
                    ((ViewGroup)addNewProductBinding.getRoot()).addView(coverView);
                    addNewProductBinding.addShopProgressBar.setVisibility(View.VISIBLE);
                    addNewProductBinding.addShopProgressBar.bringToFront();
                    enableDisableViewGroup((ViewGroup)addNewProductBinding.getRoot(),false);

                    product = new Product(addNewProductBinding.edtProductCategory.getText().toString(), addNewProductBinding.edtProductCompany.getText().toString(),addNewProductBinding.edtProductName.getText().toString(),shopId,Double.parseDouble(addNewProductBinding.edtProductPrice.getText().toString()));

                    addNewProductViewModel.setProduct(product);

                    addNewProductViewModel.saveProduct();
                }
                break;
            case R.id.btn_add_shop_image:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
                break;
        }
    }*/

    private void statusUpdate(int i){
        switch(i){
            case 1:
                Toast.makeText(this, "Shop saved", Toast.LENGTH_SHORT).show();
                addNewProductViewModel.setProductId();
                break;

            case 2:
                if(!userWantsImage){
                    finish();
                }
                else{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] data = baos.toByteArray();
                    addNewProductViewModel.uploadImage(data);
                }
                break;
            case 3:
                addNewProductViewModel.setProductImageLink();
                break;

            case 4:
                enableDisableViewGroup((ViewGroup)addNewProductBinding.getRoot(),true);
                ((ViewGroup)addNewProductBinding.getRoot()).removeView(coverView);
                addNewProductBinding.addShopProgressBar.setVisibility(View.GONE);
                finish();
                break;

            case -4:
                enableDisableViewGroup((ViewGroup)addNewProductBinding.getRoot(),true);
                ((ViewGroup)addNewProductBinding.getRoot()).removeView(coverView);
                addNewProductBinding.addShopProgressBar.setVisibility(View.GONE);
        }
    }

    private void updateProduct(Product product){
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
                    Glide
                            .with(this)
                            .load(imageUri)
                            .into(addNewProductBinding.btnAddProductImage);
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

}
