package com.unasusam.appcautela;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unasusam.appcautela.Adapter.MyAdapter;
import com.unasusam.appcautela.DAO.CautelasEntity;
import com.unasusam.appcautela.DAO.DadosEmprestimo;
import com.unasusam.appcautela.DAO.DadosRepository;
import com.unasusam.appcautela.Dialog.DialogDeleteItem;
import com.unasusam.appcautela.Dialog.DialogDescription;
import com.unasusam.appcautela.Dialog.DialogEditUser;
import com.unasusam.appcautela.Dialog.DialogExit;
import com.unasusam.appcautela.Dialog.DialogTablet;
import com.unasusam.appcautela.Dialog.DialogUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NovaCautelaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogUtility.DialogListener, DialogTablet.TabletListener, DialogEditUser.DialogEdit, DialogDeleteItem.TabletListener, DialogExit.ExitDialog {
    private DadosRepository dadosRepository;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<DadosEmprestimo> dadosEmprestimos = new ArrayList<>();
    List<DadosEmprestimo> listaDados = new ArrayList<>();
    List<DadosEmprestimo> currentLista = new ArrayList<>();
    List<DadosEmprestimo> listaFinal = new ArrayList<>();
    DialogUtility dialogUtility = new DialogUtility();
    DialogTablet dialogTablet = new DialogTablet();
    DialogEditUser dialogEdit = new DialogEditUser();
    DialogExit dialogExit = new DialogExit();
    private TextView textEmpty;
    private TextView saveCautela;
    boolean tableExists = false;
    TextInputEditText disciplina;
    TextInputEditText data;
    TextInputEditText professor;
    TextInputEditText monitores;
    TextInputEditText local;
    boolean isSaved = false;
    TextView saveCautelaButton;
    int id = 0;
    FloatingActionButton floatingActionButton;
    FloatingActionButton floatingActionButton2;
    CautelasEntity previousCautela;
    CautelasEntity currentCautela;
    FirebaseFirestore fire;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_cautela);
        dadosRepository = new DadosRepository(getApplication());
        EventBus.getDefault().register(this);
        String currentDate = new SimpleDateFormat("HH:mm dd MMMM yyyy").format(Calendar.getInstance().getTime());
        saveCautelaButton = findViewById(R.id.save_cautela_init);
        disciplina = findViewById(R.id.disciplina);
        data = findViewById(R.id.data);
        data.setText(currentDate);
        professor = findViewById(R.id.professor);
        monitores = findViewById(R.id.monitores);
        local = findViewById(R.id.local);
        recyclerView = findViewById(R.id.recycler);
        textEmpty = findViewById(R.id.text_empty_state);
        saveCautela = findViewById(R.id.save_cautela);
        recyclerView.setVisibility(View.GONE);
        floatingActionButton = findViewById(R.id.tablet_cout);
        floatingActionButton2 = findViewById(R.id.sync);
        fire = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setListDados();
        if (getIntent().getSerializableExtra("cautela") != null) {
            CautelasEntity cautelasEntity = (CautelasEntity) getIntent().getExtras().get("cautela");
            setCautela(cautelasEntity);
            tableExists = true;
            setIsSaved(true);
            id = cautelasEntity.getId();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NovaCautelaActivity.this, "Sincronizando...", Toast.LENGTH_SHORT).show();
                syncCautela();
            }
        });
        saveCautelaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCautela(((MyAdapter) mAdapter).getDadosEmprestimo());
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if (!canSave()) {
            openDialogExit();
        } else {
            super.onBackPressed();
        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setListDados() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(getApplicationContext(), this);
        ((MyAdapter) mAdapter).setDados(dadosEmprestimos);
        recyclerView.setAdapter(mAdapter);
    }

    public void onNewUsuarioClick(View view) {
        openDialog();
    }

    public void onEntregarTabletClick(View view) {
        openDeliver();
    }


    public void openDeliver() {

        dialogTablet.show(getSupportFragmentManager(), "entregar tablet");

    }

    public void openDialog() {
        dialogUtility.show(getSupportFragmentManager(), "novo usuario");

        dialogUtility.setDadosEmprestimo(((MyAdapter) mAdapter).getDadosEmprestimo());
    }

    public void openEditUser(DadosEmprestimo dadosEmprestimo, int position) {
        dialogEdit.setDadosEmprestimo(dadosEmprestimo, position);
        dialogEdit.show(getSupportFragmentManager(), "editar usuario");
    }

    public void openDescription(String descricao) {
        DialogDescription dialogDescription = new DialogDescription();
        dialogDescription.setDescicao(descricao);
        dialogDescription.show(getSupportFragmentManager(), "show descricao");
    }

    public void openDialogExit() {
        DialogExit dialogExit = new DialogExit();
        dialogExit.show(getSupportFragmentManager(), "dialog exit");
    }

    public void openDialogDeleteItem(int position) {
        DialogDeleteItem dialogDeleteItem = new DialogDeleteItem();
        dialogDeleteItem.setPosition(position);
        dialogDeleteItem.show(getSupportFragmentManager(), "delete item");
    }


    public void onScanTablet(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onScanUsuario(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onDeliverTablet(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 3);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void applyTexts(DadosEmprestimo dados) {
        ((MyAdapter) mAdapter).setDados(dados);
        recyclerView.setAdapter(mAdapter);
        setIsSaved(false);
    }

    @Override
    public void searchTablet(String tablet) {

        ((MyAdapter) mAdapter).updateTablet(tablet);
    }

    @Override
    public void exitDialog() {
        finish();
    }

    @Override
    public void updateUserData(DadosEmprestimo dados, int posicao) {
        ((MyAdapter) mAdapter).updateUser(dados, posicao);
        setIsSaved(false);
    }

    @Override
    public void deleteItem(int position) {

        ((MyAdapter) mAdapter).deleteItem(position);
        setIsSaved(false);

    }


    @Subscribe
    public void onEvent(MessageEvent event) {
        if (event.getmTablet() != null) {
            List<String> type = event.getmTablet();
            if (type.get(0).equals("1")) {
                dialogUtility.onUptadeTablet(type.get(1));
            } else {
                //searchTablet(type.get(1));
                dialogTablet.onUpdateTablet(type.get(1));
            }
        } else {
            dialogUtility.onUpdateUser(event.getDadosEmprestimo());
        }
    }


    public static class MessageEvent {
        public DadosEmprestimo mDadosEmprestimo;
        public List<String> mTablet;

        public MessageEvent(DadosEmprestimo dadosEmprestimo) {
            mDadosEmprestimo = dadosEmprestimo;
        }

        public MessageEvent(List<String> tablet) {
            mTablet = tablet;
        }

        public List<String> getmTablet() {
            return mTablet;
        }

        public DadosEmprestimo getDadosEmprestimo() {
            return mDadosEmprestimo;
        }
    }


    public void showEmpty() {
        recyclerView.setVisibility(View.GONE);
        textEmpty.setVisibility(View.VISIBLE);
    }

    public void hideEmpty() {
        recyclerView.setVisibility(View.VISIBLE);
        textEmpty.setVisibility(View.GONE);

    }

    public void insert(DadosEmprestimo dadosEmprestimo) {
        dadosRepository.Insert(dadosEmprestimo);
    }

   /* public void delete(DadosEmprestimo dadosEmprestimo) {
        dadosRepository.Delete(dadosEmprestimo);
    }*/

    public void insertAll(CautelasEntity cautelasEntities) {
        dadosRepository.InsertAllNotes(cautelasEntities);
    }

    public void update(CautelasEntity cautelasEntity) {
        dadosRepository.Update(cautelasEntity);
    }

    public void deleteAll(String id) {
        dadosRepository.DeleteAllNotes(id);
    }

    public CautelasEntity getCautela() {
        disciplina = findViewById(R.id.disciplina);
        data = findViewById(R.id.data);
        professor = findViewById(R.id.professor);
        monitores = findViewById(R.id.monitores);
        local = findViewById(R.id.local);
        CautelasEntity cautela = new CautelasEntity(disciplina.getText().toString(), professor.getText().toString(), data.getText().toString(), monitores.getText().toString(), local.getText().toString(), dadosEmprestimos);
        return cautela;
    }

    public void setCautela(CautelasEntity cautela) {
        data.setText(cautela.getData());
        disciplina.setText(cautela.getMateria());
        professor.setText(cautela.getProfessor());
        monitores.setText(cautela.getMonitores());
        local.setText(cautela.getLocal());
        ((MyAdapter) mAdapter).setDados(cautela.getEmprestimos());
        recyclerView.setAdapter(mAdapter);
    }

    public boolean getTableExists() {
        return tableExists;
    }

    public void setTableExists() {
        tableExists = true;
        setIsSaved(true);

    }

    public int getId() {
        return id;
    }

    public boolean checkFields() {
        boolean bool = true;
        if (data.getText() == null || data.getText().toString().replaceAll("\\s+", "").equals("")) {
            bool = false;
        }
        if (disciplina.getText() == null || disciplina.getText().toString().replaceAll("\\s+", "").equals("")) {
            bool = false;
        }
        if (professor.getText() == null || professor.getText().toString().replaceAll("\\s+", "").equals("")) {
            bool = false;
        }
        if (monitores.getText() == null || monitores.getText().toString().replaceAll("\\s+", "").equals("")) {
            bool = false;
        }
        if (local.getText() == null || local.getText().toString().replaceAll("\\s+", "").equals("")) {
            bool = false;
        }
        return bool;
    }


    //method to convert your text to image
    public void textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        floatingActionButton.setImageBitmap(image);
    }


    public void setIsSaved(boolean b) {
        isSaved = b;
        if (isSaved == true) {
            previousCautela = getPreviousDados();
        }
    }

    public boolean getIsSaved() {
        return isSaved;
    }

    public CautelasEntity getCurrentDados() {
        return new CautelasEntity(Objects.requireNonNull(disciplina.getText()).toString(),
                Objects.requireNonNull(professor.getText()).toString(),
                Objects.requireNonNull(data.getText()).toString(),
                Objects.requireNonNull(monitores.getText()).toString(),
                Objects.requireNonNull(local.getText()).toString(),
                null);
    }

    public CautelasEntity getPreviousDados() {
        return new CautelasEntity(Objects.requireNonNull(disciplina.getText()).toString(),
                Objects.requireNonNull(professor.getText()).toString(),
                Objects.requireNonNull(data.getText()).toString(),
                Objects.requireNonNull(monitores.getText()).toString(),
                Objects.requireNonNull(local.getText()).toString(),
                null);
    }

    public boolean isDiferent(CautelasEntity atual, CautelasEntity anterior) {
        boolean bool = true;
        if (!atual.getProfessor().equals(anterior.getProfessor())) {
            bool = false;
        } else if (!atual.getMateria().equals(anterior.getMateria())) {
            bool = false;
        } else if (!atual.getMonitores().equals(anterior.getMonitores())) {
            bool = false;
        } else if (!atual.getLocal().equals(anterior.getLocal())) {
            bool = false;
        }

        return !bool;
    }

    public boolean canSave() {
        /*setIsSaved(!isDiferent(previousCautela,getCurrentDados()));
        return isSaved;*/
        boolean bool;
        if (!getIsSaved()) {
            bool = getIsSaved();
        } else {
            bool = !isDiferent(previousCautela, getCurrentDados()); // se tem diferença retorna true, se não retorna false

        }
        return bool;
    }

    public void saveCautela(List<DadosEmprestimo> dadosEmprestimo) {
        if (!checkFields()) {
            Toast.makeText(this, "Preencha todos os dados antes de salvar!", Toast.LENGTH_SHORT).show();
        } else /*if (allTabletsDeliver(dadosEmprestimo)) */ {
            CautelasEntity cautelasEntity = getCautela();
            cautelasEntity.setEmprestimos(dadosEmprestimo);

            if (getTableExists()) { //verifica se a table foi criada no banco de dados
                if (canSave()) {  //verifica se houve alguma mudança desde de quando foi salvo
                    Toast.makeText(this, "Nenhuma mudança foi feita!", Toast.LENGTH_SHORT).show();

                } else {
                    cautelasEntity.setId(getId());
                    update(cautelasEntity);
                    setTableExists();
                    Toast.makeText(this, "Cautela foi salva com sucesso!", Toast.LENGTH_SHORT).show();
                }
            } else { // se for a primeira vez, é criada uma table no banco de dados
                insertAll(cautelasEntity);
                Toast.makeText(this, "Cautela foi salva com sucesso!", Toast.LENGTH_SHORT).show();
                setTableExists();

            }
        }
    }

    public boolean allTabletsDeliver(List<DadosEmprestimo> dadosEmprestimo) {
        boolean allDeliver = true;
        for (DadosEmprestimo dadoEmprestimo : dadosEmprestimo
        ) {
            if (dadoEmprestimo.getHoraDaDevolucao() == null) {
                allDeliver = false;
                Toast.makeText(this, "Para salvar cautela faz-se necessário que todos os tablets sejam entregues", Toast.LENGTH_SHORT).show();
            }
        }

        return allDeliver;
    }

    public void syncCautela() {
        progressBar.setVisibility(View.VISIBLE);
        CautelasEntity cautelasEntity = getCautela();
        fire.collection("cautelas")
                .document(cautelasEntity.getMateria() + "_" + cautelasEntity.getData())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<HashMap> myData = (ArrayList<HashMap>) task.getResult().getData().get("emprestimos");
                            listaDados = convertHashToDados(myData);
                            currentLista = ((MyAdapter) mAdapter).getDadosEmprestimo();
                            listaFinal = sortedLista(currentLista, listaDados);
                        }
                        ((MyAdapter) mAdapter).setDados(listaFinal);

                        CautelasEntity cautelasEntity2 = getCautela();
                        cautelasEntity2.setEmprestimos(listaFinal);
                        sendDataToFireStorage(cautelasEntity2);
                    }
                });


    }

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

    public List<DadosEmprestimo> sortedLista(List<DadosEmprestimo> currentList, List<DadosEmprestimo> firebaseList) {
        List<DadosEmprestimo> finalLista = new ArrayList<>();
        if(currentList.size() != 0 && firebaseList.size() != 0) {
            //caso haja pelo menos 1 item no celular e 1 item no firebase
            for (int i = 0; i < currentList.size(); i++) {
                for (int j = 0; j < firebaseList.size(); j++) {
                    if (currentList.get(i).getIdUnico().equals(firebaseList.get(j).getIdUnico())) {
                        if (!currentList.get(i).getHoraDaDevolucao().equals("")) {
                            finalLista.add(currentList.get(i));
                            break;
                            //adiciona na lista final caso o Horário de Devolução já tenha sido setado
                        } else if (!firebaseList.get(j).getHoraDaDevolucao().equals("")) {
                            finalLista.add(firebaseList.get(j));
                            break;
                            //adiciona na lista final caso o Horário da cautela na nuvem já tenha sido setado
                        } else {
                            finalLista.add(currentList.get(i));
                            break;
                            //adiciona na lista final caso não ainda tenha sido entregue
                        }
                    } else if (j + 1 == firebaseList.size()) {
                        finalLista.add(currentList.get(i));
                        //caso não exista na nuvem, adiciona na lista final
                    }
                }
            }
            finalLista = sortedLista2(firebaseList, finalLista);
            //adiciona na lista todos os diferentes do firebase
        }else if(currentList.size() > 0){
            finalLista.addAll(currentList);
            //caso não haja nada no firebase, adiciona toda a lista do celular
        }else if(firebaseList.size() != 0){
            finalLista.addAll(firebaseList);
            //caso não haja nada no celular, adiciona toda a lista do firebase
        }else{
            //caso não haja nada nos dois, exibe um toast
            Toast.makeText(this, "Não há nada para sincronizar.", Toast.LENGTH_SHORT).show();
        }
        return finalLista;
    }


    public List<DadosEmprestimo> sortedLista2(List<DadosEmprestimo> firebaseList, List<DadosEmprestimo> celularList) {
        List<DadosEmprestimo> finalLista = celularList;

        for (int i = 0; i < firebaseList.size(); i++) {
            for (int j = 0; j < celularList.size(); j++) {
                if(firebaseList.get(i).getIdUnico().equals(celularList.get(j).getIdUnico())){
                    break;
                }else if( j +1 == celularList.size()){
                    finalLista.add(firebaseList.get(i));
                }
            }
        }
        return finalLista;
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
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NovaCautelaActivity.this,"Sincronizado com sucesso!",Toast.LENGTH_SHORT).show();
                saveCautela(((MyAdapter) mAdapter).getDadosEmprestimo());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("###","Falha");
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NovaCautelaActivity.this,"Não foi possível sincronizar. Tente novamente, por favor.",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
