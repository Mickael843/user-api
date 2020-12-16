package com.mikaeru.user_api.domain.service;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.helper.ApplicationContextLoadHelper;
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

/**
 * Classe que realiza a autenticação de um usuário no sistema.
 * @author Mickael Luiz
 */
@Service
public class JwtTokenAuthenticationService {

    private static final Long EXPIRATION_TIME = 172800000L;
    private static final String SECRET_KEY = "secretPasswordSuperMegaDifficultForYou";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    /**
     * Método que adiciona um toke de autenticação na resposta da requisição.
     *
     * @param response {@link HttpServletResponse}
     * @param username nome de login do usuário
     * @throws IOException
     */
    public void addAuthentication(HttpServletResponse response, String username) throws IOException {
        String jwt = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();

        String token = TOKEN_PREFIX + " " + jwt;

        response.addHeader(HEADER_STRING, token);
        response.getWriter().write("{\"Authorization\" : \"" + token + "\" }");
    }

    /**
     * Método que verifica se existe um token de autenticação na requisição feita pelo client.
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return <code>{@link Authentication}</code> object
     */
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(HEADER_STRING);

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
                            userOptional.get().getRoles()
                    );
                }
            }
        }
        return null;
    }
}
