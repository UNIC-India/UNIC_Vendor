package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.FragmentMyReportsBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.helpers.OrderSummaryFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyReportsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {


    FragmentMyReportsBinding myReportsBinding;
    UserShopsViewModel userShopsViewModel;
    Dialog orderReportDialog;
    List<String> shopNames;
    Map<String,Object> orderReportSettings;
    private boolean[] initial = {true,true};

    public MyReportsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myReportsBinding = FragmentMyReportsBinding.inflate(inflater,container,false);
        userShopsViewModel = new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        userShopsViewModel.titleSetter.setValue(7);

        orderReportSettings = new HashMap<>();

        List<Shop> shops = userShopsViewModel.getShops().getValue();
        if(shops!=null && shops.size()!=0) {
            myReportsBinding.noshops.setVisibility(View.GONE);
            myReportsBinding.tvnoshops.setVisibility(View.GONE);
            myReportsBinding.cardReportOrder.setVisibility(View.VISIBLE);
            shopNames = new ArrayList<>();
            shops.forEach(shop -> shopNames.add(shop.getName()));
            orderReportSettings.put("shopId",userShopsViewModel.getShopIds().getValue().get(0));
            orderReportSettings.put("time",0);
            orderReportSettings.put("allTypes",true);
            orderReportSettings.put("allShops",false);
            orderReportSettings.put("type",0);

            myReportsBinding.cardReportOrder.setOnClickListener(this);
        }
        else {
            myReportsBinding.noshops.setVisibility(View.VISIBLE);
            myReportsBinding.tvnoshops.setVisibility(View.VISIBLE);
            myReportsBinding.cardReportOrder.setVisibility(View.GONE);
        }

        return myReportsBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_report_order:
                orderReportDialog = new Dialog(getContext());
                orderReportDialog.setContentView(getLayoutInflater().inflate(R.layout.create_order_report_dialog,myReportsBinding.getRoot(),false));
                orderReportDialog.setCanceledOnTouchOutside(true);
                ((CheckBox) orderReportDialog.findViewById(R.id.create_order_report_all_shops_checkbox)).setOnCheckedChangeListener(MyReportsFragment.this);
                orderReportDialog.findViewById(R.id.create_order_report_all_shops_checkbox).setTag("allShops");
                ((CheckBox)orderReportDialog.findViewById(R.id.create_order_report_all_types_checkbox)).setOnCheckedChangeListener(MyReportsFragment.this);
                orderReportDialog.findViewById(R.id.create_order_report_all_types_checkbox).setTag("allTypes");
                ((CheckBox) orderReportDialog.findViewById(R.id.create_order_report_all_types_checkbox)).setChecked(true);
                ((CheckBox)orderReportDialog.findViewById(R.id.create_order_report_end_time_current_checkbox)).setOnCheckedChangeListener(MyReportsFragment.this);
                orderReportDialog.findViewById(R.id.create_order_report_end_time_current_checkbox).setTag("endNow");
                ArrayAdapter adapter= new ArrayAdapter(getContext(),R.layout.simple_textbox,shopNames);
                ((Spinner) orderReportDialog.findViewById(R.id.create_order_report_shop_spinner)).setAdapter(adapter);
                ((Spinner)orderReportDialog.findViewById(R.id.create_order_report_shop_spinner)).setOnItemSelectedListener(MyReportsFragment.this);
                ((Spinner)orderReportDialog.findViewById(R.id.create_order_report_type_spinner)).setOnItemSelectedListener(MyReportsFragment.this);
                ((Spinner)orderReportDialog.findViewById(R.id.create_order_report_date_spinner)).setOnItemSelectedListener(MyReportsFragment.this);
                orderReportDialog.findViewById(R.id.btn_generate_order_report).setOnClickListener(MyReportsFragment.this);
                orderReportDialog.findViewById(R.id.create_order_report_shop_spinner).setTag("shopId");
                orderReportDialog.findViewById(R.id.create_order_report_type_spinner).setTag("type");
                orderReportDialog.findViewById(R.id.create_order_report_date_spinner).setTag("time");
                initial[0] = true;
                initial[1] = true;
                orderReportDialog.show();
                orderReportDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                break;

            case R.id.btn_generate_order_report:
                getOrderReport();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String setting = "";
        switch (buttonView.getTag().toString()) {
            case "allShops":
                setting = "allShops";
                break;
            case "allTypes":
                setting = "allTypes";
                break;
            case "endNow":
                setting = "endNow";
                break;
        }
        orderReportSettings.put(setting,isChecked);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getTag().toString()) {
            case "shopId":
                orderReportSettings.put("shopId",userShopsViewModel.getShopIds().getValue().get(position));
                if(initial[0])
                    initial[0] = false;
                else
                    ((CheckBox)orderReportDialog.findViewById(R.id.create_order_report_all_shops_checkbox)).setChecked(false);
                break;
            case "type":
                orderReportSettings.put("type",position);
                if(initial[1]) {
                    initial[1] = false;
                    ((CheckBox) orderReportDialog.findViewById(R.id.create_order_report_all_types_checkbox)).setChecked(true);
                }

                else
                    ((CheckBox) orderReportDialog.findViewById(R.id.create_order_report_all_types_checkbox)).setChecked(false);


                break;
            case "time":
                orderReportSettings.put("time",position);
                if(position==4) {
                    orderReportDialog.findViewById(R.id.create_order_result_custom_date_picker).setVisibility(View.VISIBLE);
                }
                else {
                    orderReportDialog.findViewById(R.id.create_order_result_custom_date_picker).setVisibility(View.GONE);
                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void getOrderReport(){

        Calendar calendar = Calendar.getInstance();
        if(orderReportDialog.findViewById(R.id.create_order_result_custom_date_picker).getVisibility() == View.VISIBLE) {
            DatePicker startDatePicker = orderReportDialog.findViewById(R.id.create_order_report_custom_date_start_picker);
            DatePicker endDatePicker = orderReportDialog.findViewById(R.id.create_order_report_custom_date_end_picker);

            Date startDate = new GregorianCalendar(startDatePicker.getYear(),startDatePicker.getMonth(),startDatePicker.getDayOfMonth()).getTime();
            Date endDate;
            Date currentDate = calendar.getTime();
            if(((CheckBox) orderReportDialog.findViewById(R.id.create_order_report_end_time_current_checkbox)).isChecked())
                endDate = currentDate;
            else {
                endDate = new GregorianCalendar(endDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth(), 23, 59, 59).getTime();
                if(endDate.after(currentDate))
                    endDate = currentDate;
            }
            if(endDate.before(startDate)) {
                Toast.makeText(getContext(), "Start date is after end date", Toast.LENGTH_SHORT).show();
                return;
            }

            orderReportSettings.put("startDate",startDate);
            orderReportSettings.put("endDate",endDate);

        }

        else {

            Date endDate = calendar.getTime();
            long subtract = 0;

            switch (orderReportSettings.get("time").toString()) {
                case "0":
                    subtract = 86400000L;
                    break;
                case "1":
                    subtract = 259200000L;
                    break;
                case "2":
                    subtract = 604800000L;
                    break;
                case "3":
                    subtract = 2592000000L;
            }

            Date startDate = new Date(endDate.getTime()-subtract);

            orderReportSettings.put("startDate",startDate);
            orderReportSettings.put("endDate",endDate);

        }

        orderReportDialog.dismiss();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_fragment,new OrderSummaryFragment(orderReportSettings))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }
}