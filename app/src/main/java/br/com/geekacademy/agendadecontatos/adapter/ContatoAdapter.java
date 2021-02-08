package br.com.geekacademy.agendadecontatos.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import br.com.geekacademy.agendadecontatos.model.ContatoInfo;
import br.com.geekacademy.agendadecontatos.R;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContactViewHolder>{

    private List<ContatoInfo> listaContatos;

    public ContatoAdapter(List<ContatoInfo> lista){
        listaContatos = lista;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.celula_contato, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final ContatoInfo c = listaContatos.get(position);
        holder.nome.setText(c.getRef());

        holder.ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + c.getFone()));
                view.getContext().startActivity(intent);
            }
        });

        File imgFile = new File(c.getFoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.foto.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView nome;
        ImageButton ligar;

        ContactViewHolder(View v){
            super(v);
            foto = v.findViewById(R.id.imageFoto);
            nome = v.findViewById(R.id.textoNome);
            ligar = v.findViewById(R.id.btnLigar);
        }

    }

}
