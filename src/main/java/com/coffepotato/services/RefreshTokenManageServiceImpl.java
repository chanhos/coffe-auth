package com.coffepotato.services;

import com.coffepotato.db.dao.RefreshTokenKeeperRepository;
import com.coffepotato.dto.RefreshTokenDTO;
import com.coffepotato.vo.common.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RefreshTokenManageServiceImpl implements  RefreshTokenManageService{


    private final RefreshTokenKeeperRepository refreshTokenKeeperRepository;


    @Autowired
    public RefreshTokenManageServiceImpl(RefreshTokenKeeperRepository refreshTokenKeeperRepository){
        this.refreshTokenKeeperRepository = refreshTokenKeeperRepository;
    }

    @Override
    public ResponseVo getRefreshTokenList() {
        ResponseVo res = new ResponseVo();

        List<RefreshTokenDTO> refreshTokenList = refreshTokenKeeperRepository.findAll().stream().map(token->{
                return RefreshTokenDTO.builder()
                               .tokenId(token.getTokenId()).tokenOwner(token.getTokenOwner())
                               .tokenState(token.getTokenState()).expireOn(token.getExpireOn())
                               .build();
        }).collect(Collectors.toList());

        res.setData(refreshTokenList);
        return res;
    }
}
