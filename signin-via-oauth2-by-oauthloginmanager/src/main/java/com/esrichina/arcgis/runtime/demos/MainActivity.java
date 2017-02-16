package com.esrichina.arcgis.runtime.demos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.LicenseInfo;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.security.OAuthLoginManager;
import com.esri.arcgisruntime.security.OAuthTokenCredential;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private Basemap basemap;
    private ArcGISMap arcGISMap;
    private Portal portal;
    private TextView tvLoggedUser;

    private ProgressDialog progressDialog;
    private OAuthLoginManager oAuthLoginManager;
    private OAuthTokenCredential oauthCred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Authorize your application");
        progressDialog.setMessage("Please wait....");
        progressDialog.show();

        Intent intent = getIntent();
        // Get the OAuthLoginManager object from the main activity.
        oAuthLoginManager = SplashActivity.getOAuthLoginManagerInstance();
        if (oAuthLoginManager == null) {
            return;
        }
        fetchCredentials(intent);

        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        tvLoggedUser = (TextView)findViewById(R.id.tvLoggedUser);

        basemap = new Basemap(new ArcGISTiledLayer(getResources().getString(R.string.basemap_url)));
        arcGISMap = new ArcGISMap(basemap);
        mapView.setMap(arcGISMap);
    }

    /**
     * fetch the OAuth token from the OAuthLogin Manager
     */
    private void fetchCredentials(Intent intent) {
        // Fetch oauth access token.
        final ListenableFuture<OAuthTokenCredential> future = oAuthLoginManager.fetchOAuthTokenCredentialAsync(intent);
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    oauthCred = future.get();
                    String[] portalSettings = getResources().getStringArray(R.array.portal);
                    // create a Portal using the portal url from the array
                    portal = new Portal(portalSettings[0], true);
                    // set the credentials from the browser
                    portal.setCredential(oauthCred);
                    portal.loadAsync();
                    portal.addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {
                            // if portal is LOADED, display the logged-in user and authorize the app with the user
                            if (portal.getLoadStatus() == LoadStatus.LOADED) {
                                tvLoggedUser.setText("The logged in user: " + oauthCred.getUsername());
                                LicenseInfo licenseInfo = portal.getPortalInfo().getLicenseInfo();
                                ArcGISRuntimeEnvironment.setLicense(licenseInfo);
                                progressDialog.dismiss();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
