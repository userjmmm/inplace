package team7.inplace.admin.error;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
    List<ErrorLog> findByIsResolvedFalse();
}
