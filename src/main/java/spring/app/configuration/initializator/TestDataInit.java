package spring.app.configuration.initializator;

import org.springframework.beans.factory.annotation.Autowired;
import spring.app.core.StepHolder;
import spring.app.core.StepSelector;
import spring.app.core.steps.Step;
import spring.app.model.*;
import spring.app.service.abstraction.*;

import java.time.LocalDateTime;
import java.util.Map;

public class TestDataInit {
    public static Review staticReview;//инициализируется на 362 строке

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
    private Step adminUserList;

    @Autowired
    private Step adminEditUser;

    @Autowired
    private Step adminInputNewFullnameEditedUser;

    @Autowired
    private Step adminConfirmChangeEditedUserFullname;

    @Autowired
    private Step adminInputNewVkIdEditedUser;

    @Autowired
    private Step adminConfirmChangeEditedUserVkId;

    @Autowired
    private Step adminRemoveUser;

    @Autowired
    private Step adminEditReviewGetUserList;

    @Autowired
    private Step adminEditReviewGetThemeList;

    @Autowired
    private Step adminEditReviewGetReviewList;

    @Autowired
    private Step adminEditReviewGetReviewInfo;

    @Autowired
    private Step adminEditReviewChangeReview;

    @Autowired
    private Step adminSearch;

    @Autowired
    private Step adminConfirmSearch;

    @Autowired
    private Step adminProposalChangeFullnameAddedUser;

    @Autowired
    private Step adminChangeAddedUserFullname;

    @Autowired
    private Step adminSetThemeAddedUser;

    @Autowired
    private Step userTakeReviewAddDate;

    @Autowired
    private Step userTakeReviewAddTheme;

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
    private Step userStartChooseReview;

    @Autowired
    private Step userFeedbackConfirmation;

    @Autowired
    private Step userFeedbackReviewRating;

    @Autowired
    private Step userFeedbackReviewerRating;

    @Autowired
    private Step userFeedbackComment;

    @Autowired
    private Step reviewerDeleteReview;

    @Autowired
    private Step selectingReviewToDelete;

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

        User sergey = new User();
        sergey.setFirstName("Сергей");
        sergey.setLastName("Лебедев");
        sergey.setReviewPoint(1000000);
        sergey.setVkId(80169300);
        sergey.setRole(roleAdmin);
        sergey.setChatStep(StepSelector.START);
        userService.addUser(sergey);

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

        User kirill = new User();
        kirill.setFirstName("Кирилл");
        kirill.setLastName("Башарин");
        kirill.setReviewPoint(4);
        kirill.setVkId(36654046);
        kirill.setRole(roleAdmin);
        kirill.setChatStep(StepSelector.START);
        userService.addUser(kirill);

        User dima = new User();
        dima.setFirstName("Дмитрий");
        dima.setLastName("Шепелев");
        dima.setReviewPoint(10);
        dima.setVkId(147150209);
        dima.setRole(roleAdmin);
        dima.setChatStep(StepSelector.START);
        userService.addUser(dima);

        User martyn = new User();
        martyn.setFirstName("Мартын");
        martyn.setLastName("Герасимов");
        martyn.setReviewPoint(8);
        martyn.setVkId(339070438);
        martyn.setRole(roleAdmin);
        martyn.setChatStep(StepSelector.START);
        userService.addUser(martyn);

        User ludwig = new User();
        ludwig.setFirstName("Ludwig");
        ludwig.setLastName("Phantomhive");
        ludwig.setReviewPoint(8);
        ludwig.setVkId(270263136);
        ludwig.setRole(roleAdmin);
        ludwig.setChatStep(StepSelector.START);
        userService.addUser(ludwig);

        User mikhail = new User();
        mikhail.setFirstName("Михаил");
        mikhail.setLastName("Кузиванов");
        mikhail.setReviewPoint(4);
        mikhail.setVkId(27939840);
        mikhail.setRole(roleAdmin);
        mikhail.setChatStep(StepSelector.START);
        userService.addUser(mikhail);

