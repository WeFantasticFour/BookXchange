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
import java.util.List;

public class BaseBookListFragment extends Fragment implements BooksAdapter.BookClickListener, BookDataReceiver {

    public static String BOOKS_LIST = "books";
    public ArrayList<Book> books;

    public BooksAdapter aBooks;
    private RecyclerView rvBooks;
    private LinearLayoutManager lyManager;
    private BookListClickListener clickListener;
    protected BookListReadyListener readyListener;


    public interface BookListClickListener {
        void onClickListener(Book book);
    }

    public enum FragmentType {
        SHARE,
        EXCHANGE,
        WISHLIST,
        NEAR
    }

    public interface BookListReadyListener{
        void onReadyListener(FragmentType type);
    }


    public BaseBookListFragment() {
        // Required empty public constructor
    }

    public static BaseBookListFragment newInstance() {
        BaseBookListFragment fragment = new BaseBookListFragment();
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
            books = new ArrayList<>();
        }

        rvBooks = view.findViewById(R.id.rvBooks);
        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
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
        clickListener.onClickListener(book);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookListClickListener) {
            clickListener = (BookListClickListener) context;
        }else {
            throw new ClassCastException(context.toString()
                    + " must implement onBookClickListener");
        }

        if (context instanceof BookListReadyListener) {
            readyListener = (BookListReadyListener) context;
        }else {
            throw new ClassCastException(context.toString()
                    + " must implement onReadyListener");
        }
    }

    public void pushData(List<Book> books) {
        this.books.clear();
        this.books.addAll(books);
        aBooks.notifyDataSetChanged();
    }
}
