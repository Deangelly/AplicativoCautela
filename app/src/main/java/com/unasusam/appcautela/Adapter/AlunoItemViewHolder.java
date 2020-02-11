package com.unasusam.appcautela.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unasusam.appcautela.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AlunoItemViewHolder extends RecyclerView.ViewHolder {

    public final TextView alunoNome;
   // public final TextView matriculaAluno;
    public final TextView numeroTablet;
    public final TextView horaEmprestimo;
    public final TextView horaDevolucao;
    /*public final TextView descricao;
    public final CheckBox checkBox;
    public final LinearLayout lnlLayout;
    public final RelativeLayout relativeLayout;*/
    public final ImageView tabletDeliver;
    public final ImageView editUser;
    public final ImageView observations;
    public final RelativeLayout relativeLayout;
    public final TextView saveButton;



    public AlunoItemViewHolder(@NonNull View itemView) {
        super(itemView);
        alunoNome = itemView.findViewById(R.id.user_name);
        //matriculaAluno = itemView.findViewById(R.id.aluno_matricula);
        numeroTablet = itemView.findViewById(R.id.tablet_number);
        horaEmprestimo = itemView.findViewById(R.id.date_deliver);
        horaDevolucao = itemView.findViewById(R.id.date_devolution);
        tabletDeliver = itemView.findViewById(R.id.selection);
        editUser = itemView.findViewById(R.id.edit);
        observations = itemView.findViewById(R.id.observations);
        relativeLayout = itemView.findViewById(R.id.rltv_layout);
        saveButton = itemView.findViewById(R.id.save_cautela);
        /*
        descricao = itemView.findViewById(R.id.descricao);
        checkBox = itemView.findViewById(R.id.checkbox);
        lnlLayout = itemView.findViewById(R.id.lnl_layout);
        relativeLayout = itemView.findViewById(R.id.rlvt_options);*/

    }
}
