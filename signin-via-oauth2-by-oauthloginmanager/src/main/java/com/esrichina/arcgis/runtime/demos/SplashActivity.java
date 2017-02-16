package com.esrichina.arcgis.runtime.demos;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.esri.arcgisruntime.security.OAuthLoginManager;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 3000;
    private static OAuthLoginManager oAuthLoginManager;

    public static OAuthLoginManager getOAuthLoginManagerInstance() {
        return oAuthLoginManager;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                oAuthBrowser();
                SplashActivity.this.finish();

            }

        }, SPLASH_DISPLAY_LENGHT);
    }
    private void oAuthBrowser(){
        try{
            // create a OAuthLoginManager object with portalURL, clientID,redirectUri and expiration
            String[] portalSettings = getResources().getStringArray(R.array.portal);
            oAuthLoginManager = new OAuthLoginManager(portalSettings[0],portalSettings[1],portalSettings[2],0);
            // launch the browser to get the credentials
            oAuthLoginManager.launchOAuthBrowserPage(getApplicationContext());
        }catch (Exception e){
            Log.e("Error", e.getMessage() + "");
        }
    }
}

