package com.unasusam.appcautela;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.unasusam.appcautela.Dialog.DialogLogout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements DialogLogout.LogoutDialog {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onNewCautelaClick(View view) {
        Intent intent = new Intent(this,NovaCautelaActivity.class);
        startActivity(intent);
    }

    public void onCautelasClick(View view) {
        Intent intent = new Intent (this,TodasAsCautelas.class);
        startActivity(intent);
    }

    public void onLogoutClick(View view) {
        DialogLogout dialog = new DialogLogout();
        dialog.show(getSupportFragmentManager(),"dialog logout");
    }

    @Override
    public void exitDialog() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}
