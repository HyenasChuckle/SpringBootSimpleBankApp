package dev.mike.eazybankz.repository;

import dev.mike.eazybankz.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByNumber(String username);

    Optional<Account> findByNumberAndOwnerEmail(String number, String email);
}
