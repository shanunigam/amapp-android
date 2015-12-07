package com.amapp.weeklyquote;

import android.content.ContentValues;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.amapp.AMAppMasterActivity;
import com.amapp.R;
import com.amapp.common.AMConstants;
import com.amapp.common.ExtendedViewPager;
import com.android.volley.toolbox.NetworkImageView;
import com.smart.caching.SmartCaching;
import com.smart.framework.Constants;
import com.smart.framework.SmartUtils;
import com.smart.weservice.SmartWebManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ddesai on 12/7/15.
 */
public class WeeklyQuoteActivity extends AMAppMasterActivity implements Constants {

    private ExtendedViewPager viewPager;
    private ArrayList<ContentValues> quotes = new ArrayList<>();
    private ArrayList<ContentValues> quotesImages = new ArrayList<>();
    private SmartCaching smartCaching;
    private boolean isCachedDataDisplayed = false;


    @Override
    public void preOnCreate() {
    }

    @Override
    public View getLayoutView() {
        return null;
    }

    @Override
    public int getLayoutID() {
        return R.layout.temple_gallery_activity;
    }

    @Override
    public void manageAppBar(ActionBar actionBar, Toolbar toolbar, ActionBarDrawerToggle actionBarDrawerToggle) {
        toolbar.setTitle(AMConstants.AM_Application_Title);
        SpannableString spannableString=new SpannableString(getString(R.string.nav_quote_of_the_day_title));
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
        toolbar.setSubtitle(spannableString);
    }

    @Override
    public void initComponents() {
        viewPager= (ExtendedViewPager) findViewById(R.id.viewPager);
        smartCaching = new SmartCaching(this);
    }

    @Override
    public void prepareViews() {
        getCachedWeeklyQuote();
//        quotesImages=new SmartCaching(this).parseResponse(quotes.get(0),"images").get("images");
        viewPager.setAdapter(new WeeklyQuotePagerAdapter(quotesImages));
    }

    @Override
    public void setActionListeners() {
    }

    @Override
    public int getDrawerLayoutID() {
        return 0;
    }

    private void getCachedWeeklyQuote() {
        ArrayList<ContentValues> quotes = new SmartCaching(this).getDataFromCache("quotes");

        if (quotes == null || quotes.size() <= 0) {
            isCachedDataDisplayed = false;
            getTemples();
        } else {
            this.quotes = quotes;
            //setTempleDataInFragments(temples, isCachedDataDisplayed);
            isCachedDataDisplayed = true;
        }
    }

    private void getTemples() {
        HashMap<SmartWebManager.REQUEST_METHOD_PARAMS, Object> requestParams = new HashMap<>();
        requestParams.put(SmartWebManager.REQUEST_METHOD_PARAMS.CONTEXT,this);
        requestParams.put(SmartWebManager.REQUEST_METHOD_PARAMS.PARAMS, null);
        requestParams.put(SmartWebManager.REQUEST_METHOD_PARAMS.REQUEST_TYPES, SmartWebManager.REQUEST_TYPE.JSON_OBJECT);
        requestParams.put(SmartWebManager.REQUEST_METHOD_PARAMS.TAG,AMConstants.AMS_Request_Get_Quotes_Tag);
        requestParams.put(SmartWebManager.REQUEST_METHOD_PARAMS.URL, AMConstants.AMS_Domain_Url+AMConstants.AMS_QuoteOfTheWeek_Endpoint);
        requestParams.put(SmartWebManager.REQUEST_METHOD_PARAMS.RESPONSE_LISTENER, new SmartWebManager.OnResponseReceivedListener() {

            @Override
            public void onResponseReceived(final JSONObject response, String errorMessage) {

                if (errorMessage != null && errorMessage.equalsIgnoreCase(getString(R.string.no_content_found))) {
                    SmartUtils.showSnackBar(WeeklyQuoteActivity.this, getString(R.string.no_gym_found), Snackbar.LENGTH_LONG);
                } else {
                    try {
                        smartCaching.cacheResponse(response.getJSONArray("quotes"), "quotes", true, new SmartCaching.OnResponseParsedListener() {
                            @Override
                            public void onParsed(HashMap<String, ArrayList<ContentValues>> mapTableNameAndData) {
                                quotes = mapTableNameAndData.get("quotes");
                                quotesImages=smartCaching.parseResponse(quotes.get(0),"images").get("images");
                                viewPager.setAdapter(new WeeklyQuotePagerAdapter(quotesImages));
                                //viewPager.getAdapter().notifyDataSetChanged();
                                //setTempleDataInFragments(temples, isCachedDataDisplayed);
                            }
                        }, "images");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        SmartWebManager.getInstance(getApplicationContext()).addToRequestQueue(requestParams, null, !isCachedDataDisplayed);
    }


    public class WeeklyQuotePagerAdapter extends PagerAdapter {

        ArrayList<ContentValues> images;

        public WeeklyQuotePagerAdapter(ArrayList<ContentValues> images) {
            this.images=images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((FrameLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = LayoutInflater.from(WeeklyQuoteActivity.this).inflate(R.layout.temple_pager_item, container, false);
            final NetworkImageView imgTemple= (NetworkImageView) itemView.findViewById(R.id.imgTemple);
            if(position==0){
                setViewTransitionName(imgTemple,"image");
            }
            imgTemple.setImageUrl(images.get(position).getAsString("image"), SmartWebManager.getInstance(WeeklyQuoteActivity.this).getImageLoader());
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((FrameLayout) object);
        }

    }

}
