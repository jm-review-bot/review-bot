package spring.app.configuration.initializator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.app.core.StepHolder;
import spring.app.core.StepSelector;
import spring.app.core.steps.Step;
import spring.app.model.*;
import spring.app.service.abstraction.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

public class TestDataInit {

    @Autowired
    PasswordEncoder passwordEncoder;

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
    private FeedbackService feedbackService;

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
    private Step adminChooseActionForUser;

    @Autowired
    private Step adminChooseActionForReview;

    @Autowired
    private Step adminProposalChangeFullnameAddedUser;

    @Autowired
    private Step adminChangeAddedUserFullname;

    @Autowired
    private Step adminSetPassedReview;

    @Autowired
    private Step adminSetPassedReviewGetThemesStatus;

    @Autowired
    private Step adminSetPassedReviewGetUsersList;

    @Autowired
    private Step adminSetPassedReviewResult;

    @Autowired
    private Step adminSetThemeAddedUser;

    @Autowired
    private Step examinerAddNewStudentReview;

    @Autowired
    private Step examinerChooseMethodToAddStudent;

    @Autowired
    private Step examinerChooseOldStudentReviewToEdit;

    @Autowired
    private Step examinerFreeThemesList;

    @Autowired
    private Step examinerGetInfoLastReview;

    @Autowired
    private Step examinerUsersListFromDB;

    @Autowired
    private Step examinerAddNewStudent;

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

        User defaultUser = new User();// Пользователь по-умолчанию (сдал все темы сам себе)
        defaultUser.setFirstName("Пользователь");
        defaultUser.setLastName("По-умолчанию");
        defaultUser.setVkId(0);
        defaultUser.setRole(roleAdmin);
        defaultUser.setReviewPoint(1000);
        defaultUser.setChatStep(StepSelector.START);
        defaultUser.setAccountNonExpired(true);
        defaultUser.setAccountNonLocked(true);
        defaultUser.setCredentialsNonExpired(true);
        defaultUser.setEnabled(true);
        userService.addUser(defaultUser);

        User testUser = new User();
        testUser.setFirstName("Петр");
        testUser.setLastName("Петров");
        testUser.setReviewPoint(4);
        testUser.setVkId(1582532887); // change this to your vkId for testing
        testUser.setRole(roleUser);  // change role for testing
        testUser.setChatStep(StepSelector.START);
        userService.addUser(testUser);

        User admin = new User();
        admin.setFirstName("Иван");
        admin.setLastName("Попов");
        admin.setReviewPoint(1000);
        admin.setVkId(238449263);
        admin.setRole(roleAdmin);
        admin.setPassword(passwordEncoder.encode("123123"));
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        admin.setEnabled(true);
        admin.setChatStep(StepSelector.START);
        admin.setReviewPoint(1000);
        userService.addUser(admin);

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
        nikolay.setAccountNonExpired(true);
        nikolay.setAccountNonLocked(true);
        nikolay.setCredentialsNonExpired(true);
        nikolay.setEnabled(true);
        nikolay.setPassword(passwordEncoder.encode("97957185"));
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
        mikhail.setReviewPoint(50);
        mikhail.setVkId(27939840);
        mikhail.setRole(roleAdmin);
        mikhail.setChatStep(StepSelector.START);
        mikhail.setAccountNonExpired(true);
        mikhail.setAccountNonLocked(true);
        mikhail.setCredentialsNonExpired(true);
        mikhail.setEnabled(true);
        mikhail.setPassword(passwordEncoder.encode("27939840"));
        userService.addUser(mikhail);

