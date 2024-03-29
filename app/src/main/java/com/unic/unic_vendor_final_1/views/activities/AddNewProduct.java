package com.unic.unic_vendor_final_1.views.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
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

public class AddNewProduct extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private String shopId;
    private AddNewProductViewModel addNewProductViewModel;
    private ActivityAddNewProductBinding addNewProductBinding;
    private Product product;
    private View coverView;
    private AddProductImageAdapter productImageAdapter;
    private ArrayAdapter companyAdapter,categoryAdapter;

    private static final int GALLERY_INTENT = 1001;
    private static final int CROP_IMAGE = 1002;
    public static final int ADD_PRODUCTS = 5010;

    private List<Uri> imageUris;
    private Uri currentImageUri;
    private List<String> companies;
    private List<String> categories;

    private boolean userWantsImage = true;

    private int currentPosition = 0;

    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener = state -> {
        if(state.installStatus() == InstallStatus.DOWNLOADED) {
            createAppUpdateReadySnackbar();
        }
    };

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

        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.registerListener(installStateUpdatedListener);

        shopId = getIntent().getStringExtra("shopId");
        assert shopId != null;
        addNewProductBinding.btnAddProductImage.setOnClickListener(this);
        addNewProductBinding.btnConfirmProductAddition.setOnClickListener(this);
        addNewProductBinding.productCompanySpinner.setOnItemSelectedListener(this);
        addNewProductBinding.productCategorySpinner.setOnItemSelectedListener(this);
        addNewProductBinding.addProductCompany.setOnClickListener(this);
        addNewProductBinding.addProductCategory.setOnClickListener(this);
        addNewProductViewModel.setShopId(shopId);
        addNewProductViewModel.getProductStatus().observe(this, this::statusUpdate);
        addNewProductViewModel.getProduct().observe(this,this::setProduct);
        addNewProductViewModel.getShopExtras();
        addNewProductViewModel.getCompanies().observe(this,this::setCompanies);
        addNewProductViewModel.getCategories().observe(this,this::setCategories);
        productImageAdapter = new AddProductImageAdapter(imageUris,this);
        addNewProductBinding.addProductImageSlider.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        addNewProductBinding.addProductImageSlider.setAdapter(productImageAdapter);
        addNewProductBinding.addProductImageSlider.addItemDecoration(new SpacesItemDecoration((int)dpToPx(20)));

        addNewProductViewModel.getImageUploadStatus().observe(this,aBoolean -> {
           if(aBoolean)
               uploadImage(currentPosition);
        });
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
            product.setName(addNewProductBinding.edtProductName.getText().toString().trim());
        }

        if(addNewProductBinding.edtProductCompany.getText().length()==0){
            addNewProductBinding.edtProductCompany.setError("This field is mandatory");
            done = false;
        }
        else
            product.setCompany(addNewProductBinding.edtProductCompany.getText().toString().trim());

        if(addNewProductBinding.edtProductCategory.getText().length()==0){
            addNewProductBinding.edtProductCategory.setError("This field is mandatory");
            done = false;
        }
        else
            product.setCategory(addNewProductBinding.edtProductCategory.getText().toString().trim());

        if(addNewProductBinding.edtProductPrice.getText().length()==0){
            addNewProductBinding.edtProductPrice.setError("This field is mandatory");
            done = false;
        }
        else
            product.setPrice(Double.parseDouble(addNewProductBinding.edtProductPrice.getText().toString().trim()));

        if(addNewProductBinding.edtProductTags.getText().length()!=0)
            product.setTags(addNewProductBinding.edtProductTags.getText().toString().trim());

        if(addNewProductBinding.edtProductSubCategory.getText().length()!=0){
            product.setSubcategory(addNewProductBinding.edtProductSubCategory.getText().toString().trim());
        }

        if(addNewProductBinding.edtProductDesc.getText().length()!=0)
            product.setDesc(addNewProductBinding.edtProductDesc.getText().toString().trim());
        if(addNewProductBinding.edtProductDiscount.getText().length()!=0)
            if(Double.parseDouble(addNewProductBinding.edtProductDiscount.getText().toString().trim())>=100||Double.parseDouble(addNewProductBinding.edtProductDiscount.getText().toString().trim())<=0){
                Toast.makeText(this, "Discount can only be between 0.01 & 99.99", Toast.LENGTH_SHORT).show();
                done=false;
            }
            product.setDiscount(addNewProductBinding.edtProductDiscount.getText().toString().trim().isEmpty()?0.0:Double.parseDouble(addNewProductBinding.edtProductDiscount.getText().toString().trim()));
        if(addNewProductBinding.edtProductExtrainfo1.getText().length()!=0)
            product.setExtraInfo1(addNewProductBinding.edtProductExtrainfo1.getText().toString().trim());
        if(addNewProductBinding.edtProductExtrainfo2.getText().length()!=0)
            product.setExtraInfo2(addNewProductBinding.edtProductExtrainfo2.getText().toString().trim());

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

    public void setCompanies(List<String> companies) {
        this.companies = companies == null ? new ArrayList<>() : companies;
        this.companies.add(0,"--SELECT--");

        companyAdapter = new ArrayAdapter(this,R.layout.simple_textbox,this.companies);

        addNewProductBinding.productCompanySpinner.setAdapter(companyAdapter);

    }

    public void setCategories(List<String> categories) {
        this.categories = categories == null ? new ArrayList<>() : categories;
        this.categories.add(0,"--SELECT--");

        categoryAdapter = new ArrayAdapter(this,R.layout.simple_textbox,this.categories);

        addNewProductBinding.productCategorySpinner.setAdapter(categoryAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data!=null) {

            switch (requestCode){
                case GALLERY_INTENT:
                    Uri uri = data.getData();

                    addNewProductBinding.addProductImageSlider.setVisibility(View.VISIBLE);
                    addNewProductBinding.btnAddProductImage.setVisibility(View.INVISIBLE);

                    if (imageUris == null)
                        imageUris = new ArrayList<>();

                    imageUris.add(uri);
                    productImageAdapter.setImageUris(imageUris);
                    break;

                case CROP_IMAGE:



                    imageUris.add(currentImageUri);

                    productImageAdapter.setImageUris(imageUris);
//                    productImageAdapter.notifyDataSetChanged();

            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.product_company_spinner) {
            addNewProductBinding.edtProductCompany.setText(companies == null || companies.size() == 1 || position == 0 ? "" : companies.get(position));
        }

        else if(parent.getId() == R.id.product_category_spinner) {
            addNewProductBinding.edtProductCategory.setText(categories == null || categories.size() == 1 || position == 0 ? "" : categories.get(position));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(parent.getId() == R.id.product_company_spinner) {
            addNewProductBinding.edtProductCompany.setText("");
        }

        else if(parent.getId() == R.id.product_category_spinner) {
            addNewProductBinding.edtProductCategory.setText("");
        }
//        else if(parent.getId() == R.id.)
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

        else if(v.getId() == addNewProductBinding.addProductCompany.getId()) {
            EditText etCompanyName = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Enter Company Name")
                    .setMessage("")
                    .setView(etCompanyName)
                    .setPositiveButton("DONE", (dialog, which) -> {
                        this.companies.add(etCompanyName.getText().toString());
                        addNewProductBinding.productCompanySpinner.setSelection(companies.size()-1);
                        dialog.dismiss();
                    })
                    .setNegativeButton("CANCEL", ((dialog, which) -> dialog.dismiss()))
                    .create().show();
        }

        else if(v.getId() == addNewProductBinding.addProductCategory.getId()) {
            EditText etCategoryName = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Enter Category Name")
                    .setMessage("")
                    .setView(etCategoryName)
                    .setPositiveButton("DONE", (dialog, which) -> {
                        this.categories.add(etCategoryName.getText().toString());
                        addNewProductBinding.productCategorySpinner.setSelection(categories.size()-1);
                        dialog.dismiss();
                    })
                    .setNegativeButton("CANCEL", ((dialog, which) -> dialog.dismiss()))
                    .create().show();
        }

    }

    private void cropImage(Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("return-data", true);



        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "new-product-image-" + Integer.valueOf(imageUris.size()).toString() + ".jpg");

        currentImageUri = Uri.fromFile(file);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);
        cropIntent.putExtra("output", currentImageUri);


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

    private void createAppUpdateReadySnackbar() {
        Snackbar.make(addNewProductBinding.getRoot(),"New update downloaded",Snackbar.LENGTH_INDEFINITE)
                .setAction("INSTALL",v -> {
                    if(appUpdateManager!=null)
                        appUpdateManager.completeUpdate();
                })
                .setActionTextColor(getResources().getColor(R.color.green))
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(appUpdateManager!=null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(appUpdateManager!=null)
            appUpdateManager.registerListener(installStateUpdatedListener);
    }

}
