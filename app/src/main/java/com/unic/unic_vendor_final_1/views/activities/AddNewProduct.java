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
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivityAddNewProductBinding;
import com.unic.unic_vendor_final_1.datamodels.Product;
import com.unic.unic_vendor_final_1.viewmodels.AddNewProductViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.unic.unic_vendor_final_1.commons.Helpers.enableDisableViewGroup;

public class AddNewProduct extends AppCompatActivity implements View.OnClickListener {
    private String shopId;
    private AddNewProductViewModel addNewProductViewModel;
    private ActivityAddNewProductBinding addNewProductBinding;
    private Product product;

    private View coverView;

    private static final int GALLERY_INTENT = 1001;

    private Bitmap imageBitmap;
    private AlertDialog dialog;

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
        addNewProductBinding.btnAddShopImage.setOnClickListener(this);
        addNewProductBinding.addShopStep3.setOnClickListener(this);
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
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
    }

    private void statusUpdate(int i){
        switch(i){
            case 1:
                Toast.makeText(this, "Shop saved", Toast.LENGTH_SHORT).show();
                addNewProductViewModel.setProductId();
                break;

            case 2:
                if(!userWantsImage){
                    Intent intent = new Intent(this, UserHome.class);
                    Toast.makeText(AddNewProduct.this, "Product Added", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
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
                Intent intent = new Intent(this, UserHome.class);
                Toast.makeText(AddNewProduct.this, "Product Added", Toast.LENGTH_SHORT).show();
                startActivity(intent);
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
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        addNewProductBinding.btnAddShopImage.setImageBitmap(bitmap);
                        imageSelectSuccessful = true;
                        imageBitmap = bitmap;

                        // Log.d(TAG, String.valueOf(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

            }

        }
    }



}
