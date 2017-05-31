package com.example.felixadrian.desertorest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.felixadrian.objectos.Lead;
import com.example.felixadrian.objectos.LeadsRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeadsFragment extends Fragment {


    ListView mLeadsList;
    ArrayAdapter<Lead> mLeadsAdapter;

    public LeadsFragment() {
        // Required empty public constructor
    }

    public static LeadsFragment newInstance() {
        LeadsFragment fragment = new LeadsFragment();
        // Setup parámetros
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Gets parámetros
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_leads, container, false);

        // Instancia del ListView.
        LeadsRepository leadsRepository = new LeadsRepository();
        mLeadsList = (ListView) root.findViewById(R.id.leads_list);
        mLeadsAdapter = new LeadsAdapter(getActivity(), leadsRepository.getLeads());
        mLeadsList.setAdapter(mLeadsAdapter);

        return root;
    }

}
