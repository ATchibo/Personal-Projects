package com.example.assignment1_1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ScriptController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/api/run-script")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> runScript() {
        try {
            ClassPathResource resource = new ClassPathResource("sql_queries/reset_data.sql");
            byte[] content = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String script = new String(content);

            jdbcTemplate.execute(script);

            return ResponseEntity.ok("Script executed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to execute script: " + e.getMessage());
        }
    }
}