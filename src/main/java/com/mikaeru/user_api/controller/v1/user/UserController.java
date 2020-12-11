package com.mikaeru.user_api.controller.v1.user;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.report.ReportService;
import com.mikaeru.user_api.domain.service.user.UserService;
import com.mikaeru.user_api.dto.user.in.ReportParam;
import com.mikaeru.user_api.dto.user.in.UserInput;
import com.mikaeru.user_api.dto.user.out.UserOutput;
import com.mikaeru.user_api.repository.UserRepository;
import net.sf.jasperreports.engine.JRException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v1/users")
public class UserController {

    private static final Integer ITEMS_PER_PAGE= 6;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportService reportService;

    @GetMapping("/page/{page}")
    @ResponseStatus(HttpStatus.OK)
    @CachePut(value = "usersCacheOne")
    @CacheEvict(value = "usersCacheOne", allEntries = true)
    public Page<User> getAllUsersPage(@PathVariable Integer page) {
        PageRequest pageRequest = PageRequest.of(page, ITEMS_PER_PAGE, Sort.by("name"));
        return userRepository.findAll(pageRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CachePut(value = "usersCacheOne")
    @CacheEvict(value = "usersCacheOne", allEntries = true)
    public Page<User> getAllUsers() {
        PageRequest pageRequest = PageRequest.of(0, ITEMS_PER_PAGE, Sort.by("name"));
        return userRepository.findAll(pageRequest);
    }

    @GetMapping("/search/{name}")
    @ResponseStatus(HttpStatus.OK)
    @CachePut(value = "usersCacheTwo")
    @CacheEvict(value = "usersCacheTwo", allEntries = true)
    public Page<User> getAllUserByName(@PathVariable String name) {
        PageRequest pageRequest;
        Page<User> outputPage;

        if (name == null || name.trim().isEmpty() || name.equalsIgnoreCase("undefined")) {
            pageRequest = PageRequest.of(0, ITEMS_PER_PAGE, Sort.by("name"));
            outputPage = userRepository.findAll(pageRequest);
        } else {
            pageRequest = PageRequest.of(0, ITEMS_PER_PAGE, Sort.by("name"));
            outputPage = userRepository.findByUsernamePage(name, pageRequest);
        }

        return outputPage;
    }

    @GetMapping("/search/{name}/page/{page}")
    @ResponseStatus(HttpStatus.OK)
    @CachePut(value = "usersCacheTwo")
    @CacheEvict(value = "usersCacheTwo", allEntries = true)
    public Page<User> getAllUserByNamePage(@PathVariable String name, @PathVariable Integer page) {
        PageRequest pageRequest;
        Page<User> outputPage;

        if (name == null || name.trim().isEmpty() || name.equalsIgnoreCase("undefined")) {
            pageRequest = PageRequest.of(page, ITEMS_PER_PAGE, Sort.by("name"));
            outputPage = userRepository.findAll(pageRequest);
        } else {
            pageRequest = PageRequest.of(page, ITEMS_PER_PAGE, Sort.by("name"));
            outputPage = userRepository.findByUsernamePage(name, pageRequest);
        }

        return outputPage;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserOutput> getUser(@PathVariable UUID uuid) {
        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutput saveUser(@Valid @RequestBody UserInput input) {
        return null;
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UserOutput> updateUser(@Valid @RequestBody UserInput input, @PathVariable UUID uuid) {
        return null;
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        return null;
    }

    @CachePut(value = "reportCacheOne")
    @CacheEvict(value = "reportCacheOne", allEntries = true)
    @GetMapping(value = "/report", produces = "application/text")
    public ResponseEntity<String> reportDownload(HttpServletRequest request) {
        String pdfBase64;
        try {
            byte[] pdf = reportService.generateReport("user-report", request.getServletContext());
            pdfBase64 = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
        } catch (SQLException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(pdfBase64);
    }

    @CachePut(value = "reportCacheTwo")
    @CacheEvict(value = "reportCacheTwo", allEntries = true)
    @PostMapping(value = "/report", produces = "application/text")
    public ResponseEntity<String> reportDownloadWithParam(@RequestBody ReportParam param, HttpServletRequest request) {
        String pdfBase64;
        try {
            byte[] pdf = reportService.generateReport("user-report-param", request.getServletContext(), param);
            pdfBase64 = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
        } catch (SQLException | JRException | ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(pdfBase64);
    }

    private User toEntity(UserInput input) {
        return mapper.map(input, User.class);
    }

    private UserOutput toModel(User user) {
        return mapper.map(user, UserOutput.class);
    }

    private List<UserOutput> toCollections(List<User> users) {
        return users.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
