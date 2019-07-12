package com.polda.ari.cek_dosenpa;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Frm_haver extends FragmentActivity{

    private GoogleMap mMap;
    GoogleMap googleMap;
    SupportMapFragment mapFragment;
    Location location;
    LocationManager locationManager;
    TextView txt_npm;
    ProgressDialog loading;
    public static String nidn="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_haver);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if ((ActivityCompat.checkSelfPermission(Frm_haver.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(Frm_haver.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

        }
        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        txt_npm = (TextView) findViewById(R.id.txt_npm_p);
        txt_npm.setText(Frm_login.txt_pass.getText().toString().trim());

        getData(MenuApps.txt_nidn_m.getText().toString().trim(),MenuApps.txt_lat_mhs.getText().toString().trim(),MenuApps.txt_long_mhs.getText().toString().trim());
        //Toast.makeText(getApplication(),"Data Haversine "+MenuApps.txt_lat_mhs.getText().toString().trim(),Toast.LENGTH_LONG).show();


    }

    private void getData(final String Nidn, final String Lati, final String Longi){

        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = "http://pstiubl.com/api_dozen/dt_haversine.php?nidn="+Nidn+"&lati="+Lati+"&longi="+Longi;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showData(response);
                // Tampikkan data
                //Toast.makeText(getApplication(),"Data Haversine "+Nidn+"\n"+Lati+"\n"+Longi,Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Frm_haver.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showData(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String nidn = jo.getString("nidn");
                String dosen_pa = jo.getString("nama");
                Double jarak = jo.getDouble("jarak");
                Double lt = jo.getDouble("lati");
                Double lg = jo.getDouble("longi");

                if(jarak >= 0.99){
                    Toast.makeText(getApplication(),"Dosen pembimbing sedang diluar area kampus !",Toast.LENGTH_LONG).show();
                }else{
                    petaLokasi(nidn,dosen_pa,lt,lg);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    void petaLokasi(final String nidn, final String nama_pa, final Double lats, final Double longs) {
        //Toast.makeText(getApplication(),"Data "+lokasi+"\n "+lats+"\n "+longs,Toast.LENGTH_LONG).show();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-5.397140, 105.266789), 12.0f));

                LatLng ubl = new LatLng(lats, longs);
                //Toast.makeText(getApplication(),"Data "+lats+"\n"+longs,Toast.LENGTH_LONG).show();
                MarkerOptions marker = null;
                marker = new MarkerOptions()
                        .position(ubl)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.teacher));
                //.title("Posisi Dosen Pembimbing")
                //.snippet(nidn+" - "+nama_pa);

                mMap.addMarker(marker);
                //mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                //mMap.getUiSettings().setMyLocationButtonEnabled(true);


                Criteria criteria = new Criteria();
                if ((ActivityCompat.checkSelfPermission(Frm_haver.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(Frm_haver.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                    mMap.setMyLocationEnabled(true);
                }

                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                if (location!=null){
                    //Toast.makeText(this, "Lokasi Ditemukan", Toast.LENGTH_SHORT).show();
                    LatLng MyLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(MyLocation).title("Posisi Anda").snippet(""+location.getLatitude()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation,13));
                }else{
                    //Toast.makeText(this, "Lokasi Tidak Di temukan", Toast.LENGTH_SHORT).show();
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(Frm_haver.this, Frm_bimbingan.class);
                        intent.putExtra("nidn", nidn);
                        intent.putExtra("npm", txt_npm.getText().toString().trim());
                        finish();
                        startActivity(intent);
                        return false;
                    }
                });

            }
        });
    }


}
