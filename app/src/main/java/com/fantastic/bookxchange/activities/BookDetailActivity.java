package com.fantastic.bookxchange.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Book;

import org.parceler.Parcels;


public class BookDetailActivity extends BaseActivity {

    Book book;
    private ImageView ivLargeImage;
    private TextView tvTitle;
    private TextView tvAuthor;
    //private TextView tvDetailPage;
    private TextView tvPublisher;
    private TextView tvEditorialReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Get book
        book = Parcels.unwrap(getIntent().getParcelableExtra("book"));

        ivLargeImage = findViewById(R.id.ivLargeImage);
        tvTitle = findViewById(R.id.tvAuthor);
        tvAuthor = findViewById(R.id.tvTitle);
//        tvDetailPage = findViewById(tvEdition);
        //tvDetailPage = findViewById(R.id.tvDetailPage);
        tvEditorialReview = findViewById(R.id.tvEditorialReview);
        tvPublisher = findViewById(R.id.tvPublisher);

        setupToolbar();
        setupView();


    }

    private void setupView() {
        Glide.with(this)
                .load(R.drawable.harry_potter_cover)
                .asBitmap()
                .into(ivLargeImage);

        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        // tvDetailPage.setText(book.getShortDescription());
        tvPublisher.setText(book.getPublisher());
        tvEditorialReview.setText(book.getShortDescription());
    }

    private void setupToolbar() {
        enableToolbarBackButton();
        setTitle(book.getTitle());
    }

}
