package rm.ibanc.md.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import rm.ibanc.md.adapter.NewsAdapter;
import rm.ibanc.md.constant.UrlConstant;
import rm.ibanc.md.entites.rest.NewsCategory;
import rm.ibanc.md.entites.view.NewsCategoryView;
import rm.ibanc.md.ibanc_rm.R;


public class NewsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<NewsCategoryView> newsCategoryList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        initCollapsingToolbar(view);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        newsCategoryList = new ArrayList<>();
        adapter = new NewsAdapter(getActivity(), newsCategoryList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareNews();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar(View view) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("News");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few News for testing
     */
    private void prepareNews() {
        String url = UrlConstant.findNewsCategory;

        GetNewsCategoryTask getNewsCategoryTask = new GetNewsCategoryTask(url);
        getNewsCategoryTask.execute((Void) null);

    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    public class GetNewsCategoryTask extends AsyncTask<Void, Void, NewsCategory[]> {

        private final String url;
        private ProgressDialog progressDialog;

        public GetNewsCategoryTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Getting Data ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected NewsCategory[] doInBackground(Void... params) {


            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                ResponseEntity<NewsCategory[]> responseEntity = restTemplate.getForEntity(url, NewsCategory[].class);
                return responseEntity.getBody();
            } catch (HttpClientErrorException e) {
                System.out.println("--------> HttpClientErrorException ====> " + e.toString());
                return null;
            } catch (ResourceAccessException ex) {
                System.out.println("--------> ResourceAccessException ====> " + ex.toString());
                return null;
            } catch (Exception ex) {
                System.out.println("--------> Exception ====> " + ex.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(NewsCategory[] newsCategories) {
            progressDialog.dismiss();

            if (newsCategories == null) {
                showToastMessage("Sunt probleme cu conexiunea, verificati conexiunea cu internetul");
                return;
            }

            for (int i = 0; i < newsCategories.length; i++) {
                int resId = getResources().getIdentifier(newsCategories[i].getImagePath(), "drawable", getActivity().getPackageName());
                NewsCategoryView a = new NewsCategoryView(newsCategories[i].getNameCategory(), newsCategories[i].getNumberOfNews(), resId);
                newsCategoryList.add(a);
            }
            adapter.notifyDataSetChanged();
        }


        private void showToastMessage(final String showText) {
            final Context context = getActivity();
            Handler handler = new Handler(context.getMainLooper());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, showText, duration);
                    toast.show();
                }
            });
        }

    }

}