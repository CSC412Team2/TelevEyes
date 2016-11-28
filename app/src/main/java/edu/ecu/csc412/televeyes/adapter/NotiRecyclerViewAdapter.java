package edu.ecu.csc412.televeyes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import edu.ecu.csc412.televeyes.ApplicationSingleton;
import edu.ecu.csc412.televeyes.R;
import edu.ecu.csc412.televeyes.dummy.DummyContent.DummyItem;
import edu.ecu.csc412.televeyes.model.Show;
import edu.ecu.csc412.televeyes.tv.TVMaze;

public class NotiRecyclerViewAdapter extends RecyclerView.Adapter<NotiRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;

    public NotiRecyclerViewAdapter() {
        mValues = new ArrayList<>();
        TVMaze.getInstance().getSchedule(6, new TVMaze.OnShowSearchListener() {
            @Override
            public void onResults(List<Show> shows) {
                for (int i = 0; i < shows.size(); i++) {
                    mValues.add(shows.get(i).getName() + "    A new episode is about to air");
                    notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ApplicationSingleton.getAppContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notiitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemCount() != 0) {
            holder.mContentView.setText(mValues.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
