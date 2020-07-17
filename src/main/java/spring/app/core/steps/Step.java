package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.UserService;

import static spring.app.util.Keyboards.*;

public abstract class Step {

    private final String text;
    private final String keyboard;
    @Autowired
    private UserService userService;

    public Step(String text, String keyboard) {
        this.text = text;
        this.keyboard = keyboard;
    }

    /**
     * Аналог get-метода. Выводит сообщение
     * пользователю, после корректного ввода с предыдущего(либо текущего) шага
     *
     * @param context относительно какого контекста выводится сообщение
     */
    public abstract void enter(BotContext context);

    /**
     * Аналог post-метода. Обрабатывает ответ пользователя.
     * В случае если ответ некорректен - бросает исключение,
     * и ожидает корректного ввода.
     * Если ответ корректен - переводит пользователя на следующий(либо текущий) шаг
     *
     * @param context относительно какого контекста выводится сообщение
     * @throws ProcessInputException     некорректный ввод
     * @throws NoNumbersEnteredException некорректное число
     * @throws NoDataEnteredException    некорректная дата
     */
    public abstract void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException;

    /**
     * @return Текст сообщения для пользователя, независимо от его состояния
     */
    public String getText() {
        return text;
    }

    /**
     * @return набор кнопок для пользователя, независимо от его состояния
     */
    public String getKeyboard() {
        return keyboard;
    }

    /**
     * Составляет итоговое сообщение. Реализация по умолчанию
     * подразумевает объекдинение результатов методов
     * {@link Step#getText()} и {@link Step#getDynamicText(BotContext context)} ()}
     *
     * @param context в каком контексте составляется динамическая часть сообщения
     * @return
     */
    public String getComposeText(BotContext context) {
        return text + getDynamicText(context);
    }

    /**
     * Составляет итоговый набор кнопок. Реализация по умолчанию
     * подразумевает объединение результатов методов
     * {@link Step#getHeaderKeyboardString()}
     * {@link Step#getKeyboard()}
     * {@link Step#getDynamicKeyboard(BotContext context)}
     * {@link Step#getFooterKeyboardString()} последовательно.
     *
     * @param context в каком контексте составляется динамическая часть кнопок
     * @return
     */
    public String getComposeKeyboard(BotContext context) {

        String buttons = keyboard + getDynamicKeyboard(context);

        StringBuilder result = new StringBuilder(getHeaderKeyboardString());
        // Добавляем дополнительные скобки, если добавляются статические или динамические кнопки
        if (!buttons.isEmpty()) {
            result
                    .append("[\n")
                    .append(buttons)
                    .append("]\n");
        } else {
            result.append(buttons);
        }
        result.append(getFooterKeyboardString());

        return result.toString();
    }

    /**
     * Составляет динамический текст для пользователя.
     *
     * @param context
     * @return
     */
    public abstract String getDynamicText(BotContext context);

    /**
     * Составляет динамический набор кнопок для пользователя. В случае
     * необходимости может обращаться к методу
     * {@link Step#getRowDelimiterString()} возвращающему разделитель строк для кнопок
     *
     * @param context
     * @return
     */
    public abstract String getDynamicKeyboard(BotContext context);

    private String getHeaderKeyboardString() {
        return HEADER_FR;
    }

    /**
     * Возвращает набор символов воспринимаемых как разделитель строк
     *
     * @return
     */
    public String getRowDelimiterString() {
        return ROW_DELIMETER_FR;
    }

    public String getColumnDelimiterString() {
        return COLUMN_DELIMETER_FR;
    }

    private String getFooterKeyboardString() {
        return FOOTER_FR;
    }

    /**
     * Отправляет пользователя на следующий шаг
     *
     * @param context  для определения юзера
     * @param nextStep на какой шаг
     */
    public void sendUserToNextStep(BotContext context, StepSelector nextStep) {
        User user = context.getUser();
        user.setChatStep(nextStep);
        userService.updateUser(user);
    }
}
