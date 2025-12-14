package it.unife.lp.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;

public class VoceOrdine {
    private Articolo articolo;
    private final IntegerProperty quantita;
    private final DoubleProperty prezzoTotale;

    public VoceOrdine(Articolo articolo, int quantita) {
        this.articolo = articolo;
        this.quantita = new SimpleIntegerProperty(quantita);
        this.prezzoTotale = new SimpleDoubleProperty(this.articolo.getPrezzo() * this.quantita.get());
    }

    public Articolo getArticolo() { return this.articolo; }
    public void setArticolo(Articolo articolo) { this.articolo = articolo; }

    public int getQuantita() { return this.quantita.get(); }
    public IntegerProperty quantitaProperty() { return this.quantita; }
    public void setQuantita(int quantita) {
        this.quantita.set(quantita);
        this.prezzoTotale.set(this.articolo.getPrezzo() * this.quantita.get());
    }

    public double getPrezzoTotale() { return this.prezzoTotale.get(); }
    public DoubleProperty prezzoTotaleProperty() { return this.prezzoTotale; }
    
    public VoceOrdine copy() {
        return new VoceOrdine(this.getArticolo(), this.getQuantita());
    }

}
