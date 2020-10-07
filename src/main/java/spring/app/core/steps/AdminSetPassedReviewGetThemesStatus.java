package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.StudentReviewService;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS;
import static spring.app.core.StepSelector.ADMIN_SET_PASSED_REVIEW;
import static spring.app.core.StepSelector.ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class AdminSetPassedReviewGetThemesStatus extends Step {

    private final StorageService storageService;
    private final ThemeService themeService;
    private final UserService userService;
    private final StudentReviewService studentReviewService;

    public AdminSetPassedReviewGetThemesStatus(StorageService storageService,
                                               ThemeService themeService,
                                               UserService userService,
                                               StudentReviewService studentReviewService) {
        super("", DEF_BACK_KB);
        this.storageService = storageService;
        this.themeService = themeService;
        this.userService = userService;
        this.studentReviewService = studentReviewService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        if (StringParser.isNumeric(command)) {
            int themeNumber = Integer.parseInt(command);
            List<String> idList = storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS); // При выводе сообщения пользователю в текущем шаге были сохранены ID выведенных в сообщении тем
            if (themeNumber < 1 || themeNumber > idList.size()) {
                throw new ProcessInputException("Введите подходящее число...");
            }
            Theme theme = themeService.getThemeById(Long.parseLong(idList.get(themeNumber - 1)));
            if (theme == null) { // Проверка на случай, что тему могли уже удалить из БД к текущему моменту.
                throw new ProcessInputException("Возможно, тема уже удалена из БД. Вернитесь назад и попробуйте еще раз.");
            }
            storageService.updateUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS, Arrays.asList(theme.getId().toString()));
            sendUserToNextStep(context, ADMIN_SET_PASSED_REVIEW);
        } else if (command.equals("Назад")) {
            storageService.removeUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS);
            sendUserToNextStep(context, ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    /* Этот метод выводит сообщение, содержащие список всех существующих тем в БД и помечает их статус (пройдена или нет) для выбранного
     * в предыдущем шаге студента. Ввиду того, что состояние БД может измениться в процессе выбора, в текущий шаг сохраняется информация
     * обо всех выведенных админу тем в виде List<String>, содержащего в себе ID тем в том же порядке, в котором они представлены в информационном
     * сообщении. Это также поможет в следующих шагах избежать NPE в случае, когда админ выбрал тему, а ее в последствии удалили из БД. */
    @Override
    public String getDynamicText(BotContext context) {
        List<Theme> allThemes = themeService.getAllThemes();
        Long studentId = Long.parseLong(storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST).get(0));
        User student = userService.getUserById(studentId);
        List<String> idList = new ArrayList<>();
        StringBuilder infoMessage = new StringBuilder(String.format(
                "Студент %s %s. Выберите тему, которую вы хотите сделать пройденной. По данной теме, а также по всем предыдущим темам будут созданы ревью со статусом \"Пройдено\". Проверяющим по данным ревью будет назначен проверяющий по умолчанию.\n" +
                        "Список тем:\n\n",
                student.getFirstName(),
                student.getLastName()
        ));
        for (int i = 0; i < allThemes.size(); i++) {
            Theme theme = allThemes.get(i);
            infoMessage
                    .append("[")
                    .append(i + 1)
                    .append("] ")
                    .append(theme.getTitle())
                    .append(" (Статус: ")
                    .append(studentReviewService.isThemePassedByStudent(studentId, theme.getId()) ? "пройдено" : "не пройдено")
                    .append(")\n");
            idList.add(theme.getId().toString());
        }
        storageService.updateUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS, idList);
        return infoMessage.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}
