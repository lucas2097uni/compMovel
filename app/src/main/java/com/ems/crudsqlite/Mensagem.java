package com.ems.crudsqlite;

import android.app.AlertDialog;
import android.content.Context;

public class Mensagem {
    private Context _context;

    public Mensagem(Context contex){
        this._context = contex;

    }

    public void show(String titulo, String texto){

        AlertDialog.Builder msg = new AlertDialog.Builder(_context);

        msg.setTitle(titulo);
        msg.setMessage(texto);
        msg.setNeutralButton("OK", null);
        msg.show();
    }

}