        // add steps
        Map<StepSelector, Step> steps = stepHolder.getSteps();
        steps.put(StepSelector.START, start);
        steps.put(StepSelector.USER_MENU, userMenu);
        steps.put(StepSelector.ADMIN_MENU, adminMenu);
        steps.put(StepSelector.ADMIN_ADD_USER, adminAddUser);
        steps.put(StepSelector.ADMIN_USERS_LIST, adminUserList);
        steps.put(StepSelector.ADMIN_EDIT_USER, adminEditUser);
        steps.put(StepSelector.ADMIN_INPUT_NEW_FULLNAME_EDITED_USER, adminInputNewFullnameEditedUser);
        steps.put(StepSelector.ADMIN_CONFIRM_CHANGE_EDITED_USER_FULLNAME, adminConfirmChangeEditedUserFullname);
        steps.put(StepSelector.ADMIN_INPUT_NEW_VKID_EDITED_USER, adminInputNewVkIdEditedUser);
        steps.put(StepSelector.ADMIN_CONFIRM_CHANGE_EDITED_USER_VKID, adminConfirmChangeEditedUserVkId);
        steps.put(StepSelector.ADMIN_REMOVE_USER, adminRemoveUser);
        steps.put(StepSelector.ADMIN_SEARCH, adminSearch);
        steps.put(StepSelector.ADMIN_CONFIRM_SEARCH, adminConfirmSearch);
        steps.put(StepSelector.ADMIN_EDIT_REVIEW_GET_USER_LIST, adminEditReviewGetUserList);
        steps.put(StepSelector.ADMIN_EDIT_REVIEW_GET_THEME_LIST, adminEditReviewGetThemeList);
        steps.put(StepSelector.ADMIN_EDIT_REVIEW_GET_REVIEW_LIST, adminEditReviewGetReviewList);
        steps.put(StepSelector.ADMIN_EDIT_REVIEW_GET_REVIEW_INFO, adminEditReviewGetReviewInfo);
        steps.put(StepSelector.ADMIN_EDIT_REVIEW_CHANGE_REVIEW, adminEditReviewChangeReview);
        steps.put(StepSelector.ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER, adminProposalChangeFullnameAddedUser);
        steps.put(StepSelector.ADMIN_CHANGE_ADDED_USER_FULLNAME, adminChangeAddedUserFullname);
        steps.put(StepSelector.ADMIN_SET_THEME_ADDED_USER, adminSetThemeAddedUser);
        steps.put(StepSelector.USER_TAKE_REVIEW_ADD_THEME, userTakeReviewAddTheme);
        steps.put(StepSelector.USER_TAKE_REVIEW_ADD_DATE, userTakeReviewAddDate);
        steps.put(StepSelector.USER_PASS_REVIEW_ADD_THEME, userPassReviewAddTheme);
        steps.put(StepSelector.USER_PASS_REVIEW_GET_LIST_REVIEW, userPassReviewGetListReview);
        steps.put(StepSelector.USER_PASS_REVIEW_ADD_STUDENT_REVIEW, userPassReviewAddStudentReview);
        steps.put(StepSelector.USER_CANCEL_REVIEW, userCancelReview);
        steps.put(StepSelector.USER_START_CHOOSE_REVIEW, userStartChooseReview);
        steps.put(StepSelector.USER_START_REVIEW_HANGOUTS_LINK, userStartReviewHangoutsLink);
        steps.put(StepSelector.USER_START_REVIEW_RULES, userStartReviewRules);
        steps.put(StepSelector.USER_START_REVIEW_CORE, userStartReviewCore);
        steps.put(StepSelector.USER_FEEDBACK_CONFIRMATION, userFeedbackConfirmation);
        steps.put(StepSelector.USER_FEEDBACK_REVIEW_RATING, userFeedbackReviewRating);
        steps.put(StepSelector.USER_FEEDBACK_REVIEWER_RATING, userFeedbackReviewerRating);
        steps.put(StepSelector.USER_FEEDBACK_COMMENT, userFeedbackComment);
        steps.put(StepSelector.REVIEWER_DELETE_REVIEW, reviewerDeleteReview);
        steps.put(StepSelector.SELECTING_REVIEW_TO_DELETE, selectingReviewToDelete);

        //add themes
        Theme core = new Theme();
        core.setPosition(1);
        core.setReviewPoint(0);
        core.setTitle("Java Core");
        core.setCriticalWeight(8);
        themeService.addTheme(core);

