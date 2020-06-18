package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.unic.unic_vendor_final_1.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class LocationSelector extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback {

    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1005;

    private MapView mapView;
    private MapboxMap mapboxMap;
    private Button selectLocationButton;
    private ImageView hoveringMarker;
    private Layer droppedMarkerLayer;
    private Point initPoint;
    private MapboxGeocoding mapboxGeocoding;
    private MapSnapshotter mapSnapshotter;
    private boolean hasStartedSnapshotGeneration;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.map_api_key));
        setContentView(R.layout.activity_location_selector);

        String address = getIntent().getStringExtra("address");
        if (address !=null)
            Geocode(address);

        type = getIntent().getIntExtra("type",0);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        initSearchFab();
        initLocationFinder();

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        LocationSelector.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull final Style style) {
                enableLocationPlugin();

                hoveringMarker = new ImageView(LocationSelector.this);
                hoveringMarker.setImageResource(R.drawable.ic_place_red_24dp);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                hoveringMarker.setLayoutParams(params);
                mapView.addView(hoveringMarker);

                initDroppedMarker(style);

                hoveringMarker.setVisibility(View.VISIBLE);

// Hide the selected location SymbolLayer
                droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                if (droppedMarkerLayer != null) {
                    droppedMarkerLayer.setProperties(visibility(NONE));
                }

                LocationComponent locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(
                        LocationSelector.this, style).build());
                locationComponent.setLocationComponentEnabled(true);

                if (type == 0) {

// Set the component's camera mode
                    locationComponent.setCameraMode(CameraMode.TRACKING);
                    locationComponent.setRenderMode(RenderMode.NORMAL);
                }

                selectLocationButton = findViewById(R.id.select_location_button);
                selectLocationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final LatLng mapTargetLatLng = mapboxMap.getCameraPosition().target;
                        selectLocationButton.setEnabled(false);

                        if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                            GeoJsonSource source = style.getSourceAs("dropped-marker-source-id");
                            if (source != null) {
                                source.setGeoJson(Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));
                            }
                            droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                            if (droppedMarkerLayer != null) {
                                droppedMarkerLayer.setProperties(visibility(VISIBLE));
                            }
                        }

                        returnLocation(mapTargetLatLng);

                    }
                });
            }
        });
    }

    private void initDroppedMarker(@NonNull Style loadedMapStyle) {
// Add the marker image to map
        loadedMapStyle.addImage("dropped-icon-image", Objects.requireNonNull(getDrawable(R.drawable.ic_place_red_24dp)));
        loadedMapStyle.addSource(new GeoJsonSource("dropped-marker-source-id"));
        loadedMapStyle.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id").withProperties(
                iconImage("dropped-icon-image"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true),
                visibility(NONE)
        ));
    }

    private void initLocationFinder(){
        findViewById(R.id.get_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PermissionsManager.areLocationPermissionsGranted(LocationSelector.this)){
                    LocationComponent locationComponent = mapboxMap.getLocationComponent();
                    locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(
                            LocationSelector.this, Objects.requireNonNull(mapboxMap.getStyle())).build());
                    locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
                    locationComponent.setCameraMode(CameraMode.TRACKING);
                    locationComponent.setRenderMode(RenderMode.NORMAL);
                }
            }
        });
    }

    private void startSnapShot(LatLngBounds latLngBounds, int height, int width,LatLng location) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (mapSnapshotter == null) {
// Initialize snapshotter with map dimensions and given bounds
                    MapSnapshotter.Options options =
                            new MapSnapshotter.Options(width, height)
                                    .withRegion(latLngBounds)
                                    .withCameraPosition(mapboxMap.getCameraPosition())
                                    .withStyle(style.getUri());

                    mapSnapshotter = new MapSnapshotter(LocationSelector.this, options);
                } else {
// Reuse pre-existing MapSnapshotter instance
                    mapSnapshotter.setSize(width, height);
                    mapSnapshotter.setRegion(latLngBounds);
                    mapSnapshotter.setCameraPosition(mapboxMap.getCameraPosition());
                }

                mapSnapshotter.start(new MapSnapshotter.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(MapSnapshot snapshot) {

                        Bitmap bitmapOfMapSnapshotImage = snapshot.getBitmap();

                        Uri bmpUri = getLocalBitmapUri(bitmapOfMapSnapshotImage);

                        Intent intent = new Intent();
                        intent.putExtra("latitude",location.getLatitude());
                        intent.putExtra("longitude",location.getLongitude());
                        intent.setData(bmpUri);
                        setResult(RESULT_OK,intent);
                        finish();

                        hasStartedSnapshotGeneration = false;
                    }
                });
            }
        });
    }

    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            try {
                out.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            bmpUri = Uri.fromFile(file);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mapboxGeocoding!=null)
            mapboxGeocoding.cancelCall();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin() {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component. Adding in LocationComponentOptions is also an optional
// parameter
        } else {
            PermissionsManager permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted && mapboxMap != null) {
            Style style = mapboxMap.getStyle();
            if (style != null) {
                enableLocationPlugin();
            }
        } else {
            Toast.makeText(this, "Location was not granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initSearchFab() {
        findViewById(R.id.location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(getString(R.string.map_api_key))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(LocationSelector.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
    }

    private void Geocode(String address){
        mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(getString(R.string.map_api_key))
                .query(address)
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {

                assert response.body() != null;
                List<CarmenFeature> results = response.body().features();

                if (results.size() > 0) {

                    // Log the first results Point.
                    initPoint = results.get(0).center();
                    assert initPoint != null;
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(initPoint.latitude(),initPoint.longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                    Timber.d("onResponse: %s", initPoint.toString());

                } else {

                    LocationComponent locationComponent = mapboxMap.getLocationComponent();
                    locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(
                            LocationSelector.this, Objects.requireNonNull(mapboxMap.getStyle())).build());
                    locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
                    locationComponent.setCameraMode(CameraMode.TRACKING);
                    locationComponent.setRenderMode(RenderMode.NORMAL);

                    // No result for your request were found.
                    Timber.d("onResponse: No result found");

                }
            }

            @Override
            public void onFailure(@NotNull Call<GeocodingResponse> call, @NotNull Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void returnLocation(LatLng location){

        LatLng topRight = new LatLng();
        topRight.setLatitude(location.getLatitude()+0.0005);
        topRight.setLongitude(location.getLongitude()+0.0005);
        topRight.setAltitude(location.getAltitude());

        LatLng bottomLeft = new LatLng();
        bottomLeft.setLatitude(location.getLatitude()-0.0005);
        bottomLeft.setLongitude(location.getLongitude()-0.0005);
        bottomLeft.setAltitude(location.getAltitude());

        LatLngBounds latLngBounds = new LatLngBounds.Builder().include(location).include(topRight).include(bottomLeft).build();

        startSnapShot(latLngBounds,(int)dpToPx(200),(int)dpToPx(300),location);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs("geojsonSourceLayerId");
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

// Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) Objects.requireNonNull(selectedCarmenFeature.geometry())).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
        }
    }

    private float dpToPx(int dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
