package com.tmindtech.api.account;

import com.google.common.base.Strings;
import com.tmindtech.api.account.model.Account;
import com.tmindtech.api.account.model.LoginCodeRequest;
import com.tmindtech.api.account.model.OAuth2Token;
import com.tmindtech.api.account.model.OAuth2User;
import com.tmindtech.api.account.service.LoginService;
import com.tmindtech.api.account.service.OAuth2UaaService;
import com.tmindtech.api.base.annotation.AwesomeParam;
import com.tmindtech.api.base.exception.AwesomeException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import kotlin.jvm.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 测试接口
 * Created by RexQian on 2017/2/21.
 */
@RestController
@RequestMapping("/login/oauth2_uaa")
public class UaaOAuthController {

    @Autowired
    OAuth2UaaService oauth2Service;

    @Autowired
    LoginService loginService;

    /**
     * 登录重定向
     */
    @GetMapping
    public void redirect(HttpServletResponse response,
                         @AwesomeParam String redirectUrl,
                         @AwesomeParam(required = false) String state,
                         @AwesomeParam String code) throws IOException {
        String requestUrl = UriComponentsBuilder.fromHttpUrl(redirectUrl)
                .queryParam("state", state)
                .queryParam("code", code)
                .build().encode().toUriString();
        response.sendRedirect(requestUrl);
    }

    @PostMapping
    @Synchronized
    public Object login(@RequestBody LoginCodeRequest login) throws AwesomeException, InterruptedException {
        if (Strings.isNullOrEmpty(login.code)) {
            throw new AwesomeException(Config.ERROR_EMPTY_CODE);
        }

        // get user information by code
        OAuth2Token token = new OAuth2Token();
        token.openId = login.code;
        token.accessToken = "debug_access_token";
        token.expiresIn = 7200L;
        token.refreshToken = "debug_refresh_token";

        //create user if not exist
        Account account = oauth2Service.getByOpenId(token.openId);

        if (account == null) {
            OAuth2User user = new OAuth2User();
            user.openId = token.openId;
            user.imgAddr = "https://cdn.v2ex.com/gravatar/1c69025ea209147bd35c327c1710914b";
            account = oauth2Service.create(token, user);
        }
        return loginService.login(account.id);
    }
}
