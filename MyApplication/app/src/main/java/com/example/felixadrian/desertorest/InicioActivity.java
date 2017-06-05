package com.example.felixadrian.desertorest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felixadrian.objectos.Falta;
import com.example.felixadrian.objectos.Usuario;
import com.example.felixadrian.servicios.ServiciosUsuarios;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.felixadrian.desertorest.R.string.navigation_drawer_open;

public class InicioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView navigationView;

    private View inflated;

    Usuario usuario;
    Falta falta;
    private int numPresionar = 0;

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
        ServiciosUsuarios.getInstance().usuarios(usuario.getId());
        ServiciosUsuarios.getInstance().usuariosFallas();
        ServiciosUsuarios.getInstance().docentesNiveles(usuario.getId());

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
            drawer.closeDrawer(GravityCompat.START); // Cierra menu
            numPresionar = 0;
        }else {
            if (numPresionar > 0) {
                finishAffinity();
                //super.onBackPressed(); // Se va a la ventana Icial
                numPresionar = 0;
            } else {
                numPresionar++;
                Toast.makeText(getBaseContext(), "Presione otra vez para salir", Toast.LENGTH_LONG).show();
            }
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
            Intent intent = new Intent(getApplicationContext(), LeadsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.exit_to_app) {
            finishAffinity();
            // Se hace llamado a la actividad o inten inicial y se
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
