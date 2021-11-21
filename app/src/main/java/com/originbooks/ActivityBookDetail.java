package com.originbooks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ActivityBookDetail extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_activity);

        TextView setTitleAtActionBar = findViewById(R.id.search_activity_app_name);
        setTitleAtActionBar.setText(R.string.app_name);

        EditText editText = findViewById(R.id.search_activity_edit_box);
        editText.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        String bookCover = intent.getStringExtra("book_cover");
        String bookTitle = intent.getStringExtra("book_title");
        String bookAuthor = intent.getStringExtra("book_authors");
        String bookPublisher = intent.getStringExtra("book_publisher");
        String bookSampleLink = intent.getStringExtra("book_sample_url");
        String bookDescription = intent.getStringExtra("book_description");

        ImageView cover = findViewById(R.id.book_detail_cover_image);

        TextView title = findViewById(R.id.book_detail_book_title);
        TextView author = findViewById(R.id.book_detail_book_author);
        TextView publisher = findViewById(R.id.book_detail_book_publisher);
        TextView descriptionBody = findViewById(R.id.book_detail_book_description_body);
        TextView descriptionHead = findViewById(R.id.book_detail_book_description_heading);

        Button trySample = findViewById(R.id.book_detail_try_sample);
        Button goBack = findViewById(R.id.book_detail_go_back);

        Picasso.get().load(bookCover).into(cover);
        title.setText(bookTitle);
        author.setText(bookAuthor);
        publisher.setText(bookPublisher);
        descriptionHead.setText("Description");
        descriptionBody.setText(bookDescription);
        trySample.setText("Try Sample");
        trySample.setBackgroundColor(Color.WHITE);
        goBack.setText("Go Back");
        goBack.setBackgroundColor(Color.WHITE);

        trySample.setOnClickListener(
                v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bookSampleLink))));
        goBack.setOnClickListener(
                v -> startActivity(new Intent(this, MainActivity.class)));
    }
}
