package com.nasheed.now;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Stack;

import jp.wasabeef.richeditor.RichEditor;

public class DetailsFragment extends Fragment {
    private static final String ARG_ITEM_ID = "item_id";
    private int itemId;
    private RichEditor editTextItem;
    private DatabaseHelper databaseHelper;
    private Handler handler = new Handler();
    private Runnable saveRunnable;
    private Stack<String> undoStack = new Stack<>();
    private Stack<String> redoStack = new Stack<>();

    public static DetailsFragment newInstance(int itemId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        editTextItem = view.findViewById(R.id.editTextItem);

        editTextItem.setEditorHeight(200);
        editTextItem.setEditorFontSize(22);
        editTextItem.setEditorFontColor(Color.RED);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        editTextItem.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        editTextItem.setPlaceholder("Insert text here...");
        //mEditor.setInputEnabled(false);

        view.findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.undo();
            }
        });

        view.findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.redo();
            }
        });

        view.findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setBold();
            }
        });

        view.findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setItalic();
            }
        });

        view.findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setSubscript();
            }
        });

        view.findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setSuperscript();
            }
        });

        view.findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setStrikeThrough();
            }
        });

        view.findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setUnderline();
            }
        });

        view.findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setHeading(1);
            }
        });

        view.findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setHeading(2);
            }
        });

        view.findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setHeading(3);
            }
        });

        view.findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setHeading(4);
            }
        });

        view.findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setHeading(5);
            }
        });

        view.findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setHeading(6);
            }
        });

        view.findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                editTextItem.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        view.findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                editTextItem.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        view.findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setIndent();
            }
        });

        view.findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setOutdent();
            }
        });

        view.findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setAlignLeft();
            }
        });

        view.findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setAlignCenter();
            }
        });

        view.findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setAlignRight();
            }
        });

        view.findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setBlockquote();
            }
        });

        view.findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setBullets();
            }
        });

        view.findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.setNumbers();
            }
        });

        view.findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                        "dachshund", 320);
            }
        });

        view.findViewById(R.id.action_insert_youtube).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.insertYoutubeVideo("https://www.youtube.com/embed/pS5peqApgUA");
            }
        });

        view.findViewById(R.id.action_insert_audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3");
            }
        });

        view.findViewById(R.id.action_insert_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.insertVideo("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4", 360);
            }
        });

        view.findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
        view.findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextItem.insertTodo();
            }
        });






        ImageButton buttonUndo = view.findViewById(R.id.buttonUndo);
        ImageButton buttonRedo = view.findViewById(R.id.buttonRedo);
        databaseHelper = new DatabaseHelper(getActivity());


        if (getArguments() != null) {
            itemId = getArguments().getInt(ARG_ITEM_ID);
        }

        // Load data from database
        loadItemFromDatabase();

//        editTextItem.addTextChangedListener(new TextWatcher() {
//            private String previousText = "";
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Save the current text to the undo stack
//                previousText = s.toString();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Remove any pending save operations
//                handler.removeCallbacks(saveRunnable);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Push the previous text onto the undo stack
//                if (!previousText.equals(s.toString())) {
//                    undoStack.push(previousText);
//                }
//
//                // Schedule a save operation to run after a short delay
//                saveRunnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        saveItemToDatabase(s.toString());
//                    }
//                };
//                handler.postDelayed(saveRunnable, 1000); // Save after 1 second of inactivity
//            }
//        });

//        buttonUndo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!undoStack.isEmpty()) {
//                    // Push the current text onto the redo stack
//                    redoStack.push(editTextItem.getText().toString());
//                    // Set the EditText to the last value from the undo stack
//                    String lastText = undoStack.pop();
//                    editTextItem.setText(lastText);
//                }
//            }
//        });
//
//        buttonRedo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!redoStack.isEmpty()) {
//                    // Push the current text onto the undo stack
//                    undoStack.push(editTextItem.getText().toString());
//                    // Set the EditText to the last value from the redo stack
//                    String redoText = redoStack.pop();
//                    editTextItem.setText(redoText);
//                }
//            }
//        });


        editTextItem.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                saveItemToDatabase(text); // This will save the content in the database
            }
        });



        return view;
    }

    private void loadItemFromDatabase() {
        Cursor cursor = databaseHelper.getItemById(itemId);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range")
            String itemName = cursor.getString(cursor.getColumnIndex("name"));
            editTextItem.setHtml(itemName);
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