package com.tmindtech.api.account.service;

import com.tmindtech.api.account.db.AccountMapper;
import com.tmindtech.api.account.db.OAuth2UaaMapper;
import com.tmindtech.api.account.model.Account;
import com.tmindtech.api.account.model.OAuth2Token;
import com.tmindtech.api.account.model.OAuth2Uaa;
import com.tmindtech.api.account.model.OAuth2User;
import java.sql.Timestamp;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by RexQian on 2017/2/21.
 */
@Service
public class OAuth2UaaService {


    @Autowired
    OAuth2UaaMapper auth2UaaMapper;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountMapper accountMapper;

    private void fillOAuth2Uaa(long id, OAuth2Uaa uaa, OAuth2Token token) {
        uaa.id = id;
        uaa.openId = token.openId;
        uaa.accessToken = token.accessToken;
        uaa.expiredTime = Timestamp.from(Instant.now().plusSeconds(token.expiresIn));
        uaa.refreshToken = token.refreshToken;
    }

    public Account getByOpenId(String openId) {
        OAuth2Uaa uaa = new OAuth2Uaa();
        uaa.openId = openId;
        uaa = auth2UaaMapper.selectOne(uaa);
        if (uaa == null) {
            return null;
        }
        return accountService.getById(uaa.id);
    }

    public OAuth2Uaa getByAccountId(long accountId) {
        return auth2UaaMapper.selectByPrimaryKey(accountId);
    }

    @Transactional
    public Account create(OAuth2Token token, OAuth2User user) {
        Account account = accountService.create(OAuth2User.getNickname(user), user.imgAddr);

        OAuth2Uaa uaa = new OAuth2Uaa();
        fillOAuth2Uaa(account.id, uaa, token);
        auth2UaaMapper.insertSelective(uaa);
        account = accountMapper.selectByPrimaryKey(account.id);
        return account;
    }
}
