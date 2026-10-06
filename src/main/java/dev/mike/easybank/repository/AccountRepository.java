package dev.mike.easybank.repository;

import dev.mike.easybank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByNumber(String number);

    Optional<Account> findByIdAndOwnerUsername(Long id, String username);
}