        Theme multithreading = new Theme();
        multithreading.setPosition(2);
        multithreading.setReviewPoint(4);
        multithreading.setTitle("Многопоточность");
        multithreading.setCriticalWeight(8);
        themeService.addTheme(multithreading);

        Theme sql = new Theme();
        sql.setPosition(3);
        sql.setReviewPoint(4);
        sql.setTitle("SQL");
        sql.setCriticalWeight(8);
        themeService.addTheme(sql);

        Theme hibernate = new Theme();
        hibernate.setPosition(4);
        hibernate.setReviewPoint(4);
        hibernate.setTitle("Hibernate");
        hibernate.setCriticalWeight(8);
        themeService.addTheme(hibernate);

        Theme spring = new Theme();
        spring.setPosition(5);
        spring.setReviewPoint(4);
        spring.setTitle("Spring");
        spring.setCriticalWeight(8);
        themeService.addTheme(spring);

        Theme patterns = new Theme();
        patterns.setPosition(6);
        patterns.setReviewPoint(4);
        patterns.setTitle("Паттерны");
        patterns.setCriticalWeight(8);
        themeService.addTheme(patterns);

        Theme algorithm = new Theme();
        algorithm.setPosition(7);
        algorithm.setReviewPoint(4);
        algorithm.setTitle("Алгоритмы");
        algorithm.setCriticalWeight(8);
        themeService.addTheme(algorithm);

        Theme finalReview = new Theme();
        finalReview.setPosition(8);
        finalReview.setReviewPoint(4);
        finalReview.setTitle("Финальное ревью");
        finalReview.setCriticalWeight(8);
        themeService.addTheme(finalReview);

        //__=========================================================================================__
        staticReview = new Review();
        staticReview.setDate(LocalDateTime.now().plusMinutes(62));
        staticReview.setOpen(true);
        staticReview.setTheme(core);
        staticReview.setUser(nikolay);
        reviewService.addReview(staticReview);
        StudentReview stik = new StudentReview();
        stik.setUser(ludwig);
        stik.setPassed(false);
        stik.setReview(staticReview);
        studentReviewService.addStudentReview(stik);
        StudentReview stik2 = new StudentReview();
        stik2.setUser(martyn);
        stik2.setPassed(false);
        stik2.setReview(staticReview);
        studentReviewService.addStudentReview(stik2);
        //__=========================================================================================__

        // add reviews
        Review hibReview = new Review();
        hibReview.setDate(LocalDateTime.of(2021, 4, 18, 11, 13));
        hibReview.setOpen(true);
        hibReview.setTheme(hibernate);
        hibReview.setUser(nikolay);
        reviewService.addReview(hibReview);

        Review hibReview2 = new Review();
        hibReview2.setDate(LocalDateTime.of(2022, 4, 18, 11, 13));
        hibReview2.setOpen(true);
        hibReview2.setTheme(hibernate);
        hibReview2.setUser(nikolay);
        reviewService.addReview(hibReview2);

        Review revOfSt = new Review();
        revOfSt.setDate(LocalDateTime.of(3033,1,1,1,1));
        revOfSt.setOpen(true);
        revOfSt.setTheme(algorithm);
        revOfSt.setUser(ludwig);
        reviewService.addReview(revOfSt);

        Review springReviewPassed = new Review();
        springReviewPassed.setDate(LocalDateTime.of(2020, 4, 18, 11, 0));
        springReviewPassed.setOpen(false);
        springReviewPassed.setTheme(spring);
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
        springReviewPassed5.setDate(LocalDateTime.of(2020, 6, 16, 12, 5));
        springReviewPassed5.setOpen(true);
        springReviewPassed5.setTheme(core);
        springReviewPassed5.setUser(dima);
        reviewService.addReview(springReviewPassed5);

        Review springReviewPassed6 = new Review();
        springReviewPassed6.setDate(LocalDateTime.of(2020, 6, 16, 13, 5));
        springReviewPassed6.setOpen(true);
        springReviewPassed6.setTheme(algorithm);
        springReviewPassed6.setUser(dima);
        reviewService.addReview(springReviewPassed6);

