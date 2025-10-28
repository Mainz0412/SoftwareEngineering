package com.huawei.web.util;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.huawei.web.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Ruijie Zhao
 */
@Slf4j
@Component
public class TokenUtils {
    /**
     * 生成token
     *
     * @param user 用户
     * @return Token
     */
    public static String genToken(User user) {
        return JWT.create().withExpiresAt(DateUtil.offsetDay(new Date(), 1))
                .withAudience(user.getUserAccount())
                .sign(Algorithm.HMAC256(user.getUserPrivilege().toString()));
    }
}
