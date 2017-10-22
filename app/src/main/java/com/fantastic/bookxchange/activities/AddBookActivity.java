package com.fantastic.bookxchange.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.rest.BookClient;
import com.fantastic.bookxchange.rest.JsonKeys;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AddBookActivity extends BaseActivity  {

    private EditText isbn, title, author, publisher;
    private ImageView cover;
    private Button scanner, addBook;

     BookClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        isbn = findViewById(R.id.etISBNNumber);
        title = findViewById(R.id.etBookTitle);
        author = findViewById(R.id.etBookAuthor);
        publisher = findViewById(R.id.etPublisher);
        cover =  findViewById(R.id.ivCover);
        addBook = findViewById(R.id.btnAddBook);
        scanner = findViewById(R.id.btnScanner);

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchBooks(isbn.getText().toString());
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanner();
            }
        });


    }

    public void openScanner(){
        final Activity activity = this;

        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
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
                isbn.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }

    }

    public void fetchBooks(String isbn){
       
        client = new BookClient();
        client.getBooks(isbn, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try{
                    JSONArray docs;
                    if(response != null){
                        docs = response.getJSONArray(JsonKeys.DOCS);
                        // final ArrayList<Book> books = Book.fromJson(docs);
//                        pushData(books);
                        for (int i = 0; i < docs.length() ; i++) {
                            JSONObject object = docs.getJSONObject(i);
                            title.setText(object.getString(JsonKeys.TITLE));
                            author.setText(getAuthor(object));
                            publisher.setText(getPublisher(object));
                            Glide.with(getApplicationContext()).load(getCoverUrl()).asBitmap().centerCrop().into(cover);

                        }



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

    private static String getAuthor(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray(JsonKeys.BOOK_AUTHOR);
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }    private static String getPublisher(final JSONObject jsonObject) {
        try {
            final JSONArray pubs = jsonObject.getJSONArray(JsonKeys.BOOK_PUBLISHER);
            int numPubs = pubs.length();
            final String[] pubStrings = new String[numPubs];
            for (int i = 0; i < numPubs; ++i) {
                pubStrings[i] = pubs.getString(i);
            }
            return TextUtils.join(", ", pubStrings);
        } catch (JSONException e) {
            return "";
        }
    }

    public String getCoverUrl() {

        return "http://covers.openlibrary.org/b/olid/" + isbn + "-L.jpg?default=false";

    }

}
