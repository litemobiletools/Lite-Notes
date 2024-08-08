package com.socialbrowser.fragment2;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.Stack;

public class SimpleFragment extends Fragment {
    private static final String ARG_ITEM_ID = "item_id";
    private int itemId;
    private EditText editTextItem;
    private DatabaseHelper databaseHelper;
    private Handler handler = new Handler();
    private Runnable saveRunnable;
    private Stack<String> undoStack = new Stack<>();
    private Stack<String> redoStack = new Stack<>();

    public static SimpleFragment newInstance(int itemId) {
        SimpleFragment fragment = new SimpleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simple, container, false);

        editTextItem = view.findViewById(R.id.editTextItem);
        ImageButton buttonUndo = view.findViewById(R.id.buttonUndo);
        ImageButton buttonRedo = view.findViewById(R.id.buttonRedo);
        databaseHelper = new DatabaseHelper(getActivity());


        if (getArguments() != null) {
            itemId = getArguments().getInt(ARG_ITEM_ID);
        }

        // Load data from database
        loadItemFromDatabase();

        editTextItem.addTextChangedListener(new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Save the current text to the undo stack
                previousText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Remove any pending save operations
                handler.removeCallbacks(saveRunnable);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Push the previous text onto the undo stack
                if (!previousText.equals(s.toString())) {
                    undoStack.push(previousText);
                }

                // Schedule a save operation to run after a short delay
                saveRunnable = new Runnable() {
                    @Override
                    public void run() {
                        saveItemToDatabase(s.toString());
                    }
                };
                handler.postDelayed(saveRunnable, 1000); // Save after 1 second of inactivity
            }
        });

        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!undoStack.isEmpty()) {
                    // Push the current text onto the redo stack
                    redoStack.push(editTextItem.getText().toString());
                    // Set the EditText to the last value from the undo stack
                    String lastText = undoStack.pop();
                    editTextItem.setText(lastText);
                }
            }
        });

        buttonRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!redoStack.isEmpty()) {
                    // Push the current text onto the undo stack
                    undoStack.push(editTextItem.getText().toString());
                    // Set the EditText to the last value from the redo stack
                    String redoText = redoStack.pop();
                    editTextItem.setText(redoText);
                }
            }
        });

        return view;
    }

    private void loadItemFromDatabase() {
        Cursor cursor = databaseHelper.getItemById(itemId);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range")
            String itemName = cursor.getString(cursor.getColumnIndex("name"));
            editTextItem.setText(itemName);
            cursor.close();
        }
    }

    private void saveItemToDatabase(String newName) {
        boolean result = databaseHelper.updateItem(itemId, newName);
        if (result) {
            Toast.makeText(getActivity(), "Item saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Error saving item", Toast.LENGTH_SHORT).show();
        }
    }
}