package br.com.geekacademy.agendadecontatos.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import br.com.geekacademy.agendadecontatos.R;
import br.com.geekacademy.agendadecontatos.adapter.ContatoAdapter;
import br.com.geekacademy.agendadecontatos.model.ContatoInfo;
import br.com.geekacademy.agendadecontatos.repositorio.ContatoDAO;

public class EditActivity extends AppCompatActivity {

    private ContatoInfo contato;
    private ContatoDAO helper;
    private ContatoAdapter adapter;
    private List<ContatoInfo> listaContatos;

    private View layout;
    private TextView titulo;

    private ImageButton foto;
    private EditText nome;
    private EditText ref;
    private EditText email;
    private EditText fone;
    private EditText end;

    private Button salvar;
    private Button remover;
    private Button cancelar;

    private final int CAMERA = 1;
    private final int GALERIA = 2;

    private final String IMAGE_DIR = "/FotosContatos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        helper = new ContatoDAO(this);
        adapter = new ContatoAdapter(listaContatos);

        contato = getIntent().getParcelableExtra("contato");

        layout = findViewById(R.id.mainLayout);

        foto = findViewById(R.id.fotoContato);
        nome = findViewById(R.id.nomeContato);
        ref = findViewById(R.id.refContato);
        fone = findViewById(R.id.foneContato);
        titulo = findViewById(R.id.tituloContato);
        remover = findViewById(R.id.btnRemover);

        nome.setText(contato.getNome());
        ref.setText(contato.getRef());
        fone.setText(contato.getFone());

        if (contato.getNome().equals("")) {
            titulo.setText("Contato:");
        } else {
            titulo.setText(contato.getRef());
        }

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertaImagem();
            }
        });

        File imgFile = new File(contato.getFoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            foto.setImageBitmap(bitmap);
        }

        salvar = findViewById(R.id.btnSalvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contato.setNome(nome.getText().toString());
                contato.setRef(ref.getText().toString());
                contato.setFone(fone.getText().toString());

                if (contato.getNome().equals("")) {
                    Toast.makeText(EditActivity.this, "É necessário um nome para salvar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent();
                i.putExtra("contato", contato);
                setResult(RESULT_OK, i);
                finish();
            }
        });

                remover = findViewById(R.id.btnRemover);
                remover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (contato.getNome().equals("")) {
                            Toast.makeText(EditActivity.this, "Impossível remover um contato não criado!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        helper.apagarContato(contato);
                        adapter.notifyDataSetChanged();
                        Intent i = new Intent();
                        i.putExtra("contato", contato);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                });

                cancelar = findViewById(R.id.btnCancelar);
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent();
                        finish();
                    }
                });
            }

    private void alertaImagem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione a fonte da imagem");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clicaTirarFoto();
            }
        });
        builder.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clicaCarregaImagem();
            }
        });
        builder.create().show();
    }

    private void clicaTirarFoto(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            requestCameraPermission();
        } else {
            showCamera();
        }
    }

    private void requestCameraPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)){

            Snackbar.make(layout, "É necessário permitir para utilizar a câmera!",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(EditActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CAMERA);
                }
            }).show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA);
        }
    }

    private void showCamera(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA);
    }

    private void clicaCarregaImagem(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            requestGaleriaPermission();
        } else {
            showGaleria();
        }
    }

    private void requestGaleriaPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)){

            Snackbar.make(layout, "É necessário permitir para utilizar a galeria!",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(EditActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            GALERIA);
                }
            }).show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GALERIA);
        }
    }

    private void showGaleria(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALERIA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    clicaTirarFoto();
                }
                break;
            case GALERIA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    clicaCarregaImagem();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED || data == null){
            return;
        }
        if(requestCode == GALERIA){
            Uri contentURI = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                contato.setFoto(saveImage(bitmap));
                foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(requestCode == CAMERA){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            contato.setFoto(saveImage(bitmap));
            foto.setImageBitmap(bitmap);
        }

    }

    private String saveImage(Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        File directory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIR);

        if(!directory.exists()){
            directory.mkdirs();
        }

        try {
            File f = new File(directory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
