package spring.app.core;

import org.springframework.stereotype.Component;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.Keyboards;

@Component
public enum BotStep {

    Start() {
        private BotStep nextStep;
        private BotStep prevStep;

        @Override
        public void enter(BotContext context) {
            text = "Привет! Этот Бот создан для прохождения ревью. \nВведи команду /admin для перехода в админку.";
            keyboard = Keyboards.noKeyboard;
        }

        @Override
        public void processInput(BotContext context) throws ProcessInputException {
            // вызываем правильный внешний обработчик команд (пока его нет)
            String[] words = context.getInput().trim().split(" ");
            String command = words[0];
            if (command.equals("/admin")) {
                nextStep = AdminMenu;
            } else {
                nextStep = Start;
                throw new ProcessInputException("Введена неверная команда...");
            }
        }

        @Override
        public BotStep nextStep() {
            return nextStep;
        }

        @Override
        public BotStep prevStep() {
            return Start;
        }
    },

    AdminMenu {
        @Override
        public void enter(BotContext context) {
            text = "Ты в админке";
            keyboard = Keyboards.defaultKeyboard; //TODO change to noKey
        }

        @Override
        public void processInput(BotContext context) {

        }

        @Override
        public BotStep nextStep() {
            return AdminMenu;
        }

        @Override
        public BotStep prevStep() {
            return Start;
        }
    }
    ;
    private static String text;
    private static String keyboard;

    //    AdminAdd {},
//    AdminDelete {},
//    AdminDeleteCheck {}
//    private final boolean inputNeeded;
//
//    BotStep() {
//        this.inputNeeded = true;
//    }
//
//    BotStep(boolean inputNeeded) {
//        this.inputNeeded = inputNeeded;
//    }
    // абстрактные методы которые должны быть переопределены в каждом BotStep
    public abstract void enter(BotContext context);
    public abstract void processInput(BotContext context) throws ProcessInputException;
    public abstract BotStep nextStep();
    public abstract BotStep prevStep();

    public String getText() {
        return text;
    }

    public String getKeyboard() {
        return keyboard;
    }

//    public boolean isInputNeeded() {
//        return inputNeeded;
//    }
}
