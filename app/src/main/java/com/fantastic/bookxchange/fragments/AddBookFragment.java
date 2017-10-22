package com.fantastic.bookxchange.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fantastic.bookxchange.R;

/**
 * Created by dgohil on 10/17/17.
 */

public class AddBookFragment extends Fragment {

    private EditText etBookAuthor;
    private EditText etBookTitle;
    private EditText etISBNNumber;
    private Button btnAddBook;
    private Button btnAddPhoto;
    private ImageView ivPicture;

    public static Fragment newInstance() {
        Fragment fragment = new AddBookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);
        etBookTitle = view.findViewById(R.id.etBookTitle);
        etBookAuthor = view.findViewById(R.id.etBookAuthor);
        etISBNNumber = view.findViewById(R.id.etISBNNumber);
        btnAddBook = view.findViewById(R.id.btnAddBook);
        btnAddPhoto = view.findViewById(R.id.btnAddPhoto);
        ivPicture = view.findViewById(R.id.ivPicture);
        btnAddPhoto.setOnClickListener(view1 -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ivPicture.setImageBitmap(bitmap);
    }
}
