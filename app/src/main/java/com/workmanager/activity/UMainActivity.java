package com.workmanager.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.necer.ndialog.ChoiceDialog;
import com.necer.ndialog.ConfirmDialog;
import com.workmanager.R;
import com.workmanager.entity.FieldEntity;
import com.workmanager.entity.MapFieldEntity;
import com.workmanager.function.GetDataBase;
import com.workmanager.function.TaskFunction;
import com.workmanager.util.CommonUtil;
import com.workmanager.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;


public class UMainActivity extends BaseNFCActivity implements View.OnClickListener, OnMapReadyCallback, MapboxMap.OnMapClickListener {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private TextView fanye;
    private TextView nav;
    private TextView local;
    private TextView mFarmTextView;
    private TextView mMenuTextView;
    private boolean flg = true;
    private SharedPreferences workInfo;
    private File file;
    private FieldEntity ma =new FieldEntity();
    private ArrayList<MapFieldEntity> mapfieldlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiNzIzNTQwMDE4IiwiYSI6ImNrMTRjaHc5aTBpcXczaHM5cXhvczVxbTIifQ.F3MXTpzKLgwiRqklCld4dQ");
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        fanye = (TextView) findViewById(R.id.textView2);
        nav=(TextView)findViewById(R.id.nav);
        local=(TextView)findViewById(R.id.local);
        setclick();