        User slyab = new User();
        slyab.setFirstName("Михаил");
        slyab.setLastName("Клопотнюк");
        slyab.setReviewPoint(50);
        slyab.setVkId(3660561);
        slyab.setRole(roleAdmin);
        slyab.setChatStep(StepSelector.START);
        slyab.setAccountNonExpired(true);
        slyab.setAccountNonLocked(true);
        slyab.setCredentialsNonExpired(true);
        slyab.setEnabled(true);
        userService.addUser(slyab);

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
        steps.put(StepSelector.ADMIN_CHOOSE_ACTION_FOR_USER, adminChooseActionForUser);
        steps.put(StepSelector.ADMIN_CHOOSE_ACTION_FOR_REVIEW, adminChooseActionForReview);
        steps.put(StepSelector.ADMIN_EDIT_REVIEW_GET_USER_LIST, adminEditReviewGetUserList);
        steps.put(StepSelector.ADMIN_EDIT_REVIEW_GET_THEME_LIST, adminEditReviewGetThemeList);
        steps.put(StepSelector.ADMIN_EDIT_REVIEW_GET_REVIEW_LIST, adminEditReviewGetReviewList);
        steps.put(StepSelector.ADMIN_EDIT_REVIEW_GET_REVIEW_INFO, adminEditReviewGetReviewInfo);
        steps.put(StepSelector.ADMIN_EDIT_REVIEW_CHANGE_REVIEW, adminEditReviewChangeReview);
        steps.put(StepSelector.ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER, adminProposalChangeFullnameAddedUser);
        steps.put(StepSelector.ADMIN_CHANGE_ADDED_USER_FULLNAME, adminChangeAddedUserFullname);
        steps.put(StepSelector.ADMIN_SET_PASSED_REVIEW, adminSetPassedReview);
        steps.put(StepSelector.ADMIN_SET_PASSED_REVIEW_RESULT, adminSetPassedReviewResult);
        steps.put(StepSelector.ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS, adminSetPassedReviewGetThemesStatus);
        steps.put(StepSelector.ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST, adminSetPassedReviewGetUsersList);
        steps.put(StepSelector.ADMIN_SET_THEME_ADDED_USER, adminSetThemeAddedUser);
        steps.put(StepSelector.EXAMINER_ADD_NEW_STUDENT_REVIEW, examinerAddNewStudentReview);
        steps.put(StepSelector.EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT, examinerChooseMethodToAddStudent);
        steps.put(StepSelector.EXAMINER_CHOOSE_OLD_STUDENT_REVIEW_TO_EDIT, examinerChooseOldStudentReviewToEdit);
        steps.put(StepSelector.EXAMINER_FREE_THEMES_LIST, examinerFreeThemesList);
        steps.put(StepSelector.EXAMINER_GET_INFO_LAST_REVIEW, examinerGetInfoLastReview);
        steps.put(StepSelector.EXAMINER_USERS_LIST_FROM_DB, examinerUsersListFromDB);
        steps.put(StepSelector.EXAMINER_ADD_NEW_STUDENT, examinerAddNewStudent);
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

        //add fixed themes
        FixedTheme core = new FixedTheme();
        core.setReviewPoint(0);
        core.setTitle("Java Core");
        core.setCriticalWeight(8);
        themeService.addTheme(core);

        FixedTheme multithreading = new FixedTheme();
        multithreading.setReviewPoint(4);
        multithreading.setTitle("Многопоточность");
        multithreading.setCriticalWeight(8);
        themeService.addTheme(multithreading);

        FixedTheme sql = new FixedTheme();
        sql.setReviewPoint(4);
        sql.setTitle("SQL");
        sql.setCriticalWeight(8);
        themeService.addTheme(sql);

        FixedTheme hibernate = new FixedTheme();
        hibernate.setReviewPoint(4);
        hibernate.setTitle("Hibernate");
        hibernate.setCriticalWeight(8);
        themeService.addTheme(hibernate);

        FixedTheme spring = new FixedTheme();
        spring.setReviewPoint(4);
        spring.setTitle("Spring");
        spring.setCriticalWeight(8);
        themeService.addTheme(spring);

        FixedTheme patterns = new FixedTheme();
        patterns.setReviewPoint(4);
        patterns.setTitle("Паттерны");
        patterns.setCriticalWeight(8);
        themeService.addTheme(patterns);

        FixedTheme algorithm = new FixedTheme();
        algorithm.setReviewPoint(4);
        algorithm.setTitle("Алгоритмы");
        algorithm.setCriticalWeight(8);
        themeService.addTheme(algorithm);

        FixedTheme finalReview = new FixedTheme();
        finalReview.setReviewPoint(4);
        finalReview.setTitle("Финальное ревью");
        finalReview.setCriticalWeight(8);
        themeService.addTheme(finalReview);

        // add free themes
        FreeTheme freeTheme1 = new FreeTheme();
        freeTheme1.setReviewPoint(4);
        freeTheme1.setTitle("Свободная тема 1");
        freeTheme1.setCriticalWeight(8);
        freeTheme1.setExaminers(Arrays.asList(defaultUser,mikhail, nikolay));
        themeService.addTheme(freeTheme1);

        FreeTheme freeTheme2 = new FreeTheme();
        freeTheme2.setReviewPoint(4);
        freeTheme2.setTitle("Свободная тема 2");
        freeTheme2.setCriticalWeight(8);
        freeTheme2.setExaminers(Arrays.asList(defaultUser, nikolay));
        themeService.addTheme(freeTheme2);

        FreeTheme freeTheme3 = new FreeTheme();
        freeTheme3.setReviewPoint(4);
        freeTheme3.setTitle("Свободная тема 3");
        freeTheme3.setCriticalWeight(8);
        freeTheme3.setExaminers(Arrays.asList(defaultUser, mikhail));
        themeService.addTheme(freeTheme3);

