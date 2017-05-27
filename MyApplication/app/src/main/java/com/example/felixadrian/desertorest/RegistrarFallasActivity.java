package com.example.felixadrian.desertorest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felixadrian.objectos.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class RegistrarFallasActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private EditText observacion_falta;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    HashMap<Long, Usuario> spinnerMap = new HashMap<Long, Usuario>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_notifications:
                    new registrar().execute("http://192.168.0.6/desertorest-admin/ajax/ajax_actions.php?accion=registrar_faltas_android");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_fallas);
        observacion_falta = (EditText) findViewById(R.id.observacion_falta);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        new getListaUsuarios().execute();
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            Spinner spinnerUsuario = (Spinner) findViewById(R.id.usuariosSpinner);
            String text = spinnerUsuario.getSelectedItem().toString();
            int spinner_pos = spinnerUsuario.getSelectedItemPosition();

            Spinner spinnerMotivos = (Spinner) findViewById(R.id.motivosSpinner);
            String textMotivo = spinnerMotivos.getSelectedItem().toString();
            String json = "";
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id_usuario", spinnerMap.get((long) spinner_pos).getId());
            jsonObject.accumulate("falta_motivo", textMotivo);
            jsonObject.accumulate("falta_observacion", observacion_falta.getText().toString().trim());
            jsonObject.accumulate("falta_fecha", "2000/01/01");
            jsonObject.accumulate("accion", "registrar_faltas_android");

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private class registrar extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("1"))
                Toast.makeText(getBaseContext(), "Inasistencia Reportada", Toast.LENGTH_LONG).show();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class getListaUsuarios extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = "";
            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost("http://192.168.0.6/desertorest-admin/ajax/ajax_actions.php?accion=cargar_clientes");

                String json = "";
                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("accion", "cargar_clientes");
                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();
                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);
                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity
                httpPost.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);
                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                // 10. convert inputstream to string
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            // 11. return result
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Usuario usuario = new Usuario();
                        usuario.setId(jsonArray.getJSONObject(i).getLong("usuario_id"));
                        usuario.setTidentificacion(jsonArray.getJSONObject(i).getString("usuario_tidentificacion"));
                        usuario.setIdentificacion(jsonArray.getJSONObject(i).getString("usuario_identificacion"));
                        usuario.setNombres(jsonArray.getJSONObject(i).getString("usuario_nombres"));
                        usuario.setApellidos(jsonArray.getJSONObject(i).getString("usuario_apellidos"));
                        listaUsuarios.add(usuario);
                    }
                    llenarSpinner();
                } else {
                    Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void llenarSpinner() {

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < listaUsuarios.size(); i++) {
            strings.add(listaUsuarios.get(i).getNombres() + " " + listaUsuarios.get(i).getNombres());
            spinnerMap.put((long) i, listaUsuarios.get(i));
        }

        Spinner usuariosSpinner = (Spinner) findViewById(R.id.usuariosSpinner);
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strings);
        //Estiliza el spinner_adapter de forma que queden separados los elementos
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Seteamos el contenido del spinner_adapter al Spinner
        usuariosSpinner.setAdapter(spinner_adapter);

        Spinner spinnerMotivos = (Spinner) findViewById(R.id.motivosSpinner);
        ArrayAdapter spinnerElmentosAdapter = ArrayAdapter.createFromResource(this, R.array.motivos_faltas, android.R.layout.simple_spinner_item);
        spinnerElmentosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotivos.setAdapter(spinnerElmentosAdapter);
    }

}