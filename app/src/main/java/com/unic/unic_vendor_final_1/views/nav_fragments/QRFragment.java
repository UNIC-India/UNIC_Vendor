package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.unic.unic_vendor_final_1.adapters.ShopQRAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentQrBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class QRFragment extends Fragment {

    private UserShopsViewModel shopsViewModel;
    private FragmentQrBinding fragmentQrBinding;
    private ShopQRAdapter qrAdapter;


    public QRFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        shopsViewModel = new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        shopsViewModel.titleSetter.setValue(4);
        qrAdapter = new ShopQRAdapter(getContext(),this);
        shopsViewModel.getShops().observe(getViewLifecycleOwner(), new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> shops) {
                qrAdapter.setShops(shops);
                qrAdapter.notifyDataSetChanged();
                if(shops==null||shops.size()==0){
                    fragmentQrBinding.noshops.setVisibility(View.VISIBLE);
                    fragmentQrBinding.tvnoshops.setVisibility(View.VISIBLE);
                }
                else{
                    fragmentQrBinding.noshops.setVisibility(View.GONE);
                    fragmentQrBinding.tvnoshops.setVisibility(View.GONE);
                }
            }
        });
        fragmentQrBinding = FragmentQrBinding.inflate(inflater,container,false);
        fragmentQrBinding.shopQrRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentQrBinding.shopQrRecyclerView.setAdapter(qrAdapter);

        return fragmentQrBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void displayQRCode(String dynamicLink){

    }

    private void makeDynamicLink(String shopId,String shopName,String imageLink){
        shopsViewModel.buildSubscribeLink(shopId,shopName,imageLink);
    }

    public Bitmap generateQRCode(String str, int len) throws WriterException {

        len = (int)dpToPx(len);

        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE,len,len,null);
            int w = result.getWidth();
            int h = result.getHeight();

            Bitmap bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_4444);
            for(int i=0;i<h;i++){
                for(int j=0;j<w;j++)
                    bitmap.setPixel(i,j,result.get(i,j)? Color.BLACK:Color.WHITE);
            }
            return bitmap;
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            return null;
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
