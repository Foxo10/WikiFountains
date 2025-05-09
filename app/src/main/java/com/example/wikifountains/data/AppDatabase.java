package com.example.wikifountains.data;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Fuente.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract FuenteDao fuenteDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            // Eliminar la base de datos existente
            //deleteDatabase(context);
            context.deleteDatabase("fuentes_database");

            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "fuentes_database")
                    .allowMainThreadQueries()  // Permitir consultas en el hilo principal (solo para pruebas)
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                // Solo insertar si la tabla está vacía
                                if (instance.fuenteDao().countFuentes() == 0) {
                                    instance.fuenteDao().insert(PREPOPULATE_DATA);
                                    Log.d(TAG, "Datos iniciales insertados");
                                }
                            });
                        }
                    })
                    .build();
        }
        return instance;
    }

    private static void deleteDatabase(Context context) {
        try {
            File database = context.getDatabasePath("fuentes_database");
            if (database.exists()) {
                database.delete();
                Log.d(TAG, "Base de datos eliminada");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al eliminar la base de datos: ", e);
        }
    }

    // Lista de fuentes predeterminadas (sin imágenes)
    private static final List<Fuente> PREPOPULATE_DATA = Arrays.asList(
            new Fuente("Fuente de Neguri", "Getxo", "12 Av. Algortako Etorbidea", "43.33934510217123,-3.008521173851977", ""),
            new Fuente("Fuente plaza del casino", "Getxo", "49 Av. Basagoiti", "43.34876176033387,-3.0112277566790775", ""),
            new Fuente("Fuente de la playa", "Sopela", "Av. Atxabiribil, 77", "43.39000284440325,2.9923713401220398", ""),
            new Fuente("Fuente en Larrabasterra", "Sopela", "Gatzarriñe kalea, 9", "43.37695174670079,-2.9894370846529537", ""),
            new Fuente("Fuente de Zabalbide", "Bilbao", "Zabalbide Kalea, 5, Ibaiondo", "43.25581662072112,-2.921622020548674",""),
            new Fuente("Fuente del perro", "Bilbao","Txakur Kalea, 2-4, Ibaiondo","43.25780813835872,-2.924311014893625","Fuente de estilo neoclásico del 1800 adosada a una pared de la calle del Perro. Tiene tres caños y un poyo en el que descansar después de refrescarnos.")
            );

}