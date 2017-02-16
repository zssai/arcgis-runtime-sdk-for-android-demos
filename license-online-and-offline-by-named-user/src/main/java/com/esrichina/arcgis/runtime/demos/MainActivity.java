package com.esrichina.arcgis.runtime.demos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.LicenseInfo;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.security.UserCredential;


public class MainActivity extends AppCompatActivity {
    private MapView mMapView;
    private ArcGISMap mMap;
    private Portal portal;
    private Boolean boolean_offline;
    private String masterPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        masterPassword = getResources().getString(R.string.master_password);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Authorize your application");
        progressDialog.setMessage("Please wait....");

        authorizeOffline();

        mMapView = (MapView) findViewById(R.id.mapView);
        Basemap basemap = new Basemap(new ArcGISTiledLayer("http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunityENG/MapServer"));
        mMap = new ArcGISMap(basemap);
        mMapView.setMap(mMap);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.authorize) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getResources().getString(R.string.dialog_title));
            builder.setMessage(getResources().getString(R.string.dialog_message));
            builder.setPositiveButton(getResources().getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    authorizeOnline(true);

                }
            });
            builder.setNegativeButton(getResources().getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    authorizeOnline(false);
                }
            });
            builder.setNeutralButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                }
            });
            builder.show();

        }
        return super.onOptionsItemSelected(item);
    }

    public void authorizeOnline(boolean offline){
        progressDialog.show();
        UserCredential credential = new UserCredential(getResources().getString(R.string.user_name),getResources().getString(R.string.user_password));
        portal = new Portal(getResources().getString(R.string.portal_url));
        portal.setCredential(credential);
        boolean_offline = offline;
        portal.addDoneLoadingListener(new Runnable() {

            @Override
            public void run() {
                LicenseInfo licenseInfo = portal.getPortalInfo().getLicenseInfo();
                ArcGISRuntimeEnvironment.setLicense(licenseInfo);
                progressDialog.dismiss();
                if (boolean_offline) {
                    String strLicenseInfo = licenseInfo.toJson();
                    String strCryptoLicenseInfo = "";
                    try {
                        strCryptoLicenseInfo = LicenseInfoCrypto.encrypt(masterPassword,strLicenseInfo);
                        LicenseOffline.saveFile(strCryptoLicenseInfo,getApplicationContext().getFilesDir().getAbsolutePath() + "/" + getResources().getString(R.string.license_file_name));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        portal.loadAsync();
    }

    public void authorizeOffline(){
        String strLicenseFilePath = getApplicationContext().getFilesDir().getAbsolutePath().concat("/license.txt");
        if(LicenseOffline.doesExist(strLicenseFilePath)) {
            String strCryptoLicenseInfo = LicenseOffline.readFile(strLicenseFilePath);
            String strLicenseInfo = "";
            try {
                strLicenseInfo = LicenseInfoCrypto.decrypt(masterPassword, strCryptoLicenseInfo);
                ArcGISRuntimeEnvironment.setLicense(LicenseInfo.fromJson(strLicenseInfo));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
