package com.fantastic.bookxchange.adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.activities.BookDetailActivity;
import com.fantastic.bookxchange.fragments.BookDetailFragment;
import com.fantastic.bookxchange.models.Book;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by m3libea on 10/13/17.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder>{

    private ArrayList<Book> books;
    private Context context;


    BookClickListener listener;

    int rowSelectedIndex = -1;

    public interface BookClickListener{
        void onBookClickListener(Book book);
    }

    public void setListener(BookClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llBook;
        TextView tvTitle;
        TextView tvAuthor;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            llBook = itemView.findViewById(R.id.llBook);
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

        holder.llBook.setOnClickListener((View view) -> {
            rowSelectedIndex=position;
            listener.onBookClickListener(book);
            // call Detail fragment
//            Intent intent = new Intent(context, BookDetailActivity.class);
//            intent.putExtra("book", Parcels.wrap(book));
//            intent.putExtra("isbn", books.get(rowSelectedIndex).getIsbn());
//            context.startActivity(intent);

            android.support.v4.app.FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
            BookDetailFragment dialog = BookDetailFragment.newInstance(book);
            dialog.show(manager, "Dialog");

            notifyDataSetChanged();
        });
        if(rowSelectedIndex==position){
            holder.llBook.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.white));
        }
        else
        {
            holder.llBook.setBackgroundColor(Color.TRANSPARENT);
            holder.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.textDark));
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

}
