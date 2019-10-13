package com.popular.movies;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import com.popular.movies.Utils.FontCache;
import com.popular.movies.Utils.TypeFaceSpan;
import com.viewpagerindicator.UnderlinePageIndicator;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Typeface fontLatoHeavy;
    private ViewPager viewPager;
    private UnderlinePageIndicator underlinePageIndicator;
    private Button btnRecent, btnPopular;
    private ArrayList<String> arrMenu = new ArrayList<String>();
    private Toolbar toolbar;
    private static MainActivity activityInstance;
    public static MainActivity getInstance() {
        return activityInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_main);
        initToolbar();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        activityInstance = this;
        fontLatoHeavy    = FontCache.get(this, "Lato-Heavy");

        arrMenu.add("{\"cat_name\":\"Business\"}");
        arrMenu.add("{\"cat_name\":\"Entertainment\"}");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setTag("pager");
        viewPager.setOffscreenPageLimit(6);
        viewPager.setAdapter(new EventPagerAdapter(getSupportFragmentManager()));

        btnRecent = (Button) findViewById(R.id.btnRecent);
        btnRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0) {
                    btnRecent.setBackgroundColor(getResources().getColor(R.color.colorBgSelected));
                } else {
                    viewPager.setCurrentItem(0);
                }
            }
        });
        viewPager.setCurrentItem(0);

        btnPopular = (Button) findViewById(R.id.btnPopular);
        btnPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);

            }
        });

        btnRecent.setTypeface(fontLatoHeavy);
        btnPopular.setTypeface(fontLatoHeavy);

        underlinePageIndicator = (UnderlinePageIndicator) findViewById(R.id.underlinePageIndicator);
        underlinePageIndicator.setFades(false);
        underlinePageIndicator.setSelectedColor(getResources().getColor(R.color.colorSelected));
        underlinePageIndicator.setViewPager(viewPager);

        underlinePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                if (position==0) {
                    btnPopular.setBackgroundColor(getResources().getColor(R.color.colorBgUnselected));
                    btnPopular.setTextColor(getResources().getColor(R.color.colorUnselected));
                    btnRecent.setBackgroundColor(getResources().getColor(R.color.colorBgSelected));
                    btnRecent.setTextColor(getResources().getColor(R.color.colorSelected));
                } else if (position==1) {
                    btnPopular.setBackgroundColor(getResources().getColor(R.color.colorBgSelected));
                    btnPopular.setTextColor(getResources().getColor(R.color.colorSelected));
                    btnRecent.setBackgroundColor(getResources().getColor(R.color.colorBgUnselected));
                    btnRecent.setTextColor(getResources().getColor(R.color.colorUnselected));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    private void initToolbar() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SpannableString spanToolbar = new SpannableString(getString(R.string.app_name) + " " + pInfo.versionName);
        spanToolbar.setSpan(new TypeFaceSpan(MainActivity.this, "Lato-Bold"), 0, spanToolbar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //Initiate Toolbar/ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(spanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class EventPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
        public EventPagerAdapter(FragmentManager fm) { super(fm); }
        @Override
        public Fragment getItem(int position) {
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(arrMenu.get(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //root
            if (jObject.optString("cat_name").equalsIgnoreCase("Business")) {
                return RecentFragment.newInstance(jObject.optString("cat_name").toLowerCase());
            } else {
                return RecentFragment.newInstance(jObject.optString("cat_name").toLowerCase());
            }
        }
        @Override
        public int getCount() { return 2; }
        @Override
        public CharSequence getPageTitle(int position) { return ""; }
        @Override
        public void onPageScrollStateChanged(int arg0) { }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) { }
        @Override
        public void onPageSelected(int arg0) {}
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}

