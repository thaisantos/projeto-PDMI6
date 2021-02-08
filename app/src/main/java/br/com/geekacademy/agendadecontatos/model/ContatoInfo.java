package br.com.geekacademy.agendadecontatos.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ContatoInfo implements Parcelable{

    private String nome = "";
    private String ref = "";
    private String email = "";
    private String fone = "";
    private String end = "";
    private String foto = "";

    private Long id = -1L;

    public ContatoInfo(){

    }

    private ContatoInfo(Parcel in){
        String[] data = new String[5];
        in.readStringArray(data);
        setNome(data[0]);
        setRef(data[1]);
        setFone(data[2]);
        setFoto(data[3]);
        setId(Long.parseLong(data[4]));
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                getNome(), getRef(), getFone(), getFoto(), String.valueOf(getId())
        });
    }

    public static final Parcelable.Creator<ContatoInfo> CREATOR= new Parcelable.Creator<ContatoInfo>(){

        @Override
        public ContatoInfo createFromParcel(Parcel parcel) {
            return new ContatoInfo(parcel);
        }

        @Override
        public ContatoInfo[] newArray(int i) {
            return new ContatoInfo[i];
        }

    };
}
