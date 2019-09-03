package usac.trext.impresora_arduino;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

public class Galeria extends Fragment {




    public Galeria() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_galeria, container, false);
        ArrayList<String> data = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view.getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                File directory = new File("/storage/emulated/0/ImpresoraArduino");
                File[] files = directory.listFiles();
                if(files != null){
                    for (int i = 0; i < files.length; i++)
                        data.add(files[i].getName());
                }
            }else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }else{
            File directory = new File("/storage/emulated/0/ImpresoraArduino");
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++)
                data.add(files[i].getName());
        }
        RecyclerView recyclerView = view.findViewById(R.id.imagenes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(new GaleriaAdapter(data));
        return view;
    }
}
