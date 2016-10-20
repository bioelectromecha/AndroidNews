package roman.com.androidnews.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import roman.com.androidnews.R;
import roman.com.androidnews.adapters.NewsAdapter;
import roman.com.androidnews.dataobjects.Article;
import roman.com.androidnews.decoration.DividerItemDecoration;
import roman.com.androidnews.listeners.RecyclerTouchListener;
import roman.com.androidnews.network.ArticleLoader;

/**
 * A fragment representing a list of news articles.
 */
public class NewsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Article>> , RecyclerTouchListener.ClickListener {


    // id of the article loader
    private static final int LOADER_ID = 2701;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private NewsAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_indicator);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new NewsAdapter(new ArrayList<Article>(0));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        //touch events will be called on 'this'
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, this));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);

    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articleList) {
        if (articleList == null) {
            Toast.makeText(getActivity(), "Problem retrieving data - try again", Toast.LENGTH_SHORT).show();
            return;
        }
        //hide the circular progress bar and show the list
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        //replace the existing list and update the view
        mAdapter.replaceData(articleList);
    }

    /**
     * the query has changed - make a new loader with the new data
     */
    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        //querry can't change in our app
    }

    @Override
    public void onClick(View view, int position) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(mAdapter.getItem(position).getUrl()));
        startActivity(i);
    }

    @Override
    public void onLongClick(View view, int position) {
        // not implemented in our app
    }
}
