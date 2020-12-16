package com.mikaeru.user_api.domain.service.user;

import com.mikaeru.user_api.domain.model.user.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/**
 * Interface que abstrai as operações realizadas com um usuário.
 * @author Mickael Luiz
 */
public interface UserService {

    /**
     * Método responsável por salver um usuário no banco de dados.
     *
     * @param user {@link User} entity
     * @return <code>{@link User}</code> Usuário salvo
     */
    User save(User user);

    /**
     * Método responsável por atualizar um usuário no banco de dados.
     *
     * @param user {@link User} entity
     */
    void update(User user);

    /**
     * Método responsável por deleter um usuário no banco de dados com base em um id externo.
     *
     * @param externalId id externo do usuário.
     */
    void delete(UUID externalId);

    /**
     * Método responsável por atualizar a senha um usuário no banco de dados.
     *
     * @param password nova senha do usuário
     * @param idUser id do usuário
     */
    void updatePassword(String password, Long idUser);

    /**
     * Método responsável por procurar os usuários no banco de dados e retornarem os dados paginados.
     *
     * @param page Número da página
     * @param itemsPerPage Número de items por página
     * @return <code>{@link Page<User>}</code> página de usuários
     */
    Page<User> findAllPages(Integer page, Integer itemsPerPage);

    /**
     * Método responsável por procurar os usuários no banco de dados com base no nome do usuário.
     *
     * @param firstname Nome do usuário
     * @return <code>{@link List<User>}</code> lista de usuários encontrados
     */
    List<User> findAllByName(String firstname);

    /**
     * Método responsável por procurar os usuários no banco de dados com base no nome do usuário e retornarem os dados paginados.
     *
     * @param page Número da página
     * @param itemsPerPage Número dos items por página
     * @param firstname Nome do usuário
     * @return <code>{@link Page<User>}</code> Página de usuários encontrados
     */
    Page<User> findAllByName(Integer page, Integer itemsPerPage, String firstname);

    /**
     * Método responsável por procurar um usuário no banco de dados.
     *
     * @param externalId id externo do usuário
     * @return <code>{@link User}</code> object
     */
    User findByExternalId(UUID externalId);
}
