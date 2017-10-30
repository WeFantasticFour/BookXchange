package com.fantastic.bookxchange.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Book;

import org.parceler.Parcels;

/**
 * Created by gretel on 10/20/17.
 */

public class BookDetailFragment extends DialogFragment {

    public static final String EXTRA_BOOK = "book";
    static final String TAG = BookDetailFragment.class.getSimpleName();
    Book mBook;

    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvIsbn;
    private TextView tvReview;


    public BookDetailFragment() {
        // Required empty public constructor
    }


    public static BookDetailFragment newInstance(Book book) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_BOOK, Parcels.wrap(book));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBook = Parcels.unwrap(getArguments().getParcelable(EXTRA_BOOK));
            Log.i("Book", String.valueOf(mBook.getTitle()));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mBook = Parcels.unwrap(getArguments().getParcelable(EXTRA_BOOK));
        }

        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);


        ivBookCover = view.findViewById(R.id.ivBookCover);
        tvTitle = view.findViewById(R.id.tvAuthor);
        tvAuthor = view.findViewById(R.id.tvTitle);
        tvPublisher = view.findViewById(R.id.tvPublisher);
        tvIsbn = view.findViewById(R.id.tvBookIsbn);
        tvReview = view.findViewById(R.id.tvEditorialReview);



        getDialog().setTitle(tvTitle.getText().toString());

        if (mBook != null) {
            setupView();
        }
        return view;

    }


    private void setupView() {

        Log.d(TAG, mBook.getCoverUrl());

        Glide.with(getActivity())
                .load(mBook.getCoverUrl())
                .placeholder(R.drawable.ic_nocover)
                .into(ivBookCover);

        tvTitle.setText(mBook.getTitle());
        tvAuthor.setText(mBook.getAuthor());
        if(mBook.getPublisher() == null){
            tvPublisher.setVisibility(View.GONE);
        } else {
            tvPublisher.setText(mBook.getPublisher());
        }

        tvIsbn.setText("ISBN: " + mBook.getIsbn());
        tvReview.setText(mBook.getShortDescription());

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

}
