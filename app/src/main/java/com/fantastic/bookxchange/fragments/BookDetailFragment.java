package com.fantastic.bookxchange.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    static final String TAG = BookDetailFragment.class.getSimpleName();
    public static final String EXTRA_BOOK = "book";


    Book mBook;

    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;


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


        Bundle bundle = this.getArguments();
        if(bundle != null){
            mBook = Parcels.unwrap(getArguments().getParcelable(EXTRA_BOOK));
        }

        View view =  inflater.inflate(R.layout.fragment_book_detail, container, false);


        ivBookCover = view.findViewById(R.id.ivBookCover);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvAuthor = view.findViewById(R.id.tvAuthor);
        tvPublisher = view.findViewById(R.id.tvPublisher);


        getDialog().setTitle(tvTitle.getText().toString());

        if(mBook != null){
            setupView();

        }
        return view;

    }


    private void setupView(){
        Glide.with(getContext())
                .load(mBook.getCoverUrl())
                //.asBitmap()
                //.centerCrop()
                .into(ivBookCover);

        tvTitle.setText(mBook.getTitle());
        tvAuthor.setText(mBook.getAuthor());
        tvPublisher.setText(mBook.getPublisher());

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
