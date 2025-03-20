package uz.uolimov.abs.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PartitionedTable {
    TRANSACTIONS("transactions"),
    TRANSACTION_HISTORIES("transaction_histories");

    private final String name;
}
