package uz.uolimov.abs.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.uolimov.abs.model.enums.TransactionTechStatus;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_histories")
public class TransactionHistoryEntity {

    @Id
    @SequenceGenerator(name = "transaction_histories_seq", sequenceName = "transaction_histories_seq", allocationSize = 1)
    @GeneratedValue(generator = "transaction_histories_seq")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionTechStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false, updatable = false)
    private TransactionEntity transaction;
}
