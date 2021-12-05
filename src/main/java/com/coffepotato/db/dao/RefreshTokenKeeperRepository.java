package com.coffepotato.db.dao;

import com.coffepotato.db.entity.RefreshTokenKeeper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenKeeperRepository extends
        JpaRepository<RefreshTokenKeeper, Long> {

    RefreshTokenKeeper findRefreshTokenKeeperByTokenIdAndTokenOwnerAndTokenState(String tokenId, String tokenOwner, short state);

    Page<RefreshTokenKeeper> findByTokenStateOrderByExpireOnDesc(short state , Pageable pageable);
}