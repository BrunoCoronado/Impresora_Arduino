package usac.trext.impresora_arduino;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Dibujo extends Fragment {

    private PaintView paintView;

    public Dibujo() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dibujo, container, false);

        paintView = view.findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

        final Spinner colores = view.findViewById(R.id.spnColor);
        String[] items = new String[] {"Rojo", "Azul", "Negro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, items);
        colores.setAdapter(adapter);

        colores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paintView.changeColor(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btnReiniciar = view.findViewById(R.id.btnReiniciar);
        view.findViewById(R.id.btnReiniciar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.clear();
            }
        });

        view.findViewById(R.id.btnImprimir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.imprimir(getArguments().getString("nombre"));
            }
        });

        TextView txtNombre = view.findViewById(R.id.txtNombreDibujo);
        txtNombre.setText(getArguments().getString("nombre"));
        return view;
    }
}
