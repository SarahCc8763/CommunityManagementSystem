package finalProj.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JsonWebTokenUtility {

    private final String secret = "MySecretKey";
    private final Algorithm algorithm = Algorithm.HMAC256(secret);
    private final String issuer = "finalProj";

    // 建立 Token
    public String createToken(String email) {
        return JWT.create()
                .withIssuer(issuer)
//                .withClaim("userId", userId)
                .withClaim("email", email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60_000)) // 1 min
                .sign(algorithm);
    }

    // 驗證與解碼 Token
    public DecodedJWT verifyToken(String token) {
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
    }

//    public Integer getUserId(String token) {
//        return verifyToken(token).getClaim("userId").asInt();
//    }

    public String getEmail(String token) {
        return verifyToken(token).getClaim("email").asString();
    }
}

