package com.example.felixadrian.desertorest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felixadrian.objectos.Nivel;
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

import static com.example.felixadrian.desertorest.R.id.usuariosSpinner;
import static com.example.felixadrian.objectos.Estaticos.DOCENTE;
import static com.example.felixadrian.objectos.Estaticos.URL_SERVICES;
import static com.example.felixadrian.servicios.ServiciosUsuarios.listaNivel;

public class ReportarDesertorActivity extends AppCompatActivity {

    private EditText observacion_desertor;
    private ProgressBar enviarProgres;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    private Spinner spinnerCursos;
    private Spinner spinnerMotivos;
    private Spinner spinnerUsuarios;

    int spinner_pos;

    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    HashMap<Long, Usuario> spinnerMap = new HashMap<Long, Usuario>();
    HashMap<Integer, Nivel> mapNiveles = new HashMap<Integer, Nivel>();

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    finish();
                    Intent intent = new Intent(getApplicationContext(), InicioActivity.class);
                    intent.putExtra("parametro", DOCENTE);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    String text = spinnerUsuarios.getSelectedItem().toString();
                    dialog.setMessage("Desea agregar una falla al estudiante " +text);
                    dialog.show();
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

        spinnerCursos = (Spinner) findViewById(R.id.cursosSpinner);
        spinnerMotivos = (Spinner) findViewById(R.id.motivosSpinner);
        spinnerUsuarios = (Spinner) findViewById(usuariosSpinner);

        enviarProgres = (ProgressBar) findViewById(R.id.enviar_progress);
        listaUsuarios.clear();
        listaUsuarios = ServiciosUsuarios.getInstance().listaUsuarios;
        llenarSpinner();

        builder = new AlertDialog.Builder(ReportarDesertorActivity.this);
        builder.setMessage("").setTitle("Reportar Falla");
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                new registrar().execute(URL_SERVICES + "accion=registrar_desertor_android");
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // 3. Get the AlertDialog from create()
        dialog = builder.create();

        spinnerCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //adapterView.getItemAtPosition(i);
                int numPostion = adapterView.getSelectedItemPosition();
                int idNivel = mapNiveles.get(numPostion).getIdNivel();
                Toast.makeText(getBaseContext(), "Estudiantes para el curso " +mapNiveles.get(numPostion).getNombreNivel() , Toast.LENGTH_LONG).show();
                spinnerUsuarios.setAdapter(null);
                llenarSpinnerEstudiantes(idNivel);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String text = spinnerUsuarios.getSelectedItem().toString();
            spinner_pos = spinnerUsuarios.getSelectedItemPosition();
            String textMotivo = spinnerMotivos.getSelectedItem().toString();

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("usuario_id", spinnerMap.get((long) spinner_pos).getId());
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

        ArrayAdapter spinnerElmentosAdapter = ArrayAdapter.createFromResource(this, R.array.motivos, android.R.layout.simple_spinner_item);
        spinnerElmentosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotivos.setAdapter(spinnerElmentosAdapter);

        // Llenar el spinner de cursos
        ArrayList<String> niveles = new ArrayList<>();
        for (int i = 0; i < listaNivel.size(); i++) {
            niveles.add(listaNivel.get(i).getNombreNivel());
            mapNiveles.put( i, listaNivel.get(i));
        }

        ArrayAdapter spinnerCurosAdapter =new ArrayAdapter(this, android.R.layout.simple_spinner_item, niveles);
        spinnerCurosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCursos.setAdapter(spinnerCurosAdapter);
    }

    public void llenarSpinnerEstudiantes(int idNivel){
        // Llenar el spinner de Estudiantes
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getId_nivel() == idNivel){
                strings.add(listaUsuarios.get(i).getNombres() + " " + listaUsuarios.get(i).getApellidos());
                spinnerMap.put((long) i, listaUsuarios.get(i));
            }
        }
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strings);
        //Estiliza el spinner_adapter de forma que queden separados los elementos
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Seteamos el contenido del spinner_adapter al Spinner
        spinnerUsuarios.setAdapter(spinner_adapter);

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
