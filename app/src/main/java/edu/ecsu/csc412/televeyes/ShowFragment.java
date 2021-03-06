package edu.ecsu.csc412.televeyes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import edu.ecsu.csc412.televeyes.adapter.RecyclerViewAdapter;
import edu.ecsu.csc412.televeyes.database.DatabaseHelper;
import edu.ecsu.csc412.televeyes.model.Show;
import edu.ecsu.csc412.televeyes.tv.TVMaze;
import edu.ecsu.csc412.televeyes.view.DividerItemDecoration;


public class ShowFragment extends Fragment {

    private static DatabaseHelper databaseHelper;
    private static ShowFragment sInstance;
    private static String lastCat;
    private RecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShowFragment() {
        sInstance = this;
    }

    public static ShowFragment newInstance() {
        ShowFragment fragment = new ShowFragment();
        ShowFragment.databaseHelper = DatabaseHelper.getInstance();
        return fragment;
    }

    private static void refresh(boolean filter) {
        if (sInstance != null) {
            if (filter) {
                sInstance.refreshShows();
            } else {
                sInstance.refreshShows(lastCat);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_list, container, false);

        /**
         * set the adapter
         */
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            List<Show> items = new ArrayList<>();

            //If the list view isn't null then set a new ada
            //
            //
            //
            //
            //
            // pter
            adapter = new RecyclerViewAdapter(items, RecyclerViewAdapter.ListType.SAVES);

            recyclerView.setAdapter(adapter);
            view.invalidate();


            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
            refreshShows();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Refresh show list
     */
    private void refreshShows() {
        List<Integer> showIds = databaseHelper.getShowIds();
        for (int i = 0; i < showIds.size(); i++) {
            TVMaze.getInstance().getShowFromId(showIds.get(i), new TVMaze.OnShowLookupListener() {
                @Override
                public void onResult(Show show) {
                    adapter.addShow(show);
                    adapter.sortShows();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void refreshShows(final String category) {
        List<Integer> showIds = databaseHelper.getShowIds();
        for (int i = 0; i < showIds.size(); i++) {
            TVMaze.getInstance().getShowFromId(showIds.get(i), new TVMaze.OnShowLookupListener() {
                @Override
                public void onResult(Show show) {
                    for (String cat : show.getGenres()) {
                        if (cat.compareToIgnoreCase(category) == 0) {
                            adapter.addShow(show);
                        }
                    }
                    adapter.sortShows();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}