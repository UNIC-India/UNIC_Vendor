package com.unic.unic_vendor_final_1.views.helpers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivityAddShopBinding;
import com.unic.unic_vendor_final_1.databinding.FragmentAddShopBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.AddShopViewModel;
import com.unic.unic_vendor_final_1.views.activities.AddShop;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.unic.unic_vendor_final_1.commons.Helpers.enableDisableViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddShopFrag extends Fragment implements View.OnClickListener {
    private FragmentAddShopBinding addShopBinding;
    private AddShopViewModel addShopViewModel;

    private View coverView;

    private static final int GALLERY_INTENT = 1001;
    private static final int CROP_IMAGE = 1002;
    private static final int LOCATION_SELECTOR = 2001;

    private Shop shop;
    private Bitmap imageBitmap;
    private AlertDialog dialog;

    private boolean userWantsImage = true;
    private boolean imageSelectSuccessful = false;

    private Uri imageurl,outputFileUri;


    private Map<String,Double> location = new HashMap<>();

    public AddShopFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addShopBinding = FragmentAddShopBinding.inflate(getLayoutInflater());

        addShopBinding.btnAddShopImage.setOnClickListener(this);
        addShopBinding.addShopStep2.setOnClickListener(this);
        addShopBinding.shopLocationSelect.setOnClickListener(this);
        addShopViewModel = new ViewModelProvider(getActivity()).get(AddShopViewModel.class);
        addShopViewModel.getShopAddStatus().observe(getViewLifecycleOwner(), this::statusUpdate);
        addShopViewModel.getShop().observe(getViewLifecycleOwner(), this::updateShop);
        return addShopBinding.getRoot();
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_shop_step_2:

                if(imageBitmap==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                            .setMessage("No shop Image selected. Do you want to select one?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                userWantsImage = true;
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent,GALLERY_INTENT);
                            })
                            .setNegativeButton("No", (dialog, which) -> userWantsImage = false);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                if(userWantsImage) {

                    if (addShopBinding.etAddShopName.getText().toString().trim().length()==0 || (addShopBinding.etAddShopAddressLine1.getText().toString().trim().length()==0 && addShopBinding.etAddShopAddressLine2.getText().toString().trim().length()==0) || addShopBinding.etShopAddLocality.getText().toString().trim().length()==0||addShopBinding.etShopAddCity.getText().toString().trim().length()==0) {
                        Toast.makeText(getActivity(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(location.size()==0){
                        Toast.makeText(getActivity(), "Please select location on map", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    coverView = new View(getActivity());
                    coverView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    coverView.setBackgroundResource(R.color.gray_1);
                    coverView.setAlpha(0.5f);
                    ((ViewGroup)addShopBinding.getRoot()).addView(coverView);
                    addShopBinding.addShopProgressBar.setVisibility(View.VISIBLE);
                    addShopBinding.addShopProgressBar.bringToFront();
                    enableDisableViewGroup((ViewGroup)addShopBinding.getRoot(),false);

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
                Intent locationIntent = new Intent(getActivity(), LocationSelector.class);
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
                Toast.makeText(getActivity(), "Shop saved", Toast.LENGTH_SHORT).show();
                addShopViewModel.setShopId();
                break;

            case 2:
                if(!userWantsImage){
                    Intent intent = new Intent(getActivity(), SetShopStructure.class);
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
                enableDisableViewGroup((ViewGroup)addShopBinding.getRoot(),true);
                ((ViewGroup)addShopBinding.getRoot()).removeView(coverView);
                addShopBinding.addShopProgressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), SetShopStructure.class);
                intent.putExtra("shopId",shop.getId());
                intent.putExtra("template",Integer.valueOf(1));
                startActivity(intent);
                break;
            case -1:
            case -2:
            case -3:
            case -4:
                enableDisableViewGroup((ViewGroup)addShopBinding.getRoot(),true);
                ((ViewGroup)addShopBinding.getRoot()).removeView(coverView);
                addShopBinding.addShopProgressBar.setVisibility(View.GONE);
        }
    }

    private void updateShop(Shop shop){
        this.shop = shop;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data!=null) {

            switch (requestCode){
                case GALLERY_INTENT:
                    Uri uri = data.getData();
                    cropImage(uri);
                    break;
                case LOCATION_SELECTOR:
                    location.put("latitude",data.getDoubleExtra("latitude",0.0));
                    location.put("longitude",data.getDoubleExtra("longitude",0.0));
                    break;
                case CROP_IMAGE:
                    Bitmap bitmap;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),outputFileUri);

                        imageBitmap = bitmap;
                        addShopBinding.btnAddShopImage.setImageBitmap(imageBitmap);
                        imageSelectSuccessful = true;

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
        File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "new-shop-image-" + ".jpg");
        outputFileUri = Uri.fromFile(file);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        cropIntent.putExtra("output",outputFileUri);
        startActivityForResult(cropIntent, CROP_IMAGE);
        Toast.makeText(getActivity(), "Cropping image", Toast.LENGTH_SHORT).show();
    }

}
