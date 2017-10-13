package com.fantastic.bookxchange.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.adapters.BooksAdapter;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.utils.ListDivider;

import org.parceler.Parcels;

import java.util.ArrayList;

public class BaseBookListFragment extends Fragment implements BooksAdapter.BookClickListener {

    public static String BOOKS_LIST = "books";
    private ArrayList<Book> books;

    private BooksAdapter aBooks;
    private RecyclerView rvBooks;
    private LinearLayoutManager lyManager;
    private BookListClickListenr listener;


    public interface BookListClickListenr{
        void onClickListener(Book book);
    }
    public BaseBookListFragment() {
        // Required empty public constructor
    }

    public static BaseBookListFragment newInstance(ArrayList<Book> books) {
        BaseBookListFragment fragment = new BaseBookListFragment();
        Bundle args = new Bundle();
        args.putParcelable(BOOKS_LIST, Parcels.wrap(books));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            books = Parcels.unwrap(getArguments().getParcelable(BOOKS_LIST));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_base_book_list, container, false);
        if (books == null){
            books = new ArrayList<Book>();
        }

        rvBooks = view.findViewById(R.id.rvBooks);
        setupReciclerView();

        return view;
    }

    private void setupReciclerView() {
        aBooks = new BooksAdapter(getContext(), books);
        aBooks.setListener(this);
        rvBooks.setAdapter(aBooks);
        lyManager = new LinearLayoutManager(getContext());
        rvBooks.setLayoutManager(lyManager);


        //Line between rows
        ListDivider line = new ListDivider(getContext());
        rvBooks.addItemDecoration(line);
    }

    @Override
    public void onBookClickListener(Book book) {
        listener.onClickListener(book);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookListClickListenr) {
            listener = (BookListClickListenr) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement onBookClickListener");
        }
    }
}
