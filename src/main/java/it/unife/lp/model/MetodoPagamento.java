package it.unife.lp.model;

public enum MetodoPagamento {
    CONTANTI("Contanti"),
    CARTA_DI_CREDITO("Carta di Credito"),
    CARTA_DI_DEBITO("Carta di Debito");

    private final String displayName;

    MetodoPagamento(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}