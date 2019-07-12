package com.polda.ari.cek_dosenpa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

import java.net.URLEncoder;
import java.util.HashMap;

public class MenuApps extends AppCompatActivity {
    public static TextView txt_npm, txt_lat_mhs, txt_long_mhs,txt_nidn_m;
    ImageButton btn_cek, btn_bimb, btn_info, btn_logout;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_apps);
        txt_npm = (TextView) findViewById(R.id.txt_npm_mn);
        txt_lat_mhs = (TextView) findViewById(R.id.txt_lat_mhs);
        txt_long_mhs = (TextView) findViewById(R.id.txt_long_mhs);
        txt_nidn_m = (TextView) findViewById(R.id.txt_nidn_m);
        btn_cek = (ImageButton) findViewById(R.id.btn_cek);
        btn_bimb = (ImageButton) findViewById(R.id.btn_bimb);
        btn_logout = (ImageButton) findViewById(R.id.btn_logout);
        btn_info = (ImageButton) findViewById(R.id.btn_tentang);
        txt_npm.setText(Frm_login.txt_pass.getText().toString().trim());

        getJSON(txt_npm.getText().toString().trim());

        btn_cek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuApps.this, Frm_haver.class);
                startActivity(intent);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuApps.this, Frm_login.class);
                startActivity(intent);
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuApps.this, Frm_tentang.class);
                startActivity(intent);
            }
        });

        btn_bimb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuApps.this,Frm_det_status.class);
                startActivity(intent);
            }
        });
    }


    private void getJSON(final String id){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MenuApps.this,"Menampilkan Data","Tunggu Sebentar...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getApplication(),"Data "+s,Toast.LENGTH_LONG).show();
                //showDetail(s);
                showJSON2(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> nama_lok = new HashMap<>();
                nama_lok.put("npm", id);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("http://pstiubl.com/api_dozen/dt_dosenpa.php?npm=", nama_lok);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showJSON2(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject c = result.getJSONObject(i);
                txt_nidn_m.setText(c.getString("nidn"));
                txt_lat_mhs.setText(c.getString("lati"));
                txt_long_mhs.setText(c.getString("longi"));

                //Toast.makeText(getApplication(),"Data NIDN "+c.getString("nidn"),Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    }

