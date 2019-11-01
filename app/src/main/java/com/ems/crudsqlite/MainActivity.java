package com.ems.crudsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

    //Variáveis
    EditText editNome, editId, editQtde, editPreco;
    Button btnIncluir, btnExcluir, btnAlterar, btnPesquisar, btnListar;
    String nome, id, qtde, preco;

    // Banco
    SQLiteDatabase db;


    // instancia da classe mensagem
    private Mensagem mensagem = new Mensagem(MainActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = (EditText) findViewById(R.id.editNome);
        editId = (EditText) findViewById(R.id.editId);
        editQtde = (EditText) findViewById(R.id.editQtde);
        editPreco = (EditText) findViewById(R.id.editPreco);

        btnIncluir = (Button) findViewById(R.id.btIncluir);
        btnExcluir = (Button) findViewById(R.id.btExcluir);
        btnAlterar = (Button) findViewById(R.id.btAlterar);
        btnPesquisar = (Button) findViewById(R.id.btPesquisar);
        btnListar = (Button) findViewById(R.id.btListar);


        btnIncluir.setOnClickListener(this);
        btnExcluir.setOnClickListener(this);
        btnAlterar.setOnClickListener(this);
        btnPesquisar.setOnClickListener(this);
        btnListar.setOnClickListener(this);

        //abre o banco
        db = openOrCreateDatabase("DbLoja", Context.MODE_PRIVATE, null);

        //caso não exista, cria um novo
        db.execSQL("CREATE TABLE IF NOT EXISTS produto(nome varchar, id varchar, qtde varchar, preco varchar)");

    }

    //classe com os eventos dos botões
    @Override
    public void onClick(View view){

        nome = editNome.getText().toString();
        id = editId.getText().toString();
        qtde = editQtde.getText().toString();
        preco = editPreco.getText().toString();

        if (view == btnIncluir){
            if (verificaCampos())
            {
                mensagem.show("Erro", "Valor inválido");
                return;
            }
            //executa o codigo sql para inserir os dados
            db.execSQL("INSERT INTO produto VALUES('"+ nome + "','" + id + "','" + qtde + "','" + preco + "');");
            mensagem.show("Aceito", "Produto Adicionado!");
            limparCampos();
        }
        if (view == btnListar){

            //Cria um cursor e faz o select de todo o banco
            Cursor c = db.rawQuery("SELECT * FROM produto", null);
            if (c.getCount() == 0){
                mensagem.show("Alerta", "Nenhum registro!");
                return;
            }


            StringBuffer buffer = new StringBuffer();
            while(c.moveToNext()){
                buffer.append("Nome: " + c.getString(0) + "\n");
                buffer.append("Id: " + c.getString(1) + "\n");
                buffer.append("Quantidade: " + c.getString(2) + "\n");
                buffer.append("Preço: " + c.getString(3) + "\n\n");
            }

            mensagem.show("Produtos", buffer.toString());

        }
        if (view == btnPesquisar){
            if (id.trim().length() == 0){
                mensagem.show("Erro", "Insira um valor");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM produto WHERE id='" + id +"'", null);
            if (c.moveToFirst()){
                editNome.setText(c.getString(0));
                editQtde.setText(c.getString(2));
                editPreco.setText(c.getString(3));
            }else{
                mensagem.show("Erro", "Produto não encontrado!");
                return;
            }

        }
        if (view == btnAlterar){
            if (id.trim().length() == 0){
                mensagem.show("Erro", "Insira um valor");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM produto WHERE id='" + id +"'", null);

            if (c.moveToFirst()){
                db.execSQL("UPDATE produto SET nome='" + nome + "', qtde='" + qtde + "', preco='" + preco + "' WHERE id='" + id + "';");
                mensagem.show("Avido", "Produto alterado");

            }else{
                mensagem.show("Erro", "Produto não encontrado");
                return;

            }
            limparCampos();
        }


        if (view == btnExcluir){
            if (id.trim().length() == 0){
                mensagem.show("Erro", "Insira um valor");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM produto WHERE id='" + id +"'", null);

            if (c.moveToFirst()){
                db.execSQL("DELETE FROM produto WHERE id='" + id + "';");
                mensagem.show("Confirmado", "Produto excluido");
            }else{
                mensagem.show("Erro", "Produto não encontrado");
                return;

            }
            limparCampos();

        }

    }
    // verifica campos vazios
    public boolean verificaCampos(){

        return (nome.trim().length() == 0 || id.trim().length() == 0 || qtde.trim().length() == 0 || preco.trim().length() == 0);
    }
    //limpa os campos do form
    public void limparCampos(){
        editNome.setText("");
        editId.setText("");
        editQtde.setText("");
        editPreco.setText("");
        editNome.requestFocus();
    }

}
