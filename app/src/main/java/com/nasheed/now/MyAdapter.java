package com.nasheed.now;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Item> itemList;
    private OnItemClickListener listener;
    private OnDeleteClickListener deleteListener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
    public interface OnDeleteClickListener {
        void onDeleteClick(Item item);
    }
    public MyAdapter(List<Item> itemList, OnItemClickListener listener, OnDeleteClickListener deleteListener) {
        this.itemList = itemList;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item, listener, deleteListener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateItemList(List<Item> newItemList) {
        itemList = newItemList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName, dateTime;
        private ImageButton buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textView);
            dateTime = itemView.findViewById(R.id.dateTime);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(final Item item, final OnItemClickListener listener, final OnDeleteClickListener deleteListener) {
            itemName.setText(item.getName());
            dateTime.setText(item.getDate_Time());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.onDeleteClick(item);
                }
            });
        }


    }
}

class Item {
    private int id;
    private String name;
    private String date_Time;

    public Item(int id, String name, String date_Time) {
        this.id = id;
        this.name = name;
        this.date_Time = date_Time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getDate_Time() {
        return date_Time;
    }
}