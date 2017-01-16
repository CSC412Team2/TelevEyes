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
import java.util.Iterator;
import java.util.List;

import edu.ecsu.csc412.televeyes.adapter.RecyclerViewAdapter;
import edu.ecsu.csc412.televeyes.model.Show;
import edu.ecsu.csc412.televeyes.tv.Heroku;
import edu.ecsu.csc412.televeyes.view.DividerItemDecoration;

public class DiscoverFragment extends Fragment {
    private RecyclerViewAdapter adapter;

    private ArrayList<Show> shows;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DiscoverFragment() {
    }

    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover_list, container, false);

        /**
         * set the adapter
         */
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(adapter);

            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
            refreshShows();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (adapter == null) {
            shows = new ArrayList<>();
            adapter = new RecyclerViewAdapter(shows, RecyclerViewAdapter.ListType.DISCOVER);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Refresh show list
     */
    private void refreshShows() {
        Heroku.getInstance().getSchedule(15, new Heroku.OnShowSearchListener() {
            @Override
            public void onResults(List<Show> shows) {
                //Get the list view
                RecyclerView view = (RecyclerView) getView();

                if (adapter == null) {
                    shows = new ArrayList<>();
                    adapter = new RecyclerViewAdapter(shows, RecyclerViewAdapter.ListType.DISCOVER);
                }

                adapter.clearItems();


                //If the list view isn't null then set a new adapter
                if (view != null) {
                    for (Show show : shows) {
                        adapter.addShow(show);
                    }
                    view.invalidate();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                refreshShows();
            }
        });
    }

    public void refreshShows(final String cat) {
        if (cat.compareToIgnoreCase("all") == 0) {
            refreshShows();
        } else {
            Heroku.getInstance().getSchedule(100, new Heroku.OnShowSearchListener() {
                @Override
                public void onResults(List<Show> results) {
                    //Get the list view
                    RecyclerView view = (RecyclerView) getView();

                    Iterator it = results.iterator();

                    while (it.hasNext()) {
                        boolean fits = false;
                        Show show = (Show) it.next();
                        List<String> genres = show.getGenres();
                        for (String genre : genres) {
                            if (cat.compareToIgnoreCase(genre) == 0) {
                                fits = true;
                            }
                        }

                        if (!fits) it.remove();
                    }

                    adapter.clearItems();
                    //If the list view isn't null then set a new adapter
                    if (view != null) {
                        for (int i = 0; i < results.size(); i++) {
                            adapter.addShow(results.get(i));
                        }
                        view.invalidate();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    refreshShows(cat);
                }
            });
        }
    }
}
