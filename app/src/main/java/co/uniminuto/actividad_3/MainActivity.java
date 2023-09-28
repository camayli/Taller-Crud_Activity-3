package co.uniminuto.actividad_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etDocumento;

    private EditText etUsuario;

    private EditText etNombres; private EditText etApellidos;

    private EditText etContra; private ListView listaUsuarios;

    int documento;

    String usuario;

    String nombres;

    String apellidos;

    String contra;

    private GestionBD gestionbd;

    SQLiteDatabase baseDatos;

    Boolean validar;

    int v1=0;
    int v2=0;
    int v3=0;
    int v4=0;
    int v5=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializador();
    }


    private void inicializador(){
        etDocumento = findViewById(R.id.ETDocumento);
        etUsuario = findViewById(R.id.ETUsuario);
        etNombres = findViewById(R.id.ETNombres);
        etApellidos = findViewById(R.id.ETApellido);
        etContra = findViewById(R.id.ETContraseña);
        listaUsuarios = findViewById(R.id.LV_lista);
        this.listarUsuarios();
    }

    private void listarUsuarios(){
        UsuarioDao usuarioDao = new UsuarioDao(this,findViewById(R.id.LV_lista));
        ArrayList<Usuario> userlist = usuarioDao.getUserList();
        ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,userlist);
        listaUsuarios.setAdapter(adapter);
    }

    public void SetearDatos(){
        //Validar documento
        if(!validardocumento(etDocumento.getText().toString().trim())){
            Toast.makeText(this, "Valide el documento, no debe tener letras y debe ser de 7 o 10 digitos", Toast.LENGTH_LONG).show();
        }else{
            this.documento= Integer.parseInt(etDocumento.getText().toString());
            v1=1;
        }
        //Validar nombres
        if(!validarnombre(etNombres.getText().toString().trim())){
            Toast.makeText(this, "El nombre es muy corto o tiene caracteres especiales", Toast.LENGTH_LONG).show();
        }else{
            this.nombres=etNombres.getText().toString();
            v2=1;
        }
        //Validar Apellidos
        if(!validarnombre(etApellidos.getText().toString().trim())){
            Toast.makeText(this, "El apellido es muy corto o tiene caracteres especiales", Toast.LENGTH_LONG).show();
        }else{
            this.apellidos=etApellidos.getText().toString();
            v3=1;
        }
        //Validar Usuarios
        if(!validarnombre(etUsuario.getText().toString().trim())){
            Toast.makeText(this, "El usuario es muy corto o tiene caracteres especiales", Toast.LENGTH_LONG).show();
        }else{
            this.usuario=etUsuario.getText().toString();
            v4=1;
        }
        //Validar Contraseña
        if(!validarcontra(etContra.getText().toString().trim())){
            Toast.makeText(this, "La contraseña debe tener mas de 6 caracteres y maximo 25 e incluir por lo menos una mayuscula y un numero", Toast.LENGTH_LONG).show();
        }else{
            this.contra=etContra.getText().toString();
            v5=1;
        }
    }
    public void accionRegistrar(View view){

        SetearDatos();
        if(v1==1 && v2==1 && v3==1 && v4==1 && v5==1 ) {
            UsuarioDao usuarioDao = new UsuarioDao(this, view);
            Usuario usuario1 = new Usuario();
            usuario1.setNombres(this.nombres);
            usuario1.setApellidos(this.apellidos);
            usuario1.setUsuario(this.usuario);
            usuario1.setPassword(this.contra);
            usuario1.setDocumento(this.documento);
            usuarioDao.insertUser(usuario1);
            this.listarUsuarios();

            Toast.makeText(this, "Se ha registrado el usuario", Toast.LENGTH_LONG).show();
            v1=0;
            v2=0;
            v3=0;
            v4=0;
            v5=0;
        }else {
            Toast.makeText(this, "No se pudo completar el registro, valide los datos ingresados", Toast.LENGTH_LONG).show();
        }
    }

    //Metodo para buscar documento -Listar
    public void Traerdatos(View view){
        try {
            GestionBD bd = new GestionBD(this);
            SQLiteDatabase db = bd.getWritableDatabase();
            String docubuscar= etDocumento.getText().toString();

            if (!docubuscar.isEmpty()) {
                Cursor fila = db.rawQuery
                        ("select USU_NOMBRES, USU_APELLIDOS, USU_USUARIOS, USU_PASS from usuarios where USU_DOCUMENTO =" + docubuscar, null);
                if (fila.moveToFirst()) {
                    etNombres.setText(fila.getString(0));
                    etApellidos.setText(fila.getString(1));
                    etUsuario.setText(fila.getString(2));
                    etContra.setText(fila.getString(3));
                    db.close();
                } else {
                    Toast.makeText(this, "No se encuentra el documento, por favor verifique", Toast.LENGTH_LONG).show();
                    db.close();
                }
            }
        }catch(SQLException sqlException){
            Log.i("DB", ""+sqlException);
        }
    }

    //Metodo para modificar
    public void modificar (View view){
        try {
            GestionBD bd = new GestionBD(this);
            SQLiteDatabase db = bd.getWritableDatabase();

            String doc= etDocumento.getText().toString();
            String name= etNombres.getText().toString();
            String lastname= etApellidos.getText().toString();
            String user= etUsuario.getText().toString();
            String pass= etContra.getText().toString();

            if (!name.isEmpty() && !lastname.isEmpty() && !user.isEmpty() && !pass.isEmpty()) {

                ContentValues values1 = new ContentValues();
                values1.put("USU_DOCUMENTO", doc);
                values1.put("USU_USUARIOS", user);
                values1.put("USU_NOMBRES", name );
                values1.put("USU_APELLIDOS", lastname);
                values1.put("USU_PASS", pass);

                int validarupdate = db.update("usuarios", values1, "USU_DOCUMENTO="+ doc, null);

if (validarupdate==1){
    Toast.makeText(this, "Se ha modificado el registro", Toast.LENGTH_LONG).show();
    db.close();
    this.listarUsuarios();
            } else {
                    Toast.makeText(this, "No se encuentra el documento, por favor verifique", Toast.LENGTH_LONG).show();
                    db.close();
                }
            }
        }catch(SQLException sqlException){
            Log.i("DB", ""+sqlException);
        }
    }

    //Metodos REGEX de validacion de campos
    public static boolean validardocumento(String docu){
        return docu.matches("^[0-9]{7,10}$");
    }
    public static boolean validarnombre(String nombre){
        return nombre.matches("^[a-zA-Z]{3,145}$");
    }
    public static boolean validarcontra(String nombre){
        return nombre.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,25}$");
    }

}