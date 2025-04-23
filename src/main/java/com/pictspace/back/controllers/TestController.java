package com.pictspace.back.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db")
    public String testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return "Database connection successful!";
        } catch (SQLException e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
} 