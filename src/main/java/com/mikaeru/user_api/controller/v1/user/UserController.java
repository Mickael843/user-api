package com.mikaeru.user_api.controller.v1.user;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.report.ReportService;
import com.mikaeru.user_api.domain.service.user.UserService;
import com.mikaeru.user_api.domain.validation.user.UserValidationGroup.CreateValidation;
import com.mikaeru.user_api.domain.validation.user.UserValidationGroup.UpdateValidation;
import com.mikaeru.user_api.dto.user.in.ReportParam;
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

    @PutMapping("/{externalId}")
    public ResponseEntity<UserOutput> updateUser(@Validated(UpdateValidation.class) @RequestBody UserInput userIn, @PathVariable UUID externalId) {
        userService.update(userIn.convertToEntity());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page/{page}")
    @CachePut(value = "usersCacheOne")
    @CacheEvict(value = "usersCacheOne", allEntries = true)
    public ResponseEntity<Page<UserOutput>> getAllPages(@PathVariable Integer page) {
        return ResponseEntity.ok(convertToPageDTO(userService.findAllPages(page, ITEMS_PER_PAGE)));
    }

    @GetMapping("/search/{firstname}")
    @CachePut(value = "usersCacheTwo")
    @CacheEvict(value = "usersCacheTwo", allEntries = true)
    public ResponseEntity<List<UserOutput>> getAllUserByName(@PathVariable String firstname) {
        return ResponseEntity.ok(convertToCollections(userService.findAllByName(firstname)));
    }

    @GetMapping("/search/{firstname}/page/{page}")
    @CachePut(value = "usersCacheTwo")
    @CacheEvict(value = "usersCacheTwo", allEntries = true)
    public ResponseEntity<Page<UserOutput>> getAllUserByNamePage(@PathVariable String firstname, @PathVariable Integer page) {
        return ResponseEntity.ok(convertToPageDTO(userService.findAllByName(page, ITEMS_PER_PAGE, firstname)));
    }

    @GetMapping("/{externalId}")
    public ResponseEntity<UserOutput> getUser(@PathVariable UUID externalId) {
        return ResponseEntity.ok(userService.findByExternalId(externalId).convertToDTO());
    }

    @DeleteMapping("/{externalId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID externalId) {
        userService.delete(externalId);
        return ResponseEntity.noContent().build();
    }

    @CachePut(value = "reportCacheOne")
    @CacheEvict(value = "reportCacheOne", allEntries = true)
    @GetMapping(value = "/report", produces = "application/text")
    public ResponseEntity<String> reportDownload(HttpServletRequest request) {
        return ResponseEntity.ok(reportService.generateReport(request));
    }

    @CachePut(value = "reportCacheTwo")
    @CacheEvict(value = "reportCacheTwo", allEntries = true)
    @PostMapping(value = "/report", produces = "application/text")
    public ResponseEntity<String> reportDownloadWithParam(@RequestBody ReportParam param, HttpServletRequest request) {
        // TODO gerar os relatórios e adicionar na aplicação
        return ResponseEntity.ok(reportService.generateReport(param, request));
    }

    private List<UserOutput> convertToCollections(List<User> users) {
        List<UserOutput> output = new ArrayList<>();

        users.forEach(user -> output.add(user.convertToDTO()));

        return output;
    }

    private Page<UserOutput> convertToPageDTO(Page<User> userPage) {

        List<UserOutput> outputs = new ArrayList<>();

        userPage.get().forEach(user -> {
            outputs.add(user.convertToDTO());
        });

        return new PageImpl<>(outputs);
    }
}