        // add reviews
        Review hibReview = new Review();
        hibReview.setDate(LocalDateTime.of(2021, 4, 18, 11, 13));
        hibReview.setIsOpen(true);
        hibReview.setTheme(hibernate);
        hibReview.setUser(nikolay);
        reviewService.addReview(hibReview);

        Review hibReview2 = new Review();
        hibReview2.setDate(LocalDateTime.of(2022, 4, 18, 11, 13));
        hibReview2.setIsOpen(true);
        hibReview2.setTheme(hibernate);
        hibReview2.setUser(nikolay);
        reviewService.addReview(hibReview2);

        Review revOfSt = new Review();
        revOfSt.setDate(LocalDateTime.of(3033,1,1,1,1));
        revOfSt.setIsOpen(true);
        revOfSt.setTheme(algorithm);
        revOfSt.setUser(ludwig);
        reviewService.addReview(revOfSt);

        Review springReviewPassed = new Review();
        springReviewPassed.setDate(LocalDateTime.of(2020, 4, 18, 11, 0));
        springReviewPassed.setIsOpen(false);
        springReviewPassed.setTheme(spring);
        springReviewPassed.setUser(roman);
        reviewService.addReview(springReviewPassed);

        Review springReview = new Review();
        springReview.setDate(LocalDateTime.of(2020, 4, 18, 13, 0));
        springReview.setIsOpen(true);
        springReview.setTheme(hibernate);
        springReview.setUser(anton);
        reviewService.addReview(springReview);

        Review springReviewPassed2 = new Review();
        springReviewPassed2.setDate(LocalDateTime.of(2020, 4, 18, 10, 0));
        springReviewPassed2.setIsOpen(true);
        springReviewPassed2.setTheme(hibernate);
        springReviewPassed2.setUser(anton);
        reviewService.addReview(springReviewPassed2);

        Review springReviewPassed3 = new Review();
        springReviewPassed3.setDate(LocalDateTime.of(2020, 4, 18, 10, 0));
        springReviewPassed3.setIsOpen(true);
        springReviewPassed3.setTheme(hibernate);
        springReviewPassed3.setUser(maksim);
        reviewService.addReview(springReviewPassed3);

        Review springReviewPassed4 = new Review();
        springReviewPassed4.setDate(LocalDateTime.of(2020, 4, 18, 11, 0));
        springReviewPassed4.setIsOpen(true);
        springReviewPassed4.setTheme(hibernate);
        springReviewPassed4.setUser(maksim);
        reviewService.addReview(springReviewPassed4);

        Review springReviewPassed5 = new Review();
        springReviewPassed5.setDate(LocalDateTime.of(2020, 6, 16, 12, 5));
        springReviewPassed5.setIsOpen(true);
        springReviewPassed5.setTheme(core);
        springReviewPassed5.setUser(dima);
        reviewService.addReview(springReviewPassed5);

        Review springReviewPassed6 = new Review();
        springReviewPassed6.setDate(LocalDateTime.of(2020, 6, 16, 13, 5));
        springReviewPassed6.setIsOpen(true);
        springReviewPassed6.setTheme(algorithm);
        springReviewPassed6.setUser(dima);
        reviewService.addReview(springReviewPassed6);

        Review springReviewPassed7 = new Review();
        springReviewPassed7.setDate(LocalDateTime.of(2020, 6, 16, 13, 5));
        springReviewPassed7.setIsOpen(true);
        springReviewPassed7.setTheme(spring);
        springReviewPassed7.setUser(dima);
        reviewService.addReview(springReviewPassed7);

        Review reviewForDefaultUser1 = new Review();
        reviewForDefaultUser1.setDate(LocalDateTime.of(1970, 1, 1, 1, 0));
        reviewForDefaultUser1.setTheme(core);
        reviewForDefaultUser1.setIsOpen(false);
        reviewForDefaultUser1.setUser(defaultUser);
        reviewService.addReview(reviewForDefaultUser1);

        Review reviewForDefaultUser2 = new Review();
        reviewForDefaultUser2.setDate(LocalDateTime.of(1970, 1, 1, 1, 0));
        reviewForDefaultUser2.setTheme(multithreading);
        reviewForDefaultUser2.setIsOpen(false);
        reviewForDefaultUser2.setUser(defaultUser);
        reviewService.addReview(reviewForDefaultUser2);

        Review reviewForDefaultUser3 = new Review();
        reviewForDefaultUser3.setDate(LocalDateTime.of(1970, 1, 1, 1, 0));
        reviewForDefaultUser3.setTheme(sql);
        reviewForDefaultUser3.setIsOpen(false);
        reviewForDefaultUser3.setUser(defaultUser);
        reviewService.addReview(reviewForDefaultUser3);

