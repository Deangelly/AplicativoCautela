package com.unasusam.appcautela;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unasusam.appcautela.Adapter.CautelaAdapter;
import com.unasusam.appcautela.DAO.CautelasEntity;
import com.unasusam.appcautela.DAO.DadosEmprestimo;
import com.unasusam.appcautela.DAO.DadosRepository;
import com.unasusam.appcautela.Dialog.DialogDelete;
import com.unasusam.appcautela.Dialog.DialogPDF;
import com.unasusam.appcautela.Dialog.DialogUpload;
import com.unasusam.appcautela.PDFCreator.PDFCreator;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TodasAsCautelas extends AppCompatActivity implements DialogPDF.DialogListener, DialogDelete.DeleteDialog, DialogUpload.uploadDialog {
    private static final int REQUEST_WRITE = 1;


    private DadosRepository dadosRepository;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    LiveData<List<DadosEmprestimo>> dados;
    CautelasEntity cautelasEntity;
    int position;
    TextView empty_state;
    FirebaseFirestore fire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todas_as_cautelas);
        dadosRepository = new DadosRepository(getApplication());
        dadosRepository.getAllDados().observe(this, new Observer<List<CautelasEntity>>() {
            @Override
            public void onChanged(List<CautelasEntity> dadosEmprestimos) {
                setListDados(dadosEmprestimos);
            }
        });
        fire = FirebaseFirestore.getInstance();
        empty_state = findViewById(R.id.text_empty_state);
        recyclerView = findViewById(R.id.recycler);
        FloatingActionButton back_button = findViewById(R.id.arow_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //setListDados();
    }

    public void openDialogPDF(CautelasEntity cautelasEntity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissions()) {
                requestPermission();
            }else{
                setDadosEmprestimo(cautelasEntity);
                DialogPDF dialogPDF = new DialogPDF();
                dialogPDF.show(getSupportFragmentManager(), "dialog pdf");
            }
        }

    }

    public void uploadDialog(CautelasEntity cautelasEntity){
        setDadosEmprestimo(cautelasEntity);
        DialogUpload dialogUpload = new DialogUpload();
        dialogUpload.show(getSupportFragmentManager(), "dialog upload");
    }
    public void setDadosEmprestimo(CautelasEntity cautelasEntity){
        this.cautelasEntity = cautelasEntity;
    }

    public boolean checkPermissions(){
        return (ContextCompat.checkSelfPermission(TodasAsCautelas.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
    }
    public void setListDados(List<CautelasEntity> dadosEmprestimo) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CautelaAdapter(getApplicationContext(), this);
        ((CautelaAdapter) mAdapter).setCautela(dadosEmprestimo);
        if(dadosEmprestimo.size() > 0){
            hideEmpty();
        }else{
            showEmpty();
        }
        recyclerView.setAdapter(mAdapter);
    }

    public void startActivity(CautelasEntity cautelasEntity) {
        Intent intent = new Intent(this, NovaCautelaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("cautela", (Serializable) cautelasEntity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void applyTexts(String type) {
        if (type.equals("frequencia")) {
            PDFCreator pdfCreator = new PDFCreator();
            pdfCreator.criandoPdf(cautelasEntity,this,this, type);
            File pathPDF = new File(pdfCreator.getPdfPath());
            if(pathPDF.exists()){
                Toast.makeText(this, "PDF criado com sucesso", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "Não foi possível criar o PDF :(", Toast.LENGTH_SHORT).show();
            }
        } else if (type.equals("cautela")) {
            PDFCreator pdfCreator = new PDFCreator();
            pdfCreator.criandoPdf(cautelasEntity,this,this,type);
            File pathPDF = new File(pdfCreator.getPdfPath());
            if(pathPDF.exists()){
                Toast.makeText(this, "PDF criado com sucesso", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "Não foi possível criar o PDF :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void openDialogDelete(int position){
        this.position = position;
        DialogDelete dialogDelete = new DialogDelete();
        dialogDelete.show(getSupportFragmentManager(), "dialog delete");
    }

    @Override
    public void deleteDialog() {
        ((CautelaAdapter) mAdapter).deleteItem(position);
    }
    public void delete(CautelasEntity cautelasEntity) {
        dadosRepository.Delete(cautelasEntity);
    }

    public void showEmpty() {
        recyclerView.setVisibility(View.GONE);
        empty_state.setVisibility(View.VISIBLE);
    }

    public void hideEmpty() {
        recyclerView.setVisibility(View.VISIBLE);
        empty_state.setVisibility(View.GONE);

    }

    @Override
    public void uploadDialog() {
        sendDataToFireStorage(cautelasEntity);
    }

    public void sendDataToFireStorage(final CautelasEntity cautelasEntity){
        Map<String, Object> avaliacao = new HashMap<>();
        avaliacao.put("id",String.valueOf(cautelasEntity.getId()));
        avaliacao.put("materia",cautelasEntity.getMateria());
        avaliacao.put("professor",cautelasEntity.getProfessor());
        avaliacao.put("data",cautelasEntity.getData());
        avaliacao.put("monitores",cautelasEntity.getMonitores());
        avaliacao.put("local",cautelasEntity.getLocal());
        avaliacao.put("emprestimos", cautelasEntity.getEmprestimos());
        /*List<DadosEmprestimo> dados = cautelasEntity.getEmprestimos();
        for(int i = 0; i < dados.size(); i ++){
            dados.get(i).setId(String.valueOf(dados.get(i).getId()));
        }*/




        fire.collection("cautelas").document(cautelasEntity.getMateria() + "_" + cautelasEntity.getData()).set(avaliacao).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("###","Sucesso");
                Toast.makeText(TodasAsCautelas.this,"Salvo na nuvem com sucesso!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("###","Falha");
                Toast.makeText(TodasAsCautelas.this,"Não foi possível fazer upload. Tente novamente, por favor.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onDownloadCautelasClick(View view) {
        Intent i = new Intent(TodasAsCautelas.this,CloudCautelasActivity.class);
        startActivity(i);
    }
}
