package com.workmanager.activity;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
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
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.necer.ndialog.ChoiceDialog;
import com.necer.ndialog.ConfirmDialog;
import com.workmanager.R;
import com.workmanager.entity.FieldEntity;
import com.workmanager.function.GetDataBase;
import com.workmanager.function.TaskFunction;
import com.workmanager.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class MMainActivity extends BaseNFCActivity implements View.OnClickListener, OnMapReadyCallback, MapboxMap.OnMapClickListener {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private TextView fanye;
    private TextView nav;
    private TextView local;
    private TextView mFarmTextView;
    private TextView mMenuTextView;
    private boolean flg = true;
    private SharedPreferences login;
    private FieldEntity ma =new FieldEntity();
    private static final String geoJsonSourceId = "geoJsonData";
    private static final String geoJsonLayerId = "polygonFillLayer";
    private File file;
    Style style;
    //11
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiNzIzNTQwMDE4IiwiYSI6ImNrMTRjaHc5aTBpcXczaHM5cXhvczVxbTIifQ.F3MXTpzKLgwiRqklCld4dQ");
        setContentView(R.layout.activity_main);

//        Toast.makeText(MMainActivity.this, R.string.app_name,
//                Toast.LENGTH_SHORT).show();

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        fanye = (TextView) findViewById(R.id.textView2);
        nav=(TextView)findViewById(R.id.nav);
        local=(TextView)findViewById(R.id.local);
        setclick();
        login=getSharedPreferences("login", MODE_PRIVATE);
        mFarmTextView = (TextView) findViewById(R.id.farm);
        mFarmTextView.setOnClickListener(this);
        mMenuTextView = (TextView) findViewById(R.id.Menu);
        mMenuTextView.setOnClickListener(this);
        Intent data= getIntent();
        if (data!=null){
            ma=(FieldEntity) data.getSerializableExtra("point");
        }
    }
    private void setclick(){
        fanye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flg = !flg;
                mapView.getMapAsync(new OnMapReadyCallback() {

                    @Override
                    public void onMapReady(@NonNull MapboxMap mapboxMap) {
                        if (flg) {
                            mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {

                                }
                            });
                        } else {
                            mapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {

                                }
                            });
                        }

                    }
                });
            }
        });

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull MapboxMap mapboxMap) {
                        if (ma.getLat()!=0&&ma.getLat()!=0){
                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(ma.getLat()), Double.valueOf(ma.getLon())),16));
                        }
                    }
                });
            }
        });

        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull MapboxMap mapboxMap) {
                        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                                enableLocationComponent(style);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.farm:
                Intent IntentFieldBinding = new Intent(MMainActivity.this, FieldListActivity.class);
                IntentFieldBinding.putExtra("title", "ほ場リスト");
                startActivityForResult(IntentFieldBinding,0);
                break;
            case R.id.Menu:
                new ChoiceDialog(this, true)
                        .setItems(new String[]{"ほ場情報紐付け","計量場情報紐付け","乾燥機情報紐付け","ログアウト"})
                        .hasCancleButton(true)
                        .setOnItemClickListener(new ChoiceDialog.OnItemClickListener() {
                            @Override
                            public void onItemClick(TextView onClickView, int position) {
                                if (position == 0) {
                                    Intent IntentFieldBinding = new Intent(MMainActivity.this, FieldBindingActivity.class);
                                    IntentFieldBinding.putExtra("modify", true);
                                    startActivity(IntentFieldBinding);
                                }else if(position==1){
                                    Intent IntentCarBinding = new Intent(MMainActivity.this, WeightBindingActivity.class);
                                    startActivity(IntentCarBinding);
                                }else if(position==2){
                                    Intent IntentDryingBinding = new Intent(MMainActivity.this, DryingBindingActivity.class);
                                    startActivity(IntentDryingBinding);
                                }
                                else if(position==3){
                                    GetDataBase.deleteall();
                                    Intent IntentLogin = new Intent(MMainActivity.this, LoginActivity.class);
                                    startActivity(IntentLogin);
                                    finish();
                                }
                            }
                        }).create().show();
                break;
        }
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapboxMap.getUiSettings().setCompassMargins(0, 170, 0, 0);
                mapboxMap.getUiSettings().setAttributionEnabled(false);
                mapboxMap.getUiSettings().setLogoEnabled(false);
                mapboxMap.addOnMapClickListener(MMainActivity.this);
                enableLocationComponent(style);
            }
        });
    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setRenderMode(RenderMode.COMPASS);
        locationComponent.setCameraMode(CameraMode.TRACKING,0,16.0,null,null,null);

    }


    @Override
    public boolean onMapClick(@NonNull LatLng point) {
//        PointF pointf = mapboxMap.getProjection().toScreenLocation(point);
//        RectF rectF = new RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10, pointf.y + 10);
//        List<Feature> featureList = mapboxMap.queryRenderedFeatures(rectF, geoJsonLayerId);
//        if (featureList.size() > 0) {
//            for (Feature feature : featureList) {
//                Timber.d("Feature found with %1$s", feature.toJson());
//                Toast.makeText(MMainActivity.this, "欢迎来到这里面",
//                        Toast.LENGTH_SHORT).show();
//            }
//            return true;
//        }
        return false;
    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次切换后台", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            Toast.makeText(getApplicationContext(), "应用程序已切换到后台", Toast.LENGTH_SHORT).show();
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!DateUtil.sen(GetDataBase.lastlogindata)){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            mapView.onResume();
        }

