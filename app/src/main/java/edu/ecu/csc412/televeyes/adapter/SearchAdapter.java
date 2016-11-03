package edu.ecu.csc412.televeyes.adapter;

/**
 * Created by nater on 10/11/2016.
 */

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import edu.ecu.csc412.televeyes.R;
import edu.ecu.csc412.televeyes.DiscoverFragment;
import edu.ecu.csc412.televeyes.VolleySingleton;
import edu.ecu.csc412.televeyes.dummy.DummyContent;
import edu.ecu.csc412.televeyes.model.Show;


/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyContent.DummyItem} and makes a call to the
 * specified {@link DiscoverFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final List<Show> mValues;
    private ImageLoader mImageLoader;
    private Context mContext;

    public SearchAdapter(List<Show> items, Context context) {
        mValues = items;
        mContext = context;
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
            holder.mSummaryView.setText(Html.fromHtml(mValues.get(position).getSummary(), Html.FROM_HTML_MODE_COMPACT).toString());
        } else {
            holder.mSummaryView.setText(Html.fromHtml(mValues.get(position).getSummary()).toString());
        }

            String url = mValues.get(position).getImage();
            if (url != null) {
                fadeTransition(1, 0, holder.mImageView, url);
            }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        if(position == getItemCount() - 1){
            int left,right,top,bottom;
            View view = holder.mView;

            left = view.getPaddingLeft();
            right = view.getPaddingRight();
            top = view.getPaddingTop();

            bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, mContext.getResources().getDisplayMetrics());

            view.setPadding(left, top, right, bottom);
        }
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
        public Show mItem;

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

