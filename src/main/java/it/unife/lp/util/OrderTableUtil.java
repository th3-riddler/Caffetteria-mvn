package it.unife.lp.util;

import it.unife.lp.model.Ordine;
import it.unife.lp.model.VoceOrdine;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class OrderTableUtil {
    public static void populateItemsTable(
        TableView<VoceOrdine> table,
        TableColumn<VoceOrdine, String> nomeCol,
        TableColumn<VoceOrdine, String> prezzoUnitarioCol,
        TableColumn<VoceOrdine, Number> quantitaCol,
        TableColumn<VoceOrdine, String> prezzoTotaleCol,
        Ordine ordine
    ) {
        nomeCol.setCellValueFactory(cellData -> cellData.getValue().getArticolo().nomeProperty());

        // Uses Bindings to format the price values as strings with the euro symbol in order to keep them updated if the price of the article changes
        prezzoUnitarioCol.setCellValueFactory(cellData -> 
            Bindings.createStringBinding(
                () -> String.format("%.2f €", cellData.getValue().getArticolo().getPrezzo()),
                cellData.getValue().getArticolo().prezzoProperty()
            )
        );
        quantitaCol.setCellValueFactory(cellData -> cellData.getValue().quantitaProperty());

        // Uses Bindings to format the total price values as strings with the euro symbol in order to keep them updated if the price of the article or the quantity changes
        prezzoTotaleCol.setCellValueFactory(cellData -> 
            Bindings.createStringBinding(
                () -> String.format("%.2f €", cellData.getValue().getPrezzoTotale()),
                cellData.getValue().prezzoTotaleProperty()
            )
        );
        table.setItems(ordine.getVoci());
    }
}
