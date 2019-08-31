package usac.trext.impresora_arduino;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                nuevoDibujo();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_nuevo) {
            nuevoDibujo();
            return true;
        }else if(id == R.id.action_galeria){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void nuevoDibujo(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText txtNombre = new EditText(this);
        builder.setTitle("Nombre del dibujo");
        builder.setView(txtNombre);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fab.hide();
                Bundle bundle = new Bundle();
                bundle.putString("nombre", txtNombre.getText().toString());
                Dibujo dibujo = new Dibujo();
                dibujo.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, dibujo).commit();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}
