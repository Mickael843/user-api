package com.mikaeru.user_api.controller.v1.user;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.report.ReportService;
import com.mikaeru.user_api.domain.service.user.UserService;
import com.mikaeru.user_api.domain.validation.user.UserValidationGroup.CreateValidation;
import com.mikaeru.user_api.domain.validation.user.UserValidationGroup.UpdateValidation;
import com.mikaeru.user_api.dto.user.in.UserInput;
import com.mikaeru.user_api.dto.user.out.UserOutput;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/v1/users")
@Api(produces = APPLICATION_JSON_VALUE, description = "Responsável pelas Operações do usuário.", tags = {"User Controller"})
public class UserController {

    private static final Integer ITEMS_PER_PAGE= 6;

    @Autowired private UserService userService;

    @Autowired private ReportService reportService;

    /**
     * Método responsável pela criação de novos usuários no sistema.
     *
     * @param userIn {@link UserInput} Objeto de transferência de dados.
     * @return <code>{@link ResponseEntity}</code>
     *
     * HTTP STATUS:
     *
     * 201 (Created) - Caso o fluxo de criação de um novo usuário seja executado com sucesso.
     * 400 (Bad Request) - Os dados enviados pelo client são inválidos.
     */
    @PostMapping
    public ResponseEntity<UserOutput> saveUser(@Validated(CreateValidation.class) @RequestBody UserInput userIn) {

        User user = userService.save(userIn.convertToEntity());

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{externalId}")
                        .buildAndExpand(user.getExternalId())
                        .toUri()
        ).build();
    }

    /**
     * Método responsável pela atualização de um usuário especifico.
     *
     * @param userIn {@link UserInput} Objeto de transferência de dados.
     * @param externalId id externo do usuário
     * @return <code>{@link ResponseEntity}</code> object
     *
     * HTTP STATUS:
     *
     * 204 (No Content) - Caso o fluxo de atualização seja executado com sucesso.
     * 400 (Bad Request) - Os dados enviados pelo client são inválidos.
     * 404 (Not Found) - Usuário não encontrado.
     */
    @PutMapping("/{externalId}")
    public ResponseEntity<UserOutput> updateUser(@Validated(UpdateValidation.class) @RequestBody UserInput userIn, @PathVariable UUID externalId) {
        userService.update(userIn.convertToEntity());
        return ResponseEntity.noContent().build();
    }

    /**
     * Método que retorna os dados dos usuários paginados e ordenados pelo FIRSTNAME.
     *
     * @param page Número da página
     * @return <code>{@link ResponseEntity}</code> object
     *
     * HTTP STATUS:
     *
     * 200 (OK) - Dados retornados com sucesso.
     * 204 (Not Found) - Nenhum usuário encontrado.
     */
    @GetMapping("/page/{page}")
    @CachePut(value = "usersCacheOne")
    @CacheEvict(value = "usersCacheOne", allEntries = true)
    public ResponseEntity<Page<UserOutput>> getAllPages(@PathVariable Integer page) {
        return ResponseEntity.ok(convertToPageDTO(userService.findAllPages(page, ITEMS_PER_PAGE)));
    }

    /**
     * Procura um usuário com base em no FIRSTNAME.
     *
     * @param firstname nome do usuário
     * @return <code>{@link ResponseEntity<List<UserOutput>>}</code> object
     *
     * HTTP STATUS:
     *
     * 200 (OK) - Dados retornados com sucesso.
     * 204 (Not Found) - Nenhum usuário encontrado.
     */
    @GetMapping("/search/{firstname}")
    @CachePut(value = "usersCacheTwo")
    @CacheEvict(value = "usersCacheTwo", allEntries = true)
    public ResponseEntity<List<UserOutput>> getAllUserByName(@PathVariable String firstname) {
        return ResponseEntity.ok(convertToCollections(userService.findAllByName(firstname)));
    }

    /**
     *
     * Procura os usuários pelo nome e retorna os dados paginados.
     *
     * @param firstname Nome do usuário
     * @param page Número da página
     * @return
     *
     * HTTP STATUS:
     *
     * 200 (OK) - Dados retornados com sucesso.
     * 204 (Not Found) - Nenhum usuário encontrado.
     */
    @GetMapping("/search/{firstname}/page/{page}")
    @CachePut(value = "usersCacheTwo")
    @CacheEvict(value = "usersCacheTwo", allEntries = true)
    public ResponseEntity<Page<UserOutput>> getAllUserByNamePage(@PathVariable String firstname, @PathVariable Integer page) {
        return ResponseEntity.ok(convertToPageDTO(userService.findAllByName(page, ITEMS_PER_PAGE, firstname)));
    }

    /**
     * Procura um usuário pelo seu id externo.
     *
     * @param externalId id externo do usuário
     * @return <code>{@link ResponseEntity<UserOutput>}</code> object
     *
     * HTTP STATUS:
     *
     * 200 (OK) - Dados retornados com sucesso.
     * 204 (Not Found) - Nenhum usuário encontrado.
     */
    @GetMapping("/{externalId}")
    public ResponseEntity<UserOutput> getUser(@PathVariable UUID externalId) {
        return ResponseEntity.ok(userService.findByExternalId(externalId).convertToDTO());
    }

    /**
     * Deleta um usuário no sistema com base no id externo.
     *
     * @param externalId id externo do usuário
     * @return <code>{@link ResponseEntity}</code> object
     *
     * HTTP STATUS:
     *
     * 204 (No Content) - Usuário deletado com sucesso
     * 204 (Not Found) - Nenhum usuário encontrado.
     */
    @DeleteMapping("/{externalId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID externalId) {
        userService.delete(externalId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retorna um relatórios com os principais dados do usuário.
     *
     * @param request {@link HttpServletRequest}
     * @return <code>{@link ResponseEntity<String>}</code> base64
     *
     * HTTP STATUS
     *
     * 200 (OK) - Retorna relatório
     * 500 (Internal Server Error) - error ao gerar relatório de usuários.
     */
    @CachePut(value = "reportCacheOne")
    @CacheEvict(value = "reportCacheOne", allEntries = true)
    @GetMapping(value = "/report", produces = "application/text")
    public ResponseEntity<String> reportDownload(HttpServletRequest request) {
        return ResponseEntity.ok(reportService.generateReport(request));
    }

    /**
     * Método responsável por converter uma lista de Usuários (ENTITY) em uma lista de DTOs.
     *
     * @param users lista de usuários (ENTITY)
     * @return <code>{@link List<UserOutput>}</code> lista de DTOs
     */
    private List<UserOutput> convertToCollections(List<User> users) {
        List<UserOutput> output = new ArrayList<>();

        users.forEach(user -> output.add(user.convertToDTO()));

        return output;
    }

    /**
     * Método responsável por converter um {@link Page<User>} de Usuários (ENTITY) em um {@link Page<UserOutput>} de DTOs.
     *
     * @param userPage {@link Page<User>} de usuários (ENTITY)
     * @return <code>{@link Page<UserOutput>}</code>
     */
    private Page<UserOutput> convertToPageDTO(Page<User> userPage) {

        List<UserOutput> outputs = new ArrayList<>();

        userPage.get().forEach(user -> {
            outputs.add(user.convertToDTO());
        });

        return new PageImpl<>(outputs);
    }
}
