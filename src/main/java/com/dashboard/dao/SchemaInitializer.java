package com.dashboard.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SchemaInitializer {

    public static void initialize() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = new String(Files.readAllBytes(Paths.get("src/main/resources/schema.sql")));
            stmt.executeUpdate(sql);

            System.out.println("✅ DB schema created or already exists.");

        } catch (Exception e) {
            System.err.println("❌ Failed to initialize schema: " + e.getMessage());
        }
    }
}