        Review reviewForDefaultUser4 = new Review();
        reviewForDefaultUser4.setDate(LocalDateTime.of(1970, 1, 1, 1, 0));
        reviewForDefaultUser4.setTheme(hibernate);
        reviewForDefaultUser4.setIsOpen(false);
        reviewForDefaultUser4.setUser(defaultUser);
        reviewService.addReview(reviewForDefaultUser4);

        Review reviewForDefaultUser5 = new Review();
        reviewForDefaultUser5.setDate(LocalDateTime.of(1970, 1, 1, 1, 0));
        reviewForDefaultUser5.setTheme(spring);
        reviewForDefaultUser5.setIsOpen(false);
        reviewForDefaultUser5.setUser(defaultUser);
        reviewService.addReview(reviewForDefaultUser5);

        Review reviewForDefaultUser6 = new Review();
        reviewForDefaultUser6.setDate(LocalDateTime.of(1970, 1, 1, 1, 0));
        reviewForDefaultUser6.setTheme(patterns);
        reviewForDefaultUser6.setIsOpen(false);
        reviewForDefaultUser6.setUser(defaultUser);
        reviewService.addReview(reviewForDefaultUser6);

        Review reviewForDefaultUser7 = new Review();
        reviewForDefaultUser7.setDate(LocalDateTime.of(1970, 1, 1, 1, 0));
        reviewForDefaultUser7.setTheme(algorithm);
        reviewForDefaultUser7.setIsOpen(false);
        reviewForDefaultUser7.setUser(defaultUser);
        reviewService.addReview(reviewForDefaultUser7);

        Review reviewForDefaultUser8 = new Review();
        reviewForDefaultUser8.setDate(LocalDateTime.of(1970, 1, 1, 1, 0));
        reviewForDefaultUser8.setTheme(finalReview);
        reviewForDefaultUser8.setIsOpen(false);
        reviewForDefaultUser8.setUser(defaultUser);
        reviewService.addReview(reviewForDefaultUser8);

        Review reviewForDefaultUser9 = new Review();
        reviewForDefaultUser9.setDate(LocalDateTime.of(1970, 1, 1, 1, 0));
        reviewForDefaultUser9.setTheme(freeTheme1);
        reviewForDefaultUser9.setIsOpen(false);
        reviewForDefaultUser9.setUser(defaultUser);
        reviewService.addReview(reviewForDefaultUser9);

        Review reviewForDefaultUser10 = new Review();
        reviewForDefaultUser10.setDate(LocalDateTime.of(1970, 1, 1, 1, 0));
        reviewForDefaultUser10.setTheme(freeTheme2);
        reviewForDefaultUser10.setIsOpen(false);
        reviewForDefaultUser10.setUser(defaultUser);
        reviewService.addReview(reviewForDefaultUser10);

        Review reviewForDefaultUser11 = new Review();
        reviewForDefaultUser11.setDate(LocalDateTime.of(1970, 1, 1, 1, 0));
        reviewForDefaultUser11.setTheme(freeTheme3);
        reviewForDefaultUser11.setIsOpen(false);
        reviewForDefaultUser11.setUser(defaultUser);
        reviewService.addReview(reviewForDefaultUser11);

        // add student reviews
        StudentReview sr = new StudentReview();
        sr.setUser(nikolay);
        sr.setIsPassed(false);
        sr.setReview(revOfSt);
        studentReviewService.addStudentReview(sr);

        StudentReview hibStudentReview = new StudentReview();
        hibStudentReview.setUser(martyn);
        hibStudentReview.setIsPassed(false);
        hibStudentReview.setReview(hibReview);
        studentReviewService.addStudentReview(hibStudentReview);

        StudentReview hibStudentReview2 = new StudentReview();
        hibStudentReview2.setUser(ludwig);
        hibStudentReview2.setIsPassed(false);
        hibStudentReview2.setReview(hibReview2);
        studentReviewService.addStudentReview(hibStudentReview2);

        StudentReview studentReview = new StudentReview();
        studentReview.setUser(sergey);
        studentReview.setIsPassed(true);
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
        studentReview5.setIsPassed(false);
        studentReview5.setReview(springReviewPassed5);
        studentReviewService.addStudentReview(studentReview5);

        StudentReview studentReviewForDefaultUser1 = new StudentReview();
        studentReviewForDefaultUser1.setIsPassed(true);
        studentReviewForDefaultUser1.setReview(reviewForDefaultUser1);
        studentReviewForDefaultUser1.setUser(defaultUser);
        studentReviewService.addStudentReview(studentReviewForDefaultUser1);

        StudentReview studentReviewForDefaultUser2 = new StudentReview();
        studentReviewForDefaultUser2.setIsPassed(true);
        studentReviewForDefaultUser2.setReview(reviewForDefaultUser2);
        studentReviewForDefaultUser2.setUser(defaultUser);
        studentReviewService.addStudentReview(studentReviewForDefaultUser2);

