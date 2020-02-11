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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogUtility extends AppCompatDialogFragment {

    private DialogListener listener;
    DadosEmprestimo dadosEmprestimo;
    List<DadosEmprestimo> dadosEmprestimoList = new ArrayList<>();
    View view = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.novo_usuario, null);
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
                    if (dadosEmprestimo != null) {
                        if (canBorrowThisTablet(Objects.requireNonNull(tablet.getText()).toString()) && canThisPersonBorrowThisTablet(Objects.requireNonNull(dadosEmprestimo.getMatricula()))) {
                            String currentDate = new SimpleDateFormat("HH:mm dd MMMM yyyy").format(Calendar.getInstance().getTime());

                            dados.setNome(Objects.requireNonNull(usuario.getText()).toString());
                            dados.setTablet(Objects.requireNonNull(tablet.getText()).toString());
                            if (dadosEmprestimo != null) {
                                dados.setMatricula(Objects.requireNonNull(dadosEmprestimo.getMatricula()));
                                dados.setEmail(Objects.requireNonNull(dadosEmprestimo.getEmail()));
                                dados.setUnidade(Objects.requireNonNull(dadosEmprestimo.getUnidade()));
                            } else {
                                dados.setMatricula("");
                                dados.setEmail("");
                                dados.setUnidade("");
                            }
                            dados.setDescricao(Objects.requireNonNull(descricao.getText()).toString());
                            dados.setHoraDoEmprestimo(currentDate);
                            dados.setCautela("");
                            dados.setCodCurso("");
                            dados.setNomeCurso("");
                            dados.setHoraDaDevolucao("");
                            long time = System.currentTimeMillis();
                            dados.setIdUnico(String.valueOf(time));
                            listener.applyTexts(dados);

                        }
                    } else{
                        if (canBorrowThisTablet(Objects.requireNonNull(tablet.getText()).toString())) {
                            String currentDate = new SimpleDateFormat("HH:mm dd MMMM yyyy").format(Calendar.getInstance().getTime());

                            dados.setNome(Objects.requireNonNull(usuario.getText()).toString());
                            dados.setTablet(Objects.requireNonNull(tablet.getText()).toString());
                            if (dadosEmprestimo != null) {
                                dados.setMatricula(Objects.requireNonNull(dadosEmprestimo.getMatricula()));
                                dados.setEmail(Objects.requireNonNull(dadosEmprestimo.getEmail()));
                                dados.setUnidade(Objects.requireNonNull(dadosEmprestimo.getUnidade()));
                            } else {
                                dados.setMatricula("");
                                dados.setEmail("");
                                dados.setUnidade("");
                            }
                            dados.setDescricao(Objects.requireNonNull(descricao.getText()).toString());
                            dados.setHoraDoEmprestimo(currentDate);
                            dados.setCautela("");
                            dados.setCodCurso("");
                            dados.setNomeCurso("");
                            dados.setHoraDaDevolucao("");
                            long time = System.currentTimeMillis();
                            dados.setIdUnico(String.valueOf(time));
                            listener.applyTexts(dados);

                        }
                    }
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

  /*  @Override
    public void applyResult(DadosEmprestimo dados) {
        TextInputEditText usuario = view.findViewById(R.id.usuario);
        TextInputEditText tablet = view.findViewById(R.id.tablet);
        TextInputEditText descricao = view.findViewById(R.id.observations);
        if(dados.getNome() != null){
            usuario.setText(dados.getNome());
        }
        if(dados.getTablet() != null){
            tablet.setText(dados.getTablet());
        }
        if(dados.getDescricao() != null){
            descricao.setText(dados.getDescricao());
        }

    }*/

    public interface DialogListener {
        void applyTexts(DadosEmprestimo dados);

    }




    /* public static void showDialogLogout(final Activity activity, int width, int height) {
        final LayoutInflater inflater = activity.getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.novo_usuario, null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        Button btnCancelar = alertLayout.findViewById(R.id.buttonCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btnSalvar = alertLayout.findViewById(R.id.buttonSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkFields(alertLayout)) {
                    Toast.makeText(activity, "Preencha todos os dados necessários", Toast.LENGTH_SHORT).show();
                } else {
                    DadosEmprestimo dados = new DadosEmprestimo();
                    TextInputEditText usuario = alertLayout.findViewById(R.id.usuario);
                    TextInputEditText tablet = alertLayout.findViewById(R.id.tablet);
                    TextInputEditText descricao = alertLayout.findViewById(R.id.descricao);

                    dados.setNomeAluno(Objects.requireNonNull(usuario.getText()).toString());
                    dados.setTablet(Objects.requireNonNull(tablet.getText()).toString());
                    dados.setDescricao(Objects.requireNonNull(descricao.getText()).toString());

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);

    }*/

    private static boolean checkFields(View view) {
        boolean fields = true;
        TextInputEditText usuario = view.findViewById(R.id.usuario);
        TextInputEditText tablet = view.findViewById(R.id.tablet);
        if (Objects.requireNonNull(usuario.getText()).toString().replace(" ", "").equals("")) {
            fields = false;
        } else if (Objects.requireNonNull(tablet.getText()).toString().replace(" ", "").equals("")) {
            fields = false;
        }

        return fields;
    }

    public void onUpdateUser(DadosEmprestimo dadosEmprestimo) {
        this.dadosEmprestimo = dadosEmprestimo;
        TextInputEditText usuario = view.findViewById(R.id.usuario);
        usuario.setText(dadosEmprestimo.getNome());
    }

    public void onUptadeTablet(String codTablet) {
        TextInputEditText tablet = view.findViewById(R.id.tablet);
        tablet.setText(codTablet);
    }

    public void setDadosEmprestimo(List<DadosEmprestimo> dadosEmprestimoList) {
        this.dadosEmprestimoList = dadosEmprestimoList;
    }

    public boolean canBorrowThisTablet(String tablet) {
        boolean bool = true;

        for (int i = 0; i < dadosEmprestimoList.size(); i++) {
            if (dadosEmprestimoList.get(i).getTablet().equals(tablet)) {
                if (dadosEmprestimoList.get(i).getHoraDaDevolucao().equals("")) {
                    Toast.makeText(getActivity(), "Tablet " + tablet + " ainda não foi devolvido!", Toast.LENGTH_SHORT).show();
                    bool = false;
                }
            }
        }


        return bool;
    }

    public boolean canThisPersonBorrowThisTablet(String matricula) {
        boolean bool = true;

        for (int i = 0; i < dadosEmprestimoList.size(); i++) {
            if (dadosEmprestimoList.get(i).getMatricula().equals(matricula)) {
                if (dadosEmprestimoList.get(i).getHoraDaDevolucao().equals("")) {
                    Toast.makeText(getActivity(), "Essa pessoa tem um tablet pendente, não é possível emprestar outro.", Toast.LENGTH_SHORT).show();
                    bool = false;

                }
            }
        }

        return bool;

    }
}