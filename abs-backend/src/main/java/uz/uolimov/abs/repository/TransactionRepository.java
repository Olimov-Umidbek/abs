package uz.uolimov.abs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uolimov.abs.model.dto.transaction.TransactionDetailsDTO;
import uz.uolimov.abs.model.entity.TransactionEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    @Query(
        value = "SELECT " +
        "t.id as id, s.name||' '||s.surname as senderName, r.name||' '||r.surname as receiverName, t.amount, t.status " +
        "FROM transactions t " +
        "INNER JOIN users s ON s.id = t.sender_id " +
        "INNER JOIN users r ON r.id = t.receiver_id " +
        "WHERE t.id = :id and t.sender_id = :userId",
        nativeQuery = true
    )
    Optional<TransactionDetailsDTO> loadTransactionDetailsByIdAndUserId(UUID id, UUID userId);

    @Query(
        value = "SELECT " +
            "t.id as id, s.name||' '||s.surname as senderName, r.name||' '||r.surname as receiverName, t.amount, t.status " +
            "FROM transactions t " +
            "INNER JOIN users s ON s.id = t.sender_id " +
            "INNER JOIN users r ON r.id = t.receiver_id " +
            "WHERE t.sender_id = :userId and s.status = 'ACTIVE'",
        nativeQuery = true
    )
    Page<TransactionDetailsDTO> loadAllByUserId(UUID userId, PageRequest pageRequest);

    @Query(
        value = "SELECT " +
            "t.id as id, s.name||' '||s.surname as senderName, r.name||' '||r.surname as receiverName, t.amount, t.status " +
            "FROM transactions t " +
            "INNER JOIN users s ON s.id = t.sender_id " +
            "INNER JOIN users r ON r.id = t.receiver_id " +
            "WHERE t.createdAt between :startDate and :endDate and t.sender_id = :userId and s.status = 'ACTIVE'",
        nativeQuery = true
    )
    Page<TransactionDetailsDTO> loadAllByDateRange(UUID userId, LocalDate startDate, LocalDate endDate, PageRequest pageRequest);
}
