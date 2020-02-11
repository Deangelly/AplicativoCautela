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
import com.unasusam.appcautela.DAO.DadosEmprestimo;
import com.unasusam.appcautela.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogEditUser extends AppCompatDialogFragment {
    private DialogEditUser.DialogEdit listener;
    DadosEmprestimo dadosEmprestimo;
    View view = null;
    int posicao;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.edit_user,null);
        setDados(dadosEmprestimo,posicao);
        builder.setView(view)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!checkFields(view)) {
                    Toast.makeText(getActivity(), "Preencha todos os dados necessários", Toast.LENGTH_SHORT).show();
                } else {
                    DadosEmprestimo dados = new DadosEmprestimo();
                    TextInputEditText usuario = view.findViewById(R.id.usuario);
                    TextInputEditText tablet = view.findViewById(R.id.tablet);
                    TextInputEditText descricao = view.findViewById(R.id.observations);

                    dados.setNome(Objects.requireNonNull(usuario.getText()).toString());
                    dados.setTablet(Objects.requireNonNull(tablet.getText()).toString());
                    dados.setDescricao(Objects.requireNonNull(descricao.getText()).toString());

                    listener.updateUserData(dados,posicao);
                }
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogEdit) context;

        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement DialogEdit");
        }
    }


    public interface DialogEdit{
        void updateUserData(DadosEmprestimo dados, int position);

    }



    private static boolean checkFields(View view) {
        boolean fields = true;
        TextInputEditText usuario = view.findViewById(R.id.usuario);
        TextInputEditText tablet = view.findViewById(R.id.tablet);
        if (Objects.requireNonNull(usuario.getText()).toString().replace(" ","").equals("")) {
            fields = false;
        } else if (Objects.requireNonNull(tablet.getText()).toString().replace(" ", "").equals("")) {
            fields = false;
        }

        return fields;
    }


    public void setDados(DadosEmprestimo dados, int position){
        TextInputEditText usuario = view.findViewById(R.id.usuario);
        TextInputEditText tablet = view.findViewById(R.id.tablet);
        TextInputEditText descricao = view.findViewById(R.id.observations);

        usuario.setText(Objects.requireNonNull(dados.getNome()));
        tablet.setText(Objects.requireNonNull(dados.getTablet()));
        descricao.setText(Objects.requireNonNull(dados.getDescricao()));
    }

    public void setDadosEmprestimo(DadosEmprestimo dadosEmprestimo, int position){
        this.dadosEmprestimo = dadosEmprestimo;
        posicao = position;
    }



}
