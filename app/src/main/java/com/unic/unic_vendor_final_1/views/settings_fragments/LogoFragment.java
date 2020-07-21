package com.unic.unic_vendor_final_1.views.settings_fragments;

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

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.FragmentLogoBinding;
import com.unic.unic_vendor_final_1.viewmodels.AddShopViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.unic.unic_vendor_final_1.commons.Helpers.enableDisableViewGroup;


public class LogoFragment extends Fragment implements View.OnClickListener {
    private FragmentLogoBinding logoBinding;
    private AddShopViewModel addShopViewModel;

    private View coverView;
    private boolean imageSelectSuccessful = false;

    private static final int GALLERY_INTENT = 1001;
    private static final int CROP_IMAGE = 1002;


    private String shopId;
    private String shopLogoLink;
    private Bitmap imageBitmap;
    private AlertDialog dialog;



    private Uri outputFileUri;






    public LogoFragment() {
        // Required empty public constructor
    }
    public LogoFragment(String shopId, String shopLogoLink){
        this.shopId=shopId;
        this.shopLogoLink=shopLogoLink;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        logoBinding = FragmentLogoBinding.inflate(getLayoutInflater());

        logoBinding.btnAddShopImage.setOnClickListener(this);
        logoBinding.addShopStep2.setOnClickListener(this);
        if(shopLogoLink.length()>2){
            Glide
                    .with(getContext())
                    .load(shopLogoLink)
                    .into(logoBinding.btnAddShopImage);
        }

        addShopViewModel = new ViewModelProvider(getActivity()).get(AddShopViewModel.class);
        addShopViewModel.logoStatus.observe(getViewLifecycleOwner(), this::statusUpdate);

        return logoBinding.getRoot();
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_shop_step_2:

                if(imageBitmap==null){
                    Toast.makeText(getActivity(), "No new logo added.", Toast.LENGTH_SHORT).show();
                }
               else{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] data = baos.toByteArray();

                    coverView = new View(getActivity());
                    coverView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    coverView.setBackgroundResource(R.color.gray_1);
                    coverView.setAlpha(0.5f);
                    ((ViewGroup)logoBinding.getRoot()).addView(coverView);
                    logoBinding.addShopProgressBar.setVisibility(View.VISIBLE);
                    logoBinding.addShopProgressBar.bringToFront();
                    enableDisableViewGroup((ViewGroup)logoBinding.getRoot(),false);

                   addShopViewModel.saveShopLogo(shopId,data);
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
            case 3:
                addShopViewModel.setShopLogoLink(shopId);
                break;

            case 4:
                enableDisableViewGroup((ViewGroup)logoBinding.getRoot(),true);
                ((ViewGroup)logoBinding.getRoot()).removeView(coverView);
                logoBinding.addShopProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Logo Saved.", Toast.LENGTH_SHORT).show();
                break;
            case -1:
            case -2:
            case -3:
            case -4:
                enableDisableViewGroup((ViewGroup)logoBinding.getRoot(),true);
                ((ViewGroup)logoBinding.getRoot()).removeView(coverView);
                logoBinding.addShopProgressBar.setVisibility(View.GONE);
        }
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
                case CROP_IMAGE:
                    Bitmap bitmap;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),outputFileUri);

                        imageBitmap = bitmap;
                        logoBinding.btnAddShopImage.setImageBitmap(imageBitmap);
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
