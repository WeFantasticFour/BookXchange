package com.fantastic.bookxchange.activities;

import android.app.Activity;
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class AddBookActivity extends BaseActivity {

    private static final String TAG = AddBookActivity.class.getSimpleName();
    private EditText etBookAuthor;
    private EditText etBookTitle;
    private EditText etISBNNumber;
    private EditText etDescription;
    private ImageView ivPicture;
    private EditText etPublisher;

    private ImageView cover;
    private Button scanner, addBook;

    BookClient client;
    private Button btnAddPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etPublisher = findViewById(R.id.etPublisher);
        cover = findViewById(R.id.ivCover);
        addBook = findViewById(R.id.btnAddBook);
        showToolbarBackButton();
        enableToolbarBackButton();
        etBookTitle = findViewById(R.id.etBookTitle);
        etBookAuthor = findViewById(R.id.etBookAuthor);
        etISBNNumber = findViewById(R.id.etISBNNumber);
        etDescription = findViewById(R.id.etDescription);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        ivPicture = findViewById(R.id.ivPicture);
        ivPicture.setDrawingCacheEnabled(true);
        ivPicture.buildDrawingCache();
        scanner = findViewById(R.id.btnScanner);
        btnAddPhoto.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);
        });


        addBook.setOnClickListener(view -> fetchBooks(getText(etISBNNumber)));
        scanner.setOnClickListener(view -> openScanner());

    }

    public void openScanner() {
        final Activity activity = this;

        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
        btnAddPhoto.setOnClickListener(view -> AddBookActivityPermissionsDispatcher.addPhotoWithCheck(this));
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void addPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 111);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AddBookActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==111){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivPicture.setImageBitmap(bitmap);
            return;
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("BookSearchActivity", "Cancelled scan");
                Toast.makeText(this, "Canceled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("BookSearchActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                etISBNNumber.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }

    }

    public void fetchBooks(String isbn) {

        client = new BookClient();
        client.getBooks(isbn, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray docs;
                    if (response != null) {
                        docs = response.getJSONArray(JsonKeys.DOCS);
                        // final ArrayList<Book> books = Book.fromJson(docs);
//                        pushData(books);
                        for (int i = 0; i < docs.length(); i++) {
                            JSONObject object = docs.getJSONObject(i);
                            etBookTitle.setText(object.getString(JsonKeys.TITLE));
                            etBookAuthor.setText(getAuthor(object));
                            etPublisher.setText(getPublisher(object));
                            Glide.with(getApplicationContext()).load(getCoverUrl()).into(cover);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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
    }

    private static String getPublisher(final JSONObject jsonObject) {
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
        return "http://covers.openlibrary.org/b/olid/" + getText(etISBNNumber) + "-L.jpg?default=false";
    }

    public void addBook(View view) {


        if (!validate(etBookTitle, etBookAuthor, etISBNNumber, etDescription)) {
            return;
        }
        String isbn = getText(etISBNNumber);
        startProgress();
        searchAndSave(isbn);
    }

    private void searchAndSave(final String isbn) {
        FirebaseDatabase.getInstance()
                .getReference("books")
                .child(isbn)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null) {
                            uploadCoverPage(ivPicture.getDrawingCache(), isbn);
                        } else {
                            saveUserBookRelation(isbn, Book.CATEGORY.EXCHANGE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "databaseError: " + databaseError);
                    }
                });
    }

    private void saveBook(String url) {
        String title = getText(etBookTitle);
        String author = getText(etBookAuthor);
        String isbn = getText(etISBNNumber);
        String description = getText(etDescription);


        Book book = Book.Builder.get()
                .title(title)
                .author(author)
                .urlPicture(url)
                .description(description)
                .isbn(isbn)
                .active(true)
                .category(Book.CATEGORY.EXCHANGE)
                .user(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .build();
        startProgress();
        FirebaseDatabase.getInstance()
                .getReference("books")
                .child(isbn)
                .setValue(book)
                .addOnCompleteListener(task -> {
                    if (task.getException() != null) {
                        doneProgress();
                        snakebar(etBookTitle, task.getException().getMessage());
                    } else {
                        saveUserBookRelation(isbn, Book.CATEGORY.EXCHANGE);
                        //snakebar(etBookTitle, "Your Content has been saved!");
                    }
                });
    }

    private void saveUserBookRelation(String isbn, Book.CATEGORY category) {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("isbn", isbn);
        valueMap.put("category", category);

        FirebaseDatabase.getInstance()
                .getReference("user_book")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(isbn)
                .setValue(valueMap)
                .addOnCompleteListener(task -> {
                    doneProgress();
                    if (task.getException() != null) {
                        snakebar(etBookTitle, task.getException().getMessage());
                    } else {
                        snakebar(etBookTitle, "Your Content has been saved!");
                    }
                });

    }

    private void uploadCoverPage(Bitmap bitmap, String isbn) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef = storageRef.child("book_cover_pages").child(String.format("%s_%s", isbn, new Date().getTime()));
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            Log.e(TAG, exception.getMessage(), exception);
            doneProgress();
        }).addOnSuccessListener(taskSnapshot -> {
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            saveBook(downloadUrl.toString());
        });
    }

}