        StudentReview studentReviewForDefaultUser3 = new StudentReview();
        studentReviewForDefaultUser3.setIsPassed(true);
        studentReviewForDefaultUser3.setReview(reviewForDefaultUser3);
        studentReviewForDefaultUser3.setUser(defaultUser);
        studentReviewService.addStudentReview(studentReviewForDefaultUser3);

        StudentReview studentReviewForDefaultUser4 = new StudentReview();
        studentReviewForDefaultUser4.setIsPassed(true);
        studentReviewForDefaultUser4.setReview(reviewForDefaultUser4);
        studentReviewForDefaultUser4.setUser(defaultUser);
        studentReviewService.addStudentReview(studentReviewForDefaultUser4);

        StudentReview studentReviewForDefaultUser5 = new StudentReview();
        studentReviewForDefaultUser5.setIsPassed(true);
        studentReviewForDefaultUser5.setReview(reviewForDefaultUser5);
        studentReviewForDefaultUser5.setUser(defaultUser);
        studentReviewService.addStudentReview(studentReviewForDefaultUser5);

        StudentReview studentReviewForDefaultUser6 = new StudentReview();
        studentReviewForDefaultUser6.setIsPassed(true);
        studentReviewForDefaultUser6.setReview(reviewForDefaultUser6);
        studentReviewForDefaultUser6.setUser(defaultUser);
        studentReviewService.addStudentReview(studentReviewForDefaultUser6);

        StudentReview studentReviewForDefaultUser7 = new StudentReview();
        studentReviewForDefaultUser7.setIsPassed(true);
        studentReviewForDefaultUser7.setReview(reviewForDefaultUser7);
        studentReviewForDefaultUser7.setUser(defaultUser);
        studentReviewService.addStudentReview(studentReviewForDefaultUser7);

        StudentReview studentReviewForDefaultUser8 = new StudentReview();
        studentReviewForDefaultUser8.setIsPassed(true);
        studentReviewForDefaultUser8.setReview(reviewForDefaultUser8);
        studentReviewForDefaultUser8.setUser(defaultUser);
        studentReviewService.addStudentReview(studentReviewForDefaultUser8);

        StudentReview studentReviewForDefaultUser9 = new StudentReview();
        studentReviewForDefaultUser9.setIsPassed(true);
        studentReviewForDefaultUser9.setReview(reviewForDefaultUser9);
        studentReviewForDefaultUser9.setUser(defaultUser);
        studentReviewService.addStudentReview(studentReviewForDefaultUser9);

        StudentReview studentReviewForDefaultUser10 = new StudentReview();
        studentReviewForDefaultUser10.setIsPassed(true);
        studentReviewForDefaultUser10.setReview(reviewForDefaultUser10);
        studentReviewForDefaultUser10.setUser(defaultUser);
        studentReviewService.addStudentReview(studentReviewForDefaultUser10);

        StudentReview studentReviewForDefaultUser11 = new StudentReview();
        studentReviewForDefaultUser11.setIsPassed(true);
        studentReviewForDefaultUser11.setReview(reviewForDefaultUser11);
        studentReviewForDefaultUser11.setUser(defaultUser);
        studentReviewService.addStudentReview(studentReviewForDefaultUser11);

        // add Feedbacks
        Feedback feedback1 = new Feedback();
        feedback1.setStudentReview(hibStudentReview);
        feedback1.setComment("comment_(feedback1)");
        feedback1.setRatingReview(1);
        feedback1.setRatingReviewer(2);
        feedback1.setUser(martyn);
        feedbackService.addFeedback(feedback1);

        Feedback feedback2 = new Feedback();
        feedback2.setStudentReview(hibStudentReview2);
        feedback2.setComment("comment_(feedback2)");
        feedback2.setRatingReview(3);
        feedback2.setRatingReviewer(4);
        feedback2.setUser(ludwig);
        feedbackService.addFeedback(feedback2);

        Feedback feedback3 = new Feedback();
        feedback3.setStudentReview(studentReview);
        feedback3.setComment("comment_(feedback3)");
        feedback3.setRatingReview(5);
        feedback3.setRatingReviewer(6);
        feedback3.setUser(sergey);
        feedbackService.addFeedback(feedback3);

        Feedback feedback4 = new Feedback();
        feedback4.setStudentReview(studentReview4);
        feedback4.setComment("comment_(feedback4)");
        feedback4.setRatingReview(7);
        feedback4.setRatingReviewer(8);
        feedback4.setUser(roman);
        feedbackService.addFeedback(feedback4);

        Feedback feedback5 = new Feedback();
        feedback5.setStudentReview(studentReview5);
        feedback5.setComment("comment_(feedback5)");
        feedback5.setRatingReview(9);
        feedback5.setRatingReviewer(10);
        feedback5.setUser(anton);
        feedbackService.addFeedback(feedback5);

