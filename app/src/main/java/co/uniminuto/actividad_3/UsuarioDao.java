package co.uniminuto.actividad_3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class UsuarioDao {
    private GestionBD gestionBD;
    Context context;
    View view;
    Usuario usuario;

    public UsuarioDao(Context context) {
        this.context = context;
    }

    public UsuarioDao(View view) {
        this.view = view;
    }


    public UsuarioDao(Context context, View view){
        this.context= context;
        this.view= view;
        gestionBD = new GestionBD(this.context);
    }

    //Insertar datos en la tabla Usuarios
    public  void insertUser(Usuario usuario){
        try {
            SQLiteDatabase db = gestionBD.getWritableDatabase();
            if (db != null) {
                ContentValues values = new ContentValues();
                values.put("USU_DOCUMENTO", usuario.getDocumento());
                values.put("USU_USUARIOS", usuario.getUsuario());
                values.put("USU_NOMBRES", usuario.getNombres());
                values.put("USU_APELLIDOS", usuario.getApellidos());
                values.put("USU_PASS", usuario.getPassword());
                //Vamos a mandar a insertar
                long response = db.insert("usuarios", null, values);
                //Muestra la respues de la  insercion
                db.close();
            } else {
                Snackbar.make(this.view, "No se ha registrado el usuario. ", Snackbar.LENGTH_LONG).show();
            }

        }catch(SQLException sqlException){
            Log.i("DB", ""+sqlException);
        }
    }

    public ArrayList<Usuario> getUserList(){
        SQLiteDatabase db = gestionBD.getReadableDatabase();
        String query= "select * from usuarios";
        ArrayList<Usuario> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                usuario = new Usuario();
                usuario.setDocumento(cursor.getInt(0));
                usuario.setUsuario(cursor.getString(1));
                usuario.setNombres(cursor.getString(2));
                usuario.setApellidos(cursor.getString(3));
                usuario.setPassword(cursor.getString(4));
                userList.add(usuario);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

}
