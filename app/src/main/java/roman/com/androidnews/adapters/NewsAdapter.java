package roman.com.androidnews.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import roman.com.androidnews.R;
import roman.com.androidnews.dataobjects.Article;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final List<Article> mArticleList;

    public NewsAdapter(List<Article> articles) {
        mArticleList = articles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTitleView.setText(mArticleList.get(position).getTitle());
        holder.mDateView.setText(mArticleList.get(position).getDate());
        holder.mSectionView.setText(mArticleList.get(position).getSection());

    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDateView;
        public final TextView mSectionView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.textview_title);
            mDateView = (TextView) view.findViewById(R.id.textview_date);
            mSectionView = (TextView) view.findViewById(R.id.textview_section);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

    /**
     * get the article at specified position in the list
     * @param position
     * @return
     */
    public Article getItem(int position) {
        return mArticleList.get(position);
    }

    public void replaceData(List<Article> articleList) {
        mArticleList.clear();
        mArticleList.addAll(articleList);
        notifyDataSetChanged();
    }
}
