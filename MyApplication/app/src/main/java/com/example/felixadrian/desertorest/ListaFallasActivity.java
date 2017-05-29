package com.example.felixadrian.desertorest;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.felixadrian.objectos.Usuario;
import com.example.felixadrian.servicios.ServiciosUsuarios;

import java.util.ArrayList;

public class ListaFallasActivity extends AppCompatActivity {

    private TextView mTextMessage;

    ListView lista;
    ArrayAdapter<String> adaptador;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_fallas);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        lista = (ListView)findViewById(R.id.lista_fallas);
        llenarAdactador();
    }

    public void llenarAdactador() {
        listaUsuarios = ServiciosUsuarios.getInstance().listaUsuarios;
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < listaUsuarios.size(); i++) {
            strings.add(listaUsuarios.get(i).getNombres() + " " + listaUsuarios.get(i).getNombres());

        }

        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, strings);
        lista.setAdapter(adaptador);
    }


}
