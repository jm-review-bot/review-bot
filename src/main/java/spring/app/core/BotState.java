package spring.app.core;

import org.springframework.stereotype.Component;

@Component
public enum BotState {

    Start {
        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage("Привет! Этот Бот создан для прохождения ревью. Введи команду /admin перехода в админку.",
                    context.getVkId());
        }

        @Override
        public BotState nextState() {
            return AdminMenu;
        }

        @Override
        public BotState prevState() {
            return Start;
        }
    },
    AdminMenu {},
    AdminAdd {},
    AdminDelete {},
    AdminDeleteCheck {}

    // абстрактные методы которые должны быть переопределены в каждом BotState
    public abstract void enter(BotContext context);
    public abstract BotState nextState();
    public abstract BotState prevState();
}
