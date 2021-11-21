package com.originbooks;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final int BOOK_LOADER_ID = 1;

    private BookAdapter bookAdapter;
    private ProgressBar progressBar;
    private TextView emptyView;
    private EditText searchBox;
    private ImageView searchIcon;
    private ImageView searchIcon2;
    private TextView setTitleActionBar;

    /**
     * URL for OriginBook to fetch the details from Google Play Book API
     */
    private static final String GOOGLE_PLAY_API =
            "https://www.googleapis.com/books/v1/volumes?";

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitleActionBar = findViewById(R.id.search_activity_app_name);
        setTitleActionBar.setText(R.string.app_name);

        searchIcon = findViewById(R.id.search_activity_search_icon);
        searchIcon.setImageResource(R.drawable.ic_launcher_foreground);
        searchIcon2 = findViewById(R.id.search_activity_search_icon_2);
        searchIcon2.setImageResource(R.drawable.ic_launcher_foreground);
        searchIcon2.setVisibility(View.INVISIBLE);

        searchBox = findViewById(R.id.search_activity_edit_box);
        searchBox.setVisibility(View.INVISIBLE);

        progressBar = findViewById(R.id.progress_circular);
        emptyView = findViewById(R.id.empty_view);

        searchIcon.setOnClickListener(v -> {
            searchBox.setVisibility(View.VISIBLE);
            searchBox.setOnEditorActionListener((v1, actionId, event) -> {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (isActive()) {
                        bookAdapter.clear();
                        emptyView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    }
                    handled = true;
                }
                return handled;
            });
            setTitleActionBar.setVisibility(View.INVISIBLE);
            searchIcon.setVisibility(View.INVISIBLE);
            searchIcon2.setVisibility(View.VISIBLE);
        });

        searchIcon2.setOnClickListener(v -> {
            if (isActive()) {
                bookAdapter.clear();
                emptyView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
            }
        });

        if (isActive()) {

            AtomicReference<LoaderManager> loaderManager = new AtomicReference<>(getLoaderManager());
            loaderManager.get().initLoader(BOOK_LOADER_ID, null, this);

        } else {

            progressBar.setVisibility(View.GONE);
            emptyView.setText("No Internet Connection");
            emptyView.setTextColor(Color.WHITE);

            Button button = findViewById(R.id.retry_button);
            button.setText("Retry");
            button.setTextColor(Color.WHITE);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {

                if (isActive()) {

                    progressBar.setVisibility(View.VISIBLE);
                    button.setVisibility(View.INVISIBLE);
                    emptyView.setVisibility(View.INVISIBLE);

                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);

                } else {
                    emptyView.setText("Please Check Your Internet Connection");
                }
            });
        }

        ArrayList<Book> bookArrayList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookArrayList);

        GridView listView = findViewById(R.id.gridView_main);
        listView.setAdapter(bookAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Book url = bookAdapter.getItem(position);
             Intent activityBookDetailIntent = new Intent(MainActivity.this, ActivityBookDetail.class);
            activityBookDetailIntent.putExtra("book_title", url.getBookTitle());
            activityBookDetailIntent.putExtra("book_authors", url.getBookAuthors());
            activityBookDetailIntent.putExtra("book_publisher", url.getBookPublisher());
            activityBookDetailIntent.putExtra("book_cover", url.getImageView());
            activityBookDetailIntent.putExtra("book_description", url.getDescription());
            activityBookDetailIntent.putExtra("book_sample_url", url.getSampleWebURL());
            startActivity(activityBookDetailIntent);
        });

    }

    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @NonNull Bundle args) {

        Uri baseUri = Uri.parse(GOOGLE_PLAY_API);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", String.valueOf(searchBox.getText()));

        return new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {

        // On Load finish make progress bar invisible
        progressBar.setVisibility(View.GONE);

        bookAdapter.clear();
        if (data != null && !data.isEmpty()) {
            bookAdapter.addAll(data);
            bookAdapter.notifyDataSetChanged();
        } else {
            emptyView.setText("NO DATA FOUND\nPLEASE ENTER YOUR QUERY");
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookAdapter.clear();
        progressBar.setVisibility(View.VISIBLE);
    }

    public boolean isActive() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}