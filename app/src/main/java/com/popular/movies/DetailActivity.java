package com.popular.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.popular.movies.Model.Movies;
import com.popular.movies.Utils.Constant;
import com.popular.movies.Utils.TypeFaceSpan;

public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Movies movies;
    private ImageView imageView;
    private TextView txtGenre, txtSynopsis;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_detail);

        movies      = getIntent().getParcelableExtra("MOVIES");
        imageView   = (ImageView) findViewById(R.id.imageView);
        txtGenre    = (TextView) findViewById(R.id.txtGenre);
        txtSynopsis = (TextView) findViewById(R.id.txtSynopsis);
        webView     = (WebView) findViewById(R.id.webView);

        initToolbar();
        /*Glide.with(DetailActivity.this).load(Constant.URL + "pictures/" + movies.Pic)
                .placeholder(R.drawable.img_loader).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).override(500, 500)
                .dontAnimate().into(imageView);
        txtGenre.setText(movies.Genre);
        txtSynopsis.setText(movies.Synopsis);*/
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(movies.Synopsis);
    }

    private void initToolbar() {
        SpannableString spanToolbar = new SpannableString(movies.Title);
        spanToolbar.setSpan(new TypeFaceSpan(DetailActivity.this, "Lato-Bold"), 0, spanToolbar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //Initiate Toolbar/ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(spanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}

