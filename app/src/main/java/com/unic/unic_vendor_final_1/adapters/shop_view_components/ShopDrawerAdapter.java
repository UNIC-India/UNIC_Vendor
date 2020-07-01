package com.unic.unic_vendor_final_1.adapters.shop_view_components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Page;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ShopPageFragment;

import java.util.Collections;
import java.util.List;


public class ShopDrawerAdapter extends RecyclerView.Adapter<ShopDrawerAdapter.ViewHolder> {

    List<Page> pages;
    private Context context;
    private SetStructureViewModel setStructureViewModel;

    public ShopDrawerAdapter(Context context){
        this.context = context;
        setStructureViewModel = new ViewModelProvider((FragmentActivity)context).get(SetStructureViewModel.class);
    }

    class MovePageUpListener implements View.OnClickListener{

        int position;

        public MovePageUpListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(position==0)
                return;
            Collections.swap(pages,position,position-1);
            pages.get(position).setPageId(1001+position);
            pages.get(position-1).setPageId(1000+position);
            notifyItemRangeChanged(position-1,2);
        }
    }

    class MovePageDownListener implements View.OnClickListener{

        int position;

        public MovePageDownListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(position==pages.size()-1)
                return;
            Collections.swap(pages,position,position+1);
            pages.get(position).setPageId(1001+position);
            pages.get(position+1).setPageId(1002+position);
            notifyItemRangeChanged(position,2);

        }
    }

    class ChangePageNameListener implements View.OnClickListener{

        int position;

        public ChangePageNameListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            final EditText etPageName = new EditText(context);
            etPageName.setText(pages.get(position).getPageName());
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enter Page Name");
            builder.setMessage("");
            builder.setView(etPageName);
            builder.setPositiveButton("DONE", (dialog, which) -> {
                pages.get(position).setPageName(etPageName.getText().toString());
                dialog.dismiss();
                notifyItemChanged(position);
            });
            builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    class DeletePageListener implements View.OnClickListener{

        int position;

        public DeletePageListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("CONFIRM YOUR ACTIONS")
                    .setMessage("Are you sure you want to delete " + pages.get(position).getPageName())
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deletePage(position);
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

    class PageSelectedListener implements View.OnClickListener{

        int position;

        public PageSelectedListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            setStructureViewModel.getCloseDrawers().setValue(true);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.shop_pages_loader, new ShopPageFragment(pages.get(position)), pages.get(position).getPageName())
                    .commit();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvPageName;
        ImageButton ibMovePageUp,ibMovePageDown,ibPageDelete,ibEditPageName;
        ConstraintLayout bounding;

        public ViewHolder(View itemView){
            super(itemView);

            tvPageName = itemView.findViewById(R.id.drawer_item_name);
            ibMovePageUp = itemView.findViewById(R.id.button_move_page_up);
            ibMovePageDown = itemView.findViewById(R.id.button_move_page_down);
            ibPageDelete = itemView.findViewById(R.id.shop_page_delete);
            ibEditPageName = itemView.findViewById(R.id.edit_page_name);
            bounding = itemView.findViewById(R.id.shop_drawer_item_bounding);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_navigation_drawer_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvPageName.setText(pages.get(position).getPageName());

        if(position==0) {
            holder.ibMovePageUp.setBackgroundResource(R.drawable.direction_up_disabled);
            holder.ibMovePageUp.setEnabled(false);
        }
        else {
            holder.ibMovePageUp.setBackgroundResource(R.drawable.direction_up);
            holder.ibMovePageUp.setEnabled(true);
        }

        if(position==pages.size()-1) {
            holder.ibMovePageDown.setBackgroundResource(R.drawable.direction_down_disabled);
            holder.ibMovePageDown.setEnabled(false);
        }
        else {
            holder.ibMovePageDown.setBackgroundResource(R.drawable.direction_down);
            holder.ibMovePageDown.setEnabled(true);
        }

        holder.ibMovePageUp.setOnClickListener(new MovePageUpListener(position));
        holder.ibMovePageDown.setOnClickListener(new MovePageDownListener(position));
        holder.ibEditPageName.setOnClickListener(new ChangePageNameListener(position));
        holder.ibPageDelete.setOnClickListener(new DeletePageListener(position));
        holder.bounding.setOnClickListener(new PageSelectedListener(position));

    }

    @Override
    public int getItemCount() {
        if(pages==null)
            return 0;
        return pages==null?0:pages.size();
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    private void deletePage(int position){
        if(((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.shop_pages_loader).getClass()==ShopPageFragment.class&&((ShopPageFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.shop_pages_loader)).getPageId()==pages.get(position).getPageId()){

            if(position==0&&pages.size()>1){
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.shop_pages_loader, new ShopPageFragment(pages.get(position+1)), pages.get(position+1).getPageName())
                        .commit();
            }

            else if(position>0) {
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.shop_pages_loader, new ShopPageFragment(pages.get(position - 1)), pages.get(position - 1).getPageName())
                        .commit();
            }
            else if(pages.size()==1){
                Toast.makeText(context, "Can't delete the only page in your structure", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        pages.remove(position);
        notifyItemRemoved(position);
    }

}
