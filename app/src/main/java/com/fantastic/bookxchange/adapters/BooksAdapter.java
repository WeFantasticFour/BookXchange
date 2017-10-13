package com.fantastic.bookxchange.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Book;

import java.util.ArrayList;

/**
 * Created by m3libea on 10/13/17.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder>{

    private ArrayList<Book> books;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvAuthor;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
        }

        public void bind(Book book){
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
        }
    }
    public BooksAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
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
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

}
