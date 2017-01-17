package com.esrichina.arcgis.runtime.demos;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.arcgisruntime.layers.ArcGISVectorTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inflate MapView from layout
        mMapView = (MapView) findViewById(R.id.mapView);
        // create new Vector Tiled Layer from a local vector tile package
        ArcGISVectorTiledLayer vectorTiledLayerBaseMap = new ArcGISVectorTiledLayer(Environment.getExternalStorageDirectory() + "/arcgis/china.vtpk");
        // set vector tiled layer as basemap
        Basemap basemap = new Basemap(vectorTiledLayerBaseMap);
        // create a map with the basemap
        ArcGISMap map = new ArcGISMap(basemap);
        // set the map to be displayed in this view
        mMapView.setMap(map);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
    }
}
