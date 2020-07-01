package com.unic.unic_vendor_final_1.views.helpers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.ShopAdapter;
import com.unic.unic_vendor_final_1.adapters.ShopListAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentIntermediateShopListBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.activities.AddShop;
import com.unic.unic_vendor_final_1.views.nav_fragments.NotificationsFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntermediateShopList extends Fragment {
    private UserShopsViewModel shopsViewModel;


    private ShopListAdapter adapter;
    FragmentIntermediateShopListBinding intermediateShopListBinding;
    int from=0;

    public IntermediateShopList() {
        // Required empty public constructor
    }
    public IntermediateShopList(int from){
        this.from=from;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intermediateShopListBinding= FragmentIntermediateShopListBinding.inflate( getLayoutInflater(),container, false);




        adapter = new ShopListAdapter(getContext(),from);
        shopsViewModel = new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        adapter.setShops(shopsViewModel.getShops().getValue());

        shopsViewModel.notificationStatus.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==0) {
                    intermediateShopListBinding.loading.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Notification sent!", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.home_fragment, new NotificationsFragment())
                            .commit();
                }
                else if(integer==1)
                    intermediateShopListBinding.loading.setVisibility(View.VISIBLE);

            }
        });
        shopsViewModel.getShops().observe(getViewLifecycleOwner(), new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> shops) {
                adapter.setShops(shops);
                adapter.notifyDataSetChanged();
                if(shops==null||shops.size()==0){
                    intermediateShopListBinding.noshops.setVisibility(View.VISIBLE);
                    intermediateShopListBinding.tvnoshops.setVisibility(View.VISIBLE);
                }
                else{
                    intermediateShopListBinding.noshops.setVisibility(View.GONE);
                    intermediateShopListBinding.tvnoshops.setVisibility(View.GONE);
                }
            }
        });
        intermediateShopListBinding.rvShops.setAdapter(adapter);
        intermediateShopListBinding.rvShops.setLayoutManager(new LinearLayoutManager(getContext()));



        return intermediateShopListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
