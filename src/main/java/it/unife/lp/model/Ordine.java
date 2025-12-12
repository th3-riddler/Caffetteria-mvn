package it.unife.lp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Ordine {
    private final IntegerProperty id;
    private final ObjectProperty<LocalDateTime> dataOra;
    private final ObservableList<VoceOrdine> voci;
    private final DoubleProperty scontoPercentuale;
    private final DoubleProperty prezzoTotale;
    private final ObjectProperty<MetodoPagamento> metodoPagamento;
    private final BooleanProperty pagato;

    private final DoubleProperty importoRicevuto;
    private final DoubleProperty resto; // Resto dovuto in caso di pagamento in contanti

    /**
     * Constructor for Ordine. It initializes a new empty Order.
     * 
     * @param id
     */
    public Ordine (int id) {
        this.id = new SimpleIntegerProperty(id);
        this.dataOra = new SimpleObjectProperty<>(LocalDateTime.now());
        this.voci = FXCollections.observableArrayList();
        this.scontoPercentuale = new SimpleDoubleProperty(0.0);
        this.prezzoTotale = new SimpleDoubleProperty(0.0);
        this.metodoPagamento = new SimpleObjectProperty<>(null);
        this.pagato = new SimpleBooleanProperty(false);

        this.importoRicevuto = new SimpleDoubleProperty(0.0);
        this.resto = new SimpleDoubleProperty(0.0);

        // Adds a listener to scontoPercentuale in order to update the amount based on the discount
        this.scontoPercentuale.addListener((obs, oldValue, newValue) -> calcolaTotale());
    }

    // ------------------------------
    // Generic Getters
    // ------------------------------
    public int getId() { return this.id.get(); }
    public IntegerProperty idProperty() { return this.id; }
    public void setId(int id) { this.id.set(id); }

    public LocalDateTime getDataOra() { return this.dataOra.get(); }
    public ObjectProperty<LocalDateTime> dataOraProperty() { return this.dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra.set(dataOra); }

    public ObservableList<VoceOrdine> getVoci() { return this.voci; }

    public double getSconto() { return this.scontoPercentuale.get(); }
    public DoubleProperty scontoProperty() { return this.scontoPercentuale; }
    public void setScontoPercentuale(double scontoPercentuale) { this.scontoPercentuale.set(scontoPercentuale); }

    public double getPrezzoTotale() { return this.prezzoTotale.get(); }
    public DoubleProperty prezzoTotaleProperty() { return this.prezzoTotale; }

    public MetodoPagamento getMetodoPagamento() { return this.metodoPagamento.get(); }
    public ObjectProperty<MetodoPagamento> metodoPagamentoProperty() { return this.metodoPagamento; }
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) { this.metodoPagamento.set(metodoPagamento); }

    public boolean isPagato() { return this.pagato.get(); }
    public BooleanProperty pagatoProperty() { return this.pagato; }
    public void setPagato(boolean pagato) { this.pagato.set(pagato); }

    public double getImportoRicevuto() { return this.importoRicevuto.get(); }
    public DoubleProperty importoRicevutoProperty() { return this.importoRicevuto; }
    public void setImportoRicevuto(double importoRicevuto) {
        this.importoRicevuto.set(importoRicevuto);
        calcolaResto();
    }
    
    public double getResto() { return this.resto.get(); }
    public DoubleProperty restoProperty() { return this.resto; }

    // ------------------------------
    // Items handlers
    // ------------------------------

    /**
     * Adds an item to the order
     * @param voce
     */
    public void aggiungiVoce(VoceOrdine voce) {
        this.voci.add(voce);

        // Adds a listener on the new Item added, in order to update the amount if the price of the item changes
        voce.prezzoTotaleProperty().addListener((obs, oldValue, newValue) -> calcolaTotale());
        calcolaTotale();
    }

    /**
     * Removes an item from the order
     * @param voce
     */
    public void rimuoviVoce(VoceOrdine voce) {
        this.voci.remove(voce);
        calcolaTotale();
    }

    /**
     * Clears the order
     */
    public void clear() {
        this.voci.clear();
        this.scontoPercentuale.set(0.0);
        this.prezzoTotale.set(0.0);
        this.metodoPagamento.set(null);
        this.pagato.set(false);
        this.importoRicevuto.set(0.0);
        this.resto.set(0.0);        
    }

    // ------------------------------
    // Calculation Functions
    // ------------------------------

    /**
     * Calculates the total amount to pay considering the discount
     */
    private void calcolaTotale() {

        // Finds the amount to pay without the discount
        double totaleSenzaSconto = this.voci.stream().mapToDouble(VoceOrdine::getPrezzoTotale).sum();

        // Applies the discount 
        this.prezzoTotale.set(totaleSenzaSconto * (1 - this.scontoPercentuale.get() / 100.0));

        // Calculates the change if needed
        calcolaResto();
    }

    /**
     * Calculates the change to give back to the customer
     */
    private void calcolaResto() {
        // Calculates the change only if the amount received is greater than 0
        if (this.importoRicevuto.get() > 0) {
            this.resto.set(this.importoRicevuto.get() - this.prezzoTotale.get());
        } else {
            this.resto.set(0.0);
        }
    }
}
