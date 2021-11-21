package com.originbooks;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {

    }

    public static ArrayList<Book> fetchBookData(String requestURL) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createURL(requestURL);

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error closing input stream");
        }

        return extractBook(jsonResponse);
    }

    private static URL createURL(String requestURL) {
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null then return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Book> extractBook(String jsonResponse) {

        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        ArrayList<Book> bookArrayList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);

            //Getting JSONArray node
            JSONArray itemsArray = root.getJSONArray("items");

            //looping through all books
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemsArrayJSONObject = itemsArray.getJSONObject(i);
                JSONObject volumeInfoJSONObject = itemsArrayJSONObject.getJSONObject("volumeInfo");
                JSONArray authorsArray = volumeInfoJSONObject.getJSONArray("authors");
                JSONObject accessInfoJSONObject = itemsArrayJSONObject.getJSONObject("accessInfo");
                JSONObject imageLinksJSONObject = volumeInfoJSONObject.getJSONObject("imageLinks");

                String imageURL = imageLinksJSONObject.getString("thumbnail");
                String URL_CUTTER = "img=1";
                String newURL = "";
                if (imageURL.contains(URL_CUTTER)) {
                    String[] part = imageURL.split(URL_CUTTER);
                    newURL = part[0] + URL_CUTTER;
                    newURL = newURL.replace("http://", "https://");
                } else {
                    Log.e(LOG_TAG, "URL could not split properly");
                }

                String bookTitle = volumeInfoJSONObject.getString("title");
                String authors = authorsArray.toString();
                String bookPublisher = volumeInfoJSONObject.getString("publisher");
                String sampleBookAccess = accessInfoJSONObject.getString("webReaderLink");
                String bookDescription = volumeInfoJSONObject.getString("description");

                bookArrayList.add(new Book(newURL, bookTitle, authors, bookPublisher, sampleBookAccess, bookDescription));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bookArrayList;
    }
}
