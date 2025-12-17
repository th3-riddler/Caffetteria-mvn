package it.unife.lp.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.unife.lp.model.Articolo;
import it.unife.lp.model.Ordine;
import it.unife.lp.model.VoceOrdine;

public class StatisticsUtil {

    public static Map<Articolo, Integer> dailySalesMap(List<Ordine> ordini, LocalDate date) {
        return ordini.stream()
                // Checks if the order date matches the specified date
                .filter(o -> o.getData().isEqual(date))
                .flatMap(o -> o.getVoci().stream())
                .collect(
                    Collectors.groupingBy(
                        VoceOrdine::getArticolo,
                        Collectors.summingInt(VoceOrdine::getQuantita)
                    )
                );
    }

    public static Map<Articolo, Integer> inventoryMap(List<Articolo> articoli) {
        return articoli.stream()
                .collect(
                    Collectors.toMap(
                        articolo -> articolo,
                        Articolo::getStoccaggio
                    )
                );
    }

    public static Map<Articolo, Integer> topItemsMap(List<Ordine> ordini, LocalDate startDate, LocalDate endDate) {
        return ordini.stream()
                // Checks if the order date is within the specified range (inclusive)
                .filter(o -> (o.getData().isEqual(startDate) || o.getData().isAfter(startDate)) && (o.getData().isEqual(endDate) || o.getData().isBefore(endDate)))
                .flatMap(o -> o.getVoci().stream())
                .collect(
                    Collectors.groupingBy(
                        VoceOrdine::getArticolo,
                        Collectors.summingInt(VoceOrdine::getQuantita)
                    )
                ).entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
