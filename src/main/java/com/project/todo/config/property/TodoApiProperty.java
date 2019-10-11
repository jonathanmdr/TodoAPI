package com.project.todo.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("todoapi")
public class TodoApiProperty {

    private String originPermited = "http://localhost:8100";
    private final Security security = new Security();

    @Getter
    @Setter
    public class Security {
        private boolean enableHttps;
    }

}
