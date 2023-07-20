package dev.hub.security.domain.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.hub.security.entity.UserRoles;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private String secretKey = "@l1[@r.42t!:20230720T1234";
    final long tokenValidTime = 30 * 60 * 1000L;  // 30 minutes
    //final long tokenValidTime = 60 * 60 * 1000L * 24 ;  // 24 hour

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * JWT 토큰 생성
     * @param userRoles
     * @return
     */
    public String createNewToken(UserRoles userRoles) {
        String token = null;
        Date expireDate = new Date(System.currentTimeMillis() + tokenValidTime);
        try {
            //UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
            UserDetails userDetails = User
                    .builder()
                    .username(userRoles.getUserId())
                    .password(userRoles.getPassWord())
                    //.roles(String.valueOf(userRoles.getRoleType()))
                    .authorities("ROLE_"+userRoles.getRoleType())
                    .build();

            Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("username", userRoles.getUserId())
                    .withClaim("password", userRoles.getPassWord())
                    .withClaim("roles", "ROLE_"+userRoles.getRoleType())
                    .withExpiresAt(expireDate)
                    .sign(algorithm);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return token;
    }


    //select authentication
    public Authentication getAuthentication(String token){

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        DecodedJWT jwt = JWT.require(algorithm)
                .withIssuer("auth0")
                .build()
                .verify(token);

        String userId = jwt.getClaim("username").asString();
        String roles = jwt.getClaim("roles").asString();

        UserDetails userDetails = User
                .builder()
                .username(userId)
                .password("pass")
                .authorities(roles)
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        DecodedJWT jwt;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            jwt = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build()
                    .verify(token);
            log.info("Token successfully validated.");
            return true;
        } catch (JWTVerificationException e){
            log.error("JWT token is expired or invalid", e);
            throw new RuntimeException("JWT token is expired or invalid", e);
        }
    }




    public DecodedJWT getTokenInfo(String token){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        DecodedJWT jwt = JWT.require(algorithm)
                .withIssuer("auth0")
                .build()
                .verify(token);
        System.out.println("jwt = " + jwt);
        return jwt;
    }
}
