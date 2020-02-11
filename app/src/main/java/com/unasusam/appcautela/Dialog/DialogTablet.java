package com.unasusam.appcautela.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.unasusam.appcautela.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogTablet extends AppCompatDialogFragment {

    private DialogTablet.TabletListener listener;
    View view = null;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.scan_tablet,null);
        builder.setView(view)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!checkFields(view)) {
                    Toast.makeText(getActivity(), "Preencha o código do tablet", Toast.LENGTH_SHORT).show();
                } else {
                    TextInputEditText tablet = view.findViewById(R.id.tablet);
                    listener.searchTablet(Objects.requireNonNull(tablet.getText()).toString());
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogTablet.TabletListener) context;

        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface TabletListener{
        void searchTablet(String tablet);

    }



    private static boolean checkFields(View view) {
        boolean fields = true;
        TextInputEditText tablet = view.findViewById(R.id.tablet);
        if (Objects.requireNonNull(tablet.getText()).toString().replace(" ", "").equals("")) {
            fields = false;
        }

        return fields;
    }

    public void onUpdateTablet(String tablet){
        TextInputEditText tabletNumber = view.findViewById(R.id.tablet);
        tabletNumber.setText(tablet);
    }
}
