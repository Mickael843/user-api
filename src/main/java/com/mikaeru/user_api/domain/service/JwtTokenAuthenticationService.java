package com.mikaeru.user_api.domain.service;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.helper.ApplicationContextLoadHelper;
import com.mikaeru.user_api.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenAuthenticationService {

    private static final Long EXPIRATION_TIME = 172800000L;
    private static final String SECRET_KEY = "secretPasswordSuperMegaDifficultForYou";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    public void addAuthentication(HttpServletResponse response, String username) throws IOException {
        String jwt = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();

        String token = TOKEN_PREFIX + " " + jwt;

        openCORS(response);

        response.addHeader(HEADER_STRING, token);
        response.getWriter().write("{\"Authorization\" : \"" + token + "\" }");
    }

    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(HEADER_STRING);

        openCORS(response);

        if (token != null) {
            String username = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, " "))
                    .getBody().getSubject();
            if (username != null) {
                Optional<User> userOptional = ApplicationContextLoadHelper
                        .getApplicationContext()
                        .getBean(UserRepository.class)
                        .findByUsername(username);

                if (userOptional.isPresent()) {
                    return new UsernamePasswordAuthenticationToken(
                            userOptional.get().getUsername(),
                            userOptional.get().getPassword(),
                            userOptional.get().getAuthorities()
                    );
                }
            }
        }
        return null;
    }

    private void openCORS(HttpServletResponse response) {
//        if (response.getHeader("Access-Control-Allow-Origin") == null) {
//            response.addHeader("Access-Control-Allow-Origin", "*");
//        }
//
//        if (response.getHeader("Access-Control-Allow-Headers") == null) {
//            response.addHeader("Access-Control-Allow-Headers", "*");
//        }
    }
}
