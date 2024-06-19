package com.example.proyectoufc.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProyectoUFC extends SQLiteOpenHelper {
    private static final String nameDB = "ProyectoUFC.db";
    private static final int versionDB = 1;
    private static final String createTableUsuario = "create table if not exists Usuario (id integer, correo varchar(255), clave varchar(255))";
    private static final String dropTableUsuario = "drop table if exists Usuario";
    private static final String createTableHistorial = "CREATE TABLE IF NOT EXISTS Historial (id INTEGER PRIMARY KEY AUTOINCREMENT, terminoBusqueda VARCHAR(255), fecha TEXT)";
    private static final String dropTableHistorial = "drop table if exists Historial";

    public ProyectoUFC(@Nullable Context context) {
        super(context, nameDB, null, versionDB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableUsuario);
        db.execSQL(createTableHistorial);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTableUsuario);
        db.execSQL(createTableUsuario);
        db.execSQL(dropTableHistorial);
        db.execSQL(createTableHistorial);
    }

    public boolean agregarUsuario(int id, String correo, String clave) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("correo", correo);
            values.put("clave", clave);
            db.insert("Usuario", null, values);
            db.close();
            return true;
        }
        return false;
    }

    public boolean agregarHistorial(String terminoBusqueda) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("terminoBusqueda", terminoBusqueda);
            values.put("fecha", getCurrentDate());
            long result = db.insert("Historial", null, values);
            db.close();
            return true;
        }
        return false;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public boolean recordoSesion() {
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            Cursor cursor = db.rawQuery("select id from Usuario;", null);
            boolean result = cursor.moveToNext();
            cursor.close();
            return result;
        }
        return false;
    }

    public String getValue(String campo) {
        SQLiteDatabase db = getReadableDatabase();
        String consulta = String.format("select %s from Usuario", campo);
        if (db != null) {
            Cursor cursor = db.rawQuery(consulta, null);
            if (cursor.moveToNext()) {
                String value = cursor.getString(0);
                cursor.close();
                return value;
            }
            cursor.close();
        }
        return null;
    }

    public boolean eliminarUsuario() {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            db.execSQL("delete from Usuario;");
            db.close();
            return true;
        }
        return false;
    }

    public boolean eliminarTablaHistorial() {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            db.execSQL("drop table if exists Historial");
            db.close();
            return true;
        }
        return false;
    }
}