package com.fantastic.bookxchange.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.fragments.BaseBookListFragment;
import com.fantastic.bookxchange.fragments.BookDetailFragment;
import com.fantastic.bookxchange.fragments.SearchListFragment;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.rest.BookClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.parceler.Parcels;

import static com.fantastic.bookxchange.R.id.container;

/**
 * Created by gretel on 10/20/17.
 */

public class BookSearchActivity extends BaseActivity implements SearchListFragment.BookListClickListener, SearchListFragment.BookListReadyListener {

    public static final String EXTRA_BOOK = "book";
    BookClient client;

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


    private void loadFragment(String query) {

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

        if (null != searchManager) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        final EditText et = (EditText) searchView.findViewById(searchEditId);


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

        if (id == R.id.action_search) {
            return true;
        }

        if (id == R.id.action_scan) {

            final Activity activity = this;

            IntentIntegrator integrator = new IntentIntegrator(activity);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("BookSearchActivity", "Cancelled scan");
                Toast.makeText(this, "Canceled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("BookSearchActivity", "Scanned");

                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }

    }


    @Override
    public void onReadyListener(BaseBookListFragment.FragmentType type) {

        //  fragment.pushData();

    }

}
