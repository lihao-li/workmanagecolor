//package com.workmanager.function;
//
//import android.app.Activity;
//import android.graphics.Color;
//
//import androidx.annotation.NonNull;
//
//import com.mapbox.mapboxsdk.maps.Style;
//import com.mapbox.mapboxsdk.style.layers.FillLayer;
//import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.charset.Charset;
//
//import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
//import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
//import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;
//
//public class test  extends Activity {
//    private File file;
//    //加载地图颜色
//    public  void load(@NonNull final Style style) {
//        GeoJsonSource hotelSource = new GeoJsonSource("hotels", createjson());
//        style.addSource(hotelSource);
//
//        FillLayer hotelLayer = new FillLayer("hotels", "hotels").withProperties(
//                fillColor(Color.parseColor("#5a9fcf")),//lanse
//                visibility(VISIBLE)
//        );
//        style.addLayer(hotelLayer);
//
//        final FillLayer hotels = (FillLayer) style.getLayer("hotels");
//
////        hotelColorAnimator = ValueAnimator.ofObject(
////                new ArgbEvaluator(),
////                Color.parseColor("#5a9fcf"), // Brighter shade
////                Color.parseColor("#2C6B97") // Darker shade
////        );
////        hotelColorAnimator.setDuration(1000);z
////        hotelColorAnimator.setRepeatCount(ValueAnimator.INFINITE);
////        hotelColorAnimator.setRepeatMode(ValueAnimator.REVERSE);
////        hotelColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
////
////            @Override
////            public void onAnimationUpdate(ValueAnimator animator) {
////                if (hotels != null) {
////                    hotels.setProperties(fillColor((int) animator.getAnimatedValue())
////                    );
////                }
////            }
////        });
//
//    }
//    public String createjson(){
//        try {
//            file = new File(getFilesDir(),"Test.json");
////            List<String> contentList = new ArrayList<>();
////            contentList.add("[237.49145507812497,36.155617833818525]");
////            contentList.add("[242.2430419921875,33.054716488042736]");
//            JSONArray coordinates = new JSONArray();
//            JSONArray  coordinates2 = new JSONArray();
//            JSONArray  coordinatesout = new JSONArray();
//            coordinates.put(237.49145507812497);
//            coordinates.put(36.155617833818525);
//            coordinates2.put(242.2430419921875);
//            coordinates2.put(33.054716488042736);
//            coordinatesout.put(coordinates);
//            coordinatesout.put(coordinates2);
//            JSONObject geometry = new JSONObject();
//            geometry.put("type","LineString");
//            geometry.put("coordinates",coordinatesout);
//            JSONObject properties = new JSONObject();
//            JSONArray features = new JSONArray();
//            JSONObject featuresin = new JSONObject();
//            featuresin.put("type","Feature");
//            featuresin.put("properties",properties);
//            featuresin.put("geometry",geometry);
//            features.put(featuresin);
//            JSONObject root = new JSONObject();
//            root.put("type","FeatureCollection");
//            root.put("features",features);
//
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(root.toString().getBytes());
//            fos.close();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            FileInputStream is=new FileInputStream(file);
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            return new String(buffer, Charset.forName("UTF-8"));
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//
//
//    }
//}
