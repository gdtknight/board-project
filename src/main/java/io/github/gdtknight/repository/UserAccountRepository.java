package io.github.gdtknight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.gdtknight.domain.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

}
