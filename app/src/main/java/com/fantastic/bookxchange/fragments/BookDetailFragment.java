package com.fantastic.bookxchange.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.api.BookApi;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.SearchRequest;
import com.fantastic.bookxchange.utils.DataTest;

import org.parceler.Parcels;


public class BookDetailFragment extends DialogFragment {

    static final String TAG = BookDetailFragment.class.getSimpleName();
    public static final String EXTRA_BOOK = "book";


    Book mBook;

    private ImageView ivLargeImage;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
   // private TextView tvEditorialReview;



    public BookDetailFragment() {
        // Required empty public constructor
    }


    public static BookDetailFragment newInstance(Book book) {
        BookDetailFragment fragment = new BookDetailFragment();
       // Bundle args = new Bundle();
       // args.putParcelable(EXTRA_BOOK, Parcels.wrap(book));
       // fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBook = getArguments().getParcelable(EXTRA_BOOK);
            Log.i("Book", String.valueOf(mBook.getTitle()));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBook = DataTest.getFakeBook().get(0);


        Bundle bundle = this.getArguments();
        if(bundle != null){
            mBook = bundle.getParcelable(EXTRA_BOOK);
        }

        View view =  inflater.inflate(R.layout.fragment_book_detail, container, false);


        ivLargeImage = view.findViewById(R.id.ivLargeImage);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvAuthor = view.findViewById(R.id.tvAuthor);
        tvPublisher = view.findViewById(R.id.tvPublisher);
       // tvEditorialReview = view.findViewById(R.id.tvEditorialReview);

        getDialog().setTitle(tvTitle.getText().toString());

        if(mBook != null){
            setupView();

        }
        return view;

    }

    private void setUpApi(){

    }

    private void setupView(){
        Glide.with(this)
                .load(R.drawable.harry_potter_cover)
                 //.asBitmap()
                 //.centerCrop()
                .into(ivLargeImage);

        tvTitle.setText(mBook.getTitle());
        tvAuthor.setText(mBook.getAuthor());
        tvPublisher.setText(mBook.getPublisher());
       // tvEditorialReview.setText(mBook.getShortDescription());
    }



    @Override
    public void onDetach() {
        super.onDetach();

    }


}
