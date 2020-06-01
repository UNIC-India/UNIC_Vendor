package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ImagePickerAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentImagePickerBinding;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class ImagePicker extends Fragment implements View.OnClickListener{

    private int pageId,viewCode;

    private List<Map<String,Object>> data = new ArrayList<>();

    private FragmentImagePickerBinding imagePickerBinding;

    private ImagePickerAdapter adapter;

    private SetStructureViewModel setStructureViewModel;

    private boolean uploading = false;

    private static final int PICK_IMAGE_MULTIPLE = 6001;

    public ImagePicker() {
        // Required empty public constructor
    }

    public ImagePicker(int pageId,int viewCode){
        this.pageId = pageId;
        this.viewCode = viewCode;
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

        setStructureViewModel.setCurrentFrag(this);

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

    public void savePickedImages(ClipData clipData){

        data = adapter.getData();

        for (int i = 0; i< clipData.getItemCount();i++){
            Map<String,Object> imageData = new HashMap<>();
            imageData.put("imageUri",clipData.getItemAt(i).getUri());
            data.add(imageData);
        }

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

    public void addImages(ClipData clipData){

        for(int i=0;i<clipData.getItemCount();i++){
            Map<String,Object> imageData = new HashMap<>();
            imageData.put("imageUri",clipData.getItemAt(i).getUri());
            data.add(imageData);
        }

        adapter.setData(data);
        adapter.notifyDataSetChanged();

    }

    public void uploadImagesToFirebase(){
        data = adapter.getData();

        for(int i=0;i<data.size();i++){
            Uri uri = (Uri)data.get(i).get("imageUri");
            String tag = data.get(i).get("tag").toString();

            setStructureViewModel.uploadViewImage(pageId,viewCode,i,tag,uri);

            if (i==data.size()-1)
                ((SetShopStructure) Objects.requireNonNull(getActivity())).returnToPage(pageId);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data && null != data.getClipData()) {

                ClipData mClipData = data.getClipData();

                if(mClipData.getItemCount()>0)
                    addImages(mClipData);

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
}