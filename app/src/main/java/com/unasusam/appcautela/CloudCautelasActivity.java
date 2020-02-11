package com.unasusam.appcautela;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unasusam.appcautela.Adapter.CloudAdapter;
import com.unasusam.appcautela.DAO.CautelasEntity;
import com.unasusam.appcautela.DAO.DadosEmprestimo;
import com.unasusam.appcautela.DAO.DadosRepository;
import com.unasusam.appcautela.Dialog.DialogDownloadFromCloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CloudCautelasActivity extends AppCompatActivity implements DialogDownloadFromCloud.downloadCloudDialog {

    private DadosRepository dadosRepository;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    LiveData<List<DadosEmprestimo>> dados;
    CautelasEntity cautelasEntity;
    List<CautelasEntity> cautelasEntities = new ArrayList<>();
    TextView empty_state;
    FirebaseFirestore db;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_cautelas);
        recyclerView = findViewById(R.id.recycler);
        empty_state = findViewById(R.id.text_empty_state);
        progressBar = findViewById(R.id.progress_bar);
        db = FirebaseFirestore.getInstance();
        dadosRepository = new DadosRepository(getApplication());
        FloatingActionButton back_button = findViewById(R.id.arow_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getListApresentadores();
        hideEmpty();
    }

    public void setListDados(List<CautelasEntity> dadosEmprestimo) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CloudAdapter(getApplicationContext(), this);
        ((CloudAdapter) mAdapter).setCautela(dadosEmprestimo);
        if (dadosEmprestimo.size() > 0) {
            hideEmpty();
            progressBar.setVisibility(View.GONE);
        } else {
            showEmpty();
            progressBar.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(mAdapter);
    }

    public void showEmpty() {
        recyclerView.setVisibility(View.GONE);
        empty_state.setVisibility(View.VISIBLE);
    }

    public void hideEmpty() {
        recyclerView.setVisibility(View.VISIBLE);
        empty_state.setVisibility(View.GONE);

    }

    public void getListApresentadores() {
        cautelasEntities.clear();


        db.collection("cautelas")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        ArrayList<HashMap> myData = (ArrayList<HashMap>) document.getData().get("emprestimos");
                        CautelasEntity cautelasEntity = new CautelasEntity((String) document.get("materia"), (String) document.get("professor"), (String) document.get("data"), (String) document.get("monitores"), (String) document.get("local"), convertHashToDados(myData));
                        cautelasEntities.add(cautelasEntity);
                        Log.d("###", document.getId() + " => " + document.getData());
                    }
                    setListDados(cautelasEntities);

                } else {
                    Log.w("###", "Error getting documents.", task.getException());
                    progressBar.setVisibility(View.GONE);

                }
            }
        });

    }

  /*  private List<DadosEmprestimo> collectApresentationData(Map<String, Object> users) {

        //ArrayList<Apresentacao> apresentacoes = new ArrayList<>();
        List<DadosEmprestimo> dadosEmprestimos = new ArrayList<>();
        DadosEmprestimo dadosEmprestimo = null;
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            //  apresentacoes.add(new Apresentacao((String) singleUser.get("nome"),(String) singleUser.get("title")));
            dadosEmprestimo = new DadosEmprestimo((int) singleUser.get(0), (String) singleUser.get(1), (String) singleUser.get(2), (String) singleUser.get(3), (String) singleUser.get(4), (String) singleUser.get(5), (String) singleUser.get(6), (String) singleUser.get(7), (String) singleUser.get(8), (String) singleUser.get(9), (String) singleUser.get(10), (String) singleUser.get(11));
            dadosEmprestimos.add(dadosEmprestimo);
        }
        //resumos.addAll(apresentacoes);
        return dadosEmprestimos;
    }*/

    private List<DadosEmprestimo> convertHashToDados(ArrayList<HashMap> myData) {
        List<DadosEmprestimo> dadosEmprestimos = new ArrayList<>();

        for (int i = 0; i < myData.size(); i++) {
            DadosEmprestimo dadosEmprestimo =
                    new DadosEmprestimo(/*Integer.valueOf((String) myData.get(i).get("id")),*/
                            (String) myData.get(i).get("matricula"),
                            (String) myData.get(i).get("nome"),
                            (String) myData.get(i).get("email"),
                            (String) myData.get(i).get("unidade"),
                            (String) myData.get(i).get("codCurso"),
                            (String) myData.get(i).get("nomeCurso"),
                            (String) myData.get(i).get("tablet"),
                            (String) myData.get(i).get("descricao"),
                            (String) myData.get(i).get("horaDoEmprestimo"),
                            (String) myData.get(i).get("horaDaDevolucao"),
                            (String) myData.get(i).get("cautela"),
                            (String) myData.get(i).get("idUnico"));
            dadosEmprestimos.add(dadosEmprestimo);

        }


        return dadosEmprestimos;
    }

    @Override
    public void downloadCloudDialog(CautelasEntity cautela) {
        insertAll(cautela);
        Toast.makeText(this, "Cautela foi salva com sucesso!", Toast.LENGTH_SHORT).show();
    }

    public void showDialogDownloadFromCloud(CautelasEntity cautelasEntity){
        DialogDownloadFromCloud dialog = new DialogDownloadFromCloud();
        dialog.show(getSupportFragmentManager(),"downloadFromCloud");
        dialog.setCautelasEntity(cautelasEntity);
    }

    public void update(CautelasEntity cautelasEntity) {
        dadosRepository.Update(cautelasEntity);
    }
    public void insertAll(CautelasEntity cautelasEntities) {
        dadosRepository.InsertAllNotes(cautelasEntities);
    }

}
