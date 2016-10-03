package edu.ecu.csc412.televeyes.adapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import edu.ecu.csc412.televeyes.R;
import edu.ecu.csc412.televeyes.ShowFragment.OnListFragmentInteractionListener;
import edu.ecu.csc412.televeyes.VolleySingleton;
import edu.ecu.csc412.televeyes.dummy.DummyContent.DummyItem;
import edu.ecu.csc412.televeyes.json.Image;
import edu.ecu.csc412.televeyes.json.Series;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ShowRecyclerViewAdapter extends RecyclerView.Adapter<ShowRecyclerViewAdapter.ViewHolder> {

    private final List<Series> mValues;
    private final OnListFragmentInteractionListener mListener;
    private ImageLoader mImageLoader;

    public ShowRecyclerViewAdapter(List<Series> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

        mImageLoader = VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_show, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).name);
        holder.mTitleView.setText(mValues.get(position).show.name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mSummaryView.setText(Html.fromHtml(mValues.get(position).show.summary, Html.FROM_HTML_MODE_COMPACT).toString());
        } else {
            holder.mSummaryView.setText(Html.fromHtml(mValues.get(position).show.summary).toString());
        }

        Image image = mValues.get(position).show.image;

        if (image != null) {
            String url = mValues.get(position).show.image.medium != null ? mValues.get(position).show.image.medium : mValues.get(position).show.image.original != null ? mValues.get(position).show.image.original : null;

            if (url != null) {
                holder.mImageView.setImageUrl(url, mImageLoader);
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSummaryView;
        public final TextView mTitleView;
        public final NetworkImageView mImageView;
        public Series mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSummaryView = (TextView) view.findViewById(R.id.summary);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mImageView = (NetworkImageView) view.findViewById(R.id.showimage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
