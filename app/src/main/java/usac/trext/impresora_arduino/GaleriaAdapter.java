package usac.trext.impresora_arduino;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.GaleriaViewHolder> {

    private ArrayList<String> data;

    public GaleriaAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public GaleriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GaleriaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.imagen_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GaleriaViewHolder holder, final int position) {
        holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeFile("/storage/emulated/0/ImpresoraArduino/" + data.get(position)), 800,800, false));
        holder.txtImagen.setText(data.get(position).replace(".png",""));
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File("/storage/emulated/0/ImpresoraArduino/" + data.get(position));
                file.delete();
                data.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, data.size());
            }
        });
        holder.btnImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class GaleriaViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button btnImprimir, btnEliminar;
        TextView txtImagen;

        public GaleriaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_dibujo);
            btnImprimir = itemView.findViewById(R.id.btnImprimirImg);
            btnEliminar = itemView.findViewById(R.id.btnEliminarImg);
            txtImagen = itemView.findViewById(R.id.txtNombreDibujoImg);
        }
    }
}
