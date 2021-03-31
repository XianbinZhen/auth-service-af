package com.revature.auth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

// TODO JwtUtil methods need to be implemented.
public class JwtUtil {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256(System.getenv("AF_SECRET"));

    public static String generate(String email, String role) {

        return JWT.create()
                .withClaim("email", email)
                .withClaim("role", role)
                .sign(ALGORITHM);
    }

    public static DecodedJWT isValidJWT(String jwt) {

        return JWT.require(ALGORITHM).build().verify(jwt);
    }
}