        Review springReviewPassed7 = new Review();
        springReviewPassed7.setDate(LocalDateTime.of(2020, 6, 16, 13, 5));
        springReviewPassed7.setOpen(true);
        springReviewPassed7.setTheme(spring);
        springReviewPassed7.setUser(dima);
        reviewService.addReview(springReviewPassed7);

        // add student reviews
        StudentReview sr = new StudentReview();
        sr.setUser(nikolay);
        sr.setPassed(false);
        sr.setReview(revOfSt);
        studentReviewService.addStudentReview(sr);

        StudentReview hibStudentReview = new StudentReview();
        hibStudentReview.setUser(martyn);
        hibStudentReview.setPassed(false);
        hibStudentReview.setReview(hibReview);
        studentReviewService.addStudentReview(hibStudentReview);

        StudentReview hibStudentReview2 = new StudentReview();
        hibStudentReview2.setUser(ludwig);
        hibStudentReview2.setPassed(false);
        hibStudentReview2.setReview(hibReview2);
        studentReviewService.addStudentReview(hibStudentReview2);

        StudentReview studentReview = new StudentReview();
        studentReview.setUser(sergey);
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
        studentReview5.setUser(anton);
        studentReview5.setPassed(false);
        studentReview5.setReview(springReviewPassed5);
        studentReviewService.addStudentReview(studentReview5);

        // add Questions
        Question question1 = new Question();
        question1.setAnswer("«Bean» – это объект, который интегрируется и конфигурируется контейнером IOC.");
        question1.setPosition(1);
        question1.setQuestion("Что такое bean??");
        question1.setTheme(spring);
        question1.setWeight(8);
        questionService.addQuestion(question1);

        Question question2 = new Question();
        question2.setAnswer("IOC означает инверсию контроля. Это основной контейнер Java Spring. Он использует вышеупомянутое внедрение зависимостей для управления и настройки различных интегрированных приложений. В настоящее время в Spring может быть два типа IOC – ApplicationContext и BeanFactory.");
        question2.setPosition(2);
        question2.setQuestion("Опишите IOC своими словами");
        question2.setTheme(spring);
        question2.setWeight(8);
        questionService.addQuestion(question2);

        Question question3 = new Question();
        question3.setAnswer("Dependency injection (внедрение зависимостей) используется для предоставления определенных специфических зависимостей для объектов. Это шаблон проектирования, который делает ваши проекты более плавными и более подходящими для таких действий, как тестирование.");
        question3.setPosition(3);
        question3.setQuestion("Что такое Dependency Injection?");
        question3.setTheme(spring);
        question3.setWeight(8);
        questionService.addQuestion(question3);

        Question question4 = new Question();
        question4.setAnswer("Spring Boot – это версия Spring, цель которой – сделать процесс создания приложений более удобным. Одна из его ключевых особенностей заключается в том, что она устраняет необходимость определения шаблонных конфигураций – несомненно, это порадует многих разработчиков.");
        question4.setPosition(4);
        question4.setQuestion("Что такое Spring Boot?");
        question4.setTheme(spring);
        question4.setWeight(8);
        questionService.addQuestion(question4);

        Question question5 = new Question();
        question5.setAnswer("АОП расшифровывается как Аспектно-ориентированное программирование (Aspect-Oriented Programming). Он отличается от ООП (объектно-ориентированного программирования) тем, что ООП фокусируется на классах, в то время как ключевым модульным модулем АОП является аспект. В АОП аспекты реализуют и подчеркивают сквозные проблемы.");
        question5.setPosition(5);
        question5.setQuestion("Что такое AOP?");
        question5.setTheme(spring);
        question5.setWeight(8);
        questionService.addQuestion(question5);

        Question question6 = new Question();
        question6.setAnswer("‘Autowriting‘ позволяет разработчику вводить bean-компоненты в свое приложение автоматически, без необходимости ручного вмешательства.");
        question6.setPosition(6);
        question6.setQuestion("Что такое autowriting?");
        question6.setTheme(spring);
        question6.setWeight(8);
        questionService.addQuestion(question6);

        Question question7 = new Question();
        question7.setAnswer("Как только аспекты переключаются на объект, он автоматически становится целевым объектом (target object). Некоторые также любят называть его «рекомендованным объектом».");
        question7.setPosition(7);
        question7.setQuestion("Что такое target object?");
        question7.setTheme(spring);
        question7.setWeight(8);
        questionService.addQuestion(question7);

