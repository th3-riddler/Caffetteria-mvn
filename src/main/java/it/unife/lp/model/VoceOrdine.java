package it.unife.lp.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.IntegerProperty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javafx.beans.property.DoubleProperty;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class VoceOrdine {
    private Articolo articolo;
    private final IntegerProperty quantita;
    private final DoubleProperty prezzoTotale;

    public VoceOrdine(Articolo articolo, int quantita) {
        this.articolo = articolo;
        this.quantita = new SimpleIntegerProperty(quantita);
        this.prezzoTotale = new SimpleDoubleProperty(this.articolo.getPrezzo() * this.quantita.get());
    }

    public VoceOrdine() {
        this(new Articolo(), 0);
    }

    public Articolo getArticolo() { return this.articolo; }
    public void setArticolo(Articolo articolo) { this.articolo = articolo; }
    public void setPrezzoArticolo(double prezzo) {
        this.articolo.setPrezzo(prezzo);
        this.prezzoTotale.set(this.articolo.getPrezzo() * this.quantita.get());
    }

    public int getQuantita() { return this.quantita.get(); }
    public IntegerProperty quantitaProperty() { return this.quantita; }
    public void setQuantita(int quantita) {
        this.quantita.set(quantita);
        this.prezzoTotale.set(this.articolo.getPrezzo() * this.quantita.get());
    }

    public double getPrezzoTotale() { return this.prezzoTotale.get(); }
    public DoubleProperty prezzoTotaleProperty() { return this.prezzoTotale; }
    public void setPrezzoTotale(double prezzoTotale) { this.prezzoTotale.set(prezzoTotale); }
    
    public VoceOrdine copy() {
        return new VoceOrdine(this.articolo.copy(), this.getQuantita());
    }

}
