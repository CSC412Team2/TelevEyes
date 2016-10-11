package edu.ecu.csc412.televeyes.adapter;

/**
 * Created by nater on 10/11/2016.
 */

import android.animation.Animator;
import android.animation.ValueAnimator;
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
import edu.ecu.csc412.televeyes.ShowFragment;
import edu.ecu.csc412.televeyes.VolleySingleton;
import edu.ecu.csc412.televeyes.dummy.DummyContent;
import edu.ecu.csc412.televeyes.json.Image;
import edu.ecu.csc412.televeyes.json.Series;
import edu.ecu.csc412.televeyes.json.ShowContainer;


/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyContent.DummyItem} and makes a call to the
 * specified {@link ShowFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final List<ShowContainer> mValues;
    private ImageLoader mImageLoader;

    public SearchAdapter(List<ShowContainer> items) {
        mValues = items;


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
                fadeTransition(1, 0, holder.mImageView, url);
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
        public final TextView mSummaryView;
        public final TextView mTitleView;
        public final NetworkImageView mImageView;
        public ShowContainer mItem;

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

