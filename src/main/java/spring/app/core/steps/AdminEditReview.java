package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Question;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_AND_EDIT_STATUS_KB;
import static spring.app.util.Keyboards.BACK_KB;

import spring.app.util.StringParser;

/**
 * Многоступенчатый шаг, для редактирования ревью
 * пользователей админом.
 * ADMIN_EDIT_REVIEW->ADMIN_USERS_LIST->ADMIN_THEME_LIST->ADMIN_REVIEW_LIST->ADMIN_CHANGE_REVIEW
 *
 * @author AkiraRokudo on 13.05.2020 in one of sun day
 */
@Component
public class AdminEditReview extends Step {

    private boolean reviewChanged;
    private boolean reviewSelected;

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();

        StringBuilder stringBuilder = new StringBuilder();
        if (storageService.getUserStorage(vkId, ADMIN_USERS_LIST) == null) {
            //1 вариант - мы только попали на шаг - предложим список пользователей
            stringBuilder.append("Выберите пользователя, ревью по которому вы хотите редактировать\n");
            List<String> userToReviewChange = new ArrayList<>();
            final int[] userCounter = {1}; //обход финальности для лямбды
            context.getUserService()
                    .getAllUsers().stream()
                    .sorted(Comparator.comparing(User::getLastName))
                    .forEach(user -> {
                        stringBuilder.append("[").append(userCounter[0]++).append("] ")
                                .append(user.getLastName())
                                .append(" ")
                                .append(user.getFirstName())
                                .append(", https://vk.com/id")
                                .append(user.getVkId());
                        if (user.getRole().isAdmin()) {
                            stringBuilder.append(" (админ)");
                        }
                        stringBuilder.append("\n");
                        // сохраняем ID юзера в лист
                        userToReviewChange.add(user.getId().toString());
                    });
            text = stringBuilder.toString();
            keyboard = BACK_KB;
            storageService.updateUserStorage(vkId, ADMIN_USERS_LIST, userToReviewChange);
        } else if (storageService.getUserStorage(vkId, ADMIN_THEME_LIST) == null) {
            //2 вариант - мы имеем пользователя - предложим тему
            stringBuilder.append("Выберите тему ревью\n");
            List<String> themesToReview = new ArrayList<>();
            context.getThemeService()
                    .getAllThemes().stream()
                    .forEach(theme -> {
                        stringBuilder.append("[").append(theme.getPosition()).append("] ")
                                .append(theme.getTitle());
                        stringBuilder.append("\n");
                        // сохраняем ID юзера в лист
                        themesToReview.add(theme.getPosition().toString());
                    });
            text = stringBuilder.toString();
            keyboard = BACK_KB;
            storageService.updateUserStorage(vkId, ADMIN_THEME_LIST, themesToReview);
        } else if (storageService.getUserStorage(vkId, ADMIN_REVIEW_LIST) == null) {
            //3 вариант - мы имеем пользователя и тему - предложим пройденные пользователем ревью
            String selectedUserVKIds = storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0);
            Long selectedUserVKId = Long.parseLong(selectedUserVKIds);
            String selectedThemePosition = storageService.getUserStorage(vkId, ADMIN_THEME_LIST).get(0);
            Theme selectedTheme = context.getThemeService().getByPosition(Integer.parseInt(selectedThemePosition));
            stringBuilder.append("Выберите ревью для редактирования\n");
            List<String> reviewToChange = new ArrayList<>();
            final int[] reviewCounter = {1}; //обход финальности для лямбды
            context.getStudentReviewService()
                    .getAllStudentReviewsByStudentVkIdAndTheme(selectedUserVKId, selectedTheme).stream()
                    .forEach(sreview -> {
                        stringBuilder.append("[").append(reviewCounter[0]++).append("] ")
                                .append(sreview.getReview().getDate())
                                .append(" (");
                        if (!sreview.getPassed()) {
                            stringBuilder.append("не ");
                        }
                        stringBuilder.append("пройдено)");
                        stringBuilder.append("\n");
                        // сохраняем ID юзера в лист
                        reviewToChange.add(sreview.getId().toString());
                    });
            text = stringBuilder.toString();
            keyboard = BACK_KB;
            storageService.updateUserStorage(vkId, ADMIN_REVIEW_LIST, reviewToChange);
        } else if (storageService.getUserStorage(vkId, ADMIN_CHANGE_REVIEW) == null) {
            //4 вариант - мы имеем пользователя,тему и ревью - выведем по нему информацию и предложим изменить
            String sStudentReviewToChange = storageService.getUserStorage(vkId, ADMIN_REVIEW_LIST).get(0);
            StudentReview studentReview = context.getStudentReviewService().getStudentReviewById(Long.parseLong(sStudentReviewToChange));
            List<String> selectedReviewList = new ArrayList<>();
            stringBuilder
                    .append("Ревью по теме '").append(studentReview.getReview().getTheme().getTitle()).append("' ").append("за ").append(studentReview.getReview().getDate()).append(".\n")
                    .append("Студент: ").append(studentReview.getUser().getFirstName()).append(" ").append(studentReview.getUser().getLastName()).append("\n")
                    .append("Принимающий: ").append(studentReview.getReview().getUser().getFirstName()).append(" ").append(studentReview.getReview().getUser().getLastName()).append("\n");
            context.getStudentReviewAnswerService()
                    .getStudentReviewAnswersByStudentReviewId(studentReview.getId()).stream()
                    .sorted(Comparator.comparingInt(sra -> sra.getQuestion().getPosition())) //TODO:проверить необходимость сортировки
                    .forEach(sra -> {
                        stringBuilder
                                .append(sra.getQuestion().getPosition())
                                .append(". ")
                                .append(sra.getQuestion().getQuestion())
                                .append(" ")
                                .append(sra.getRight() ? "+" : "-")
                                .append("\n");
                    });
            selectedReviewList.add(sStudentReviewToChange);
            text = stringBuilder.toString();
            keyboard = BACK_AND_EDIT_STATUS_KB;
            storageService.updateUserStorage(vkId, ADMIN_CHANGE_REVIEW, selectedReviewList);
        } else if (reviewSelected) {
            stringBuilder.append("Выберите статус\n")
                    .append("[1] Пройдено\n")
                    .append("[2] Не пройдено\n");
            text = stringBuilder.toString();
            keyboard = BACK_KB;
        }
        //если мы вернулись в шаг "ревью пользователя по теме" после изменения ревью
        if (reviewChanged) {
            text = "Статус ревью успешно изменен.\n\n" + text;
            reviewChanged = false;
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        //возможны 3 варианта. пришло назад, изменить, и номер
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        StepSelector currentStep = getCurrentStep(context);
        if (wordInput.equals("назад")) {
            //если назад - смотрим последний шаг,и откатываем один селект.
            storageService.removeUserStorage(vkId, currentStep);
            removePrevStorage(storageService, vkId, currentStep);
            nextStep = ADMIN_USERS_LIST == currentStep ? ADMIN_MENU : ADMIN_EDIT_REVIEW;
        } else if (wordInput.equals("изменить") && storageService.getUserStorage(vkId, ADMIN_CHANGE_REVIEW) != null) {
            reviewSelected = true;
            nextStep = ADMIN_EDIT_REVIEW;
        } else if (StringParser.isNumeric(wordInput)) {
            Integer selectedNumber = Integer.parseInt(wordInput);
            boolean storageUpdate = false;
            if (currentStep == ADMIN_USERS_LIST) {
                //значит мы выбрали пользователя. Обновим коллекцию
                updateStorage(storageService, vkId, ADMIN_USERS_LIST, selectedNumber);
                nextStep = ADMIN_EDIT_REVIEW;
            }
            if (currentStep == ADMIN_THEME_LIST) {
                //значит мы выбрали тему. Обновим коллекцию
                updateStorage(storageService, vkId, ADMIN_THEME_LIST, selectedNumber);
                nextStep = ADMIN_EDIT_REVIEW;
            }
            if (currentStep == ADMIN_REVIEW_LIST) {
                //значит мы выбрали ревью. Обновим коллекцию
                updateStorage(storageService, vkId, ADMIN_REVIEW_LIST, selectedNumber);
                nextStep = ADMIN_EDIT_REVIEW;
            }
            if (currentStep == ADMIN_CHANGE_REVIEW) {
                if (selectedNumber == 1 || selectedNumber == 2) {
                    String selectedStudentReview = storageService.getUserStorage(vkId, ADMIN_CHANGE_REVIEW).get(0);
                    StudentReview studentReview = context.getStudentReviewService().getStudentReviewById(Long.parseLong(selectedStudentReview));
                    if (studentReview.getPassed() && selectedNumber == 1 ||
                            !studentReview.getPassed() && selectedNumber == 2) {
                        throw new ProcessInputException("Ревью уже находится в этом статусе");
                    } else {
                        studentReview.setPassed(selectedNumber == 1);
                        reviewSelected = false;
                        reviewChanged = true;
                        storageService.removeUserStorage(vkId, ADMIN_REVIEW_LIST);
                        storageService.removeUserStorage(vkId, ADMIN_CHANGE_REVIEW); //особой нужды в этом нет, перестраховка
                    }
                } else {
                    throw new ProcessInputException("Введена неверная команда...");
                }
            }
            if (nextStep == null) {
                nextStep = ADMIN_EDIT_REVIEW;
                throw new ProcessInputException("Текущий шаг не позволяет вводить числа");
            }
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    /**
     * @return Последний шаг. В качестве ориентира смотрит по последнему незаполненному хранилищу
     */
    private StepSelector getCurrentStep(BotContext context) {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        //ADMIN_EDIT_REVIEW->ADMIN_USERS_LIST->ADMIN_THEME_LIST->ADMIN_REVIEW_LIST->ADMIN_CHANGE_REVIEW
        if (storageService.getUserStorage(vkId, ADMIN_THEME_LIST) == null) {
            return ADMIN_USERS_LIST;
        }
        if (storageService.getUserStorage(vkId, ADMIN_REVIEW_LIST) == null) {
            return ADMIN_THEME_LIST;
        }
        if (storageService.getUserStorage(vkId, ADMIN_CHANGE_REVIEW) == null) {
            return ADMIN_REVIEW_LIST;
        }
        return ADMIN_CHANGE_REVIEW; //значит что все хранилища выбраны и мы смотрим ревью
    }

    private boolean updateStorage(StorageService storageService, Integer vkId, StepSelector storageStep, Integer selectedNumber) throws ProcessInputException {
        List<String> userList = storageService.getUserStorage(vkId, storageStep);
        if (selectedNumber <= 0 || selectedNumber > userList.size()) {
            throw new ProcessInputException("Введен некорректный номер пользователя");
        }
        String selectedValue = userList.get(selectedNumber - 1);
        userList.clear();
        userList.add(selectedValue);
        storageService.updateUserStorage(vkId, storageStep, userList);
        return true;
    }

    private void removePrevStorage(StorageService storageService, Integer vkId, StepSelector currentStep) {
        switch (currentStep) {
            case ADMIN_CHANGE_REVIEW:
                storageService.removeUserStorage(vkId, ADMIN_REVIEW_LIST);
                break;
            case ADMIN_REVIEW_LIST:
                storageService.removeUserStorage(vkId, ADMIN_THEME_LIST);
                break;
            case ADMIN_THEME_LIST:
                storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
                break;
        }
    }
}
