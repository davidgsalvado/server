/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGeneratorImpl implements JwtGenerator{

    @Value("${project.jwt.signKey}")
    private String signKey;

    @Value("${project.jwt.expirationMinutes}")
    private long expirationMinutes;

    @Override
    public String generate(JwtInfo info) {

        return Jwts.builder()
                .claim("userId", info.getUserId())
                .claim("role", info.getRole())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMinutes*60*1000))
                .signWith(SignatureAlgorithm.HS512, signKey.getBytes())
                .compact();

    }

    @Override
    public JwtInfo getInfo(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(signKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return new JwtInfo(
                ((Integer) claims.get("userId")).longValue(),
                claims.getSubject(),
                (String) claims.get("role"));

    }
}
