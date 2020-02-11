package com.unasusam.appcautela.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unasusam.appcautela.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CautelaItemViewHolder extends RecyclerView.ViewHolder {

    public final TextView date;
    public final TextView subject;
    public final TextView teacher;
    public final TextView monitor;
    public final RelativeLayout relativeLayout;
    public final ImageView download;
    public final ImageView uplooad;
    public final ImageView imageViewMonitor;
    public CautelaItemViewHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.date_text);
        subject = itemView.findViewById(R.id.subject_text);
        teacher = itemView.findViewById(R.id.teacher_text);
        relativeLayout = itemView.findViewById(R.id.rltv_layout);
        download = itemView.findViewById(R.id.download);
        uplooad = itemView.findViewById(R.id.upload);
        monitor = itemView.findViewById(R.id.monitor_text);
        imageViewMonitor = itemView.findViewById(R.id.monitor);
    }
}
