/*
 * Copyright (c) 2016. Anoopam Mission. This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License at "http://www.gnu.org/licenses/licenses.html" for more details.
 *
 */

package org.anoopam.main.anoopamaudio;

import android.content.ContentValues;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;

import org.anoopam.ext.smart.customviews.Log;
import org.anoopam.ext.smart.customviews.SmartTextView;
import org.anoopam.main.AMAppMasterActivity;
import org.anoopam.main.R;
import org.anoopam.main.common.AMConstants;
import org.anoopam.main.common.CircleImageView;

import java.io.IOException;

public class AudioPlayerActivity extends AMAppMasterActivity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    public static  final String AUDIO = "audio";
    public static  final String AUDIO_CAT = "audio_cat";
    private static final String TAG = "AudioPlayerActivity";

    private FrameLayout frmMain;
    private CardView cardView;
    private SmartTextView txtCatName;
    private CircleImageView imgAudio;
    private SmartTextView txtAudioTitle;
    private SmartTextView txtAudioDuration;
    private ContentValues audioDetails;
    private SmartTextView txtAudioLoading;
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;

    private Handler handler = new Handler();

    String catName;


    @Override
    public View getLayoutView() {
        return null;
    }

    @Override
    public int getLayoutID() {
        return R.layout.audio_player;
    }

    @Override
    public View getFooterLayoutView() {
        return null;
    }

    @Override
    public int getFooterLayoutID() {
        return 0;
    }

    @Override
    public View getHeaderLayoutView() {
        return null;
    }

    @Override
    public int getHeaderLayoutID() {
        return 0;
    }

    @Override
    public void setAnimations() {
        super.setAnimations();
        getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        getWindow().setReturnTransition(new Slide(Gravity.BOTTOM));
    }

    @Override
    public void preOnCreate() {

    }

    @Override
    public void initComponents() {
        super.initComponents();
        frmMain = (FrameLayout)findViewById( R.id.frmMain );
        cardView = (CardView)findViewById( R.id.cardView );
        imgAudio = (CircleImageView)findViewById( R.id.imgAudio );
        txtAudioTitle = (SmartTextView)findViewById( R.id.txtAudioTitle );
        txtAudioDuration = (SmartTextView)findViewById( R.id.txtAudioDuration );
        txtAudioLoading = (SmartTextView)findViewById( R.id.txtAudioLoading);
        txtCatName = (SmartTextView)findViewById( R.id.txtCatName);


        processIntent();
    }

    @Override
    public void prepareViews() {

        txtCatName.setText(catName);
        txtAudioTitle.setText(audioDetails.getAsString("audioTitle"));
        txtAudioDuration.setText("Duration : " + audioDetails.getAsString("duration"));

        if(audioDetails.containsKey("audioImage")){
//            SmartApplication.REF_SMART_APPLICATION.getAQuery().id(imgAudio).image(audioDetails.getAsString("audioImage"),true,true,getDeviceWidth(),0,new BitmapAjaxCallback(){
//                @Override
//                protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
//                    super.callback(url, iv, bm, status);
//
//                }
//            });
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);

        mediaController = new MediaController(this);

        try {
            mediaPlayer.setDataSource(audioDetails.getAsString("audioURL"));
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setActionListeners() {
        super.setActionListeners();

        frmMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaController.show();
            }
        });

        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        txtAudioLoading.setText("Loading");
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        txtAudioLoading.setText("Playing");
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDrawerItem(NAVIGATION_ITEMS.ANOOPAM_AUDIO);

    }

    @Override
    public void manageAppBar(ActionBar actionBar, Toolbar toolbar, ActionBarDrawerToggle actionBarDrawerToggle) {
        toolbar.setTitle(AMConstants.AM_Application_Title);
        SpannableString spannableString=new SpannableString(getString(R.string.app_subtitle));
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
        toolbar.setSubtitle(spannableString);
    }

    @Override
    public boolean shouldKeyboardHideOnOutsideTouch() {
        return false;
    }

    private void processIntent() {

        if(getIntent()!=null && getIntent().getExtras()!=null){

            if(getIntent().getExtras().get(AUDIO)!=null){
                audioDetails = (ContentValues) getIntent().getExtras().get(AUDIO);
            }

            catName = getIntent().getStringExtra(AUDIO_CAT);

        }
    }

    @Override
    public void onBackPressed() {
        try {
            if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer=null;

        }catch (Exception e){
            e.printStackTrace();
        }

        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //the MediaController will hide after 3 seconds - tap the screen to make it appear again
        mediaController.show();
        return false;
    }


    @Override
    public void start() {
        mediaPlayer.start();
        txtAudioLoading.setText("Playing");
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
        txtAudioLoading.setText("Paused");

    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared");
        txtAudioLoading.setText("Playing");
        mediaPlayer.start();
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(frmMain);

        handler.post(new Runnable() {
            public void run() {
                try {
                    mediaController.setEnabled(true);
                    mediaController.show();
                }catch (Throwable e){
                    e.printStackTrace();
                }

            }
        });

    }
}


