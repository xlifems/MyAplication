package com.example.felixadrian.desertorest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felixadrian.objectos.Usuario;
import com.example.felixadrian.servicios.ServiciosUsuarios;

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

public class ReportarDesertorActivity extends AppCompatActivity {


    private EditText observacion_desertor;
    private ProgressBar enviarProgres;


    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    HashMap<Long, Usuario> spinnerMap = new HashMap<Long, Usuario>();

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_notifications:
                    new registrar().execute("http://192.168.0.6/desertorest-admin/ajax/ajax_actions.php?accion=registrar_desertor_android");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_desertor);
        observacion_desertor = (EditText) findViewById(R.id.observacion_desertor);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        enviarProgres = (ProgressBar) findViewById(R.id.enviar_progress);
        listaUsuarios = ServiciosUsuarios.getInstance().listaUsuarios;
        llenarSpinner();
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
            jsonObject.accumulate("desertor_motivo", textMotivo);
            jsonObject.accumulate("desertor_observacion", observacion_desertor.getText().toString().trim());
            jsonObject.accumulate("desertor_fecha", "2000/01/01");
            jsonObject.accumulate("accion", "registrar_desertor_android");

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
            showProgress(true);
            return POST(urls[0]);

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            showProgress(false);
            if (result.equals("1")){
                Toast.makeText(getBaseContext(), "Desertor Reportado Existosamente!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
            }

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
        ArrayAdapter spinnerElmentosAdapter = ArrayAdapter.createFromResource(this, R.array.motivos, android.R.layout.simple_spinner_item);
        spinnerElmentosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotivos.setAdapter(spinnerElmentosAdapter);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            enviarProgres.setVisibility(show ? View.GONE : View.VISIBLE);
            enviarProgres.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    enviarProgres.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            enviarProgres.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }

}
