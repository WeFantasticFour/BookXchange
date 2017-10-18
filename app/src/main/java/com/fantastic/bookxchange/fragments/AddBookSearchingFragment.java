package com.fantastic.bookxchange.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.adapters.BookTestAdapter;
import com.fantastic.bookxchange.api.BookClient;
import com.fantastic.bookxchange.api.JsonKeys;
import com.fantastic.bookxchange.models.BookTest;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import retrofit2.http.PUT;


public class AddBookSearchingFragment extends Fragment {

    private ListView lvBooks;
    private BookTestAdapter bookTestAdapter;
    private BookClient client;
    private ArrayList<BookTest> mBooks;
    private ProgressBar mProgressBar;


    private OnFragmentInteractionListener mListener;

    public AddBookSearchingFragment() {
        // Required empty public constructor
    }


    public static AddBookSearchingFragment newInstance() {
        AddBookSearchingFragment fragment = new AddBookSearchingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_add_book_searching, container, false);

        lvBooks = view.findViewById(R.id.lvBooks);
        mBooks = new ArrayList<>();
        bookTestAdapter = new BookTestAdapter(getContext(), mBooks);

        setupFooter();

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: ADD BOOK TO FIREBASE
            }
        });




        return view;

    }

    public void setupFooter(){
        View footerView = getLayoutInflater().inflate(R.layout.footer_progress, null);
        mProgressBar = footerView.findViewById(R.id.pbFooterLoading);
        lvBooks.addFooterView(footerView);
        lvBooks.setAdapter(bookTestAdapter);
    }

    public void fetchBooks(String query){
        client = new BookClient();
        client.getBooks(query, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try{
                    JSONArray docs;
                    if(response != null){
                        docs = response.getJSONArray(JsonKeys.DOCS);
                        final ArrayList<BookTest> bookTests = BookTest.fromJson(docs);
                        bookTestAdapter.clear();

                        for(BookTest bookTest : bookTests){
                            bookTestAdapter.add(bookTest);
                        }
                        bookTestAdapter.notifyDataSetChanged();
                        hideProgress();

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);
            }

        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showProgress();
                fetchBooks(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    void showProgress() {
        Toast.makeText(getContext(), "Starting progress", Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.VISIBLE);
    }


    void hideProgress() {
        Toast.makeText(getContext(), "Stopping progress", Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
