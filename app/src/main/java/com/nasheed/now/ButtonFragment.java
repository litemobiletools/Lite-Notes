package com.nasheed.now;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ButtonFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Item> itemList;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton fab;

    private ImageView emptyviewId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_button, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        fab = view.findViewById(R.id.fab);
        emptyviewId = view.findViewById(R.id.emptyviewId);

        databaseHelper = new DatabaseHelper(getActivity());

        // Populate the database with sample data if it's empty
//        if (databaseHelper.getAllItems().getCount() == 0) {
//            for (int i = 1; i <= 20; i++) {
//                databaseHelper.insertItem("Item " + i);
//            }
//        }

        loadDataFromDatabase();

        adapter = new MyAdapter(itemList, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                // Handle the item click
//                Toast.makeText(getActivity(), "Clicked: " + item.getName(), Toast.LENGTH_SHORT).show();
                // Navigate to SimpleFragment
                loadFragment(SimpleFragment.newInstance(item.getId()));
            }
        }, new MyAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Item item) {
                // Handle the delete click
                databaseHelper.deleteItem(item.getId());
                loadDataFromDatabase();
                adapter.updateItemList(itemList);
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        // Handle FAB click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insert a new item in the database
                String newItemName = "";
                long newItemId = databaseHelper.insertItem(newItemName);
                if (newItemId != -1) { // Check if insertion was successful
                    // Refresh the list
                    loadDataFromDatabase();
                    //adapter.notifyDataSetChanged();
                    adapter.updateItemList(itemList);
                    // Navigate to SimpleFragment with the new item ID
                    loadFragment(SimpleFragment.newInstance((int) newItemId));
                } else {
                    Toast.makeText(getActivity(), "Error inserting item", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void loadDataFromDatabase() {
        itemList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllItems();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if(name.isEmpty()){
                    name = "Untitled";
                }
                @SuppressLint("Range")
                String date_Time = cursor.getString(cursor.getColumnIndex("date_Time"));

                itemList.add(new Item(id, name, date_Time));
            }
            cursor.close();

            // Check if itemList is empty after processing the cursor
            if (itemList.isEmpty()) {
                emptyviewId.setVisibility(View.VISIBLE);
            } else {
                emptyviewId.setVisibility(View.GONE);
            }
        }else{
            emptyviewId.setVisibility(View.VISIBLE);
        }
    }

    private void loadFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}