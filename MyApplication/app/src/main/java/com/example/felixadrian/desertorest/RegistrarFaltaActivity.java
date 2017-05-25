package com.example.felixadrian.desertorest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegistrarFaltaActivity extends AppCompatActivity {

    private TextView nombre_usuario_falta;
    private TextView motivo_falta;
    private TextView observacion_falta;

    private Button registrar_falta_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_falta);
        nombre_usuario_falta = (TextView) findViewById(R.id.nombre_usuario_falta);
        motivo_falta = (TextView) findViewById(R.id.motivo_falta);
        observacion_falta = (TextView) findViewById(R.id.observacion_falta);
        registrar_falta_button = (Button) findViewById(R.id.registrar_falta_button);

        registrar_falta_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               registrarFalta();
            }
        });

    }

    public void registrarFalta()  {

        RequestParams params = new RequestParams();

        params.put("id_usuario", nombre_usuario_falta.getText());
        params.put("falta_motivo", motivo_falta.getText());
        params.put("falta_observacion", observacion_falta.getText());
        params.put("falta_fecha", "2000/01/01");
        params.put("accion", "registrar_faltas_android");

        String str = "";
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://192.168.0.18/desertorest-admin/ajax/ajax_actions.php?";
        //String url = "http://desertorest.flibdig.com/ajax/ajax_actions.php?";
        RequestHandle requestHandle = client.post(url + params , new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String str = new String(response);
                try {
                    JSONObject jsonObj = new JSONObject(str);
                    if (jsonObj.length() > 0) {

                    } else {
                        Toast.makeText(RegistrarFaltaActivity.this, "Datos de acceso invalidos", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "404 !", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "500 !", Toast.LENGTH_LONG).show();
                } else if (statusCode == 403) {
                    Toast.makeText(getApplicationContext(), "403 !", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onRetry(int retryNo) {
                int i = 0;
            }

        });
    }
}
