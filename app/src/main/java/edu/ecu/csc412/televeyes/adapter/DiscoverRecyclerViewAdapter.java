package edu.ecu.csc412.televeyes.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import edu.ecu.csc412.televeyes.R;
import edu.ecu.csc412.televeyes.DiscoverFragment.OnListFragmentInteractionListener;
import edu.ecu.csc412.televeyes.VolleySingleton;
import edu.ecu.csc412.televeyes.dummy.DummyContent.DummyItem;
import edu.ecu.csc412.televeyes.model.Show;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DiscoverRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverRecyclerViewAdapter.ViewHolder> {

    private final List<Show> mValues;
    private final OnListFragmentInteractionListener mListener;
    private ImageLoader mImageLoader;

    public DiscoverRecyclerViewAdapter(List<Show> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

        mImageLoader = VolleySingleton.getInstance().getImageLoader();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_discover_show, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //holder.mSummaryView.setText(Html.fromHtml(mValues.get(position).show.summary, Html.FROM_HTML_MODE_COMPACT).toString());
        } else {
            //holder.mSummaryView.setText(Html.fromHtml(mValues.get(position).show.summary).toString());
        }

            String url = mValues.get(position).getImage();

            if (url != null) {
                fadeTransition(1, 0, holder.mImageView, url);
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

    public void fadeTransition(final float start, final float end, final View view, final String url){
        ValueAnimator animator = ValueAnimator.ofFloat(start,end);
        animator.setDuration(500);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setAlpha(animation.getAnimatedFraction());
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                    NetworkImageView networkImageView = (NetworkImageView) view;
                    networkImageView.setImageUrl(url, mImageLoader);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mSummaryView;
        public final TextView mTitleView;
        public final NetworkImageView mImageView;
        public Show mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mSummaryView = (TextView) view.findViewById(R.id.summary);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mImageView = (NetworkImageView) view.findViewById(R.id.showimage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
