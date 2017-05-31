package com.example.felixadrian.objectos;

import com.example.felixadrian.desertorest.R;
import com.example.felixadrian.servicios.ServiciosUsuarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Created by Felix Adrian on 30/05/2017.
 */

public class LeadsRepository {

    private HashMap<String, Lead> leads = new HashMap<>();

    public LeadsRepository () {
        crearData();
    }

    public void  crearData() {
        ArrayList<Usuario> lista = ServiciosUsuarios.getInstance().getListaUsuariosFallas();
        for (int i = 0; i < lista.size(); i++) {
            saveLead(new Lead(lista.get(i).getNombres().toUpperCase() + " " + lista.get(i).getNombres().toUpperCase(), "N° Fallas: " + lista.get(i).getTotalFallas() + " ", "Estudiante", R.drawable.ic_img_desk));
        }
//        saveLead(new Lead("Carlos Lopez", "Asistente", "Hospital Blue",  R.drawable.ic_img_desk));
//        saveLead(new Lead("Sara Bonz", "Directora de Marketing", "Electrical Parts ltd",  R.drawable.ic_img_desk));
    }

    private void saveLead(Lead lead) {
        leads.put(lead.getId(), lead);
    }

    public List<Lead> getLeads() {
        return new ArrayList<>(leads.values());
    }
}
