package roman.com.androidnews.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import roman.com.androidnews.dataobjects.Article;

/**
 * an asyntaskloader to get the artciles api data into our app
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    public ArticleLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        List<Article> bookList = null;
        URL url = NewsApiHelper.getApiQueryUrl();
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = NewsApiHelper.makeHttpRequest(url);
        } catch (IOException e) {
            System.out.println("Problem making the HTTP request.");
            e.printStackTrace();
        }
        if (jsonResponse != null) {
            bookList = NewsApiHelper.parseJsonToArticles(jsonResponse);
        } else {
            System.out.println("jsonResponse is null ");
        }
        return bookList;
    }
}