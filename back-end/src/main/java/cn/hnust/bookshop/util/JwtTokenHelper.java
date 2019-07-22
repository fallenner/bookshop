package com.jsdai.jsdaimanage.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsdai.jsdaimanage.bean.ResponseResult;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenHelper {

    private static final String salt = "jsdai";
    //最大过期时间
    private static final long maxExpiredMillis = 24 * 60 * 60 * 1000;
    //人为控制过期时间，在此间隔时间内如果没有访问接口,自动过期。使得每次访问接口自动延长过期时间
    private static final long expiredMillis = 60 * 60 * 1000;
    private static Map<String, Object> tokenMap = new HashMap<>();

    public static String createJWT(String id, Object object) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //签名算法的秘钥，解析token时的秘钥需要和此时的一样
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(salt);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        ObjectMapper mapper = new ObjectMapper();
        //构造
        JwtBuilder builder = null;
        try {
            builder = Jwts.builder().setId(id)
                    .setIssuedAt(now)
                    .setSubject(mapper.writeValueAsString(object))
                    .signWith(signatureAlgorithm, signingKey);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //给token设置过期时间
        long maxExpMillis = nowMillis + maxExpiredMillis;
        Date exp = new Date(maxExpMillis);
        builder.setExpiration(exp);
        long expMillis = nowMillis + expiredMillis;
        tokenMap.put(id, expMillis);
        return builder.compact();
    }


    public static ResponseResult checkToken(HttpServletRequest request) {
        ResponseResult result = new ResponseResult();
        String token = request.getHeader("token");
        if (null == token || token.isEmpty()) {
            result.setUnAuthMsg();
            return result;
        }
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(salt)).parseClaimsJws(token).getBody();
            String userId = claims.getId();
            long nowMillis = System.currentTimeMillis();//现在的时间
            Object tokenExpiredMillis = tokenMap.get(userId);
            if (tokenMap.get(userId) == null || nowMillis > (long) tokenExpiredMillis) {
                tokenMap.remove(userId);
                result.setUnAuthMsg();
                return result;
            } else {
                request.setAttribute("userInfo", claims.getSubject());
                tokenMap.put(userId, nowMillis + expiredMillis);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setUnAuthMsg();
            return result;
        }
    }


    public static void clearToken(String token) throws Exception {
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(salt)).parseClaimsJws(token).getBody();
        String userId = claims.getId();
        tokenMap.remove(userId);
    }
}
