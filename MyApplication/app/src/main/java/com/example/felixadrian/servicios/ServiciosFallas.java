package com.example.felixadrian.servicios;

import android.os.AsyncTask;
import android.util.Log;

import com.example.felixadrian.objectos.Falta;

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
 * Created by Felix Adrian on 30/05/2017.
 */

public class ServiciosFallas {

    private static ServiciosFallas serviciosFallas = new ServiciosFallas();
    public  static ArrayList<Falta> listaFallas = new ArrayList<>();

    public static ServiciosFallas getInstance() {
        return serviciosFallas;
    }

    public  void fallasById(){
        new getCountFallas().execute();
    }

    private class getCountFallas extends AsyncTask<String, Void, String> {
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
                jsonObject.accumulate("accion", "consultar_faltas");
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
                        Falta falta = new Falta();
                        falta.setIdUsuario(jsonArray.getJSONObject(i).getInt("usuario_id"));
                        falta.setMotivoFalta(jsonArray.getJSONObject(i).getString("falta_motivo"));
                        falta.setObservacionFalta(jsonArray.getJSONObject(i).getString("falta_observacion"));
                        falta.setFechaFalta(jsonArray.getJSONObject(i).getString("falta_fecha"));
                        listaFallas.add(falta);
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
