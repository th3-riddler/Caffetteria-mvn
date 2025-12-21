package it.unife.lp.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nome")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Articolo {
    private final StringProperty nome;
    private final StringProperty descrizione;
    private final DoubleProperty prezzo;
    private final IntegerProperty stoccaggio;

    public Articolo() { this("", "", 0.0, 0); }
    
    /**
     * Constructor for Articolo
     * 
     * @param nome
     * @param descrizione
     * @param prezzo
     * @param stoccaggio
     */
    public Articolo(String nome, String descrizione, double prezzo, int stoccaggio) {
        this.nome = new SimpleStringProperty(nome);
        this.descrizione = new SimpleStringProperty(descrizione);
        this.prezzo = new SimpleDoubleProperty(prezzo);
        this.stoccaggio = new SimpleIntegerProperty(stoccaggio);
    }


    public String getNome() { return this.nome.get(); }
    public StringProperty nomeProperty() { return this.nome; }
    public void setNome(String nome) { this.nome.set(nome); }

    public String getDescrizione() { return this.descrizione.get(); }
    public StringProperty descrizioneProperty() { return this.descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione.set(descrizione); }

    public double getPrezzo() { return this.prezzo.get(); }
    public DoubleProperty prezzoProperty() { return this.prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo.set(prezzo); }

    public int getStoccaggio() { return this.stoccaggio.get(); }
    public IntegerProperty stoccaggioProperty() { return this.stoccaggio; }
    public void setStoccaggio(int stoccaggio) { this.stoccaggio.set(stoccaggio); }

    public Articolo copy() {
        return new Articolo(this.getNome(), this.getDescrizione(), this.getPrezzo(), this.getStoccaggio());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getNome() == null) ? 0 : getNome().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Articolo other = (Articolo) obj;
        if (getNome() == null) {
            if (other.getNome() != null)
                return false;
        } else if (!getNome().equals(other.getNome()))
            return false;
        return true;
    }

    

    // @Override
    // public int hashCode() {
    //     final int prime = 31;
    //     int result = 1;
    //     result = prime * result + ((nome == null) ? 0 : nome.hashCode());
    //     result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
    //     result = prime * result + ((prezzo == null) ? 0 : prezzo.hashCode());
    //     result = prime * result + ((stoccaggio == null) ? 0 : stoccaggio.hashCode());
    //     return result;
    // }

    // @Override
    // public boolean equals(Object obj) {
    //     if (this == obj)
    //         return true;
    //     if (obj == null)
    //         return false;
    //     if (getClass() != obj.getClass())
    //         return false;
    //     Articolo other = (Articolo) obj;
    //     if (nome == null) {
    //         if (other.nome != null)
    //             return false;
    //     } else if (!nome.equals(other.nome))
    //         return false;
    //     if (descrizione == null) {
    //         if (other.descrizione != null)
    //             return false;
    //     } else if (!descrizione.equals(other.descrizione))
    //         return false;
    //     if (prezzo == null) {
    //         if (other.prezzo != null)
    //             return false;
    //     } else if (!prezzo.equals(other.prezzo))
    //         return false;
    //     if (stoccaggio == null) {
    //         if (other.stoccaggio != null)
    //             return false;
    //     } else if (!stoccaggio.equals(other.stoccaggio))
    //         return false;
    //     return true;
    // }
    
}
