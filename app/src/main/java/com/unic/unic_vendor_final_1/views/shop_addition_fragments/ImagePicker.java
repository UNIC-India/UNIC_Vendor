package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ImagePickerAdapter;
import com.unic.unic_vendor_final_1.databinding.ActivityImagePickerBinding;
import com.unic.unic_vendor_final_1.viewmodels.ImagePickerViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImagePicker extends AppCompatActivity implements View.OnClickListener {
    private ActivityImagePickerBinding imagePickerBinding;
    private ImagePickerViewModel imagePickerViewModel;

    private ImagePickerAdapter imagePickerAdapter;

    private List<Map<String,String>> images = new ArrayList<>();

    private View coverView;

    private static final int IMAGE_SELECT_GALLERY = 1005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickerBinding = ActivityImagePickerBinding.inflate(getLayoutInflater());
        setContentView(imagePickerBinding.getRoot());

        imagePickerViewModel = new ViewModelProvider(this).get(ImagePickerViewModel.class);

        imagePickerBinding.btnAddDisplayImage.setOnClickListener(this);
        imagePickerBinding.confirmImages.setOnClickListener(this);

        imagePickerAdapter = new ImagePickerAdapter(this);

        coverView = new View(this);
        coverView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        coverView.setBackgroundResource(R.color.gray_1);
        coverView.setAlpha(0.5f);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);

        imagePickerBinding.imagePickerRecyclerView.setLayoutManager(layoutManager);
        imagePickerBinding.imagePickerRecyclerView.setAdapter(imagePickerAdapter);

        imagePickerViewModel.getImageLinks().observe(this, new Observer<List<Map<String, String>>>() {
            @Override
            public void onChanged(List<Map<String, String>> maps) {
                imagePickerAdapter.setData(maps);
                imagePickerAdapter.notifyDataSetChanged();
            }
        });

        imagePickerViewModel.getImageLinks().observe(this, new Observer<List<Map<String, String>>>() {
            @Override
            public void onChanged(List<Map<String, String>> maps) {
                setImages(maps);
            }
        });

        imagePickerViewModel.getUploadStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1){
                    EditText textView = new EditText(ImagePicker.this);

                    Map<String,String> data = imagePickerViewModel.getCurrentSet().getValue();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ImagePicker.this);
                    builder.setTitle("Enter TAG")
                            .setMessage(" ")
                            .setView(textView)
                            .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    data.put("TAG",textView.getText().toString());
                                    images.add(data);
                                    imagePickerViewModel.getImageLinks().setValue(images);
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_add_display_image:

                PickImage();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_SELECT_GALLERY&&resultCode==RESULT_OK&&data!=null){

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imagePickerViewModel.uploadImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    void PickImage(){

        View popupView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.image_source_chooser,null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(imagePickerBinding.getRoot(), Gravity.CENTER,0,0);

        int type;

        popupView.findViewById(R.id.pick_option_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_SELECT_GALLERY);
            }
        });



    }

    private float dpToPx(int dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    public void setImages(List<Map<String, String>> images) {
        this.images = images;
    }
}
