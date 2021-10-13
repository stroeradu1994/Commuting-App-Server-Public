package com.commuting.commutingapp.security.repo;

import com.commuting.commutingapp.security.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, String> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByPhoneNumber(String email);

    boolean existsByPhoneNumber(String email);

    Optional<Account> findByEmailVerificationKey(String key);

}