//        if (hotelColorAnimator != null) {
//            hotelColorAnimator.start();
//        }
//        if (parkColorAnimator != null) {
//            parkColorAnimator.start();
//        }
//        if (attractionsColorAnimator != null) {
//            attractionsColorAnimator.start();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
//        if (hotelColorAnimator != null) {
//            hotelColorAnimator.cancel();
//        }
//        if (parkColorAnimator != null) {
//            parkColorAnimator.cancel();
//        }
//        if (attractionsColorAnimator != null) {
//            attractionsColorAnimator.cancel();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }



    //触发一个事件
    private void setLayerVisible(String layerId,@NonNull Style loadedMapStyle) {
        Layer layer = loadedMapStyle.getLayer(layerId);
        if (layer == null) {
            return;
        }
        if (VISIBLE.equals(layer.getVisibility().getValue())) {
            // Layer is visible
            layer.setProperties(
                    visibility(Property.NONE)
            );
        } else {
            // Layer isn't visible
            layer.setProperties(
                    visibility(VISIBLE)
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (data!=null){
                    ma= (FieldEntity)data.getSerializableExtra("field");
                    local();
                }
                break;
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {

    }





    protected  void local(){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                if (ma!=null) {
                    IconFactory iconFactory1 = IconFactory.getInstance(MMainActivity.this);
                    Icon icon1 = iconFactory1.fromResource(R.drawable.blue_marker);
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(ma.getLat()), Double.valueOf(ma.getLon())), 16));
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.valueOf(ma.getLat()), Double.valueOf(ma.getLon())))
                            .title(ma.getName())
                            .snippet(String.valueOf(ma.getThingId()))
                            .icon(icon1)
                    );
                    mapboxMap.setInfoWindowAdapter(new MapboxMap.InfoWindowAdapter() {
                        @Nullable
                        @Override
                        public View getInfoWindow(@NonNull final Marker marker) {
                            View infoWindow = LayoutInflater.from(MMainActivity.this).inflate(R.layout.symbol_layer_info_window_layout_callout, null);
                            ImageView imageView = new ImageView(MMainActivity.this);
                            TextView add = infoWindow.findViewById(R.id.info_window_addr);
                            TextView user=infoWindow.findViewById(R.id.info_window_user);
                            TextView name = infoWindow.findViewById(R.id.info_window_name);
                            TextView textView2 = infoWindow.findViewById(R.id.info);
                            FieldEntity f=TaskFunction.findFieldByThingid(Integer.parseInt(marker.getSnippet()));
                            add.setText(f.getAddress());
                            user.setText(f.getOwner());
                            name.setText(f.getName());
                            textView2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent IntentFieldBinding = new Intent(MMainActivity.this, FieldBindingActivity.class);
                                    IntentFieldBinding.putExtra("field",(Serializable)TaskFunction.findFieldByThingid(Integer.parseInt(marker.getSnippet())));
                                    IntentFieldBinding.putExtra("modify",false);
                                    startActivity(IntentFieldBinding);
                                }
                            });
                            return infoWindow;
                        }
                    });
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(ma.getLat()), Double.valueOf(ma.getLon())), 16));
                }
            }
        });
    }



}
