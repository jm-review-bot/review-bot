package spring.app.core;

import org.springframework.stereotype.Component;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.util.Keyboards;

import java.util.Comparator;

@Component
public enum BotStep {

    Start() {
        private BotStep nextStep;

        @Override
        public void enter(BotContext context) {
            text = "Привет! Этот Бот создан для прохождения ревью. \nНажми \"Начать\" для запуска.";
            keyboard = Keyboards.start;
            if (context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
                text = "Привет! Этот Бот создан для прохождения ревью. " +
                        "\nНажми \"Начать\" для запуска или введи команду /admin для перехода в админку.";
            }
        }

        @Override
        public void processInput(BotContext context) throws ProcessInputException {
            // вызываем правильный внешний обработчик команд (пока его нет)
            String[] words = context.getInput().trim().split(" ");
            String command = words[0];
            if (command.equals("/admin")
                    && context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
                nextStep = AdminMenu;
            } else if (command.equals("Начать")) {
                nextStep = UserMenu;
            } else if (command.equals("/start")) {
                nextStep = Start;
            } else {
                keyboard = Keyboards.noKeyboard;
                throw new ProcessInputException("Введена неверная команда...");
            }
        }

        @Override
        public BotStep nextStep() {
            return nextStep;
        }
    },

    UserMenu {
        private BotStep nextStep;

        @Override
        public void enter(BotContext context) {
            // TODO проверку из ТЗ
            text = "Пользователь с таким vk id не найден в базе. Обратитесь к Герману Севостьянову или Станиславу Сорокину";
            keyboard = Keyboards.defaultKeyboard;
        }

        @Override
        public void processInput(BotContext context) throws ProcessInputException {

        }

        @Override
        public BotStep nextStep() {
            return null;
        }
    },

    AdminMenu {
        private BotStep nextStep;

        @Override
        public void enter(BotContext context) {
            text = "Привет %username%! Ты в админке";
            keyboard = Keyboards.adminMenu;
        }

        @Override
        public void processInput(BotContext context) throws ProcessInputException {
            String[] words = context.getInput().trim().split(" ");
            String command = words[0];
            if ("Добавить".equals(command)) {
                nextStep = AdminAddUser;
            } else if ("Удалить".equals(command)) {
                nextStep = AdminRemoveUser;
            } else if ("/start".equals(command)) {
                nextStep = Start;
            } else {
                throw new ProcessInputException("Введена неверная команда...");
            }
        }

        @Override
        public BotStep nextStep() {
            return nextStep;
        }
    },

    AdminAddUser {
        private BotStep nextStep;

        @Override
        public void enter(BotContext context) {
            text = "Тут будем добавлять юзеров";
        }

        @Override
        public void processInput(BotContext context) throws ProcessInputException {

        }

        @Override
        public BotStep nextStep() {
            return null;
        }
    },

    AdminRemoveUser {
        private BotStep nextStep;

        @Override
        public void enter(BotContext context) {
            StringBuilder userList = new StringBuilder("Вот список всех пользователей. Для удаления, напиши vkId одного пользователя или нескольких пользователей через пробел.\n\n");
            context.getUserService().getAllUsers().stream()
                    .filter(user -> !user.getRole().isAdmin())
                    .sorted(Comparator.comparing(User::getLastName))
                    .forEach(user -> userList
                            .append(user.getLastName())
                            .append(" ")
                            .append(user.getFirstName())
                            .append(". vkId ")
                            .append(user.getVkId())
                            .append("\n")
                    );
            text = userList.toString();
        }

        @Override
        public void processInput(BotContext context) throws ProcessInputException {

        }

        @Override
        public BotStep nextStep() {
            return null;
        }
    };

//    AdminDeleteCheck {}
//    private final boolean inputNeeded;

    private static String text = "Тут должен быть осмысленный текст, но что-то пошло не так...";
    private static String keyboard = Keyboards.noKeyboard;

    // абстрактные методы которые должны быть переопределены в каждом BotStep
    public abstract void enter(BotContext context);

    public abstract void processInput(BotContext context) throws ProcessInputException;

    public abstract BotStep nextStep();

    public String getText() {
        return text;
    }

    public String getKeyboard() {
        return keyboard;
    }
}
