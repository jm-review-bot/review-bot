package spring.app.core;

import org.springframework.stereotype.Component;

@Component
public enum BotState {

    Start(false) {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(
                    "Привет! Этот Бот создан для прохождения ревью. \n" +
                            "Введи команду /admin для перехода в админку.",
                    context.getVkId()
            );
        }

        @Override
        public void processInput(BotContext context) {
        }

        @Override
        public BotState nextState() {
            return StartInput;
        }

        @Override
        public BotState prevState() {
            return Start;
        }
    },

    StartInput {
        private BotState next;

        @Override
        public void enter(BotContext context) {

        }

        @Override
        public void processInput(BotContext context) {
            // вызываем правильный внешний обработчик команд (пока его нет)
            String[] words = context.getInput().trim().split(" ");
            String command = words[0];
            if (command.equals("/admin")) {
                next = AdminMenu;
            } else {
                context.getBot().sendMessage(
                        "Введена неверная команда",
                        context.getVkId()
                );
            }

        }

        @Override
        public BotState nextState() {
            return AdminMenu;
        }

        @Override
        public BotState prevState() {
            return StartInput;
        }
    },

    AdminMenu {
        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(
                    "Ты в админке",
                    context.getVkId());
        }

        @Override
        public void processInput(BotContext context) {

        }

        @Override
        public BotState nextState() {
            return AdminMenu;
        }

        @Override
        public BotState prevState() {
            return Start;
        }
    }
    ;
//    AdminAdd {},
//    AdminDelete {},
//    AdminDeleteCheck {}
    private final boolean inputNeeded;

    BotState() {
        this.inputNeeded = true;
    }

    BotState(boolean inputNeeded) {
        this.inputNeeded = inputNeeded;
    }
    // абстрактные методы которые должны быть переопределены в каждом BotState
    public abstract void enter(BotContext context);
    public abstract void processInput(BotContext context);
    public abstract BotState nextState();
    public abstract BotState prevState();

    public boolean isInputNeeded() {
        return inputNeeded;
    }
}
