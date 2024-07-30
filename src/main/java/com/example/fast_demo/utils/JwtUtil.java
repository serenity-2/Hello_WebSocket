package com.example.fast_demo.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * JWT工具类
 **/
@Component
@Slf4j
public class JwtUtil {
    /**
     * 登录TOKEN过期时间72小时
     */
    public static final long EXPITE_TIME = 72 * 60 * 60 * 1000;

    @Value(value = "token.verify")
    private String verifyToken;

    /**
     * 获得token中的userCode
     *
     * @return token中包含的用户名
     */
    public String getUserCode(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            log.info("getUserCode 调用token解出来的数据{}", JSON.toJSONString(jwt));
            return jwt.getClaim("userCode").asString();
        } catch (JWTDecodeException e) {
            log.error("getUserCode异常：", e);
            return null;
        }
    }

    /**
     * 获得token中的secret
     *
     * @return token中包含的用户名
     */

    public static String getSecret(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("secret").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 校验TOKEN
     *
     * @param token 入参
     * @return 结果
     */
    public boolean verifyToken(String token) {
        String userCode = getUserCode(token);
        Map<String, Object> param = new HashMap<>();
        param.put("token", token);
        param.put("sn", UUID.randomUUID());
        param.put("userCode", userCode);
        log.info("verifyToken 组成的参数：{}", JSON.toJSONString(param));
        String post = HttpRequest.post(verifyToken)
                .header("Content-Type", "application/json")
                .body(JSON.toJSONString(param)).execute().body();
        ;
        JSONObject object = JSON.parseObject(post);
        log.info("verifyToken时 获取到服务端返回的结果是 {}", object);
        return "1".equals(object.getJSONObject("data").getString("status"));
    }

    /**
     * 生成签名,expireTime后过期
     * @param userId
     * @param phoneNumber
     * @param secret
     * @return
     */
    public static String sign(String userId,String phoneNumber, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPITE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withClaim("userId", userId)
                .withClaim("phoneNumber",phoneNumber)
                .withExpiresAt(date)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    /**
     * 生成签名,expireTime后过期
     * @param userId
     * @param phoneNumber
     * @param identity
     * @param secret
     * @return
     */
    public static String sign2(String userId,String phoneNumber, String identity, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPITE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withClaim("userId", userId)
                .withClaim("phoneNumber",phoneNumber)
                .withClaim("identity",identity)
                .withExpiresAt(date)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    /**
     * 校验token是否正确
     * @param userName
     * @param secret
     * @param token
     * @return
     */
    public static boolean verify(String token, String userName, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).withClaim("username", userName).build();
        try {
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取token中的基本信息-用户名
     *
     * @param token
     * @return
     */
    public static String getUserName(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("username").asString();
    }

    /**
     * 获取token中的基本信息-手机号
     *
     * @param token
     * @return
     */
    public static String getPhoneNumber(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("phoneNumber").asString();
    }

    /**
     * 获取token中的基本信息-登录人员身份
     *
     * @param token
     * @return
     */
    public static String getIdentity(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("identity").asString();
    }

    /**
     * 获取token签发时间
     * @param token
     * @return
     */
    public static Date getIssueAt(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getIssuedAt();
    }
    /**
     * 验证token是否过期
     * @param token
     * @return
     */
    public static boolean isTokenExpired(String token) {
        Date now = Calendar.getInstance().getTime();
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getExpiresAt().before(now);
    }
}
