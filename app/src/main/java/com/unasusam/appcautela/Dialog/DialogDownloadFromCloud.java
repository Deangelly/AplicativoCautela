package com.unasusam.appcautela.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.unasusam.appcautela.DAO.CautelasEntity;
import com.unasusam.appcautela.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogDownloadFromCloud extends AppCompatDialogFragment {

    private DialogDownloadFromCloud.downloadCloudDialog listener;
    CautelasEntity cautelasEntity;
    View view = null;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_download_from_cloud,null);
        builder.setView(view)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.downloadCloudDialog(cautelasEntity);

            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogDownloadFromCloud.downloadCloudDialog) context;

        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement DownloadDialogFromCloud");
        }
    }

    public interface downloadCloudDialog{
        void downloadCloudDialog(CautelasEntity cautelasEntity);
    }

    public void setCautelasEntity(CautelasEntity cautelasEntity){
        this.cautelasEntity = cautelasEntity;
    }

}
