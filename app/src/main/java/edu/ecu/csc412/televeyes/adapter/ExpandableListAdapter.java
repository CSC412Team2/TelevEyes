package edu.ecu.csc412.televeyes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import edu.ecu.csc412.televeyes.R;
import edu.ecu.csc412.televeyes.model.Episode;
import edu.ecu.csc412.televeyes.model.Season;

/**
 * Created by bi00dsh0t on 11/23/16.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    List<Season> seasons;
    List<Episode> episodes;

    Context context;


    public ExpandableListAdapter(List<Season> seasons, List<Episode> episodes, Context context){
        this.seasons = seasons;
        this.episodes = episodes;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return seasons.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;

        //TODO Crash occurs here sometimes... fix it later
        for (int i = 0; seasons.get(groupPosition).getNumber() == episodes.get(i).getSeason(); i++) {
            count++;
        }

        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return seasons.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return episodes.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
       return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Season season = (Season) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.season_header, null);
        }

        TextView number = (TextView) convertView.findViewById(R.id.season_number);

        number.setText(String.valueOf("Season #" + season.getNumber()));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Episode episode = (Episode) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.season_item, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.episode_name);

        textView.setText(episode.getNumber() + ". " + episode.getName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
