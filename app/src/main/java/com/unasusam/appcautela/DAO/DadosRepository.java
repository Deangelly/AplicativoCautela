package com.unasusam.appcautela.DAO;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class DadosRepository {

    private DadosDao dadosDao;
    private LiveData<List<CautelasEntity>> allDados;

    public DadosRepository(Application application){
        DadosDatabase database = DadosDatabase.getInstance(application);
        dadosDao = database.dadosDao();
        allDados = dadosDao.getAllDados();
    }

    public void Insert(DadosEmprestimo dadosEmprestimo){
        new InsertDadosAsyncTask(dadosDao).execute(dadosEmprestimo);
    }
    public void InsertAllNotes(CautelasEntity cautelasEntity){
        new InsertAllDadosAsyncTask(dadosDao).execute(cautelasEntity);

    }
    public void Update(CautelasEntity cautelasEntity){
        new UpdateDadosAsyncTask(dadosDao).execute(cautelasEntity);

    }
    public void Delete(CautelasEntity cautelasEntity){
        new DeleteDadosAsyncTask(dadosDao).execute(cautelasEntity);

    }
    public void DeleteAllNotes(String id){
        new DeleteCautelaDadosAsyncTask(dadosDao).execute(id);

    }

    public LiveData<List<CautelasEntity>> getAllDados(){
        return allDados;
    }

    private static class InsertDadosAsyncTask extends AsyncTask<DadosEmprestimo,Void ,Void>{
        private DadosDao dadosDao;

        private InsertDadosAsyncTask(DadosDao dadosDao){
            this.dadosDao = dadosDao;
        }

        @Override
        protected Void doInBackground(DadosEmprestimo ... dadosEmprestimos){
            dadosDao.insert(dadosEmprestimos[0]);
            return null;
        }
    }

    private static class InsertAllDadosAsyncTask extends AsyncTask<CautelasEntity,Void ,Void>{
        private DadosDao dadosDao;

        private InsertAllDadosAsyncTask(DadosDao dadosDao){
            this.dadosDao = dadosDao;
        }

        @Override
        protected Void doInBackground(CautelasEntity ...cautelasEntities) {
            dadosDao.insertAll(cautelasEntities[0]);
            return null;
        }
    }

    private static class UpdateDadosAsyncTask extends AsyncTask<CautelasEntity,Void ,Void>{
        private DadosDao dadosDao;

        private UpdateDadosAsyncTask(DadosDao dadosDao){
            this.dadosDao = dadosDao;
        }

        @Override
        protected Void doInBackground(CautelasEntity ... cautelasEntities){
            dadosDao.update(cautelasEntities[0]);
            return null;
        }
    }

    private static class DeleteDadosAsyncTask extends AsyncTask<CautelasEntity,Void ,Void>{
        private DadosDao dadosDao;

        private DeleteDadosAsyncTask(DadosDao dadosDao){
            this.dadosDao = dadosDao;
        }

        @Override
        protected Void doInBackground(CautelasEntity ... cautelasEntities){
            dadosDao.delete(cautelasEntities[0]);
            return null;
        }
    }

    private static class DeleteCautelaDadosAsyncTask extends AsyncTask<String,Void ,Void> {
        private DadosDao dadosDao;

        private DeleteCautelaDadosAsyncTask(DadosDao dadosDao){
            this.dadosDao = dadosDao;
        }

        @Override
        protected Void doInBackground(String ... strings){
            dadosDao.deleteCautela(strings[0]);
            return null;
        }
    }
}