        Question question8 = new Question();
        question8.setAnswer("В Spring Framework DAO это объект доступа к данным. Этот инструмент позволяет разработчикам легче подходить и работать с инструментами доступа к данным, особенно на Java.");
        question8.setPosition(8);
        question8.setQuestion("Что такое DAO?");
        question8.setTheme(spring);
        question8.setWeight(8);
        questionService.addQuestion(question8);

        Question question9 = new Question();
        question9.setAnswer("Эта команда используется, когда вы хотите сопоставить определенный метод HTTP с определенным классом. Вы можете использовать эту команду как на уровне класса, так и на уровне метода.");
        question9.setPosition(9);
        question9.setQuestion("Что делает @RequestMapping?");
        question9.setTheme(spring);
        question9.setWeight(8);
        questionService.addQuestion(question9);

        Question question10 = new Question();
        question10.setAnswer("В Spring MVC Interceptor может использоваться для обработки запроса клиента до, во время и даже после обработки. Это отличный инструмент, позволяющий избежать нежелательных повторений кода.");
        question10.setPosition(10);
        question10.setQuestion("Что такое MVC Interceptor?");
        question10.setTheme(spring);
        question10.setWeight(8);
        questionService.addQuestion(question10);

        User akira = new User();
        akira.setFirstName("Akira");
        akira.setLastName("Rokudo");
        akira.setReviewPoint(30);
        akira.setVkId(167464635);
        akira.setRole(roleAdmin);
        akira.setChatStep(StepSelector.START);
        userService.addUser(akira);

        //Ревью и связь о прохождении кора
        Review akiraCorePassed = new Review();
        akiraCorePassed.setDate(LocalDateTime.of(2020, 4, 13, 11, 0));
        akiraCorePassed.setOpen(false);
        akiraCorePassed.setTheme(core);
        akiraCorePassed.setUser(akira);//кто принимал
        reviewService.addReview(akiraCorePassed);
        StudentReview akiraCoreSuccesReview = new StudentReview();
        akiraCoreSuccesReview.setUser(akira);
        akiraCoreSuccesReview.setPassed(true);
        akiraCoreSuccesReview.setReview(akiraCorePassed);
        studentReviewService.addStudentReview(akiraCoreSuccesReview);
        //Ревью и связь о прохождении многопоточки
        Review akiraMultithreadingPassed = new Review();
        akiraMultithreadingPassed.setDate(LocalDateTime.of(2020, 4, 14, 11, 0));
        akiraMultithreadingPassed.setOpen(false);
        akiraMultithreadingPassed.setTheme(multithreading);
        akiraMultithreadingPassed.setUser(akira);//кто принимал
        reviewService.addReview(akiraMultithreadingPassed);
        StudentReview akiraMultithreadingSuccesReview = new StudentReview();
        akiraMultithreadingSuccesReview.setUser(akira);
        akiraMultithreadingSuccesReview.setPassed(true);
        akiraMultithreadingSuccesReview.setReview(akiraMultithreadingPassed);
        studentReviewService.addStudentReview(akiraMultithreadingSuccesReview);

        //первый юзер сдающий ревью по многопоточке. То есть у него 1 пройденное ревью - кор
        User studentForCriticalWeight = new User();
        studentForCriticalWeight.setFirstName("Алексей");
        studentForCriticalWeight.setLastName("Травов");
        studentForCriticalWeight.setReviewPoint(10);
        studentForCriticalWeight.setVkId(561687031);
        studentForCriticalWeight.setRole(roleAdmin);
        studentForCriticalWeight.setChatStep(StepSelector.START);
        userService.addUser(studentForCriticalWeight);

        //Ревью и связь о прохождении кора
        Review studentForCriticalWeightCorePassed = new Review();
        studentForCriticalWeightCorePassed.setDate(LocalDateTime.of(2020, 4, 13, 11, 0));
        studentForCriticalWeightCorePassed.setOpen(false);
        studentForCriticalWeightCorePassed.setTheme(core);
        studentForCriticalWeightCorePassed.setUser(studentForCriticalWeight);//кто принимал
        reviewService.addReview(studentForCriticalWeightCorePassed);
        StudentReview studentForCriticalWeightCoreSuccesReview = new StudentReview();
        studentForCriticalWeightCoreSuccesReview.setUser(studentForCriticalWeight);
        studentForCriticalWeightCoreSuccesReview.setPassed(true);
        studentForCriticalWeightCoreSuccesReview.setReview(studentForCriticalWeightCorePassed);
        studentReviewService.addStudentReview(studentForCriticalWeightCoreSuccesReview);

