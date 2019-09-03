package usac.trext.impresora_arduino;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Dibujo extends Fragment {

    private PaintView paintView;
    private ProgressDialog progressDialog;

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

        view.findViewById(R.id.btnReiniciar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.clear();
            }
        });

        view.findViewById(R.id.btnImprimir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (view.getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        new ImprimirImagen().execute(getArguments().getString("nombre"));
                     else
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }else
                    new ImprimirImagen().execute(getArguments().getString("nombre"));
            }
        });

        TextView txtNombre = view.findViewById(R.id.txtNombreDibujo);
        txtNombre.setText(getArguments().getString("nombre"));
        return view;
    }

    private class ImprimirImagen extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show( getActivity(), "Guardando imagen...", "Por favor espere.");
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... strings) {
            if(!paintView.imprimir(strings[0]))
                Toast.makeText(getContext(),"Error generar imagen.",Toast.LENGTH_SHORT).show();
            //cambiar activity
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
