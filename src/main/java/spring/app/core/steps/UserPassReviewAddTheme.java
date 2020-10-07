package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.StudentReviewService;
import spring.app.service.abstraction.ThemeService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static spring.app.core.StepSelector.USER_PASS_REVIEW_ADD_THEME;
import static spring.app.core.StepSelector.START;
import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.core.StepSelector.USER_CANCEL_REVIEW;
import static spring.app.core.StepSelector.USER_PASS_REVIEW_GET_LIST_REVIEW;
import static spring.app.util.Keyboards.DEF_BACK_KB;
import static spring.app.util.Keyboards.DELETE_STUDENT_REVIEW;

@Component
public class UserPassReviewAddTheme extends Step {

    private final StorageService storageService;
    private final ThemeService themeService;
    private final StudentReviewService studentReviewService;

    public UserPassReviewAddTheme(StorageService storageService, ThemeService themeService,
                                  StudentReviewService studentReviewService) {
        //у шага нет статического текста, но есть статические(видимые независимо от юзера) кнопки
        super("", DEF_BACK_KB);
        this.storageService = storageService;
        this.themeService = themeService;
        this.studentReviewService = studentReviewService;
    }

    @Override
    public void enter(BotContext context) {
        StringBuilder themeList = new StringBuilder();
        themeList.append("Выберите тему, которые вы хотите сдать, в качестве ответа пришлите цифру (номер темы):\n\n");
        for (Theme theme : themeService.getAllThemes()) {
            String themeRow = String.format(
                    "[%d] %s, стоимость %d RP \n",
                    theme.getPosition(), theme.getTitle(), theme.getReviewPoint()
            );
            themeList.append(themeRow);
        }
        storageService.updateUserStorage(context.getVkId(), USER_PASS_REVIEW_ADD_THEME, Arrays.asList(themeList.toString()));
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        Integer vkId = context.getVkId();
        String currentInput = context.getInput();
        Optional<StudentReview> studentReviewOptional = studentReviewService.getStudentReviewIfAvailableAndOpen(context.getUser().getId());
        //если записи на ревью нету, значит ожидаем номер темы
        if (!studentReviewOptional.isPresent() && StringParser.isNumeric(currentInput)) {
            Integer command = StringParser.toNumbersSet(currentInput).iterator().next();
            Theme selectedTheme = themeService.getAllThemes().stream().filter(theme -> theme.getPosition().equals(command)).findFirst().orElse(null);
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
            if ("отмена".equals(command) && studentReviewOptional.isPresent()) {
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
        Optional<StudentReview> studentReviewOptional = studentReviewService.getStudentReviewIfAvailableAndOpen(context.getUser().getId());
        if (studentReviewOptional.isPresent()) {
            StudentReview studentReview = studentReviewOptional.get();
            return String.format("Вы уже записаны на ревью:\n" +
                            "Тема: %s\n" +
                            "Дата: %s\n" +
                            "Вы можете отменить запись на это ревью, нажав на кнопку “отмена записи”", studentReview.getReview().getTheme().getTitle(),
                    StringParser.localDateTimeToString(studentReview.getReview().getDate()));
        } else {
            return storageService.getUserStorage(context.getVkId(), USER_PASS_REVIEW_ADD_THEME).get(0);
        }
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        Optional<StudentReview> studentReviewOptional = studentReviewService.getStudentReviewIfAvailableAndOpen(context.getUser().getId());
        if (studentReviewOptional.isPresent()) {
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