        mFarmTextView = (TextView) findViewById(R.id.farm);
        mFarmTextView.setOnClickListener(this);
        mMenuTextView = (TextView) findViewById(R.id.Menu);
        mMenuTextView.setOnClickListener(this);
       /* Intent data= getIntent();
        if (data!=null){
            ma=(FieldEntity) data.getSerializableExtra("point");
        }*/
        workInfo= getSharedPreferences("workInfo", MODE_PRIVATE);
        if (!workInfo.getString("status", "").equals("1")&&!workInfo.getString("status", "").equals("")) {
            Intent nfc = new Intent(UMainActivity.this, NFCActivity.class);
            nfc.putExtra("flg", true);
            startActivity(nfc);
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
                Intent IntentFieldBinding = new Intent(UMainActivity.this, FieldListActivity.class);
                IntentFieldBinding.putExtra("title", "ほ場リスト");
                startActivityForResult(IntentFieldBinding,0);
                break;
            case R.id.Menu:
                new ChoiceDialog(this, true)
                        .setItems(new String[]{"ログアウト"})
                        .hasCancleButton(true)
                        .setOnItemClickListener(new ChoiceDialog.OnItemClickListener() {
                            @Override
                            public void onItemClick(TextView onClickView, int position) {
                                if (position == 0) {
                                    GetDataBase.deleteall();
                                    Intent IntentLogin = new Intent(UMainActivity.this, LoginActivity.class);
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
                mapboxMap.addOnMapClickListener(UMainActivity.this);
                enableLocationComponent(style);
                load(style);
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
        return false;
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

    private String nfcId;
    @Override
    protected void onNewIntent(Intent intent) {
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(detectedTag);
        nfcId=CommonUtil.readNfcTag(intent);
            if (TaskFunction.findFieldByNfcid(nfcId).getBaseFieldId()==0){
                new ConfirmDialog(UMainActivity.this, true)
                        .setTtitle("確認")
                        .setMessage("無効なNFCタグです。再度ご確認ください")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create().show();
            }else {
                ma=TaskFunction.findFieldByNfcid(nfcId);
                local();
            }
        }

        protected  void local(){
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    if (ma!=null) {
                        IconFactory iconFactory1 = IconFactory.getInstance(UMainActivity.this);
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
                                View infoWindow = LayoutInflater.from(UMainActivity.this).inflate(R.layout.symbol_layer_info_window_layout_callout, null);
                                ImageView imageView = new ImageView(UMainActivity.this);
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
                                        new ConfirmDialog(UMainActivity.this, true)
                                                .setTtitle("確認")
                                                .setMessage("選択したほ場が間違いなければ、タスクを開始します。\nよろしいですか？")
                                                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Intent IntentFieldBinding = new Intent(UMainActivity.this, NFCActivity.class);
                                                                FieldEntity field =TaskFunction.findFieldByThingid(Integer.parseInt(marker.getSnippet()));
                                                                GetDataBase.spfE.setThingid(field.getThingId());
                                                                GetDataBase.spfE.setFieldName(field.getName());
                                                                GetDataBase.spfE.setAddr(field.getAddress());
                                                                LoginActivity.spf.writeSharedPreferences(GetDataBase.spfE);
                                                                IntentFieldBinding.putExtra("flg",false);
                                                                startActivity(IntentFieldBinding);
                                                            }
                                                        }).start();
                                                    }
                                                })
                                                .setNegativeButton("いいえ",new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                }).create().show();

                                    }
                                });
                                return infoWindow;
                            }
                        });
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(ma.getLat()), Double.valueOf(ma.getLon())), 16));
                       /* if (GetDataBase.fieldList.size() != 0) {
                            for (FieldEntity f:GetDataBase.fieldList){
                            }
                        }*/
                    }
                }

            });
        }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!DateUtil.sen(GetDataBase.lastlogindata)){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
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

    //加载地图颜色
    public  void load(@NonNull final Style style) {
        mapfieldlist= GetDataBase.mapfieldList;
        GeoJsonSource hotelSource = new GeoJsonSource("hotels", createjson());
        style.addSource(hotelSource);

        FillLayer hotelLayer = new FillLayer("hotels", "hotels").withProperties(
                fillColor(Color.parseColor("#FF0000")),//lanse
                visibility(VISIBLE)
        );
        style.addLayer(hotelLayer);

        final FillLayer hotels = (FillLayer) style.getLayer("hotels");

//        hotelColorAnimator = ValueAnimator.ofObject(
//                new ArgbEvaluator(),
//                Color.parseColor("#5a9fcf"), // Brighter shade
//                Color.parseColor("#2C6B97") // Darker shade
//        );
//        hotelColorAnimator.setDuration(1000);z
//        hotelColorAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        hotelColorAnimator.setRepeatMode(ValueAnimator.REVERSE);
//        hotelColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animator) {
//                if (hotels != null) {
//                    hotels.setProperties(fillColor((int) animator.getAnimatedValue())
//                    );
//                }
//            }
//        });

    }
    public String createjson(){
        try {
            file = new File(getFilesDir(),"Test.json");

            JSONArray features = new JSONArray();
            for (int i = 0;i<mapfieldlist.size();i++){
                JSONArray  coordinatesout = new JSONArray();
                JsonArray Jsonlist= mapfieldlist.get(i).getArrayshape();
                for (int j=0;j<Jsonlist.size();j++)
                {
                    JSONArray coordinates = new JSONArray();
                    coordinates.put(Jsonlist.get(j).getAsJsonObject().get("Lon").getAsDouble());
                    coordinates.put(Jsonlist.get(j).getAsJsonObject().get("Lat").getAsDouble());
                    coordinatesout.put(coordinates);
                    if (j==Jsonlist.size()-1){
                        JSONArray Firstcoordinates = new JSONArray();
                        Firstcoordinates.put(Jsonlist.get(0).getAsJsonObject().get("Lon").getAsDouble());
                        Firstcoordinates.put(Jsonlist.get(0).getAsJsonObject().get("Lat").getAsDouble());
                        coordinatesout.put(Firstcoordinates);
                    }
                }
                    JSONObject geometry = new JSONObject();
                    geometry.put("type","Polygon");
                    JSONArray coordinatess = new JSONArray();
                    coordinatess.put(coordinatesout);
                    geometry.put("coordinates",coordinatess);
                    JSONObject properties = new JSONObject();
                    JSONObject featuresin = new JSONObject();
                    featuresin.put("type","Feature");
                    featuresin.put("properties",properties);
                    featuresin.put("geometry",geometry);
                    features.put(featuresin);
           }


            JSONObject root = new JSONObject();
            root.put("type","FeatureCollection");
            root.put("features",features);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(root.toString().getBytes());
            fos.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream is=new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, Charset.forName("UTF-8"));

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }


    }
}