        //второй юзер сдающий ревью по многопоточке. То есть у него 1 пройденное ревью - кор
        User secondStudentForCriticalWeight = nikolay;

        //Ревью и связь о прохождении кора
        Review secondStudentForCriticalWeightCorePassed = new Review();
        secondStudentForCriticalWeightCorePassed.setDate(LocalDateTime.of(2020, 4, 13, 11, 0));
        secondStudentForCriticalWeightCorePassed.setOpen(false);
        secondStudentForCriticalWeightCorePassed.setTheme(core);
        secondStudentForCriticalWeightCorePassed.setUser(secondStudentForCriticalWeight);//кто принимал
        reviewService.addReview(secondStudentForCriticalWeightCorePassed);
        StudentReview secondStudentForCriticalWeightCoreSuccesReview = new StudentReview();
        secondStudentForCriticalWeightCoreSuccesReview.setUser(secondStudentForCriticalWeight);//кто сдавал
        secondStudentForCriticalWeightCoreSuccesReview.setPassed(true);
        secondStudentForCriticalWeightCoreSuccesReview.setReview(secondStudentForCriticalWeightCorePassed);
        studentReviewService.addStudentReview(secondStudentForCriticalWeightCoreSuccesReview);


        //ревью по многопоточке, на котором 1 принимает, 2 сдает ревью.
        //НЕЛЬЗЯ ЗАБЫВАТЬ УКАЗЫВАТЬ ДАТУ
        Review criticalWeightReview = new Review();
        criticalWeightReview.setDate(LocalDateTime.of(2020, 6, 5, 2, 35));
        criticalWeightReview.setOpen(true);
        criticalWeightReview.setTheme(multithreading);
        criticalWeightReview.setUser(akira);//кто принимает
        reviewService.addReview(criticalWeightReview);

        //Связь для ревью.
        // первый из сдающих
        StudentReview criticalWeightFirstStudentReview = new StudentReview();
        criticalWeightFirstStudentReview.setUser(studentForCriticalWeight);//кто сдает
        criticalWeightFirstStudentReview.setReview(criticalWeightReview);
        studentReviewService.addStudentReview(criticalWeightFirstStudentReview);
        //второй сдающий
        StudentReview criticalWeightSecondStudentReview = new StudentReview();
        criticalWeightSecondStudentReview.setUser(secondStudentForCriticalWeight);//кто сдает
        criticalWeightSecondStudentReview.setReview(criticalWeightReview);
        studentReviewService.addStudentReview(criticalWeightSecondStudentReview);

        //ревью по многопоточке, на котором 1 принимает, 2 сдает ревью.
        //НЕЛЬЗЯ ЗАБЫВАТЬ УКАЗЫВАТЬ ДАТУ
        Review criticalWeightReview2 = new Review();
        criticalWeightReview2.setDate(LocalDateTime.of(2020, 6, 10, 21, 25));
        criticalWeightReview2.setOpen(true);
        criticalWeightReview2.setTheme(multithreading);
        criticalWeightReview2.setUser(akira);//кто принимает
        reviewService.addReview(criticalWeightReview2);

        //Связь для ревью.
        // первый из сдающих
        StudentReview criticalWeightFirstStudentReview2 = new StudentReview();
        criticalWeightFirstStudentReview2.setUser(studentForCriticalWeight);//кто сдает
        criticalWeightFirstStudentReview2.setReview(criticalWeightReview2);
        studentReviewService.addStudentReview(criticalWeightFirstStudentReview2);
        //второй сдающий
        StudentReview criticalWeightSecondStudentReview2 = new StudentReview();
        criticalWeightSecondStudentReview2.setUser(secondStudentForCriticalWeight);//кто сдает
        criticalWeightSecondStudentReview2.setReview(criticalWeightReview2);
        studentReviewService.addStudentReview(criticalWeightSecondStudentReview2);

