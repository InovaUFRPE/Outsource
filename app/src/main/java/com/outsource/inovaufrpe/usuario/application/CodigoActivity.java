package com.outsource.inovaufrpe.usuario.application;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.outsource.inovaufrpe.usuario.R;

public class CodigoActivity extends AppCompatActivity {
    EditText etCodigo;
    Button btConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo);
        etCodigo = (EditText) findViewById(R.id.etCodigoID);
        btConfirmar = (Button) findViewById(R.id.btConfirmarID);

    }
}
