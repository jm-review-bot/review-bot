package spring.app.configuration.initializator;

import org.springframework.beans.factory.annotation.Autowired;
import spring.app.core.StepSelector;
import spring.app.core.StepHolder;
import spring.app.core.steps.*;
import spring.app.model.*;
import spring.app.service.abstraction.*;

import java.util.Map;

import java.time.LocalDateTime;

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
    private Step userStartReview;

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
        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setReviewPoint(999);
        admin.setVkId(1374221); // change this to your vkId for testing
        admin.setRole(roleAdmin);
        admin.setChatStep("START");
        userService.addUser(admin);

        User admin2 = new User();
        admin2.setFirstName("Максим");
        admin2.setLastName("Ботюк");
        admin2.setReviewPoint(0);
        admin2.setVkId(87632583); // change this to your vkId for testing
        admin2.setRole(roleAdmin);
        admin2.setChatStep("START");
        userService.addUser(admin2);

        User user = new User();
        user.setFirstName("Антон");
        user.setLastName("Таврель");
        user.setReviewPoint(4);
        user.setVkId(582532887);
        user.setRole(roleUser);
        user.setChatStep("START");
        userService.addUser(user);

        User user2 = new User();
        user2.setFirstName("Петр");
        user2.setLastName("Петров");
        user2.setReviewPoint(4);
        user2.setVkId(1582532887);
        user2.setRole(roleUser);
        user2.setChatStep("START");
        userService.addUser(user2);

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
        steps.put(StepSelector.USER_START_REVIEW, userStartReview);

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
        Review springReviewPassed = new Review(); // ревью, которое я сдал
        springReviewPassed.setDate(LocalDateTime.of(2020, 3, 3, 23, 0));
        springReviewPassed.setOpen(false);
        springReviewPassed.setTheme(spring);
        springReviewPassed.setUser(admin);
        reviewService.addReview(springReviewPassed);

        Review springReview = new Review(); // ревью по спрингу которое я буду принимать
        springReview.setDate(LocalDateTime.of(2020, 5, 20, 15, 0));
        springReview.setOpen(true);
        springReview.setTheme(spring);
        springReview.setUser(user);
        reviewService.addReview(springReview);

        // add student reviews
        StudentReview studentReview = new StudentReview(); // студент-ревью, которое я прошел, чтобы теперь принимать спринг
        studentReview.setUser(user);
        studentReview.setPassed(true);
        studentReview.setReview(springReviewPassed);
        studentReviewService.addStudentReview(studentReview);

        StudentReview studentReview2 = new StudentReview(); // чувак 1, который записался ко мне на ревью
        studentReview2.setUser(admin2);
        //studentReview2.setPassed(true);
        studentReview2.setReview(springReview);
        studentReviewService.addStudentReview(studentReview2);

        StudentReview studentReview3 = new StudentReview(); // чувак 2, котороый записался ко мне на ревью
        studentReview3.setUser(admin);
        //studentReview3.setPassed(true);
        studentReview3.setReview(springReview);
        studentReviewService.addStudentReview(studentReview3);

        // add Questions
        Question question1 = new Question();
        question1.setAnswer("Весна");
        question1.setPosition(1);
        question1.setQuestion("Что такое спринг?");
        question1.setTheme(spring);
        questionService.addQuestion(question1);

        Question question2 = new Question();
        question2.setAnswer("Боб");
        question2.setPosition(2);
        question2.setQuestion("Что такое Bean?");
        question2.setTheme(spring);
        questionService.addQuestion(question2);

        Question question3 = new Question();
        question3.setAnswer("Инъекция зависимостей");
        question3.setPosition(3);
        question3.setQuestion("Что такое Dependency Injection?");
        question3.setTheme(spring);
        questionService.addQuestion(question3);

        Question question4 = new Question();
        question4.setAnswer("Конечно");
        question4.setPosition(4);
        question4.setQuestion("Джаву любишь?");
        question4.setTheme(spring);
        questionService.addQuestion(question4);

        /*
        // add student review answers
        StudentReviewAnswer answer1 = new StudentReviewAnswer();
        answer1.setRight(true);
        answer1.setQuestion(question1);
        answer1.setStudentReview(studentReview);
        studentReviewAnswerService.addStudentReviewAnswer(answer1);

        StudentReviewAnswer answer2 = new StudentReviewAnswer();
        answer2.setRight(true);
        answer2.setQuestion(question2);
        answer2.setStudentReview(studentReview);
        studentReviewAnswerService.addStudentReviewAnswer(answer2);

        StudentReviewAnswer answer3 = new StudentReviewAnswer();
        answer3.setRight(true);
        answer3.setQuestion(question3);
        answer3.setStudentReview(studentReview);
        studentReviewAnswerService.addStudentReviewAnswer(answer3);

        StudentReviewAnswer answer4 = new StudentReviewAnswer();
        answer4.setRight(true);
        answer4.setQuestion(question4);
        answer4.setStudentReview(studentReview);
        studentReviewAnswerService.addStudentReviewAnswer(answer4);

         */
    }
}
