package com.unasusam.appcautela.PDFCreator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.unasusam.appcautela.DAO.CautelasEntity;
import com.unasusam.appcautela.DAO.DadosEmprestimo;
import com.unasusam.appcautela.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PDFCreator extends PdfPageEventHelper {
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private static final int REQUEST_WRITE = 1;
    Context context;
    String pdfPath;
    Activity activity;

    public void criandoPdf(CautelasEntity cautelasEntity, Context context, Activity activity, String type) {
        this.context = context;
        this.activity = activity;
        Document document = new Document(PageSize.A4);
        isPermissionGaranted();
        if (checkPermission()) {

            try {

                document.setMargins(40, 40, 40, 80);
                String cautelaName = cautelasEntity.getMateria() + "_" + cautelasEntity.getData().substring(6, cautelasEntity.getData().length());
                cautelaName = cautelaName.replaceAll("\\s+", "_");
                String filename = cautelaName + ".pdf";
                String texto = getTexto(cautelasEntity,type);
                if(type.equals("cautela")){
                    filename = "Cautela_" + filename;
                }

                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

                pdfPath = path + "/" + filename;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.getParentFile().mkdirs();
                }
                File file = new File(dir, filename);

                FileOutputStream fOut = new FileOutputStream(file);
                fOut.flush();
                PdfWriter writer = PdfWriter.getInstance(document, fOut);
                writer.setPageEvent(this);
                document.open();
                String dados = "";
                Paragraph paragrafo = new Paragraph(dados, catFont);

                String title = "";
                if(type.equals("frequencia")) {
                   title = "Frequência de Alunos";
                }else{
                    title = "Controle de Tablets";
                }
                paragrafo.setAlignment(Element.ALIGN_CENTER);
                paragrafo.add(title);
                document.add(paragrafo);

                paragrafo = new Paragraph(dados, catFont);
                paragrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                paragrafo.add(texto);
                document.add(paragrafo);
                document.close();

            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                document.close();
            }

        } else {
            Toast.makeText(context, "Ative a permissão para memória nas configurações do seu aparelho", Toast.LENGTH_SHORT).show();
        }
    }

    private String getTexto(CautelasEntity cautelasEntity, String type) {
        String texto =
                "\n\nData: " + cautelasEntity.getData()
                        + "\nProfessor: " + cautelasEntity.getProfessor()
                        + "\nDisciplina: " + cautelasEntity.getMateria()
                        + "\nLocal de Prova: " + cautelasEntity.getLocal();
        if(type.equals("frequencia")){
            texto += "\nMATRÍCULA  ALUNO\n";
        }else{
            texto += "\nTABLET STATUS  MATRÍCULA  ALUNO\n";
        }

        for (DadosEmprestimo dados : cautelasEntity.getEmprestimos()) {
            if (type.equals("frequencia")) {
                if (dados.getMatricula().equals("")) {
                    texto += "------------------" + "   " + dados.getNome() + "\n";
                } else {
                    texto += dados.getMatricula() + "       " + dados.getNome() + "\n";
                }
            } else if(type.equals("cautela")) {
                String status = "Entregue";
                if(dados.getHoraDaDevolucao().equals("")){
                    status = "Pendente";
                }
                if (dados.getMatricula().equals("")) {
                    texto += dados.getTablet() + " " + status + " ------------------" + "   " + dados.getNome() + "\n";
                } else {
                    texto += dados.getTablet() + " " + status + dados.getMatricula() + "       " + dados.getNome() +"\n";
                }
            }
        }
        return texto;
    }

    Image image = null;

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        super.onOpenDocument(writer, document);
        Drawable d = context.getResources().getDrawable(R.drawable.imagem_pdf);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

        try {
            image = Image.getInstance(stream.toByteArray());
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.scalePercent(10);
        image.setAlignment(Element.ALIGN_CENTER);
        image.setAbsolutePosition(document.getPageSize().getWidth() / 2 - image.getScaledWidth() / 2, 0);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        super.onEndPage(writer, document);
        try {
            writer.getDirectContent().addImage(image);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void isPermissionGaranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }
    }

}
