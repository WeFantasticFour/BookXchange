package com.fantastic.bookxchange.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.api.BookClient;
import com.fantastic.bookxchange.fragments.BaseBookListFragment;
import com.fantastic.bookxchange.fragments.BookDetailFragment;
import com.fantastic.bookxchange.fragments.SearchListFragment;
import com.fantastic.bookxchange.models.Book;

import org.parceler.Parcels;

/**
 * Created by gretel on 10/20/17.
 */

public class BookSearchActivity extends BaseActivity implements SearchListFragment.BookListClickListener, SearchListFragment.BookListReadyListener {

    BookClient client;
    public static final String EXTRA_BOOK = "book";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        client = new BookClient();

        setupToolbar();

    }




    @Override
    public void onClickListener(Book book) {
        //TODO Itent to Book Detail page

        BookDetailFragment bookDetailFragment = new BookDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_BOOK, Parcels.wrap(book));
        Log.i("BOOK_ACTIVITY", Parcels.wrap(book).toString());
        bookDetailFragment.setArguments(bundle);
    }


    private void loadFragment(String query){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment newFragment = new SearchListFragment();
        Bundle args = new Bundle();
        args.putString("q", query);
        newFragment.setArguments(args);

        transaction.add(container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    private void setupToolbar() {
        setSupportActionBar(toolbar);
        enableToolbarBackButton();
        showToolbarBackButton();
        setTitle(R.string.search_text);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView = (SearchView) searchMenuItem.getActionView();

        if(null!=searchManager){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        searchView.setIconifiedByDefault(false);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();
                loadFragment(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id== R.id.action_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onReadyListener(BaseBookListFragment.FragmentType type) {

        //  fragment.pushData();

    }


}
