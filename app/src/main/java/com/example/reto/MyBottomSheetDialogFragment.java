package com.example.reto;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private ListView listView;
    private Adaptador adapter;

    private CheckBox camaras;
    private CheckBox incidencias;

    private OnCheckedChangeListener listener;
    private OnCheckedChangeListener camaraCheckedChangeListener;
    private OnCheckedChangeListener incidenciaCheckedChangeListener;

    public static MyBottomSheetDialogFragment newInstance(ArrayList<String> itemList) {
        MyBottomSheetDialogFragment fragment = new MyBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("itemList", itemList);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public void setCamaraCheckedChangeListener(OnCheckedChangeListener listener) {
        this.camaraCheckedChangeListener = listener;
    }

    public void setIncidenciaCheckedChangeListener(OnCheckedChangeListener listener) {
        this.incidenciaCheckedChangeListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_dialog, container, false);
        listView = view.findViewById(R.id.listView);

        ArrayList<String> itemList = getArguments().getStringArrayList("itemList");
        adapter = new Adaptador(requireContext(), itemList);
        listView.setAdapter(adapter);

        CheckBox checkBoxCam = view.findViewById(R.id.checkBox3);
        CheckBox checkBoxInci = view.findViewById(R.id.checkBox);

        checkBoxCam.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int checkBoxCamId = R.id.checkBox3;  // ID del CheckBox de cámaras
            saveCheckboxState(checkBoxCamId, isChecked);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).handleCamaraCheckboxChange(isChecked);
            }
        });

        checkBoxInci.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int checkBoxInciId = R.id.checkBox;  // ID del CheckBox de incidencias
            saveCheckboxState(checkBoxInciId, isChecked);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).handleIncidenciaCheckboxChange(isChecked);
            }
        });

        CheckBox checkBoxUs = view.findViewById(R.id.checkBox2);  // CheckBox del usuario
        checkBoxUs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int checkBoxUsId = R.id.checkBox2;  // ID del CheckBox del usuario
            saveCheckboxState(checkBoxUsId, isChecked);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).handleUserCheckboxChange(isChecked);
            }
        });

// Utiliza el ID del recurso para cargar los estados
        checkBoxCam.setChecked(loadCheckboxState(R.id.checkBox3));
        checkBoxInci.setChecked(loadCheckboxState(R.id.checkBox));
        checkBoxUs.setChecked(loadCheckboxState(R.id.checkBox2));


        return view;
    }


    private void saveCheckboxState(int id, boolean isChecked) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(String.valueOf(id), isChecked);
        editor.apply();
    }

    private boolean loadCheckboxState(int id) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(String.valueOf(id), true);
    }

}

