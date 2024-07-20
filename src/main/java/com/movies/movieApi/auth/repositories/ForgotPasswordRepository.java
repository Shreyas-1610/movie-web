
package com.movies.movieApi.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.movies.movieApi.auth.entities.ForgotPassword;
import com.movies.movieApi.auth.entities.User;


public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    
    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
}
