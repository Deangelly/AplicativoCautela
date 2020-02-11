package com.unasusam.appcautela.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.unasusam.appcautela.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogDescription extends AppCompatDialogFragment {
    View view = null;
    String descricao;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_description,null);
        updateDescricao(descricao);
        builder.setView(view)
                .setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    public void setDescicao(String descricao){
        this.descricao = descricao;
    }

    public void updateDescricao(String descricao){
        TextInputEditText description = view.findViewById(R.id.observations);
        description.setText(descricao);
        description.setEnabled(false);
    }
}
