package uz.uolimov.abs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uolimov.abs.model.entity.TransactionHistoryEntity;
import java.util.UUID;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistoryEntity, Long> {

    @Query(
        value = "SELECT count(id) FROM transaction_histories WHERE transaction_id = :transactionId and status = :status",
        nativeQuery = true
    )
    Integer getCountByTransactionIdAndStatus(UUID transactionId, String status);
}