        // add Questions
        Question question1 = new Question();
        question1.setAnswer("«Bean» – это объект, который интегрируется и конфигурируется контейнером IOC.");
        question1.setQuestion("Что такое bean??");
        question1.setFixedTheme(spring);
        question1.setWeight(8);
        questionService.addQuestion(question1);

        Question question2 = new Question();
        question2.setAnswer("IOC означает инверсию контроля. Это основной контейнер Java Spring. Он использует вышеупомянутое внедрение зависимостей для управления и настройки различных интегрированных приложений. В настоящее время в Spring может быть два типа IOC – ApplicationContext и BeanFactory.");
        question2.setQuestion("Опишите IOC своими словами");
        question2.setFixedTheme(spring);
        question2.setWeight(8);
        questionService.addQuestion(question2);

        Question question3 = new Question();
        question3.setAnswer("Dependency injection (внедрение зависимостей) используется для предоставления определенных специфических зависимостей для объектов. Это шаблон проектирования, который делает ваши проекты более плавными и более подходящими для таких действий, как тестирование.");
        question3.setQuestion("Что такое Dependency Injection?");
        question3.setFixedTheme(spring);
        question3.setWeight(8);
        questionService.addQuestion(question3);

        Question question4 = new Question();
        question4.setAnswer("Spring Boot – это версия Spring, цель которой – сделать процесс создания приложений более удобным. Одна из его ключевых особенностей заключается в том, что она устраняет необходимость определения шаблонных конфигураций – несомненно, это порадует многих разработчиков.");
        question4.setQuestion("Что такое Spring Boot?");
        question4.setFixedTheme(spring);
        question4.setWeight(8);
        questionService.addQuestion(question4);

        Question question5 = new Question();
        question5.setAnswer("АОП расшифровывается как Аспектно-ориентированное программирование (Aspect-Oriented Programming). Он отличается от ООП (объектно-ориентированного программирования) тем, что ООП фокусируется на классах, в то время как ключевым модульным модулем АОП является аспект. В АОП аспекты реализуют и подчеркивают сквозные проблемы.");
        question5.setQuestion("Что такое AOP?");
        question5.setFixedTheme(spring);
        question5.setWeight(8);
        questionService.addQuestion(question5);

        Question question6 = new Question();
        question6.setAnswer("‘Autowriting‘ позволяет разработчику вводить bean-компоненты в свое приложение автоматически, без необходимости ручного вмешательства.");
        question6.setQuestion("Что такое autowriting?");
        question6.setFixedTheme(spring);
        question6.setWeight(8);
        questionService.addQuestion(question6);

        Question question7 = new Question();
        question7.setAnswer("Как только аспекты переключаются на объект, он автоматически становится целевым объектом (target object). Некоторые также любят называть его «рекомендованным объектом».");
        question7.setQuestion("Что такое target object?");
        question7.setFixedTheme(spring);
        question7.setWeight(8);
        questionService.addQuestion(question7);

        Question question8 = new Question();
        question8.setAnswer("В Spring Framework DAO это объект доступа к данным. Этот инструмент позволяет разработчикам легче подходить и работать с инструментами доступа к данным, особенно на Java.");
        question8.setQuestion("Что такое DAO?");
        question8.setFixedTheme(spring);
        question8.setWeight(8);
        questionService.addQuestion(question8);

        Question question9 = new Question();
        question9.setAnswer("Эта команда используется, когда вы хотите сопоставить определенный метод HTTP с определенным классом. Вы можете использовать эту команду как на уровне класса, так и на уровне метода.");
        question9.setQuestion("Что делает @RequestMapping?");
        question9.setFixedTheme(spring);
        question9.setWeight(8);
        questionService.addQuestion(question9);

