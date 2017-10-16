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
import com.fantastic.bookxchange.models.Book;


public class BookDetailFragment extends BaseBookListFragment {

    static final String TAG = BookDetailFragment.class.getSimpleName();
    public static final String EXTRA_BOOK = "book";

    Book mBook;

    private ImageView ivLargeImage;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvEditorialReview;



    private BookListClickListener mListener;

    public BookDetailFragment() {
        // Required empty public constructor
    }


    public static BookDetailFragment newInstance(Book book) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_BOOK, (Parcelable) book);
        fragment.setArguments(args);
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

        Bundle bundle = this.getArguments();
        if(bundle != null){
            mBook = bundle.getParcelable("book");

        }

        View view =  inflater.inflate(R.layout.fragment_book_detail, null);

        ivLargeImage = view.findViewById(R.id.ivLargeImage);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvAuthor = view.findViewById(R.id.tvAuthor);
        tvPublisher = view.findViewById(R.id.tvPublisher);
        tvEditorialReview = view.findViewById(R.id.tvEditorialReview);

        if(mBook != null){
            setupView();


        }
        return view;

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
        tvEditorialReview.setText(mBook.getShortDescription());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookListClickListener) {
            mListener = (BookListClickListener) context;
        }else {
            throw new ClassCastException(context.toString()
                    + " must implement onBookClickListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface BookListClickListener {
        void clickListener(Book book);
    }
}
