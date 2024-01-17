package com.example.reto;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private ListView listView;
    private Adaptador adapter;

    public static MyBottomSheetDialogFragment newInstance(ArrayList<String> itemList) {
        MyBottomSheetDialogFragment fragment = new MyBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("itemList", itemList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_dialog, container, false);
        listView = view.findViewById(R.id.listView);

        // Obtiene la lista de elementos del argumento y configúrala en el ListView
        ArrayList<String> itemList = getArguments().getStringArrayList("itemList");
        adapter = new Adaptador(requireContext(), itemList);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtén el texto del argumento y muestra el TextView
        String text = getArguments().getString("text");
        if (text != null) {
            showHelloTextView();
        }
    }

    // Método para agregar un solo "Hola" al adaptador y actualizar la lista
    public void showHelloTextView() {
        if (adapter != null) {
            adapter.add("Hola");
            adapter.notifyDataSetChanged();
        }
    }
}