        Question question10 = new Question();
        question10.setAnswer("В Spring MVC Interceptor может использоваться для обработки запроса клиента до, во время и даже после обработки. Это отличный инструмент, позволяющий избежать нежелательных повторений кода.");
        question10.setQuestion("Что такое MVC Interceptor?");
        question10.setFixedTheme(spring);
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
        akiraCorePassed.setIsOpen(false);
        akiraCorePassed.setTheme(core);
        akiraCorePassed.setUser(akira);//кто принимал
        reviewService.addReview(akiraCorePassed);
        StudentReview akiraCoreSuccesReview = new StudentReview();
        akiraCoreSuccesReview.setUser(akira);
        akiraCoreSuccesReview.setIsPassed(true);
        akiraCoreSuccesReview.setReview(akiraCorePassed);
        studentReviewService.addStudentReview(akiraCoreSuccesReview);
        //Ревью и связь о прохождении многопоточки
        Review akiraMultithreadingPassed = new Review();
        akiraMultithreadingPassed.setDate(LocalDateTime.of(2020, 4, 14, 11, 0));
        akiraMultithreadingPassed.setIsOpen(false);
        akiraMultithreadingPassed.setTheme(multithreading);
        akiraMultithreadingPassed.setUser(akira);//кто принимал
        reviewService.addReview(akiraMultithreadingPassed);
        StudentReview akiraMultithreadingSuccesReview = new StudentReview();
        akiraMultithreadingSuccesReview.setUser(akira);
        akiraMultithreadingSuccesReview.setIsPassed(true);
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
        studentForCriticalWeightCorePassed.setIsOpen(false);
        studentForCriticalWeightCorePassed.setTheme(core);
        studentForCriticalWeightCorePassed.setUser(studentForCriticalWeight);//кто принимал
        reviewService.addReview(studentForCriticalWeightCorePassed);
        StudentReview studentForCriticalWeightCoreSuccesReview = new StudentReview();
        studentForCriticalWeightCoreSuccesReview.setUser(studentForCriticalWeight);
        studentForCriticalWeightCoreSuccesReview.setIsPassed(true);
        studentForCriticalWeightCoreSuccesReview.setReview(studentForCriticalWeightCorePassed);
        studentReviewService.addStudentReview(studentForCriticalWeightCoreSuccesReview);

        //второй юзер сдающий ревью по многопоточке. То есть у него 1 пройденное ревью - кор
        User secondStudentForCriticalWeight = nikolay;

        //Ревью и связь о прохождении кора
        Review secondStudentForCriticalWeightCorePassed = new Review();
        secondStudentForCriticalWeightCorePassed.setDate(LocalDateTime.of(2020, 4, 13, 11, 0));
        secondStudentForCriticalWeightCorePassed.setIsOpen(false);
        secondStudentForCriticalWeightCorePassed.setTheme(core);
        secondStudentForCriticalWeightCorePassed.setUser(secondStudentForCriticalWeight);//кто принимал
        reviewService.addReview(secondStudentForCriticalWeightCorePassed);
        StudentReview secondStudentForCriticalWeightCoreSuccesReview = new StudentReview();
        secondStudentForCriticalWeightCoreSuccesReview.setUser(secondStudentForCriticalWeight);//кто сдавал
        secondStudentForCriticalWeightCoreSuccesReview.setIsPassed(true);
        secondStudentForCriticalWeightCoreSuccesReview.setReview(secondStudentForCriticalWeightCorePassed);
        studentReviewService.addStudentReview(secondStudentForCriticalWeightCoreSuccesReview);


        //ревью по многопоточке, на котором 1 принимает, 2 сдает ревью.
        //НЕЛЬЗЯ ЗАБЫВАТЬ УКАЗЫВАТЬ ДАТУ
        Review criticalWeightReview = new Review();
        criticalWeightReview.setDate(LocalDateTime.of(2020, 6, 5, 2, 35));
        criticalWeightReview.setIsOpen(true);
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
        criticalWeightReview2.setIsOpen(true);
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
        criticalQuestion1.setQuestion("Кто такие фиксики");
        criticalQuestion1.setFixedTheme(multithreading);
        criticalQuestion1.setWeight(1);
        questionService.addQuestion(criticalQuestion1);

        Question criticalQuestion2 = new Question();
        criticalQuestion2.setAnswer("Столько же, сколько накануне, ибо Йозеф еще спит");
        criticalQuestion2.setQuestion("Сколько будет весить Йозеф, если греки выступили на рассвете?");
        criticalQuestion2.setFixedTheme(multithreading);
        criticalQuestion2.setWeight(2);
        questionService.addQuestion(criticalQuestion2);

        Question criticalQuestion3 = new Question();
        criticalQuestion3.setAnswer("Путь праведника труден, ибо препятствуют ему себялюбивые и тираны из злых людей.");
        criticalQuestion3.setQuestion("Назовите первое предложение Ветхого Завета, Книги Иезекииля,Главы 25,17 Стиха ");
        criticalQuestion3.setFixedTheme(multithreading);
        criticalQuestion3.setWeight(3);
        questionService.addQuestion(criticalQuestion3);

        Question criticalQuestion4 = new Question();
        criticalQuestion4.setAnswer("завершает работу цикла");
        criticalQuestion4.setQuestion("Что делает оператор break?");
        criticalQuestion4.setFixedTheme(multithreading);
        criticalQuestion4.setWeight(4);
        questionService.addQuestion(criticalQuestion4);

