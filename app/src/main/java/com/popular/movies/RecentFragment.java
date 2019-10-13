package com.popular.movies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.popular.movies.Adapter.MoviesRecyclerAdapter;
import com.popular.movies.Model.Movies;
import com.popular.movies.Utils.Constant;
import com.popular.movies.Utils.SpacesItemDecoration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class RecentFragment extends Fragment {
    private Activity mParentActivity;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private MoviesRecyclerAdapter adapter;
    private String string;

    public static RecentFragment newInstance(String string) {
        RecentFragment f = new RecentFragment();
        Bundle args = new Bundle();
        args.putString("CATEGORIES", string);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            string = getArguments().getString("CATEGORIES");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recent, container, false);

        queue    	 = Volley.newRequestQueue(mParentActivity);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        pDialog      = new ProgressDialog(mParentActivity);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mParentActivity, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    // 1 : means is showing item, 0: means loading text
                    case 0:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.product_list_item_padding), false));
        getMovies();

        return view;
    }

    public void getMovies() {
        pDialog.show();
        String strURL = Constant.URL + "&category=" + string;
        System.out.println("getMovies (url) : " + strURL);

        queue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                System.out.println("getMovies (response) : " + strResponse);
                try {
                    ArrayList<Movies> moviesArrayList = new ArrayList<>();
                    JSONObject object = new JSONObject(strResponse);
                    JSONArray jsonArray = object.getJSONArray("articles");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Movies movies   = new Movies();
                        movies.Title    = jsonObject.getString("title");
                        movies.Synopsis = jsonObject.getString("url");
                        movies.Pic      = jsonObject.getString("urlToImage");
                        moviesArrayList.add(movies);
                    }

                    adapter = new MoviesRecyclerAdapter(mParentActivity, moviesArrayList);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity)
            mParentActivity = (MainActivity) activity;
        if (mParentActivity == null)
            mParentActivity = MainActivity.getInstance();
    }
}
