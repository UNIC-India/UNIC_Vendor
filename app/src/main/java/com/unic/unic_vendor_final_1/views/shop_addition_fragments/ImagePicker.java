package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ImagePickerAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentImagePickerBinding;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ImagePicker extends Fragment implements View.OnClickListener{

    private int pageId,code;

    private com.unic.unic_vendor_final_1.datamodels.View view;

    private List<Map<String,Object>> data = new ArrayList<>();

    private FragmentImagePickerBinding imagePickerBinding;

    private ImagePickerAdapter adapter;

    private SetStructureViewModel setStructureViewModel;

    private boolean uploading = false;
    private int currentUpload = -1;
    View coverView;



    private static final int PICK_IMAGE_MULTIPLE = 6001;

    public ImagePicker() {
        // Required empty public constructor
    }

    public ImagePicker(int pageId, com.unic.unic_vendor_final_1.datamodels.View  view,int code){
        this.pageId = pageId;
        this.view = view;
        this.code = code;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        imagePickerBinding = FragmentImagePickerBinding.inflate(inflater,container,false);

        adapter = new ImagePickerAdapter(getContext());

        setStructureViewModel = new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);

        setStructureViewModel.getCurrentImageUpload().setValue(-1);

        setStructureViewModel.setCurrentFrag(this);

        setStructureViewModel.getCurrentImageUpload().observe(getViewLifecycleOwner(),integer -> currentUpload = integer);

        setStructureViewModel.getIsImagePickerUploading().observe(getViewLifecycleOwner(),aBoolean -> {
            uploading = aBoolean;
            if(!uploading&&data.size()!=0&&currentUpload!=-1) {

                if(currentUpload==data.size()) {

                    ((ViewGroup) imagePickerBinding.getRoot()).removeView(coverView);
                    imagePickerBinding.imagePickProgressBar.setVisibility(View.GONE);
                    ((SetShopStructure) getActivity()).returnToPage(pageId);
                }
                else
                    uploadImageToFirebase(currentUpload);
            }
        });

        imagePickerBinding.imagePickerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2, RecyclerView.VERTICAL,false));
        imagePickerBinding.imagePickerRecyclerView.setAdapter(adapter);

        adapter.setData(data);
        adapter.notifyDataSetChanged();


        imagePickerBinding.btnPickImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

        // Inflate the layout for this fragment
        return imagePickerBinding.getRoot();
    }

     void savePickedImages(ClipData clipData){

        data = adapter.getData();

        for (int i = 0; i< clipData.getItemCount();i++){
            Map<String,Object> imageData = new HashMap<>();
            imageData.put("imageUri",clipData.getItemAt(i).getUri());
            data.add(imageData);
        }

        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    void saveSingleImage(Uri uri){
        data = adapter.getData();
        Map<String,Object> imageData = new HashMap<>();
        imageData.put("imageUri",uri);
        data.add(imageData);

        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState!=null&&savedInstanceState.containsKey("Uris")){
            List<String> Uris = savedInstanceState.getStringArrayList("Uris");

            for (String Uri : Uris){
                Map<String,Object> imageData = new HashMap<>();
                imageData.put("imageUri",Uri);
                data.add(imageData);
            }
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        }
    }

    public void uploadImagesToFirebase(){
        data = adapter.getData();
        coverView = new View(getContext());
        coverView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        coverView.setBackgroundResource(R.color.gray_1);
        coverView.setAlpha(0.5f);

        ((ViewGroup)imagePickerBinding.getRoot()).addView(coverView);

        imagePickerBinding.imagePickProgressBar.setVisibility(View.VISIBLE);

        setStructureViewModel.getStructure().getValue().getPage(pageId).addView(view,code);

        if(data.size()>0)
            uploadImageToFirebase(0);
        else
            Toast.makeText(getContext(), "Please select at least one image", Toast.LENGTH_SHORT).show();
    }

    void uploadImageToFirebase(int position){

        try {
            uploading = true;
            currentUpload = position + 1;
            setStructureViewModel.getIsImagePickerUploading().setValue(Boolean.TRUE);

            Uri uri = (Uri) data.get(position).get("imageUri");

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);

            bitmap = getResizedBitmap(bitmap,300);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

            byte[] stream = baos.toByteArray();

            String tag = data.get(position).get("tag") != null ? data.get(position).get("tag").toString() : null;
            setStructureViewModel.uploadViewImage(pageId, view.getViewCode(), position, tag, stream);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && data!=null) {

                if(data.getData()!=null){
                    saveSingleImage(data.getData());
                }
                else if(data.getClipData()!=null) {

                    ClipData mClipData = data.getClipData();

                    if (mClipData.getItemCount() > 0)
                        savePickedImages(mClipData);
                }

            } else {
                Toast.makeText(getActivity(), "You haven't picked any Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error: Something went wrong " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        ArrayList<String> Uris = new ArrayList<>();

        for (Map map : data){
            Uris.add(map.get("imageUri").toString());
        }

        outState.putStringArrayList("Uris", Uris);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.btnRight)
            uploadImagesToFirebase();
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