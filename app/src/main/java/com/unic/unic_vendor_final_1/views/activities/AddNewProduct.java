package com.unic.unic_vendor_final_1.views.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.AddProductImageAdapter;
import com.unic.unic_vendor_final_1.databinding.ActivityAddNewProductBinding;
import com.unic.unic_vendor_final_1.datamodels.Product;
import com.unic.unic_vendor_final_1.viewmodels.AddNewProductViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.unic.unic_vendor_final_1.commons.Helpers.enableDisableViewGroup;

public class AddNewProduct extends AppCompatActivity implements View.OnClickListener {
    private String shopId;
    private AddNewProductViewModel addNewProductViewModel;
    private ActivityAddNewProductBinding addNewProductBinding;
    private Product product;
    private View coverView;
    private AddProductImageAdapter productImageAdapter;

    private static final int GALLERY_INTENT = 1001;
    private static final int CROP_IMAGE = 1002;
    public static final int ADD_PRODUCTS = 5010;

    private List<Uri> imageUris;
    private Uri currentImageUri;

    private boolean userWantsImage = true;

    private int currentPosition = 0;

    static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NotNull Rect outRect, @NotNull View view,
                                   RecyclerView parent, @NotNull RecyclerView.State state) {

            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0;
            } else {
                outRect.left = space;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNewProductBinding=ActivityAddNewProductBinding.inflate(getLayoutInflater());
        addNewProductViewModel = new ViewModelProvider(this).get(AddNewProductViewModel.class);
        setContentView(addNewProductBinding.getRoot());

        shopId = getIntent().getStringExtra("shopId");
        assert shopId != null;
        addNewProductBinding.btnAddProductImage.setOnClickListener(this);
        addNewProductBinding.btnConfirmProductAddition.setOnClickListener(this);
        addNewProductViewModel.setShopId(shopId);
        addNewProductViewModel.getProductStatus().observe(this, this::statusUpdate);
        addNewProductViewModel.getProduct().observe(this,this::setProduct);

        productImageAdapter = new AddProductImageAdapter(imageUris,this);
        addNewProductBinding.addProductImageSlider.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        addNewProductBinding.addProductImageSlider.setAdapter(productImageAdapter);
        addNewProductBinding.addProductImageSlider.addItemDecoration(new SpacesItemDecoration((int)dpToPx(20)));

        addNewProductViewModel.getImageUploadStatus().observe(this,aBoolean -> {
           if(aBoolean)
               uploadImage(currentPosition);
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
            if(userWantsImage&&imageUris==null){
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
                if(imageUris!=null){

                    uploadImage(currentPosition);

                }
                else {
                    setResult(RESULT_OK);
                    finish();
                }
                break;

            case 3:
                addNewProductViewModel.setProductImageLinkOnFirebase();
                break;

            case 4:
                enableDisableViewGroup((ViewGroup)addNewProductBinding.getRoot(),true);
                ((ViewGroup)addNewProductBinding.getRoot()).removeView(coverView);
                addNewProductBinding.addShopProgressBar.setVisibility(View.GONE);
                setResult(RESULT_OK);
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
            if(Double.parseDouble(addNewProductBinding.edtProductDiscount.getText().toString())>=100||Double.parseDouble(addNewProductBinding.edtProductDiscount.getText().toString())<=0){
                Toast.makeText(this, "Discount can only be between 0.01 & 99.99", Toast.LENGTH_SHORT).show();
                done=false;
            }
            product.setDiscount(addNewProductBinding.edtProductDiscount.getText().toString().isEmpty()?0.0:Double.parseDouble(addNewProductBinding.edtProductDiscount.getText().toString()));
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

                    addNewProductBinding.addProductImageSlider.setVisibility(View.VISIBLE);
                    addNewProductBinding.btnAddProductImage.setVisibility(View.INVISIBLE);

                    imageUris.add(currentImageUri);

                    productImageAdapter.setImageUris(imageUris);
//                    productImageAdapter.notifyDataSetChanged();

            }

        }
    }

    private void cropImage(Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("return-data", true);

        if (imageUris == null)
            imageUris = new ArrayList<>();

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "new-product-image-" + Integer.valueOf(imageUris.size()).toString() + ".jpg");

        currentImageUri = Uri.fromFile(file);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);
        cropIntent.putExtra("output", currentImageUri);

        if (cropIntent.resolveActivity(getPackageManager()) != null) {
            Toast.makeText(this, "Cropping...", Toast.LENGTH_SHORT).show();
            startActivityForResult(cropIntent, CROP_IMAGE);
        }
        else {
            imageUris.add(uri);
            productImageAdapter.setImageUris(imageUris);
        }
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
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

        if(image.getWidth()>=300||image.getHeight()>=300)
            return image;

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void addImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }

    private void uploadImage(int position){

        addNewProductViewModel.getImageUploadStatus().setValue(false);

        if(position == imageUris.size()) {
            statusUpdate(3);
            return;
        }

        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUris.get(position));
            bitmap = getResizedBitmap(bitmap, 300);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] stream = baos.toByteArray();

            addNewProductViewModel.uploadImage(stream,currentPosition++);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private float dpToPx(int dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

}
