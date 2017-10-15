package com.fantastic.bookxchange.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.User;
import com.fantastic.bookxchange.utils.DataTest;

import static com.fantastic.bookxchange.R.id.tvEdition;
import static com.fantastic.bookxchange.R.id.tvTitle;

public class BookDetailActivity extends BaseActivity {

    private ImageView ivLargeImage;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvDetailPage;
    private TextView tvPublisher;
    private TextView tvEditorialReview;

    Book book;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Get book
        // book = Parcels.unwrap(getIntent().getParcelableExtra(book);

        book = DataTest.getFakeBook().get(0);

        ivLargeImage = findViewById(R.id.ivLargeImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvDetailPage = findViewById(tvEdition);
        tvEditorialReview = findViewById(R.id.tvEditorialReview);
        tvPublisher = findViewById(R.id.tvPublisher);

        setupToolbar();
        setupView();


    }

    private void setupView(){
        Glide.with(this)
                .load(R.drawable.harry_potter_cover)
                .asBitmap()
                .centerCrop()
                .into(ivLargeImage);

        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        tvDetailPage.setText(book.getShortDescription());
        tvPublisher.setText(book.getPublisher());
        tvEditorialReview.setText(book.getShortDescription());
    }

    private void setupToolbar(){
        enableToolbarBackButton();
        setTitle(book.getTitle());
    }

}
