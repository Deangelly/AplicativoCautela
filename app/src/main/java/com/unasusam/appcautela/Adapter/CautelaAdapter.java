package com.unasusam.appcautela.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unasusam.appcautela.DAO.CautelasEntity;
import com.unasusam.appcautela.R;
import com.unasusam.appcautela.TodasAsCautelas;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CautelaAdapter extends RecyclerView.Adapter<CautelaItemViewHolder> {

    List<CautelasEntity> dadosEmprestimo;

    private  final LayoutInflater mInflater;
    private Context context;
    private com.unasusam.appcautela.TodasAsCautelas todasAsCautelas;
    public CautelaAdapter(final Context context, final TodasAsCautelas activity){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.todasAsCautelas = activity;
    }

    @NonNull
    @Override
    public CautelaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cautela_item,parent,false);
        CautelaItemViewHolder viewHolder = new CautelaItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CautelaItemViewHolder holder, final int position) {
        if(dadosEmprestimo.get(position).getMateria().length()>25) {
            holder.subject.setText(dadosEmprestimo.get(position).getMateria().substring(0,25));
        }else{
            holder.subject.setText(dadosEmprestimo.get(position).getMateria());
        }

        if(dadosEmprestimo.get(position).getData().length()>25){
            holder.date.setText(dadosEmprestimo.get(position).getData().substring(0,25));
        }else{
            holder.date.setText(dadosEmprestimo.get(position).getData());
        }
        if(dadosEmprestimo.get(position).getProfessor().length()>25){
            holder.teacher.setText(dadosEmprestimo.get(position).getProfessor().substring(0,25));
        }else{
            holder.teacher.setText(dadosEmprestimo.get(position).getProfessor());

        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todasAsCautelas.startActivity(dadosEmprestimo.get(position));
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todasAsCautelas.openDialogPDF(dadosEmprestimo.get(position));
            }
        });
        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                todasAsCautelas.openDialogDelete(position);
                return false;
            }
        });
        holder.uplooad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todasAsCautelas.uploadDialog(dadosEmprestimo.get(position));
            }
        });

        holder.monitor.setVisibility(View.GONE);
        holder.imageViewMonitor.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return dadosEmprestimo.size();
    }

    public void setCautela(List<CautelasEntity> dadosEmprestimo){
        this.dadosEmprestimo = dadosEmprestimo;
    }

    public void deleteItem(int position) {
        todasAsCautelas.delete(dadosEmprestimo.get(position));
        dadosEmprestimo.remove(position);
        if(dadosEmprestimo.size() == 0){
            todasAsCautelas.showEmpty();
        }
        notifyDataSetChanged();
    }


}
