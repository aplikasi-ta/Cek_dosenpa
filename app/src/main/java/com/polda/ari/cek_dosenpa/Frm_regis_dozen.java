package com.polda.ari.cek_dosenpa;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Frm_regis_dozen extends AppCompatActivity {
EditText txt_nidn,txt_nama,txt_mail,txt_notelp;
Button btn_reg_doz;
    Spinner sp_prodi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_regis_dozen);
        txt_nidn = (EditText) findViewById(R.id.txt_nidn);
        txt_nama = (EditText) findViewById(R.id.txt_nama_doz);
        txt_mail = (EditText) findViewById(R.id.txt_mail);
        txt_notelp = (EditText) findViewById(R.id.txt_telp);
        sp_prodi = (Spinner) findViewById(R.id.sp_prodi);
        btn_reg_doz = (Button) findViewById(R.id.btn_reg_doz);

        btn_reg_doz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanRegdozen(txt_nidn.getText().toString().trim(),txt_nama.getText().toString().trim(),txt_mail.getText().toString().trim(),
                        txt_notelp.getText().toString().trim(),sp_prodi.getSelectedItem().toString().trim());
            }
        });

    }


    public void simpanRegdozen(final String Nidn, final String Nama, final String Telp, final String Email, final String Prodi){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("nidn_npm", Nidn));
                nameValuePairs.add(new BasicNameValuePair("nama_lengkap", Nama));
                nameValuePairs.add(new BasicNameValuePair("telp", Telp));
                nameValuePairs.add(new BasicNameValuePair("email", Email));
                nameValuePairs.add(new BasicNameValuePair("prodi", Prodi));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                           "http://pstiubl.com/api_dozen/act_reg_doz.php");
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
                }else{
                    Toast.makeText(getApplication(),"Terjadi kesalahan Sistem",Toast.LENGTH_LONG).show();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Nidn,Nama,Telp,Email,Prodi);
    }




}
