package com.originbooks;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, ArrayList<Book> bookArrayList) {
        super(context, 0, bookArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.book_grid_view, parent, false);
        }

        Book book = getItem(position);

        // Find View to set the data
        ImageView bookThumbnail = view.findViewById(R.id.book_thumnail);
        TextView bookTitle = view.findViewById(R.id.book_title);
        TextView bookPublisher = view.findViewById(R.id.book_publisher);

        Picasso.get().load(book.getImageView()).into(bookThumbnail);
        bookTitle.setText(book.getBookTitle());
        bookTitle.setTextColor(Color.WHITE);
        bookPublisher.setText(book.getBookPublisher());

        return view;
    }
}