package com.esrichina.arcgis.runtime.demos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.LicenseInfo;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.security.AuthenticationManager;
import com.esri.arcgisruntime.security.DefaultAuthenticationChallengeHandler;
import com.esri.arcgisruntime.security.OAuthConfiguration;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity{
    private MapView mMapView;
    private ArcGISMap mMap;
    private Portal portal;
    private MenuItem menuSignedIn;
    private MenuItem menuSignedOut;
    private String[] portalSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.mapView);

        Basemap basemap = new Basemap(new ArcGISTiledLayer("http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunityENG/MapServer"));
        mMap = new ArcGISMap(basemap);
        mMapView.setMap(mMap);

        portalSettings = getResources().getStringArray(R.array.portal);


        // Set up an authentication handle to be used when signing in Portal
        try {
            OAuthConfiguration oAuthConfiguration = new OAuthConfiguration(portalSettings[0],portalSettings[1],portalSettings[2]);
            DefaultAuthenticationChallengeHandler authenticationChallengeHandler = new DefaultAuthenticationChallengeHandler(MainActivity.this);
            AuthenticationManager.setAuthenticationChallengeHandler(authenticationChallengeHandler);
            AuthenticationManager.addOAuthConfiguration(oAuthConfiguration);
        } catch (MalformedURLException e) {
            Log.i("OAuth","OAuth problem : " + e.getMessage());
            Toast.makeText(MainActivity.this, "The was a problem authenticating against the portal.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menuSignedIn = menu.findItem(R.id.menuSignin);
        menuSignedOut = menu.findItem(R.id.menuSignout);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menuSignin) {
            portal = new Portal(portalSettings[0],true);
            portal.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    if (portal.getLoadStatus() == LoadStatus.LOADED) {

                        menuSignedIn.setTitle("Signed as " + portal.getCredential().getUsername());
                        menuSignedIn.setEnabled(false);
                        menuSignedOut.setEnabled(true);
                        LicenseInfo licenseInfo = portal.getPortalInfo().getLicenseInfo();
                        ArcGISRuntimeEnvironment.setLicense(licenseInfo);
                    }
                }
            });
            portal.loadAsync();
        }else if (id == R.id.menuSignout) {
            AuthenticationManager.clearOAuthConfigurations();
            try {
                AuthenticationManager.removeOAuthConfiguration(portalSettings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            menuSignedOut.setEnabled(false);
            menuSignedIn.setEnabled(true);
            menuSignedIn.setTitle("Sign In");

        }
        return super.onOptionsItemSelected(item);
    }
}
