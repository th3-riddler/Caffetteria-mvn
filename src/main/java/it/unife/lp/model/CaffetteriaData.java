package it.unife.lp.model;

import java.util.List;

public class CaffetteriaData {
    private List<Articolo> articoli;
    private List<Ordine> ordini;

    public List<Articolo> getArticoli() { return articoli; }
    public void setArticoli(List<Articolo> articoli) { this.articoli = articoli; }

    public List<Ordine> getOrdini() { return ordini; }
    public void setOrdini(List<Ordine> ordini) { this.ordini = ordini; }
}