        //4 вопроса
        Question criticalQuestion1 = new Question();
        criticalQuestion1.setAnswer("Герои мультика");
        criticalQuestion1.setPosition(1);
        criticalQuestion1.setQuestion("Кто такие фиксики");
        criticalQuestion1.setTheme(multithreading);
        criticalQuestion1.setWeight(1);
        questionService.addQuestion(criticalQuestion1);

        Question criticalQuestion2 = new Question();
        criticalQuestion2.setAnswer("Столько же, сколько накануне, ибо Йозеф еще спит");
        criticalQuestion2.setPosition(2);
        criticalQuestion2.setQuestion("Сколько будет весить Йозеф, если греки выступили на рассвете?");
        criticalQuestion2.setTheme(multithreading);
        criticalQuestion2.setWeight(2);
        questionService.addQuestion(criticalQuestion2);

        Question criticalQuestion3 = new Question();
        criticalQuestion3.setAnswer("Путь праведника труден, ибо препятствуют ему себялюбивые и тираны из злых людей.");
        criticalQuestion3.setPosition(3);
        criticalQuestion3.setQuestion("Назовите первое предложение Ветхого Завета, Книги Иезекииля,Главы 25,17 Стиха ");
        criticalQuestion3.setTheme(multithreading);
        criticalQuestion3.setWeight(3);
        questionService.addQuestion(criticalQuestion3);

        Question criticalQuestion4 = new Question();
        criticalQuestion4.setAnswer("завершает работу цикла");
        criticalQuestion4.setPosition(4);
        criticalQuestion4.setQuestion("Что делает оператор break?");
        criticalQuestion4.setTheme(multithreading);
        criticalQuestion4.setWeight(4);
        questionService.addQuestion(criticalQuestion4);

        //Для проверки функционала всех кнопок меню
        Review criticalWeightReview3 = new Review();
        criticalWeightReview3.setDate(LocalDateTime.of(2020, 6, 3, 23, 25));
        criticalWeightReview3.setOpen(true);
        criticalWeightReview3.setTheme(multithreading);
        criticalWeightReview3.setUser(akira);//кто принимает
        reviewService.addReview(criticalWeightReview3);
        StudentReview criticalWeightFirstStudentReview3 = new StudentReview();
        criticalWeightFirstStudentReview3.setUser(akira);//кто сдает
        criticalWeightFirstStudentReview3.setReview(criticalWeightReview3);
        studentReviewService.addStudentReview(criticalWeightFirstStudentReview3);

        //еще 4 вопроса
        Question changeReviewQuestion = new Question();
        changeReviewQuestion.setAnswer("д");
        changeReviewQuestion.setPosition(1);
        changeReviewQuestion.setQuestion("Назовите букву которая следует за буквой идущей после буквы в");
        changeReviewQuestion.setTheme(core);
        changeReviewQuestion.setWeight(1);
        questionService.addQuestion(changeReviewQuestion);

        Question changeReviewQuestion2 = new Question();
        changeReviewQuestion2.setAnswer("от 0 до 50 процентов");
        changeReviewQuestion2.setPosition(2);
        changeReviewQuestion2.setQuestion("Если у папы рецесивный ген кривой нос, а у мамы доминантный орлиный, какой диапазон вероятностей кривого носа у их ребенка?");
        changeReviewQuestion2.setTheme(core);
        changeReviewQuestion2.setWeight(2);
        questionService.addQuestion(changeReviewQuestion2);

        Question changeReviewQuestion3 = new Question();
        changeReviewQuestion3.setAnswer("четыре и восемь");
        changeReviewQuestion3.setPosition(3);
        changeReviewQuestion3.setQuestion("назовите четвертую и 7 цифру после запятой корня числа пи");
        changeReviewQuestion3.setTheme(core);
        changeReviewQuestion3.setWeight(3);
        questionService.addQuestion(changeReviewQuestion3);

        Question changeReviewQuestion4 = new Question();
        changeReviewQuestion4.setAnswer("Исаак");
        changeReviewQuestion4.setPosition(4);
        changeReviewQuestion4.setQuestion("Как зовут Ньютона?");
        changeReviewQuestion4.setTheme(core);
        changeReviewQuestion4.setWeight(4);
        questionService.addQuestion(changeReviewQuestion4);

