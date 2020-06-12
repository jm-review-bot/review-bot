package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.ThemeService;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.util.*;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;
import static spring.app.util.Keyboards.DELETE_STUDENT_REVIEW;

@Component
public class UserPassReviewAddTheme extends Step {
    public UserPassReviewAddTheme() {
        //у шага нет статического текста, но есть статические(видимые независимо от юзера) кнопки
        super("", DEF_BACK_KB);
    }

    @Override
    public void enter(BotContext context) {
        StringBuilder themeList = new StringBuilder();
        themeList.append("Выберите тему, которые вы хотите сдать, в качестве ответа пришлите цифру (номер темы):\n\n");
        for (Theme theme : context.getThemeService().getAllThemes()) {
            themeList.append("[")
                    .append(theme.getPosition())
                    .append("] ")
                    .append(theme.getTitle())
                    .append(", стоимость ")
                    .append(theme.getReviewPoint())
                    .append(" RP")
                    .append("\n");
        }
        context.getStorageService().updateUserStorage(context.getVkId(),USER_PASS_REVIEW_ADD_THEME,Arrays.asList(themeList.toString()));
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        Integer vkId = context.getVkId();
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        StudentReview studentReview = context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId());
        //если записи на ревью нету, значит ожидаем номер темы
        if (studentReview == null && StringParser.isNumeric(currentInput)) {
            Integer command = StringParser.toNumbersSet(currentInput).iterator().next();
            Theme selectedTheme = context.getThemeService().getAllThemes().stream().filter(theme -> theme.getPosition().equals(command)).findFirst().orElse(null);
            //проверяем или номер темы не выходит за рамки
            if (selectedTheme != null) {
                User user = context.getUser();
                //проверяем хватает ли РП для сдачи выбранной темы
                if (selectedTheme.getReviewPoint() <= user.getReviewPoint()) {
                    //сохраняем ID темы для следующего шага
                    List<String> list = new ArrayList<>();
                    list.add(selectedTheme.getId().toString());
                    storageService.updateUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME, list);
                    sendUserToNextStep(context, USER_PASS_REVIEW_GET_LIST_REVIEW);
                } else {
                    throw new ProcessInputException("У Вас не хватает РП. Для того чтобы заработать нужное " +
                            "количество РП, необходимо провести ревью.");
                }
            } else {
                throw new ProcessInputException("Введен неверный номер темы...");
            }
        } else {
            //определяем нажатую кнопку или сообщаем о неверной команде
            String command = StringParser.toWordsArray(currentInput)[0];
            if ("отмена".equals(command) && studentReview != null) {
                sendUserToNextStep(context, USER_CANCEL_REVIEW);
            } else if ("/start".equals(command)) {
                sendUserToNextStep(context, START);
            } else if ("назад".equals(command)) {
                sendUserToNextStep(context, USER_MENU);
            } else {
                throw new ProcessInputException("Введена неверная команда...");
            }
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        StudentReview studentReview = context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId());
        if (studentReview != null) {
            return String.format("Вы уже записаны на ревью:\n" +
                            "Тема: %s\n" +
                            "Дата: %s\n" +
                            "Вы можете отменить запись на это ревью, нажав на кнопку “отмена записи”", studentReview.getReview().getTheme().getTitle(),
                    StringParser.localDateTimeToString(studentReview.getReview().getDate()));
        } else {
            return context.getStorageService().getUserStorage(context.getVkId(),USER_PASS_REVIEW_ADD_THEME).get(0);
        }
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        StudentReview studentReview = context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId());
        if(studentReview != null) {
            StringBuilder keys = new StringBuilder();
            keys
                    .append(this.getRowDelimiterString())
                    .append(DELETE_STUDENT_REVIEW);
            return keys.toString();
        } else {
            return "";
        }
    }
}