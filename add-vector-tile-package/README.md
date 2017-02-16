Add Local Vector Tiled Package
===================

![A snapshot for the add-vector-tiled-package](https://github.com/zssai/arcgis-runtime-sdk-for-android-demos/blob/master/add-vector-tile-package/add-vector-tiled-package.png)

The Add Local Vector Tiled Package app is a basic Map app for the ArcGIS Runtime SDK for Android using an ArcGISVectorTiledLayer basemap layer from a local vector tiled package on the device, which doesn't exist in [Esri/arcgis-runtime-samples-android](https://github.com/Esri/arcgis-runtime-samples-android).  It shows how to inflate a MapView in the layout XML of the activity, create a Vector Tiled Layer from a local ArcGIS Vector Tiled Package and bind that to a Basemap. The Basemap is used to create a Map which is used inside of the MapView. By default, this map supports basic zooming and panning operations.

----------


Features
-------------
Main API classes used in the app:

- ArcGISMap
- MapView
- ArcGISVectorTiledLayer
- Basemap