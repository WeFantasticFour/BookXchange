package com.fantastic.bookxchange.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Book;

import java.util.ArrayList;

/**
 * Created by m3libea on 10/13/17.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private static final String TAG = BooksAdapter.class.getSimpleName();
    private final static String EXTRA_BOOK = "book";
    public Context context;
    BookClickListener listener;
    int rowSelectedIndex = -1;
    private ArrayList<Book> books;

    public BooksAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    public void setListener(BookClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_book, parent, false);

        return new ViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Book book = books.get(position);
        holder.bind(book);

        holder.llBook.setOnClickListener(view -> {
            rowSelectedIndex = position;
            listener.onBookClickListener(book);
            notifyDataSetChanged();
        });
        if (rowSelectedIndex == position) {
            holder.llBook.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.llBook.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public interface BookClickListener {
        void onBookClickListener(Book book);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llBook;
        TextView tvTitle;
        TextView tvAuthor;
        ImageView ivCover;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            llBook = itemView.findViewById(R.id.llBook);
            ivCover = itemView.findViewById(R.id.ivBookCover);
        }

        public void bind(Book book) {
            Log.i(TAG, "bind: book.getCoverUrl()" + book.getCoverUrl());
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
            Glide.with(context)
                    .load(book.getIsbn() != null ? Uri.parse(book.getCoverUrl()) : R.drawable.ic_nocover)
                    .centerCrop()
                    .placeholder(R.drawable.ic_nocover)
                    .into(ivCover);
        }
    }

}
