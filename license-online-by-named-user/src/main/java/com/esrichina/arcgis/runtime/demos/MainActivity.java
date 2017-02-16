package com.esrichina.arcgis.runtime.demos;


import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.LicenseInfo;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.security.UserCredential;


public class MainActivity extends AppCompatActivity{
    private MapView mMapView;
    private Basemap basemap;
    private ArcGISMap mMap;
    private Portal portal;

    private String strPortalUrl;
    private String strUserName;
    private String strPassword;
    private CustomizeDialog customizeDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle(getResources().getString(R.string.progressdialog_title));
        progressDialog.setMessage(getResources().getString(R.string.progressdialog_message));

        mMapView = (MapView) findViewById(R.id.mapView);
        basemap = new Basemap(new ArcGISTiledLayer("http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunityENG/MapServer"));
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
        if (id == R.id.menu_authorize){
            showCustomizeDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_continue:
                    strPortalUrl = ((EditText)(customizeDialog.getEditTextView("portalURL"))).getText().toString();
                    strUserName = ((EditText)(customizeDialog.getEditTextView("username"))).getText().toString();
                    strPassword = ((EditText)(customizeDialog.getEditTextView("password"))).getText().toString();
                    UserCredential credential = new UserCredential(strUserName,strPassword);
                    portal = new Portal(strPortalUrl);
                    portal.setCredential(credential);
                    portal.addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {
                            LicenseInfo licenseInfo = portal.getPortalInfo().getLicenseInfo();
                            ArcGISRuntimeEnvironment.setLicense(licenseInfo);
                            progressDialog.dismiss();
                        }
                    });
                    portal.loadAsync();
                    customizeDialog.dismiss();
                    progressDialog.show();
                    break;
                case R.id.btn_cancel:
                    customizeDialog.dismiss();
                    break;

            }
        }
    };

    public void showCustomizeDialog(){

        customizeDialog = new CustomizeDialog(MainActivity.this, onClickListener);
        customizeDialog.show();
    }

}
