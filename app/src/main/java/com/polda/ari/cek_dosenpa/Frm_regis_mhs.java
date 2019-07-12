package com.polda.ari.cek_dosenpa;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Frm_regis_mhs extends AppCompatActivity {
    public static final String JSON_ARRAY = "result";
    private JSONArray result;
    private ArrayList<String> arrayList;
    Spinner sp_dosen,sp_prodi;
    TextView txt_nidn;
    EditText txt_npm,txt_nama,txt_mail,txt_telp,txt_judul;
    Button btn_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_regis_mhs);
        txt_nidn = (TextView) findViewById(R.id.txt_nidnn);
        txt_npm = (EditText) findViewById(R.id.txt_npm);
        txt_nama = (EditText) findViewById(R.id.txt_nama_mhs);
        txt_mail = (EditText) findViewById(R.id.txt_mail_mhs);
        txt_telp = (EditText) findViewById(R.id.txt_telp_mhs);
        txt_judul = (EditText) findViewById(R.id.txt_judul);
        sp_dosen = (Spinner) findViewById(R.id.sp_pa);
        sp_prodi = (Spinner) findViewById(R.id.sp_prodi);
        btn_reg = (Button) findViewById(R.id.btn_regs_mhs);

        arrayList = new ArrayList<String>();
        getdata();

        sp_dosen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getmailid(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplication(),"Data Kosong ",Toast.LENGTH_LONG).show();
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanRegMhs(txt_npm.getText().toString().trim(),txt_nama.getText().toString().trim(),txt_mail.getText().toString().trim(),
                        txt_telp.getText().toString().trim(),sp_prodi.getSelectedItem().toString().trim(),txt_judul.getText().toString().trim(),txt_nidn.getText().toString().trim());
                //Toast.makeText(getApplication(),"Data "+txt_telp.getText().toString().trim(),Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getdata() {
        StringRequest stringRequest = new StringRequest("http://pstiubl.com/api_dozen/dt_dosen.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY);
                            empdetails(result);
                            //Toast.makeText(getApplication(),"Data "+result,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void empdetails(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                //arrayList.add(json.getString("id_user"));
                arrayList.add(json.getString("nama_lengkap"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // arrayList.add(0,"Select Employee");
        sp_dosen.setAdapter(new ArrayAdapter<String>(Frm_regis_mhs.this, android.R.layout.simple_spinner_dropdown_item, arrayList));
    }

    private String getmailid(int position){

        String id="";
        try {
            JSONObject json = result.getJSONObject(position);
            id = json.getString("nidn");
            txt_nidn.setText(""+id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }


    public void simpanRegMhs(final String Npm,final String Nama, final String Email, final String Telp,
                             final String Prodi, final String Judul, final String Dosen){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("npm", Npm));
                nameValuePairs.add(new BasicNameValuePair("nama_lengkap", Nama));
                nameValuePairs.add(new BasicNameValuePair("email", Email));
                nameValuePairs.add(new BasicNameValuePair("telp", Telp));
                nameValuePairs.add(new BasicNameValuePair("prodi", Prodi));
                nameValuePairs.add(new BasicNameValuePair("judul", Judul));
                nameValuePairs.add(new BasicNameValuePair("nidn", Dosen));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://pstiubl.com/api_dozen/act_reg_mhs.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                } catch (ClientProtocolException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();

                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(result.equalsIgnoreCase("success")){
                    Toast.makeText(getApplication(),"Data tersimpan",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Frm_regis_mhs.this, Frm_login.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplication(),"Terjadi kesalahan Sistem",Toast.LENGTH_LONG).show();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Npm,Nama,Email,Telp,Prodi,Judul);
    }


}
