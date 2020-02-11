package com.unasusam.appcautela.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unasusam.appcautela.DAO.CautelasEntity;
import com.unasusam.appcautela.DAO.DadosEmprestimo;
import com.unasusam.appcautela.NovaCautelaActivity;
import com.unasusam.appcautela.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<AlunoItemViewHolder> {

    private List<DadosEmprestimo> dadosEmprestimo = new ArrayList<>();
    private CautelasEntity cautelasEntity;
    private final LayoutInflater mInflater;
    private Context context;

    private com.unasusam.appcautela.NovaCautelaActivity NovaCautelaActivity;

    public MyAdapter(final Context context, final NovaCautelaActivity NovaCautelaActivity) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.NovaCautelaActivity = NovaCautelaActivity;
    }

    @NonNull
    @Override
    public AlunoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;//mInflater.inflate(R.layout.aluno_item,parent,false);
        if (viewType == R.layout.aluno_item) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.aluno_item, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.button, parent, false);
        }
        AlunoItemViewHolder viewHolder = new AlunoItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AlunoItemViewHolder holder, final int position) {
        //holder.checkBox.setEnabled(false);

        if (position == dadosEmprestimo.size()) {
            holder.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NovaCautelaActivity.saveCautela(dadosEmprestimo);
                }
            });
        } else {

            if (dadosEmprestimo.get(position).getNome().length() > 25) {
                holder.alunoNome.setText(dadosEmprestimo.get(position).getNome().substring(0, 25));
            } else {
                holder.alunoNome.setText(dadosEmprestimo.get(position).getNome());
            }
            // holder.matriculaAluno.setText(dadosEmprestimo.get(position).getMatriculaAluno());
            holder.numeroTablet.setText(dadosEmprestimo.get(position).getTablet());
            holder.horaEmprestimo.setText(dadosEmprestimo.get(position).getHoraDoEmprestimo());
            if (!dadosEmprestimo.get(position).getHoraDaDevolucao().equals("")) {
                holder.tabletDeliver.setImageDrawable(context.getResources().getDrawable(R.drawable.selection_done));
                holder.horaDevolucao.setText(dadosEmprestimo.get(position).getHoraDaDevolucao());
            }
      /*  holder.horaEmprestimo.setText(dadosEmprestimo.get(position).getHoraDoEmprestimo());
        holder.horaDevolucao.setText(dadosEmprestimo.get(position).getHoraDaDevolucao());
        holder.descricao.setText(dadosEmprestimo.get(position).getDescricao());

        holder.lnlLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.relativeLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });*/
            holder.editUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NovaCautelaActivity.openEditUser(dadosEmprestimo.get(position), position);
                }
            });
            holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    NovaCautelaActivity.openDialogDeleteItem(position);
                    return false;
                }
            });

            holder.observations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dadosEmprestimo.get(position).getDescricao() != null && !dadosEmprestimo.get(position).getDescricao().equals("")) {
                        NovaCautelaActivity.openDescription(dadosEmprestimo.get(position).getDescricao());
                    } else {
                        Toast.makeText(context, "Esse tablet não contem descrição", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    public void setDados(DadosEmprestimo dadosEmprestimo) {
        this.dadosEmprestimo.add(0, dadosEmprestimo);
        Toast.makeText(NovaCautelaActivity, "Adicionado com sucesso!", Toast.LENGTH_SHORT).show();
        NovaCautelaActivity.hideEmpty();
        NovaCautelaActivity.textAsBitmap(String.valueOf(this.dadosEmprestimo.size()), 40, context.getResources().getColor(R.color.colorPurple));
    }

    public void setDados(List<DadosEmprestimo> dadosEmprestimo) {
        this.dadosEmprestimo.clear();
        this.dadosEmprestimo.addAll(dadosEmprestimo);
        if (dadosEmprestimo.size() == 0) {
            NovaCautelaActivity.showEmpty();
        } else {
            NovaCautelaActivity.hideEmpty();
        }
        notifyDataSetChanged();
        NovaCautelaActivity.textAsBitmap(String.valueOf(dadosEmprestimo.size()), 40, context.getResources().getColor(R.color.colorPurple));
    }

    public List<DadosEmprestimo> getDadosEmprestimo(){
        return dadosEmprestimo;
    }

    public void updateTablet(String tablet) {
        for (int i = 0; i < dadosEmprestimo.size(); i++) {
            if (dadosEmprestimo.get(i).getTablet().equals(tablet)) {
                if (dadosEmprestimo.get(i).getHoraDaDevolucao().equals("")) {
                    dadosEmprestimo.get(i).setHoraDaDevolucao(new SimpleDateFormat("HH:mm dd MMMM yyyy").format(Calendar.getInstance().getTime()));
                    notifyDataSetChanged();
                    Toast.makeText(NovaCautelaActivity, "Tablet entregue com sucesso!", Toast.LENGTH_SHORT).show();
                    NovaCautelaActivity.setIsSaved(false);
                    break;
                }
            } else if (i == dadosEmprestimo.size() - 1) {
                Toast.makeText(NovaCautelaActivity, "Esse tablet não foi emprestado ou não foi registrado!", Toast.LENGTH_LONG).show();

            }
        }
        if (dadosEmprestimo.size() == 0) {
            Toast.makeText(NovaCautelaActivity, "Esse tablet não foi emprestado ou não foi registrado!", Toast.LENGTH_LONG).show();
        }
    }

    public void updateUser(DadosEmprestimo dadosEmprestimo, int posicao) {
        DadosEmprestimo updateDados = this.dadosEmprestimo.get(posicao);
        updateDados.setNome(dadosEmprestimo.getNome());
        updateDados.setTablet(dadosEmprestimo.getTablet());
        updateDados.setDescricao(dadosEmprestimo.getDescricao());
        notifyDataSetChanged();
        Toast.makeText(NovaCautelaActivity, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return dadosEmprestimo.size() + 1;
    }

    public void deleteItem(int position) {
        dadosEmprestimo.remove(position);
        Toast.makeText(NovaCautelaActivity, "Removido com sucesso", Toast.LENGTH_SHORT).show();
        if (dadosEmprestimo.size() == 0) {
            NovaCautelaActivity.showEmpty();
        }
        NovaCautelaActivity.textAsBitmap(String.valueOf(dadosEmprestimo.size()), 40, context.getResources().getColor(R.color.colorPurple));
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == dadosEmprestimo.size()) ? R.layout.button : R.layout.aluno_item;
    }

    public boolean allTabletsDeliver() {
        boolean allDeliver = true;
        for (DadosEmprestimo dadosEmprestimo : dadosEmprestimo
                ) {
            if (dadosEmprestimo.getHoraDaDevolucao() == null) {
                allDeliver = false;
                Toast.makeText(context, "Para salvar cautela faz-se necessário que todos os tablets sejam entregues", Toast.LENGTH_SHORT).show();
            }
        }

        return allDeliver;
    }


}
