package edu.ecu.csc412.televeyes.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import edu.ecu.csc412.televeyes.ApplicationSingleton;
import edu.ecu.csc412.televeyes.view.CircularNetworkImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.ecu.csc412.televeyes.R;
import edu.ecu.csc412.televeyes.SynActivity;
import edu.ecu.csc412.televeyes.VolleySingleton;
import edu.ecu.csc412.televeyes.database.DatabaseHelper;
import edu.ecu.csc412.televeyes.model.Show;
import edu.ecu.csc412.televeyes.util.CounterClass;
import edu.ecu.csc412.televeyes.util.Util;
import edu.ecu.csc412.televeyes.view.CheckableImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Show> mValues;
    private ImageLoader mImageLoader;
    private ListType listType;
    private Context context;

    public RecyclerViewAdapter(List<Show> items, ListType listType) {
        mValues = items;
        mImageLoader = VolleySingleton.getInstance().getImageLoader();
        this.listType = listType;
        this.context = ApplicationSingleton.getAppContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (listType) {
            case SEARCH:
                return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_show, parent, false));
            case DISCOVER:
                return new DiscoverViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_discover_show, parent, false));
            case SAVES:
                return new SavesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_saves_show, parent, false));
            default:
                return new DiscoverViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_show, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder item, int position) {
        if (item instanceof DiscoverViewHolder) {
            setupDiscoverViewHolder(item, position);
        } else if (item instanceof SearchViewHolder) {
            setupSearchViewHolder(item, position);
        } else if (item instanceof SavesViewHolder) {
            setupSavesViewHolder(item, position);
        }
    }

    private void setupSavesViewHolder(final RecyclerView.ViewHolder item, int position) {
        final SavesViewHolder holder = (SavesViewHolder) item;
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getName());
        String url = mValues.get(position).getImage();

        if (url != null) {
            fadeTransition(1, 0, holder.mImageView, url);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SynActivity.ShowSynop(context, mValues.get(item.getAdapterPosition()).getId());
            }
        });

        Show show = mValues.get(position);


        String showTime = show.getSchedule().getDays().get(0);
        Date d = Util.getNextOccurenceOfDay(Util.getDayFromString(showTime));

        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date time = null;

        try {
            time = format.parse(show.getSchedule().getShowTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar hc = Calendar.getInstance();
        hc.setTime(time);

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(d); // sets calendar time/date

        cal.add(Calendar.HOUR_OF_DAY, hc.get(Calendar.HOUR_OF_DAY)); // adds one hour
        cal.add(Calendar.MINUTE, hc.get(Calendar.MINUTE));

        if (holder.getCounter() != null) {
            holder.getCounter().cancel();
        }

        holder.setCounterClass(new CounterClass(cal.getTimeInMillis() - System.currentTimeMillis(), 1000, holder.mTimer, show, context));
        holder.getCounter().start();
    }

    private void setupSearchViewHolder(final RecyclerView.ViewHolder item, int position) {
        final SearchViewHolder holder = (SearchViewHolder) item;

        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getName());

        String url = mValues.get(position).getImage();
        if (url != null) {
            fadeTransition(1, 0, holder.mImageView, url);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SynActivity.ShowSynop(context, mValues.get(item.getAdapterPosition()).getId());
            }
        });

        if(position == 0){

        }

        if (position == getItemCount() - 1) {
            int left, right, top, bottom;
            View view = holder.mView;

            left = view.getPaddingLeft();
            right = view.getPaddingRight();
            top = view.getPaddingTop();

            bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());

            view.setPadding(left, top, right, bottom);
        }
    }

    public void setupDiscoverViewHolder(final RecyclerView.ViewHolder item, int position){
        final DiscoverViewHolder holder = (DiscoverViewHolder) item;
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getName());

        String url = mValues.get(position).getImage();

        if (url != null) {
            fadeTransition(1, 0, holder.mImageView, url);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SynActivity.ShowSynop(context, mValues.get(item.getAdapterPosition()).getId());
            }
        });

        holder.mAddView.setChecked(DatabaseHelper.getInstance().isShowSaved(mValues.get(position)));

        holder.mAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mAddView.toggle();

                if (holder.mAddView.isChecked()) {
                    DatabaseHelper.getInstance().addShow(mValues.get(item.getAdapterPosition()));
                } else {
                    DatabaseHelper.getInstance().removeShow(mValues.get(item.getAdapterPosition()));
                }
            }
        });

    }

    public void fadeTransition(final float start, final float end, final View view, final String url) {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
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
                CircularNetworkImageView networkImageView = (CircularNetworkImageView) view;
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

    public void addShow(Show show) {
        mValues.add(show);
        notifyItemInserted(mValues.size() - 1);
        notifyDataSetChanged();
    }

    public void removeShow(Show show) {
        int position = mValues.indexOf(show);
        mValues.remove(position);
        notifyItemRemoved(position);
    }

    public void clearItems() {
        mValues.clear();
        notifyDataSetChanged();
    }

    public void sortShows() {
        Collections.sort(mValues, new Comparator<Show>() {
            @Override
            public int compare(Show o1, Show o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * This enum will be used to define the different types of list the adapter supports
     */
    public enum ListType {
        SEARCH, DISCOVER, SAVES
    }

    public class DiscoverViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mSummaryView;
        public final TextView mTitleView;
        public final CircularNetworkImageView mImageView;
        public final CheckableImageView mAddView;
        public Show mItem;

        public DiscoverViewHolder(View view) {
            super(view);
            mView = view;
            //mSummaryView = (TextView) view.findViewById(R.id.summary);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mImageView = (CircularNetworkImageView) view.findViewById(R.id.showimage);
            mAddView = (CheckableImageView) view.findViewById(R.id.add_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSummaryView;
        public final TextView mTitleView;
        public final CircularNetworkImageView mImageView;
        public Show mItem;

        public SearchViewHolder(View view) {
            super(view);
            mView = view;
            mSummaryView = (TextView) view.findViewById(R.id.summary);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mImageView = (CircularNetworkImageView) view.findViewById(R.id.showimage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

    public class SavesViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mSummaryView;
        public final TextView mTitleView;
        public final CircularNetworkImageView mImageView;
        public final TextView mTimer;
        public CounterClass counter;
        public Show mItem;

        public SavesViewHolder(View view) {
            super(view);
            mView = view;
            //mSummaryView = (TextView) view.findViewById(R.id.summary);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mImageView = (CircularNetworkImageView) view.findViewById(R.id.showimage);
            mTimer = (TextView) view.findViewById(R.id.countdown_timer);
        }

        public void setCounterClass(CounterClass counter) {
            this.counter = counter;
        }

        public CounterClass getCounter() {
            return counter;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