        //и связи под него
        StudentReviewAnswer changeReviewAnswerQuestion = new StudentReviewAnswer();
        changeReviewAnswerQuestion.setStudentReview(studentForCriticalWeightCoreSuccesReview);
        changeReviewAnswerQuestion.setRight(true);
        changeReviewAnswerQuestion.setQuestion(changeReviewQuestion);
        studentReviewAnswerService.addStudentReviewAnswer(changeReviewAnswerQuestion);

        StudentReviewAnswer changeReviewAnswerQuestion2 = new StudentReviewAnswer();
        changeReviewAnswerQuestion2.setStudentReview(studentForCriticalWeightCoreSuccesReview);
        changeReviewAnswerQuestion2.setRight(true);
        changeReviewAnswerQuestion2.setQuestion(changeReviewQuestion2);
        studentReviewAnswerService.addStudentReviewAnswer(changeReviewAnswerQuestion2);

        StudentReviewAnswer changeReviewAnswerQuestion3 = new StudentReviewAnswer();
        changeReviewAnswerQuestion3.setStudentReview(studentForCriticalWeightCoreSuccesReview);
        changeReviewAnswerQuestion3.setRight(true);
        changeReviewAnswerQuestion3.setQuestion(changeReviewQuestion3);
        studentReviewAnswerService.addStudentReviewAnswer(changeReviewAnswerQuestion3);

        StudentReviewAnswer changeReviewAnswerQuestion4 = new StudentReviewAnswer();
        changeReviewAnswerQuestion4.setStudentReview(studentForCriticalWeightCoreSuccesReview);
        changeReviewAnswerQuestion4.setRight(true);
        changeReviewAnswerQuestion4.setQuestion(changeReviewQuestion4);
        studentReviewAnswerService.addStudentReviewAnswer(changeReviewAnswerQuestion4);

        //
        User andrey = new User();
        andrey.setFirstName("Андрей");
        andrey.setLastName("Суетин");
        andrey.setReviewPoint(4);
        andrey.setVkId(175396577);
        andrey.setRole(roleAdmin);
        andrey.setChatStep(StepSelector.START);
        userService.addUser(andrey);

        User ale = new User();
        ale.setFirstName("А");
        ale.setLastName("К");
        ale.setReviewPoint(4);
        ale.setVkId(38398262);
        ale.setRole(roleUser);
        ale.setChatStep(StepSelector.START);
        userService.addUser(ale);
//
//
        Review springReviewPassed12 = new Review();
        springReviewPassed12.setDate(LocalDateTime.of(2020, 4, 18, 11, 0));
        springReviewPassed12.setOpen(false);
        springReviewPassed12.setTheme(spring);
        springReviewPassed12.setUser(anton);
        reviewService.addReview(springReviewPassed12);

        Review hibernateReviewPassed13 = new Review();
        hibernateReviewPassed13.setDate(LocalDateTime.of(2020, 4, 18, 11, 0));
        hibernateReviewPassed13.setOpen(false);
        hibernateReviewPassed13.setTheme(hibernate);
        hibernateReviewPassed13.setUser(anton);
        reviewService.addReview(hibernateReviewPassed13);

        Review review11 = new Review();
        review11.setDate(LocalDateTime.of(2020, 6, 7, 16, 0));
        review11.setOpen(true);
        review11.setTheme(spring);
        review11.setUser(andrey);
        reviewService.addReview(review11);
//
//
        StudentReview studentReview12 = new StudentReview();
        studentReview12.setUser(andrey);
        studentReview12.setPassed(true);
        studentReview12.setReview(springReviewPassed12);
        studentReviewService.addStudentReview(studentReview12);

        StudentReview studentReview13 = new StudentReview();
        studentReview13.setUser(ale);
        studentReview13.setPassed(true);
        studentReview13.setReview(hibernateReviewPassed13);
        studentReviewService.addStudentReview(studentReview13);

        StudentReview studentReview15 = new StudentReview();
        studentReview15.setUser(ale);
        studentReview15.setPassed(null);
        studentReview15.setReview(review11);
        studentReviewService.addStudentReview(studentReview15);
//
    }
}
