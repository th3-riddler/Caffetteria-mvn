package it.unife.lp.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Ordine {
    private final IntegerProperty id;
    private final ObjectProperty<LocalDate> data;
    private final ObservableList<VoceOrdine> voci;
    private final DoubleProperty scontoPercentuale;
    private final DoubleProperty prezzoTotaleParziale;
    private final DoubleProperty prezzoTotaleFinale;
    private final ObjectProperty<MetodoPagamento> metodoPagamento;

    private final DoubleProperty importoRicevuto;
    private final DoubleProperty resto; // Resto dovuto in caso di pagamento in contanti

    /**
     * Constructor for Ordine. It initializes a new empty Order.
     * 
     * @param id
     */
    public Ordine (int id) {
        this.id = new SimpleIntegerProperty(id);
        this.data = new SimpleObjectProperty<>(LocalDate.now());
        this.voci = FXCollections.observableArrayList();
        this.scontoPercentuale = new SimpleDoubleProperty(0.0);
        this.prezzoTotaleParziale = new SimpleDoubleProperty(0.0);
        this.prezzoTotaleFinale = new SimpleDoubleProperty(0.0);
        this.metodoPagamento = new SimpleObjectProperty<>(null);

        this.importoRicevuto = new SimpleDoubleProperty(0.0);
        this.resto = new SimpleDoubleProperty(0.0);

        // Adds a listener to scontoPercentuale in order to update the amount based on the discount
        this.scontoPercentuale.addListener((obs, oldValue, newValue) -> calcolaTotale());
    }

    public Ordine() {
        this(0);
    }

    // ------------------------------
    // Generic Getters
    // ------------------------------
    public int getId() { return this.id.get(); }
    public IntegerProperty idProperty() { return this.id; }
    public void setId(int id) { this.id.set(id); }

    public LocalDate getData() { return this.data.get(); }
    public ObjectProperty<LocalDate> dataProperty() { return this.data; }
    public void setdata(LocalDate data) { this.data.set(data); }

    @JsonIgnore
    public ObservableList<VoceOrdine> getVociProperty() { return this.voci; }
    public List<VoceOrdine> getVoci() { return List.copyOf(this.voci); }
    public void setVoci(List<VoceOrdine> voci) { 
        this.voci.clear();
        for (VoceOrdine voce : voci) {
            aggiungiVoce(voce);
        }
        calcolaTotale();
    }


    public double getScontoPercentuale() { return this.scontoPercentuale.get(); }
    public DoubleProperty scontoProperty() { return this.scontoPercentuale; }
    public void setScontoPercentuale(double scontoPercentuale) { this.scontoPercentuale.set(scontoPercentuale); }

    public double getPrezzoTotaleParziale() { return this.prezzoTotaleParziale.get(); }
    public DoubleProperty prezzoTotaleParzialeProperty() { return this.prezzoTotaleParziale; }
    public void setPrezzoTotaleParziale(double prezzoTotaleParziale) { this.prezzoTotaleParziale.set(prezzoTotaleParziale); }

    public double getPrezzoTotaleFinale() { return this.prezzoTotaleFinale.get(); }
    public DoubleProperty prezzoTotaleFinaleProperty() { return this.prezzoTotaleFinale; }
    public void setPrezzoTotaleFinale(double prezzoTotaleFinale) { this.prezzoTotaleFinale.set(prezzoTotaleFinale); }

    public MetodoPagamento getMetodoPagamento() { return this.metodoPagamento.get(); }
    public ObjectProperty<MetodoPagamento> metodoPagamentoProperty() { return this.metodoPagamento; }
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) { this.metodoPagamento.set(metodoPagamento); }

    public double getImportoRicevuto() { return this.importoRicevuto.get(); }
    public DoubleProperty importoRicevutoProperty() { return this.importoRicevuto; }
    public void setImportoRicevuto(double importoRicevuto) {
        this.importoRicevuto.set(importoRicevuto);
        calcolaResto();
    }
    
    public double getResto() { return this.resto.get(); }
    public DoubleProperty restoProperty() { return this.resto; }
    public void setResto(double resto) { this.resto.set(resto); }

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
    public void rimuoviVoce(VoceOrdine voce, int quantita) {
        if (quantita >= voce.getQuantita()) {
            this.voci.remove(voce);
        } else {
            voce.setQuantita(voce.getQuantita() - quantita);
        }
        calcolaTotale();
    }

    /**
     * Clears the order
     */
    public void clear() {
        this.voci.clear();
        this.scontoPercentuale.set(0.0);
        this.prezzoTotaleParziale.set(0.0);
        this.prezzoTotaleFinale.set(0.0);
        this.metodoPagamento.set(null);
        this.importoRicevuto.set(0.0);
        this.resto.set(0.0);        
    }

    public Ordine copy() {
        Ordine copia = new Ordine(this.getId());
        copia.setdata(this.getData());

        // Creates a deep copy of the items list
        ObservableList<VoceOrdine> vociCopia = FXCollections.observableArrayList();
        for (VoceOrdine voce : this.getVociProperty()) {
            vociCopia.add(voce.copy());
        }
        copia.setVoci(vociCopia);
        copia.setScontoPercentuale(this.getScontoPercentuale());
        copia.setPrezzoTotaleParziale(this.getPrezzoTotaleParziale());
        copia.setPrezzoTotaleFinale(this.getPrezzoTotaleFinale());
        copia.setMetodoPagamento(this.getMetodoPagamento());
        copia.setImportoRicevuto(this.getImportoRicevuto());
        return copia;
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
        // System.out.println("Calcolo totale ordine ID " + this.getId());
        // System.out.println("Totale senza sconto: " + totaleSenzaSconto);

        // Sets the partial total
        this.prezzoTotaleParziale.set(totaleSenzaSconto);

        // Applies the discount 
        this.prezzoTotaleFinale.set(totaleSenzaSconto * (1 - this.scontoPercentuale.get() / 100.0));

        // System.out.println("Prezzo totale parziale ID: " + this.getId() + " -> " + this.getPrezzoTotaleParziale());
        // System.out.println("Prezzo totale finale ID: " + this.getId() + " -> " + this.getPrezzoTotaleFinale());
        // System.out.println("----------------------------------------------------------");

        // Calculates the change if needed
        calcolaResto();
    }

    /**
     * Calculates the change to give back to the customer
     */
    private void calcolaResto() {
        // Calculates the change only if the amount received is greater than the total price
        if (this.importoRicevuto.get() > this.prezzoTotaleFinale.get()) {
            this.resto.set(this.importoRicevuto.get() - this.prezzoTotaleFinale.get());
        } else {
            this.resto.set(0.0);
        }
    }
}
