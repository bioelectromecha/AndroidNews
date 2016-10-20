package roman.com.androidnews.dataobjects;

/**
 * a dataobject class to hold the data returned from the books api
 */
public class Article {
    private String mTitle;
    private String mDate;
    private String mSection;
    private String mUrl;

    public Article(String title, String date, String section, String url) {
        mTitle = title;
        mDate = date;
        mSection = section;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl() {
        return mUrl;
    }
}
