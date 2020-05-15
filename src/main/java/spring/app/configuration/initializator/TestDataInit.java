package spring.app.configuration.initializator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import spring.app.core.StepHolder;
import spring.app.core.StepSelector;
import spring.app.core.steps.Step;
import spring.app.model.*;
import spring.app.service.abstraction.*;

import java.time.LocalDateTime;
import java.util.Map;

public class TestDataInit {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private StudentReviewService studentReviewService;

    @Autowired
    private StudentReviewAnswerService studentReviewAnswerService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private StepHolder stepHolder;

    @Autowired
    private Step start;

    @Autowired
    private Step userMenu;

    @Autowired
    private Step adminMenu;

    @Autowired
    private Step adminAddUser;

    @Autowired
    private Step adminRemoveUser;

    @Autowired
    private Step userTakeReviewAddDate;

    @Autowired
    private Step userTakeReviewAddTheme;

    @Autowired
    private Step userTakeReviewConfirmation;

    @Autowired
    private Step userPassReviewAddTheme;

    @Autowired
    private Step userPassReviewGetListReview;

    @Autowired
    private Step userPassReviewAddStudentReview;

    @Autowired
    private Step userCancelReview;

    @Autowired
    private Step userStartReviewHangoutsLink;

    @Autowired
    private Step userStartReviewRules;

    @Autowired
    private Step userStartReviewCore;

    @Autowired
    private Step adminAddUserInputTheme;

    public TestDataInit() {
    }

