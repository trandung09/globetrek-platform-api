package com.tvd.globetrekplatform.api.services.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tvd.globetrekplatform.api.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements IJwtService {

    @Value("${jwt.token_expiration_time}")
    long tokenExpirationTime;

    @Value("${jwt.secretkey}")
    String secretKey;

    public String generateToken(User user) {

        JWSHeader jwtHeader = new JWSHeader(JWSAlgorithm.HS512);

        String userScope = "..."; //

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("dungtv")
                .issueTime(new Date())
                .expirationTime(
                        new Date(
                                System.currentTimeMillis() + tokenExpirationTime * 1000L))
                .subject(user.getEmail())
                .claim("userId", user.getUserId())
                .claim("fullname", user.getFullName())
                // other claims...
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwtHeader, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();

        } catch (JOSEException e) {
            throw new RuntimeException("Cannot create jwt token, error: " + e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        try {
            // Returns SignedJWT with 3 components including header, payload, signature
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return signedJWT.verify(verifier) && expirationTime.after(new Date());

        } catch (JOSEException | ParseException e) {
            throw new RuntimeException("Cannot validate token: " + e.getMessage());
        }
    }

    // Claim: Map<String, Object> (In JWTClaimSet)
    public JWTClaimsSet getClaimsSetFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();

        } catch (ParseException e) {
            throw new RuntimeException("Cannot get claims from token: " + e.getMessage());
        }
    }

    public <T> T extractClaim(String token, Function<JWTClaimsSet, T> function) {
        JWTClaimsSet claims = this.getClaimsSetFromToken(token);
        return function.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        Date expirationTime = (Date) this.extractClaim(token, JWTClaimsSet::getExpirationTime);
        return expirationTime.before(new Date());
    }

    public String getSubject(String token) {
        return extractClaim(token, JWTClaimsSet::getSubject);
    }

    public String getClaimByKey(String token, String key) {
        Map<String, Object> claims = extractClaim(token, JWTClaimsSet::getClaims);
        return (String) claims.get(key);
    }
}
