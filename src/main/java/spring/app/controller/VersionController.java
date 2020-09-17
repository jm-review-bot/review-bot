package spring.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.dto.ThemeDto;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/version")
public class VersionController {

    @GetMapping
    public ResponseEntity<String> getAllThemes() {
        return ResponseEntity.ok("1.0.0");
    }
}
