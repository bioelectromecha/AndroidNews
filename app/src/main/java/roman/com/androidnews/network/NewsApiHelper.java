package roman.com.androidnews.network;

import android.text.TextUtils;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import roman.com.androidnews.dataobjects.Article;

/**
 * a helper class used by our loader to get stuff from the books api and parse the result
 */
public class NewsApiHelper {

    private static final String KEY_QUERY_URL = "http://content.guardianapis.com/search?q=android&api-key=test";
    private static final String KEY_RESPONSE_OBJECT = "response";
    private static final String KEY_RESULTS_ARRAY = "results";
    private static final String KEY_TITLE = "webTitle";
    private static final String KEY_DATE = "webPublicationDate";
    private static final String KEY_SECTION = "sectionName";
    private static final String KEY_URL = "webUrl";

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL getApiQueryUrl() {
        URL url;
        try {
            url = new URL(KEY_QUERY_URL);
        } catch (MalformedURLException exception) {
            System.out.println("Error creating URL");
            exception.printStackTrace();
            return null;
        }
        return url;
    }

    /**
     * connect to the server and get the response as a string
     *
     * @param url a Url object from where to GET the json
     * @return
     * @throws IOException
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                System.out.println("Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            System.out.println("Problem retrieving the JSON results.");
            e.printStackTrace();
            // no matter what - do this
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                //noinspection ThrowFromFinallyBlock
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static List<Article> parseJsonToArticles(String jsonResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        try {
            List<Article> articleList = new ArrayList<>();
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONObject responseObject = baseJsonResponse.getJSONObject(KEY_RESPONSE_OBJECT);
            JSONArray articleArray = responseObject.getJSONArray(KEY_RESULTS_ARRAY);

            for (int i = 0; i < articleArray.length(); i++) {
                //if parsing one article fails, continue to parse others
                try {
                    String title;
                    String date;
                    String section;
                    String url;

                    //get info specific to a single article
                    JSONObject aritcleInfo = articleArray.getJSONObject(i);

                    // get the fields we need
                    title = aritcleInfo.getString(KEY_TITLE);
                    date = aritcleInfo.getString(KEY_DATE);
                    section = aritcleInfo.getString(KEY_SECTION);
                    url = aritcleInfo.getString(KEY_URL);

                    articleList.add(new Article(title, date, section, url));
                } catch (JSONException e) {
                    System.out.println("Problem parsing a json result");
                    e.printStackTrace();
                }
            }
            //if all went well - return the article list
            return articleList;
        } catch (JSONException e) {
            System.out.println("Problem parsing the JSON results");
            e.printStackTrace();
        }
        //return null if something went wrong
        return null;
    }
}
