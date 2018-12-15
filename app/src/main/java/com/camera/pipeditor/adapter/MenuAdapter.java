package com.camera.pipeditor.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.camera.pipeditor.R;
import com.camera.pipeditor.data.Menu;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private ArrayList<Menu> listMenu;

    public MenuAdapter(ArrayList<Menu> listMenu) {
        this.listMenu = listMenu;
    }

    private int pos = -1;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_menu, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Menu menu = listMenu.get(i);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemMenu != null) {
                    onClickItemMenu.onClickMenu(menu.getIcon());
                    pos = viewHolder.itemPos;
                    notifyDataSetChanged();
                }
            }
        });
        if (pos == i) {
            viewHolder.imMenu.setImageResource(menu.getIcon_sl());
            viewHolder.imCheck.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imMenu.setImageResource(menu.getIcon());
            viewHolder.imCheck.setVisibility(View.GONE);
        }

        viewHolder.itemPos = i;
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imMenu, imCheck;
        int itemPos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imMenu = itemView.findViewById(R.id.imMenu);
            imCheck = itemView.findViewById(R.id.imCheck);
        }
    }

    public interface OnClickItemMenu {
        void onClickMenu(int menu);
    }

    public OnClickItemMenu onClickItemMenu;

    public void setOnClickItemMenu(OnClickItemMenu onClickItemMenu) {
        this.onClickItemMenu = onClickItemMenu;
    }
}
