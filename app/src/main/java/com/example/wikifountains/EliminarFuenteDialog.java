package com.example.wikifountains;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.wikifountains.data.Fuente;

public class EliminarFuenteDialog extends DialogFragment {
    public interface EliminarFuenteListener {
        void onEliminarConfirmado(Fuente fuente);
    }

    private EliminarFuenteListener listener;
    private Fuente fuente;

    public EliminarFuenteDialog(Fuente fuente) {
        this.fuente = fuente;
    }

    public void setEliminarFuenteListener(EliminarFuenteListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Eliminar fuente")
                .setMessage("¿Deseas eliminar esta fuente?")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onEliminarConfirmado(fuente);
                        }
                    }
                });

        return builder.create();
    }
}