    private void init() throws Exception {

        Role roleAdmin = new Role();
        roleAdmin.setName("ADMIN");
        roleService.addRole(roleAdmin);

        Role roleUser = new Role();
        roleUser.setName("USER");
        roleService.addRole(roleUser);

        // add users
        User testUser = new User();
        testUser.setFirstName("Петр");
        testUser.setLastName("Петров");
        testUser.setReviewPoint(4);
        testUser.setVkId(1582532887); // change this to your vkId for testing
        testUser.setRole(roleUser);  // change role for testing
        testUser.setChatStep(StepSelector.START);
        userService.addUser(testUser);

        User roman = new User();
        roman.setFirstName("Роман");
        roman.setLastName("Евсеев");
        roman.setReviewPoint(16);
        roman.setVkId(1374221);
        roman.setRole(roleUser);
        roman.setChatStep(StepSelector.START);
        userService.addUser(roman);

        User maksim = new User();
        maksim.setFirstName("Максим");
        maksim.setLastName("Ботюк");
        maksim.setReviewPoint(8);
        maksim.setVkId(87632583);
        maksim.setRole(roleAdmin);
        maksim.setChatStep(StepSelector.START);
        userService.addUser(maksim);

        User anton = new User();
        anton.setFirstName("Антон");
        anton.setLastName("Таврель");
        anton.setReviewPoint(4);
        anton.setVkId(582532887);
        anton.setRole(roleAdmin);
        anton.setChatStep(StepSelector.START);
        userService.addUser(anton);

        User nikolay = new User();
        nikolay.setFirstName("Николай");
        nikolay.setLastName("Климов");
        nikolay.setReviewPoint(4);
        nikolay.setVkId(97957185);
        nikolay.setRole(roleAdmin);
        nikolay.setChatStep(StepSelector.START);
        userService.addUser(nikolay);

        User aiBolotov = new User();
        aiBolotov.setFirstName("Александр");
        aiBolotov.setLastName("Болотов");
        aiBolotov.setReviewPoint(8);
        aiBolotov.setVkId(2738886);
        aiBolotov.setRole(roleAdmin);
        aiBolotov.setChatStep(StepSelector.START);
        userService.addUser(aiBolotov);

        User bot = new User();
        bot.setFirstName("Bot");
        bot.setLastName("Bot");
        bot.setReviewPoint(99);
        bot.setVkId(null);
        bot.setRole(roleAdmin);
        bot.setChatStep(StepSelector.START);
        userService.addUser(bot);

        // add steps
        Map<StepSelector, Step> steps = stepHolder.getSteps();
        steps.put(StepSelector.START, start);
        steps.put(StepSelector.USER_MENU, userMenu);
        steps.put(StepSelector.ADMIN_MENU, adminMenu);
        steps.put(StepSelector.ADMIN_ADD_USER, adminAddUser);
        steps.put(StepSelector.ADMIN_REMOVE_USER, adminRemoveUser);
        steps.put(StepSelector.USER_TAKE_REVIEW_ADD_THEME, userTakeReviewAddTheme);
        steps.put(StepSelector.USER_TAKE_REVIEW_ADD_DATE, userTakeReviewAddDate);
        steps.put(StepSelector.USER_TAKE_REVIEW_CONFIRMATION, userTakeReviewConfirmation);
        steps.put(StepSelector.USER_PASS_REVIEW_ADD_THEME, userPassReviewAddTheme);
        steps.put(StepSelector.USER_PASS_REVIEW_GET_LIST_REVIEW, userPassReviewGetListReview);
        steps.put(StepSelector.USER_PASS_REVIEW_ADD_STUDENT_REVIEW, userPassReviewAddStudentReview);
        steps.put(StepSelector.USER_CANCEL_REVIEW, userCancelReview);
        steps.put(StepSelector.USER_START_REVIEW_HANGOUTS_LINK, userStartReviewHangoutsLink);
        steps.put(StepSelector.USER_START_REVIEW_RULES, userStartReviewRules);
        steps.put(StepSelector.USER_START_REVIEW_CORE, userStartReviewCore);
        steps.put(StepSelector.ADMIN_ADD_USER_INPUT_THEME, adminAddUserInputTheme);

        //add themes
        Theme core = new Theme();
        core.setPosition(1);
        core.setReviewPoint(0);
        core.setTitle("Java Core");
        themeService.addTheme(core);

        Theme multithreading = new Theme();
        multithreading.setPosition(2);
        multithreading.setReviewPoint(4);
        multithreading.setTitle("Многопоточность");
        themeService.addTheme(multithreading);

        Theme sql = new Theme();
        sql.setPosition(3);
        sql.setReviewPoint(4);
        sql.setTitle("SQL");
        themeService.addTheme(sql);

        Theme hibernate = new Theme();
        hibernate.setPosition(4);
        hibernate.setReviewPoint(4);
        hibernate.setTitle("Hibernate");
        themeService.addTheme(hibernate);

        Theme spring = new Theme();
        spring.setPosition(5);
        spring.setReviewPoint(4);
        spring.setTitle("Spring");
        themeService.addTheme(spring);

        Theme patterns = new Theme();
        patterns.setPosition(6);
        patterns.setReviewPoint(4);
        patterns.setTitle("Паттерны");
        themeService.addTheme(patterns);

        Theme algorithm = new Theme();
        algorithm.setPosition(7);
        algorithm.setReviewPoint(4);
        algorithm.setTitle("Алгоритмы");
        themeService.addTheme(algorithm);

        Theme finalReview = new Theme();
        finalReview.setPosition(8);
        finalReview.setReviewPoint(4);
        finalReview.setTitle("Финальное ревью");
        themeService.addTheme(finalReview);

        // add reviews
        Review springReviewPassed = new Review();
        springReviewPassed.setDate(LocalDateTime.of(2020, 4, 18, 11, 0));
        springReviewPassed.setOpen(true);
        springReviewPassed.setTheme(hibernate);
        springReviewPassed.setUser(roman);
        reviewService.addReview(springReviewPassed);

        Review springReview = new Review();
        springReview.setDate(LocalDateTime.of(2020, 4, 18, 13, 0));
        springReview.setOpen(true);
        springReview.setTheme(hibernate);
        springReview.setUser(anton);
        reviewService.addReview(springReview);

        Review springReviewPassed2 = new Review();
        springReviewPassed2.setDate(LocalDateTime.of(2020, 4, 18, 10, 0));
        springReviewPassed2.setOpen(true);
        springReviewPassed2.setTheme(hibernate);
        springReviewPassed2.setUser(anton);
        reviewService.addReview(springReviewPassed2);

        Review springReviewPassed3 = new Review();
        springReviewPassed3.setDate(LocalDateTime.of(2020, 4, 18, 10, 0));
        springReviewPassed3.setOpen(true);
        springReviewPassed3.setTheme(hibernate);
        springReviewPassed3.setUser(maksim);
        reviewService.addReview(springReviewPassed3);

        Review springReviewPassed4 = new Review();
        springReviewPassed4.setDate(LocalDateTime.of(2020, 4, 18, 11, 0));
        springReviewPassed4.setOpen(true);
        springReviewPassed4.setTheme(hibernate);
        springReviewPassed4.setUser(maksim);
        reviewService.addReview(springReviewPassed4);

        Review springReviewPassed5 = new Review();
        springReviewPassed5.setDate(LocalDateTime.of(2020, 4, 18, 11, 0));
        springReviewPassed5.setOpen(false);
        springReviewPassed5.setTheme(core);
        springReviewPassed5.setUser(bot);
        reviewService.addReview(springReviewPassed5);

        // add student reviews
        StudentReview studentReview = new StudentReview();
        studentReview.setUser(anton);
        studentReview.setPassed(true);
        studentReview.setReview(springReviewPassed);
        studentReviewService.addStudentReview(studentReview);

        StudentReview studentReview2 = new StudentReview();
        studentReview2.setUser(anton);
        studentReview2.setReview(springReview);
        studentReviewService.addStudentReview(studentReview2);

        StudentReview studentReview3 = new StudentReview();
        studentReview3.setUser(testUser);
        studentReview3.setReview(springReview);
        studentReviewService.addStudentReview(studentReview3);

        StudentReview studentReview4 = new StudentReview();
        studentReview4.setUser(roman);
        studentReview4.setReview(springReviewPassed);
        studentReviewService.addStudentReview(studentReview4);

        StudentReview studentReview5 = new StudentReview();
        studentReview5.setUser(aiBolotov);
        studentReview5.setReview(springReviewPassed5);
        studentReview5.setPassed(true);
        studentReviewService.addStudentReview(studentReview5);

        // add Questions
        Question question1 = new Question();
        question1.setAnswer("«Bean» – это объект, который интегрируется и конфигурируется контейнером IOC.");
        question1.setPosition(1);
        question1.setQuestion("Что такое bean??");
        question1.setTheme(spring);
        questionService.addQuestion(question1);

        Question question2 = new Question();
        question2.setAnswer("IOC означает инверсию контроля. Это основной контейнер Java Spring. Он использует вышеупомянутое внедрение зависимостей для управления и настройки различных интегрированных приложений. В настоящее время в Spring может быть два типа IOC – ApplicationContext и BeanFactory.");
        question2.setPosition(2);
        question2.setQuestion("Опишите IOC своими словами");
        question2.setTheme(spring);
        questionService.addQuestion(question2);

        Question question3 = new Question();
        question3.setAnswer("Dependency injection (внедрение зависимостей) используется для предоставления определенных специфических зависимостей для объектов. Это шаблон проектирования, который делает ваши проекты более плавными и более подходящими для таких действий, как тестирование.");
        question3.setPosition(3);
        question3.setQuestion("Что такое Dependency Injection?");
        question3.setTheme(spring);
        questionService.addQuestion(question3);

        Question question4 = new Question();
        question4.setAnswer("Spring Boot – это версия Spring, цель которой – сделать процесс создания приложений более удобным. Одна из его ключевых особенностей заключается в том, что она устраняет необходимость определения шаблонных конфигураций – несомненно, это порадует многих разработчиков.");
        question4.setPosition(4);
        question4.setQuestion("Что такое Spring Boot?");
        question4.setTheme(spring);
        questionService.addQuestion(question4);

        Question question5 = new Question();
        question5.setAnswer("АОП расшифровывается как Аспектно-ориентированное программирование (Aspect-Oriented Programming). Он отличается от ООП (объектно-ориентированного программирования) тем, что ООП фокусируется на классах, в то время как ключевым модульным модулем АОП является аспект. В АОП аспекты реализуют и подчеркивают сквозные проблемы.");
        question5.setPosition(5);
        question5.setQuestion("Что такое AOP?");
        question5.setTheme(spring);
        questionService.addQuestion(question5);

        Question question6 = new Question();
        question6.setAnswer("‘Autowriting‘ позволяет разработчику вводить bean-компоненты в свое приложение автоматически, без необходимости ручного вмешательства.");
        question6.setPosition(6);
        question6.setQuestion("Что такое autowriting?");
        question6.setTheme(spring);
        questionService.addQuestion(question6);

        Question question7 = new Question();
        question7.setAnswer("Как только аспекты переключаются на объект, он автоматически становится целевым объектом (target object). Некоторые также любят называть его «рекомендованным объектом».");
        question7.setPosition(7);
        question7.setQuestion("Что такое target object?");
        question7.setTheme(spring);
        questionService.addQuestion(question7);

        Question question8 = new Question();
        question8.setAnswer("В Spring Framework DAO это объект доступа к данным. Этот инструмент позволяет разработчикам легче подходить и работать с инструментами доступа к данным, особенно на Java.");
        question8.setPosition(8);
        question8.setQuestion("Что такое DAO?");
        question8.setTheme(spring);
        questionService.addQuestion(question8);

        Question question9 = new Question();
        question9.setAnswer("Эта команда используется, когда вы хотите сопоставить определенный метод HTTP с определенным классом. Вы можете использовать эту команду как на уровне класса, так и на уровне метода.");
        question9.setPosition(9);
        question9.setQuestion("Что делает @RequestMapping?");
        question9.setTheme(spring);
        questionService.addQuestion(question9);

        Question question10 = new Question();
        question10.setAnswer("В Spring MVC Interceptor может использоваться для обработки запроса клиента до, во время и даже после обработки. Это отличный инструмент, позволяющий избежать нежелательных повторений кода.");
        question10.setPosition(10);
        question10.setQuestion("Что такое MVC Interceptor?");
        question10.setTheme(spring);
        questionService.addQuestion(question10);
    }
}
