package br.com.geekacademy.agendadecontatos.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import br.com.geekacademy.agendadecontatos.R;
import br.com.geekacademy.agendadecontatos.adapter.ContatoAdapter;
import br.com.geekacademy.agendadecontatos.model.ContatoInfo;
import br.com.geekacademy.agendadecontatos.repositorio.ContatoDAO;

public class MainActivity extends AppCompatActivity {

    private ContatoDAO helper;

    private RecyclerView contatosRecy;
    private ContatoAdapter adapter;

    private List<ContatoInfo> listaContatos;

    private final int REQUEST_NEW = 1;
    private final int REQUEST_ALTER = 2;

    private String order = "ASC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("contato", new ContatoInfo());
                startActivityForResult(i, REQUEST_NEW);
            }
        });

        helper = new ContatoDAO(this);
        listaContatos = helper.getList(order);

        contatosRecy = findViewById(R.id.contatosRecy);
        contatosRecy.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        contatosRecy.setLayoutManager(llm);

        adapter = new ContatoAdapter(listaContatos);
        contatosRecy.setAdapter(adapter);

        contatosRecy.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        abrirOpcoes(listaContatos.get(position));
                    }
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_NEW && resultCode == RESULT_OK){
            ContatoInfo contatoInfo = data.getParcelableExtra("contato");
            helper.inserirContato(contatoInfo);
            listaContatos = helper.getList(order);
            adapter = new ContatoAdapter(listaContatos);
            contatosRecy.setAdapter(adapter);
        } else if(requestCode == REQUEST_ALTER && resultCode == RESULT_OK){
            ContatoInfo contatoInfo = data.getParcelableExtra("contato");
            helper.alteraContato(contatoInfo);
            listaContatos = helper.getList(order);
            adapter = new ContatoAdapter(listaContatos);
            contatosRecy.setAdapter(adapter);
        }
    }

    private void abrirOpcoes(final ContatoInfo contato){
        Intent intent1 = new Intent(MainActivity.this, EditActivity.class);
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        intent.setData(Uri.parse("tel:" + contato.getFone()));
//        startActivity(intent);
        intent1.putExtra("contato", contato);
        startActivityForResult(intent1, REQUEST_ALTER);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(contato.getNome());
//        builder.setItems(new CharSequence[]{"Ligar", "Editar", "Excluir"},
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        switch (i){
//                            case 0:
//                                Intent intent = new Intent(Intent.ACTION_DIAL);
//                                intent.setData(Uri.parse("tel:" + contato.getFone()));
//                                startActivity(intent);
//                                break;
//                            case 1:
//                                Intent intent1 = new Intent(MainActivity.this, EditActivity.class);
//                                intent1.putExtra("contato", contato);
//                                startActivityForResult(intent1, REQUEST_ALTER);
//                                break;
//                            case 2:
//                                listaContatos.remove(contato);
//                                helper.apagarContato(contato);
//                                adapter.notifyDataSetChanged();
//                                break;
//                        }
//                    }
//                });
//        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.order_az) {
            order = "ASC";
        } else if(id == R.id.order_za){
            order = "DESC";
        }

        listaContatos = helper.getList(order);
        adapter = new ContatoAdapter(listaContatos);
        contatosRecy.setAdapter(adapter);

        return true;
    }
}
