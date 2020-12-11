package com.mikaeru.user_api.controller.v1.profession;

import com.mikaeru.user_api.domain.model.user.profession.Profession;
import com.mikaeru.user_api.domain.service.profession.CrudProfessionService;
import com.mikaeru.user_api.repository.ProfessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "v1/professions", produces = "application/json")
public class ProfessionController {

    @Autowired
    private ProfessionRepository professionRepository;

    @Autowired
    private CrudProfessionService professionService;

    @GetMapping
    public ResponseEntity<List<Profession>> getAll() {
        List<Profession> professions = professionRepository.findAll();
        return ResponseEntity.ok(professions);
    }
}
