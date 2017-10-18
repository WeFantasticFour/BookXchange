package com.fantastic.bookxchange.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.fragments.BookDetailFragment;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.BookTest;

import java.util.ArrayList;

/**
 * Created by gretel on 10/18/17.
 */

public class BookTestAdapter extends ArrayAdapter<BookTest>{

    private static class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvAuthor;
    }

    public BookTestAdapter(Context context, ArrayList<BookTest> aBooks) {
        super(context, 0, aBooks);
    }

    // Translates a particular `Book` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final BookTest book = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_book, parent, false);
            viewHolder.ivCover = convertView.findViewById(R.id.ivBookCover);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            viewHolder.tvAuthor = convertView.findViewById(R.id.tvAuthor);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate data into the template view using the data object
        viewHolder.tvTitle.setText(book.getTitle());
        viewHolder.tvAuthor.setText(book.getAuthor());
        Glide.with(getContext()).load(Uri.parse(book.getCoverUrl())).placeholder(R.drawable.harry_potter_cover).into(viewHolder.ivCover);
        // Return the completed view to render on screen
        return convertView;
    }


}
