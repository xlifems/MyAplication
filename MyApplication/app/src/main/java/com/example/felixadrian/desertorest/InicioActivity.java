package com.example.felixadrian.desertorest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felixadrian.objectos.Falta;
import com.example.felixadrian.objectos.Usuario;
import com.example.felixadrian.servicios.ServiciosUsuarios;

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

import static com.example.felixadrian.desertorest.R.id.layout_stub;
import static com.example.felixadrian.desertorest.R.string.navigation_drawer_open;

public class InicioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView navigationView;
    private TextView nombre_usuario_falta;
    private TextView motivo_falta;
    private TextView observacion_falta;
    private Button registrar_falta_button;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;

    private View inflated;
    private ViewStub stub;
    Usuario usuario;
    Falta falta;

    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    HashMap<Long, Usuario> spinnerMap = new HashMap<Long, Usuario>();

    LinearLayout ll;
    //ArrayAdapter para conectar el Spinner a nuestros recursos strings.xml
    protected ArrayAdapter<CharSequence> adapter;

    public void getUsuario() {
        usuario = getIntent().getExtras().getParcelable("parametro");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stub = (ViewStub) findViewById(layout_stub);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call AsynTask to perform network operation on separate thread
                new registrar().execute("http://192.168.0.6/desertorest-admin/ajax/ajax_actions.php?accion=registrar_faltas_android");
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call AsynTask to perform network operation on separate thread
                //new registrar().execute("http://192.168.0.18/desertorest-admin/ajax/ajax_actions.php?accion=registrar_faltas_android");
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        fab.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setDatosHeader();

            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getUsuario();
        falta = new Falta();
        //new getListaUsuarios().execute();
        ServiciosUsuarios.getInstance().usuarios();
    }

    public void setDatosHeader() {
        TextView text_usuario_nombre = (TextView) findViewById(R.id.text_usuario_nombre);
        TextView text_usuario_correo = (TextView) findViewById(R.id.text_correo);
        text_usuario_nombre.setText(usuario.getNombres());
        text_usuario_correo.setText(usuario.getCorreo());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.reportar_esertor) {
            Intent intent = new Intent(getApplicationContext(), ReportarDesertorActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getApplicationContext(), RegistrarFallasActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(getApplicationContext(), ListaFallasActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            Falta f = new Falta();

            f.setObservacionFalta(observacion_falta.getText().toString().trim());
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
                Toast.makeText(getBaseContext(), "Falla Registrada!", Toast.LENGTH_LONG).show();
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
        ArrayAdapter spinnerElmentosAdapter = ArrayAdapter.createFromResource(this, R.array.motivos, android.R.layout.simple_spinner_item);
        spinnerElmentosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotivos.setAdapter(spinnerElmentosAdapter);
    }

}
