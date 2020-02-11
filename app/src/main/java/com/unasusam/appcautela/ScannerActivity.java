package com.unasusam.appcautela;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.Result;
import com.unasusam.appcautela.DAO.DadosEmprestimo;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        type = Objects.requireNonNull(getIntent().getExtras()).getInt("type");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }
    }


    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }





    /*  public void onRequestPermissionResult(final int requestCode, String permission[], int grantResults[]) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(ScannerActivity.this, "Permissão garantida", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(ScannerActivity.this, "Permissão recusada", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                displayAlertMessage("Você precisa permitir acesso para as permissões", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                    }

                }
                break;
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }
/*

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(ScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("CANCEL", null)
                .create()
                .show();
    }
*/

    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();
        if (type == 1) {
            new JsonTask().execute("http://files.uea.edu.br/webapi/unasus/cautela.php?id=" + scanResult);
        /*    DadosEmprestimo dadosEmprestimo = new DadosEmprestimo();
            dadosEmprestimo.setNome("Gabriel Silva Brasil");
            dadosEmprestimo.setEmail("dasjdjaslda@gmail.com");
            dadosEmprestimo.setMatricula("21554006");
            dadosEmprestimo.setCodCurso("Ft-05");
            dadosEmprestimo.setNomeCurso("Ciencia da Computação");
            dadosEmprestimo.setUnidade("Ufam");
            EventBus.getDefault().post(new NovaCautelaActivity.MessageEvent(dadosEmprestimo));
            finish();*/

        } else if (type == 0) {
            List<String> lista = new ArrayList<>();
            lista.add("1");
            lista.add(scanResult);
            EventBus.getDefault().post(new NovaCautelaActivity.MessageEvent(lista));
            finish();
        } else {
            List<String> lista = new ArrayList<>();
            lista.add("3");
            lista.add(scanResult);
            EventBus.getDefault().post(new NovaCautelaActivity.MessageEvent(lista));
            finish();
        }
    }


    private class JsonTask extends AsyncTask<String, String, String> {
        DadosEmprestimo dadosEmprestimo = new DadosEmprestimo();

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {


            URLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = url.openConnection();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (reader != null) {
                    try {
                        reader.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Gson gson = new Gson();
            DadosEmprestimo dadosEmprestimo = gson.fromJson(result, DadosEmprestimo.class);
            if (dadosEmprestimo != null) {
                if (dadosEmprestimo.getMatricula() == null) {
                    Toast.makeText(ScannerActivity.this, "Aluno não encontrado", Toast.LENGTH_SHORT).show();
                    scannerView.resumeCameraPreview(ScannerActivity.this);
                } else {
                    //Toast.makeText(ScannerActivity.this,"Aluno encontrado",Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new NovaCautelaActivity.MessageEvent(dadosEmprestimo));
                    finish();
                }
            } else {
                Toast.makeText(ScannerActivity.this, "Não foi possível pesquisar pelo aluno.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
