package com.polda.ari.cek_dosenpa;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Frm_bimbingan extends AppCompatActivity {
String nidn,npm;
TextView txt_npm,txt_nidn;
EditText txt_pesan;
Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_bimbingan);
        final Intent intent = getIntent();
        nidn = intent.getStringExtra("nidn");
        npm = intent.getStringExtra("npm");
        txt_nidn = (TextView) findViewById(R.id.txt_nidn_b);
        txt_npm = (TextView) findViewById(R.id.txt_npm_b);
        txt_pesan = (EditText) findViewById(R.id.txt_pesan);
        btn_submit = (Button) findViewById(R.id.btnSubmmits_b);

        txt_nidn.setText(nidn);
        txt_npm.setText(npm);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanBimbingan(txt_npm.getText().toString().trim(),txt_nidn.getText().toString().trim(),txt_pesan.getText().toString().trim());
            }
        });

    }


    public void simpanBimbingan(final String Npm, final String Nidn, final String Pesan){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("npm", Npm));
                nameValuePairs.add(new BasicNameValuePair("nidn", Nidn));
                nameValuePairs.add(new BasicNameValuePair("pesan", Pesan));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://pstiubl.com/api_dozen/act_bimbing.php");
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
                    Toast.makeText(getApplication(),"Bimbingan berhasil diajukan",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplication(),"Terjadi kesalahan Sistem",Toast.LENGTH_LONG).show();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Npm,Nidn,Pesan);
    }

}