        //Для проверки функционала всех кнопок меню
        Review criticalWeightReview3 = new Review();
        criticalWeightReview3.setDate(LocalDateTime.of(2020, 6, 3, 23, 25));
        criticalWeightReview3.setIsOpen(true);
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
        changeReviewQuestion.setQuestion("Назовите букву которая следует за буквой идущей после буквы в");
        changeReviewQuestion.setFixedTheme(core);
        changeReviewQuestion.setWeight(1);
        questionService.addQuestion(changeReviewQuestion);

        Question changeReviewQuestion2 = new Question();
        changeReviewQuestion2.setAnswer("от 0 до 50 процентов");
        changeReviewQuestion2.setQuestion("Если у папы рецесивный ген кривой нос, а у мамы доминантный орлиный, какой диапазон вероятностей кривого носа у их ребенка?");
        changeReviewQuestion2.setFixedTheme(core);
        changeReviewQuestion2.setWeight(2);
        questionService.addQuestion(changeReviewQuestion2);

        Question changeReviewQuestion3 = new Question();
        changeReviewQuestion3.setAnswer("четыре и восемь");
        changeReviewQuestion3.setQuestion("назовите четвертую и 7 цифру после запятой корня числа пи");
        changeReviewQuestion3.setFixedTheme(core);
        changeReviewQuestion3.setWeight(3);
        questionService.addQuestion(changeReviewQuestion3);

        Question changeReviewQuestion4 = new Question();
        changeReviewQuestion4.setAnswer("Исаак");
        changeReviewQuestion4.setQuestion("Как зовут Ньютона?");
        changeReviewQuestion4.setFixedTheme(core);
        changeReviewQuestion4.setWeight(4);
        questionService.addQuestion(changeReviewQuestion4);

        //и связи под него
        StudentReviewAnswer changeReviewAnswerQuestion = new StudentReviewAnswer();
        changeReviewAnswerQuestion.setStudentReview(studentForCriticalWeightCoreSuccesReview);
        changeReviewAnswerQuestion.setIsRight(true);
        changeReviewAnswerQuestion.setQuestion(changeReviewQuestion);
        studentReviewAnswerService.addStudentReviewAnswer(changeReviewAnswerQuestion);

        StudentReviewAnswer changeReviewAnswerQuestion2 = new StudentReviewAnswer();
        changeReviewAnswerQuestion2.setStudentReview(studentForCriticalWeightCoreSuccesReview);
        changeReviewAnswerQuestion2.setIsRight(true);
        changeReviewAnswerQuestion2.setQuestion(changeReviewQuestion2);
        studentReviewAnswerService.addStudentReviewAnswer(changeReviewAnswerQuestion2);

        StudentReviewAnswer changeReviewAnswerQuestion3 = new StudentReviewAnswer();
        changeReviewAnswerQuestion3.setStudentReview(studentForCriticalWeightCoreSuccesReview);
        changeReviewAnswerQuestion3.setIsRight(true);
        changeReviewAnswerQuestion3.setQuestion(changeReviewQuestion3);
        studentReviewAnswerService.addStudentReviewAnswer(changeReviewAnswerQuestion3);

        StudentReviewAnswer changeReviewAnswerQuestion4 = new StudentReviewAnswer();
        changeReviewAnswerQuestion4.setStudentReview(studentForCriticalWeightCoreSuccesReview);
        changeReviewAnswerQuestion4.setIsRight(true);
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
        springReviewPassed12.setIsOpen(false);
        springReviewPassed12.setTheme(spring);
        springReviewPassed12.setUser(anton);
        reviewService.addReview(springReviewPassed12);

        Review hibernateReviewPassed13 = new Review();
        hibernateReviewPassed13.setDate(LocalDateTime.of(2020, 4, 18, 11, 0));
        hibernateReviewPassed13.setIsOpen(false);
        hibernateReviewPassed13.setTheme(hibernate);
        hibernateReviewPassed13.setUser(anton);
        reviewService.addReview(hibernateReviewPassed13);

        Review review11 = new Review();
        review11.setDate(LocalDateTime.of(2020, 6, 7, 16, 0));
        review11.setIsOpen(true);
        review11.setTheme(spring);
        review11.setUser(andrey);
        reviewService.addReview(review11);
//
//
        StudentReview studentReview12 = new StudentReview();
        studentReview12.setUser(andrey);
        studentReview12.setIsPassed(true);
        studentReview12.setReview(springReviewPassed12);
        studentReviewService.addStudentReview(studentReview12);

        StudentReview studentReview13 = new StudentReview();
        studentReview13.setUser(ale);
        studentReview13.setIsPassed(true);
        studentReview13.setReview(hibernateReviewPassed13);
        studentReviewService.addStudentReview(studentReview13);

        StudentReview studentReview15 = new StudentReview();
        studentReview15.setUser(ale);
        studentReview15.setIsPassed(null);
        studentReview15.setReview(review11);
        studentReviewService.addStudentReview(studentReview15);
//
    }
}