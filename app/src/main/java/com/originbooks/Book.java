package com.originbooks;

public class Book {

    private final String mImageViewURL;
    private final String mBookTitle;
    private final String mBookAuthors;
    private final String mBookPublisher;
    private final String mSampleWebURL;
    private final String mBookDescription;

    public Book(String imgViewURL, String bkTtl, String bkAut, String bkPub, String sampleWebURL, String bookDescription) {
        mImageViewURL = imgViewURL;
        mBookTitle = bkTtl;
        mBookAuthors = bkAut;
        mBookPublisher = bkPub;
        mSampleWebURL = sampleWebURL;
        mBookDescription = bookDescription;
    }

    public String getImageView() {
        return mImageViewURL;
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String getBookAuthors() {
        return mBookAuthors;
    }

    public String getBookPublisher() {
        return mBookPublisher;
    }

    public String getSampleWebURL() {
        return mSampleWebURL;
    }

    public String getDescription() {
        return mBookDescription;
    }
}
