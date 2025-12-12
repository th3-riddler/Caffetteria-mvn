package it.unife.lp.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Articolo {
    private final StringProperty nome;
    private final StringProperty descrizione;
    private final DoubleProperty prezzo;
    private final IntegerProperty stoccaggio;

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

    public String getNome() {
        return this.nome.get();
    }

    public StringProperty nomeProperty() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getDescrizione() {
        return this.descrizione.get();
    }

    public StringProperty descrizioneProperty() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione.set(descrizione);
    }

    public double getPrezzo() {
        return this.prezzo.get();
    }

    public DoubleProperty prezzoProperty() {
        return this.prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo.set(prezzo);
    }

    public int getStoccaggio() {
        return this.stoccaggio.get();
    }

    public IntegerProperty stoccaggioProperty() {
        return this.stoccaggio;
    }

    public void setStoccaggio(int stoccaggio) {
        this.stoccaggio.set(stoccaggio);
    }
    
}
