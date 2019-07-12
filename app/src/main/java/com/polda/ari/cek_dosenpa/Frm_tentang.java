package com.polda.ari.cek_dosenpa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Frm_tentang extends AppCompatActivity {
Button btn_kembali;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_tentang);
        btn_kembali = (Button) findViewById(R.id.btnKembali);
        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Frm_tentang.this,MenuApps.class);
                startActivity(intent);
            }
        });
    }
}
