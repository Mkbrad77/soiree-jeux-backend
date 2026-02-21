package com.soiree.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Convertit DATABASE_URL au format "postgresql://user:pass@host/db" (Neon/Render)
 * en URL JDBC valide (jdbc:postgresql://host:port/db?user=...&password=...).
 */
public class JdbcUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String DATABASE_URL_KEY = "DATABASE_URL";
    private static final String SPRING_DATASOURCE_URL = "spring.datasource.url";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = environment.getProperty(DATABASE_URL_KEY);
        if (databaseUrl == null) {
            databaseUrl = System.getenv(DATABASE_URL_KEY);
        }
        if (databaseUrl == null || !databaseUrl.startsWith("postgresql://") && !databaseUrl.startsWith("postgres://")) {
            return;
        }
        try {
            String jdbcUrl = toJdbcUrl(databaseUrl);
            Map<String, Object> map = new HashMap<>();
            map.put(SPRING_DATASOURCE_URL, jdbcUrl);
            environment.getPropertySources().addFirst(new MapPropertySource("jdbcUrlOverride", map));
        } catch (Exception e) {
            throw new IllegalStateException("Impossible de convertir DATABASE_URL en URL JDBC", e);
        }
    }

    private static String toJdbcUrl(String databaseUrl) throws Exception {
        String uriStr = databaseUrl
                .replace("postgresql://", "http://")
                .replace("postgres://", "http://");
        URI uri = new URI(uriStr);
        String host = uri.getHost();
        int port = uri.getPort() > 0 ? uri.getPort() : 5432;
        String path = uri.getPath();
        String db = path != null && path.length() > 1 ? path.substring(1) : "neondb";
        String query = uri.getQuery();

        StringBuilder jdbc = new StringBuilder();
        jdbc.append("jdbc:postgresql://").append(host).append(":").append(port).append("/").append(db);
        jdbc.append("?sslmode=require");
        if (query != null && !query.isEmpty()) {
            for (String param : query.split("&")) {
                if (param.startsWith("sslmode=")) continue;
                jdbc.append("&").append(param);
            }
        }
        if (uri.getUserInfo() != null) {
            String[] userInfo = uri.getUserInfo().split(":", 2);
            String user = userInfo[0];
            String pass = userInfo.length > 1 ? userInfo[1] : "";
            jdbc.append("&user=").append(URLEncoder.encode(user, StandardCharsets.UTF_8));
            jdbc.append("&password=").append(URLEncoder.encode(pass, StandardCharsets.UTF_8));
        }
        return jdbc.toString();
    }
}
