package com.example.felixadrian.servicios;

import android.os.AsyncTask;
import android.util.Log;

import com.example.felixadrian.objectos.Nivel;
import com.example.felixadrian.objectos.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import static com.example.felixadrian.objectos.Estaticos.URL_SERVICES;

/**
 * Created by Felix Adrian on 28/05/2017.
 */

public class ServiciosUsuarios {

    private static final ServiciosUsuarios ourInstance = new ServiciosUsuarios();
    public  static ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    public  static ArrayList<Nivel> listaNivel = new ArrayList<>();
    private  ArrayList<Usuario> listaUsuariosFallas = new ArrayList<>();
    private int docenteId = 0;

    public static ServiciosUsuarios getInstance() {
        return ourInstance;
    }

    private ServiciosUsuarios() {
    }

    public void usuarios(int num){
        docenteId = num;
        new getListaUsuarios().execute();
    }
    public void usuariosFallas(){
        new getCountFallasUsuarios().execute();
    }

    public void docentesNiveles(int num){
        docenteId = num;
        new getNivelesUsuarios().execute();
    }

    public ArrayList<Usuario> getListaUsuariosFallas() {
        return listaUsuariosFallas;
    }

    public void setListaUsuariosFallas(ArrayList<Usuario> listaUsuariosFallas) {
        this.listaUsuariosFallas = listaUsuariosFallas;
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
                HttpPost httpPost = new HttpPost( URL_SERVICES+"accion=load_estudiantes_android");

                String json = "";
                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("docentes_id", docenteId);
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
                        usuario.setId(jsonArray.getJSONObject(i).getInt("usuario_id"));
                        usuario.setTidentificacion(jsonArray.getJSONObject(i).getString("usuario_tidentificacion"));
                        usuario.setIdentificacion(jsonArray.getJSONObject(i).getString("usuario_identificacion"));
                        usuario.setNombres(jsonArray.getJSONObject(i).getString("usuario_nombres"));
                        usuario.setApellidos(jsonArray.getJSONObject(i).getString("usuario_apellidos"));
                        usuario.setId_nivel(jsonArray.getJSONObject(i).getInt("usuario_nivel_id"));
                        listaUsuarios.add(usuario);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    private class getCountFallasUsuarios extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = "";
            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost( URL_SERVICES+"accion=count_faltas_android");

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
                        usuario.setId(jsonArray.getJSONObject(i).getInt("usuario_id"));
                        usuario.setNombres(jsonArray.getJSONObject(i).getString("usuario_nombres"));
                        usuario.setApellidos(jsonArray.getJSONObject(i).getString("usuario_apellidos"));
                        usuario.setTotalFallas(jsonArray.getJSONObject(i).getInt("total"));
                        getListaUsuariosFallas().add(usuario);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    private class getNivelesUsuarios extends AsyncTask< String , Void, String> {
        @Override
        protected String doInBackground(String... num) {
            InputStream inputStream = null;
            String result = "";
            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost( URL_SERVICES+"accion=consultar_niveles");

                String json = "";
                Integer numero = docenteId;
                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("docentes_id",docenteId );
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
                        Nivel nivel = new Nivel();
                        nivel.setIdNivel(jsonArray.getJSONObject(i).getInt("id_nivel"));
                        nivel.setNombreNivel(jsonArray.getJSONObject(i).getString("nombre_nivel"));
                        nivel.setDocentesId(jsonArray.getJSONObject(i).getInt("docentes_id"));
                        listaNivel.add(nivel);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
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



}
