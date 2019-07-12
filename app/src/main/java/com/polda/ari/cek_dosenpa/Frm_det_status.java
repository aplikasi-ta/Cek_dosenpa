package com.polda.ari.cek_dosenpa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Frm_det_status extends AppCompatActivity {
ProgressDialog loading;
TextView txt_npm_det,txt_nama,txt_prodi,txt_status,txt_judul,txt_pesan,txt_dosen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_det_status);
        txt_npm_det = (TextView) findViewById(R.id.txt_npm_det);
        txt_nama = (TextView) findViewById(R.id.txt_nam_det);
        txt_prodi = (TextView) findViewById(R.id.txt_prodi_det);
        txt_judul = (TextView) findViewById(R.id.txt_judul_det);
        txt_status = (TextView) findViewById(R.id.txt_status_det);
        txt_pesan = (TextView) findViewById(R.id.txt_pesan_det);
        txt_dosen = (TextView) findViewById(R.id.txt_dosen_det);
        getData(MenuApps.txt_npm.getText().toString().trim(),MenuApps.txt_nidn_m.getText().toString().trim());
    }

    private void getData(String Npm, String Nidn){

        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = "http://pstiubl.com/api_dozen/dt_status.php?npm="+Npm+"&nidn="+Nidn;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showData(response);
                // Tampikkan data
                //Toast.makeText(getApplication(),"Data Mahasiswa "+response,Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Frm_det_status.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void showData(String response) {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String npm = jo.getString("npm");
                String nama = jo.getString("nama_mhs");
                String prodi = jo.getString("prodi");
                String ta = jo.getString("ta");
                String status = jo.getString("status");
                String balasan = jo.getString("balasan");
                String dosen = jo.getString("nama_dosen");

                txt_npm_det.setText(npm);
                txt_nama.setText(nama);
                txt_prodi.setText(prodi);
                txt_judul.setText(ta);
                txt_status.setText(status);
                txt_pesan.setText(balasan);
                txt_dosen.setText(dosen);
                if(status.equals("Menunggu")){
                    txt_status.setTextColor(Color.YELLOW);
                }else if(status.equals("Terima")){
                    txt_status.setTextColor(Color.GREEN);
                }else if(status.equals("Ditolak")){
                    txt_status.setTextColor(Color.RED);
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
