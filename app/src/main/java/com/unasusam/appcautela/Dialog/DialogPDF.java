package com.unasusam.appcautela.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.unasusam.appcautela.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogPDF  extends AppCompatDialogFragment {

    private DialogPDF.DialogListener listener;
    View view = null;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_pdf,null);
        builder.setView(view)
                .setNegativeButton("Cautela", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyTexts("cautela");
                    }
                }).setPositiveButton("Frequência", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    listener.applyTexts("frequencia");

            }
        }).setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogPDF.DialogListener) context;

        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement DialogPDF");
        }
    }


    public interface DialogListener{
        void applyTexts(String type);

    }

}
