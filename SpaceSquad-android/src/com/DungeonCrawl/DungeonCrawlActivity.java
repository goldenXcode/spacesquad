package com.DungeonCrawl;

import com.DungeonCrawl.DungeonCrawlMain;
import com.badlogic.gdx.backends.android.AndroidApplication;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

@SuppressWarnings("unused")
public class DungeonCrawlActivity extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
    	super.onCreate(savedInstanceState);
    	
    	//AndroidApplicationConfig config = new AndroidApplicationConfig();
    	
    //	config.numSamples = 2; 
    	
        initialize(new DungeonCrawlMain(),true);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}