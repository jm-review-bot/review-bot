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

    private Set<Integer> idsIfCancelStudentReview = new HashSet<>();
    private Map<Integer, Theme> themes = new HashMap<>();

    @Override
    public void enter(BotContext context) {
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
            //проверяем или номер темы не выходит за рамки
            if (command > 0 & command <= themes.size()) {
                Theme theme = context.getThemeService().getByPosition(command);
                User user = context.getUser();
                //проверяем хватает ли РП для сдачи выбранной темы
                if (theme.getReviewPoint() <= user.getReviewPoint()) {
                    //сохраняем ID темы для следующего шага
                    List<String> list = new ArrayList<>();
                    list.add(theme.getId().toString());
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
                context.getStudentReviewService().deleteStudentReviewById(studentReview.getId());
                idsIfCancelStudentReview.add(vkId);
                sendUserToNextStep(context, USER_PASS_REVIEW_ADD_THEME);
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
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        StudentReview studentReview = context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId());
        if (studentReview != null) {
            return String.format("Вы уже записаны на ревью:\n" +
                            "Тема: %s\n" +
                            "Дата: %s\n" +
                            "Вы можете отменить запись на это ревью, нажав на кнопку “отмена записи”", studentReview.getReview().getTheme().getTitle(),
                    StringParser.localDateTimeToString(studentReview.getReview().getDate()));
        } else {
            StringBuilder themeList = new StringBuilder();
            if (storageService.getUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME) != null){
                themeList.append("Выбранное Вами ревью уже заполнено!\n\n");
                storageService.removeUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME);
            }
            //формирую список тем и вывожу его как нумерованный список
            context.getThemeService().getAllThemes().forEach(theme -> themes.putIfAbsent(theme.getPosition(), theme));
            themeList.append("Выберите тему, которые вы хотите сдать, в качестве ответа пришлите цифру (номер темы):\n\n");
            for (Integer position : themes.keySet()) {
                themeList.append("[")
                        .append(position)
                        .append("] ")
                        .append(themes.get(position).getTitle())
                        .append(", стоимость ")
                        .append(themes.get(position).getReviewPoint())
                        .append(" RP")
                        .append("\n");
            }
            if (idsIfCancelStudentReview.contains(vkId)) {
                idsIfCancelStudentReview.remove(vkId);
                return new StringBuilder("Ревью отменено.\n\n").append(themeList).toString();
            } else {
                return themeList.toString();
            }
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