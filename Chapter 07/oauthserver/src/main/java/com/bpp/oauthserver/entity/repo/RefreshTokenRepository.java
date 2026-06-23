package com.bpp.oauthserver.entity.repo;

import com.bpp.oauthserver.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

// RefreshTokenRepository.java
@Repository
    public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("""
                UPDATE RefreshToken r
                SET r.revoked = true
                WHERE r.user.id = :userId AND r.revoked = false
            """)
    void revokeAllForUser(@Param("userId") String userId);

    @Modifying
    @Query("""
                DELETE FROM RefreshToken r
                WHERE r.expiresAt < :cutoff OR r.revoked = true
            """)
    void deleteExpiredAndRevoked(@Param("cutoff") LocalDateTime cutoff);
}
