package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.views.activities.UserHome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolder> {

    private Map<String, Object> data;
    private List<String> stdKeys = new ArrayList<>(Arrays.asList("name","company","category","price"));
    private List<String> extraKeys = new ArrayList<>(Arrays.asList("subcategory","desc","tags","extraInfo1","extraInfo2"));
    private List<String> extraKeys2 = new ArrayList<>(Arrays.asList("subcategory","desc","tags","extraInfo1","extraInfo2"));
    private Context context;
    private int mode;
    private Map<String,EditText> editableData;

    public ProductDetailsAdapter(Context context) {
        this.context = context;
        this.mode = 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKey, tvValue;
        EditText editValue;
        CardView cdDetail;
        public LinearLayout.LayoutParams params;



        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKey = itemView.findViewById(R.id.tvKey);
            tvValue = itemView.findViewById(R.id.tvValue);
            editValue = itemView.findViewById(R.id.tv_edit_value);
            cdDetail=itemView.findViewById(R.id.cdDetail);
            params=new LinearLayout.LayoutParams(0,0);

        }
    }

    @NonNull
    @Override
    public ProductDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_details_item, parent, false);
        return new ProductDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailsAdapter.ViewHolder holder, final int position) {

        if(mode ==0) {

            holder.tvValue.setVisibility(View.VISIBLE);
            holder.editValue.setVisibility(View.INVISIBLE);
            holder.tvKey.setVisibility(View.VISIBLE);

            if(position<4){
                switch (position){
                    case 0:
                        holder.tvKey.setText("Name: ");
                        break;
                    case 1:
                        holder.tvKey.setText("Company: ");
                        break;
                    case 2:
                        holder.tvKey.setText("Category: ");
                        break;
                    case 3:
                        holder.tvKey.setText("Price: ");
                }
                holder.tvValue.setText(data.get(stdKeys.get(position)).toString());
            }
            else {
                if(data.keySet().contains(extraKeys.get(position-stdKeys.size()))){
                    switch (extraKeys.get(position-stdKeys.size())){
                        case "subcategory":
                            holder.tvKey.setText("Subcategory: ");
                            holder.tvValue.setText(data.get(extraKeys.get(position-stdKeys.size())).toString());
                            break;
                        case "desc":
                            holder.tvKey.setText("Description: ");
                            holder.tvValue.setText(data.get(extraKeys.get(position-stdKeys.size())).toString());
                            break;
                        case "tags":
                            holder.tvKey.setText("Tags: ");
                            holder.tvValue.setText(data.get(extraKeys.get(position-stdKeys.size())).toString());
                            break;
                        default:
                            String[] temp = data.get(extraKeys.get(position-stdKeys.size())).toString().split(":");
                            if(temp.length>1) {
                                holder.tvKey.setText(temp[0].toUpperCase()+ ": ");
                                holder.tvValue.setText(temp[1]);
                            }
                            else {
                                holder.tvKey.setText("");
                                holder.tvValue.setText(temp[0]);
                            }
                    }

                }
            }

        }

        if(mode ==1) {

            if(editableData==null)
                editableData = new HashMap<>();

            if(position<4){
                holder.tvKey.setVisibility(View.VISIBLE);
                holder.editValue.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                switch (position){
                    case 0:
                        holder.tvKey.setText("Name: ");
                        holder.tvValue.setVisibility(View.VISIBLE);
                        holder.editValue.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        holder.tvKey.setText("Company: ");
                        holder.tvValue.setVisibility(View.VISIBLE);
                        holder.editValue.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        holder.tvKey.setText("Category: ");
                        holder.tvValue.setVisibility(View.VISIBLE);
                        holder.editValue.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        holder.tvKey.setText("Price: ");
                        holder.tvValue.setVisibility(View.INVISIBLE);
                        holder.editValue.setVisibility(View.VISIBLE);
                        holder.editValue.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        if(!editableData.containsKey("price"))
                            editableData.put("price",holder.editValue);
                }
                if(position<3)
                    holder.tvValue.setText(data.get(stdKeys.get(position)).toString());

                if(position==3)
                    holder.editValue.setText(data.get(stdKeys.get(position)).toString());

            }
            else {
                if(data.keySet().contains(extraKeys2.get(position-stdKeys.size()))){
                    switch (extraKeys2.get(position-stdKeys.size())){
                        case "subcategory":
                            holder.tvKey.setText("Subcategory: ");
                            holder.tvValue.setText(data.get(extraKeys2.get(position-stdKeys.size())).toString());
                            holder.tvValue.setVisibility(View.VISIBLE);
                            holder.editValue.setVisibility(View.INVISIBLE);
                            holder.tvKey.setVisibility(View.VISIBLE);
                            break;
                        case "desc":
                            holder.tvKey.setText("Description: ");
                            holder.editValue.setText(data.get(extraKeys2.get(position-stdKeys.size())).toString());
                            holder.tvValue.setVisibility(View.INVISIBLE);
                            holder.editValue.setVisibility(View.VISIBLE);
                            holder.tvKey.setVisibility(View.VISIBLE);
                            if(!editableData.containsKey("desc"))
                                editableData.put("desc",holder.editValue);
                            break;
                        case "tags":
                            holder.tvKey.setText("Tags: ");
                            holder.editValue.setText(data.get(extraKeys2.get(position-stdKeys.size())).toString());
                            holder.tvValue.setVisibility(View.INVISIBLE);
                            holder.editValue.setVisibility(View.VISIBLE);
                            holder.tvKey.setVisibility(View.VISIBLE);
                            if(!editableData.containsKey("tags"))
                                editableData.put("tags",holder.editValue);
                            break;
                        case "extraInfo1":
                        case "extraInfo2":
                            holder.tvKey.setVisibility(View.INVISIBLE);
                            holder.editValue.setText(data.get(extraKeys2.get(position-stdKeys.size())).toString());
                            holder.tvValue.setVisibility(View.INVISIBLE);
                            holder.editValue.setVisibility(View.VISIBLE);
                            holder.tvKey.setText(extraKeys2.get(position-stdKeys.size()) + ":");

                            if(!editableData.containsKey(extraKeys2.get(position-stdKeys.size())))
                                editableData.put(extraKeys2.get(position-stdKeys.size()),holder.editValue);

                        default:
                            holder.editValue.setText(data.get(extraKeys2.get(position-stdKeys.size())).toString());
                            holder.tvValue.setVisibility(View.INVISIBLE);
                            holder.editValue.setVisibility(View.VISIBLE);
                    }

                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return mode==0?stdKeys.size()+extraKeys.size():9;
    }

    public void setData(Map<String, Object> data) {
        Map<String,Object> load = new HashMap<>();
        data.keySet().forEach(key -> {
            if(data.get(key)!=null&&!data.get(key).toString().equals("null")&&!key.equals("firestoreId")&&!key.equals("id"))
                load.put(key,data.get(key));
        });

        this.data = data;
        List<String> keys = new ArrayList<>(load.keySet());
        extraKeys.retainAll(keys);
    }

    public void beginEditMode(){
        mode = 1;
        notifyDataSetChanged();
    }

    public Map<String ,Object> endEditMode() {
        mode = 0;
        notifyDataSetChanged();

        Map<String,Object> modifiedData = new HashMap<>();

        editableData.forEach(((s, editText) -> {
            if(s.equals("price")) {
                try {
                    if(!Double.valueOf(data.get("price").toString()).equals(Double.valueOf(editText.getText().toString().trim()))){
                        modifiedData.put(s,Double.valueOf(editText.getText().toString()));
                    }
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            else {
                if(!data.get(s).toString().equals(editableData.get(s).getText().toString())) {
                    if(editableData.get(s).getText().toString().trim().length()<=1||editableData.get(s).getText().toString().trim().equals("null")){
                        modifiedData.put(s,"null");
                    }
                    else
                        modifiedData.put(s,editableData.get(s).getText().toString());
                }
            }
        }));

        data.putAll(modifiedData);

        extraKeys.clear();

        extraKeys.addAll(extraKeys2);

        Map<String,Object> load = new HashMap<>();
        data.keySet().forEach(key -> {
            if(data.get(key)!=null&&!data.get(key).toString().equals("null")&&!key.equals("firestoreId")&&!key.equals("id"))
                load.put(key,data.get(key));
        });

        List<String> keys = new ArrayList<>(load.keySet());
        extraKeys.retainAll(keys);

        notifyDataSetChanged();

        editableData.clear();

        if(modifiedData.size()==0)
            return null;
        return modifiedData;

    }
}