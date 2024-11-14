package team7.inplace.admin.error;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
    List<ErrorLog> findByIsResolvedFalse();
}
