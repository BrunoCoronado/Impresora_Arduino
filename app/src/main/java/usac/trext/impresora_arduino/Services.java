package usac.trext.impresora_arduino;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class Services {

    private Context context;

    public Services(Context context) {
        this.context =  context;
    }

    public void enviarContenido(final String nombre){

        String requestUrl = "http://192.168.0.30:8080/imagenesPost/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Imagen Enviada", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error al enviar imagen", Toast.LENGTH_SHORT).show();
                System.out.println(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                String contenido = "";
                try{
                   contenido = obtenerContenidoArchivo(nombre);
                }catch (Exception ex){}
                postMap.put("image", contenido);
                return postMap;
            }
        };
        Volley.newRequestQueue(this.context).add(stringRequest);
    }

    private String obtenerContenidoArchivo(String nombre) throws Exception{
        File file = new File("/storage/emulated/0/ImpresoraArduino/" + nombre);
        byte[] bytes = new byte[(int)file.length()];
        FileInputStream in = new FileInputStream(file);
        try {
            in.read(bytes);
        } finally {
            in.close();
        }
        return new String(bytes);
    }
}
