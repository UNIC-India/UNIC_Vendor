package com.unic.unic_vendor_final_1.views.helpers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.unic.unic_vendor_final_1.R;

import org.jetbrains.annotations.NotNull;

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
    private  int type;

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

                if (type == 0) {
                    LocationComponent locationComponent = mapboxMap.getLocationComponent();
                    locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(
                            LocationSelector.this, style).build());
                    locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
                    locationComponent.setCameraMode(CameraMode.TRACKING);
                    locationComponent.setRenderMode(RenderMode.NORMAL);
                }

                selectLocationButton = findViewById(R.id.select_location_button);
                selectLocationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final LatLng mapTargetLatLng = mapboxMap.getCameraPosition().target;

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
        Intent intent = new Intent();
        intent.putExtra("latitude",location.getLatitude());
        intent.putExtra("longitude",location.getLongitude());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

// Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

// Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
// Then retrieve and update the source designated for showing a selected location's symbol layer icon

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
}
