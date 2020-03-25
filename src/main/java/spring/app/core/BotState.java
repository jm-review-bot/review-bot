package spring.app.core;

import org.springframework.stereotype.Component;

@Component
public enum BotState {
    Start {},
    AdminMenu {},
    AdminAdd {},
    AdminDelete {},
    AdminDeleteCheck {}
}
