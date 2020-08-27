package spring.app.core;

public enum StepSelector {
    START,
    USER_MENU,
    ADMIN_MENU,
    ADMIN_ADD_USER,
    ADMIN_EDIT_USER,
    ADMIN_INPUT_NEW_FULLNAME_EDITED_USER,
    ADMIN_CONFIRM_CHANGE_EDITED_USER_FULLNAME,
    ADMIN_INPUT_NEW_VKID_EDITED_USER,
    ADMIN_CONFIRM_CHANGE_EDITED_USER_VKID,
    ADMIN_REMOVE_USER,
    ADMIN_SEARCH,
    ADMIN_CONFIRM_SEARCH,
    ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER,
    ADMIN_CHANGE_ADDED_USER_FULLNAME,
    ADMIN_CHOOSE_ACTION_FOR_REVIEW,
    ADMIN_CHOOSE_ACTION_FOR_USER,
    ADMIN_USERS_LIST,
    ADMIN_EDIT_REVIEW_GET_USER_LIST,
    ADMIN_EDIT_REVIEW_GET_THEME_LIST,
    ADMIN_EDIT_REVIEW_GET_REVIEW_LIST,
    ADMIN_EDIT_REVIEW_GET_REVIEW_INFO,
    ADMIN_EDIT_REVIEW_CHANGE_REVIEW,
    ADMIN_SET_PASSED_REVIEW,
    ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS,
    ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST,
    ADMIN_SET_PASSED_REVIEW_RESULT,
    ADMIN_SET_THEME_ADDED_USER,
    ADMIN_UNBLOCK_USER_TAKE_REVIEW,
    EXAMINER_ADD_NEW_STUDENT_REVIEW,
    EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT,
    EXAMINER_CHOOSE_OLD_STUDENT_REVIEW_TO_EDIT,
    EXAMINER_FREE_THEMES_LIST,
    EXAMINER_GET_INFO_LAST_REVIEW,
    EXAMINER_USERS_LIST_FROM_DB,
    EXAMINER_ADD_NEW_STUDENT,
    USER_TAKE_REVIEW_ADD_THEME,
    USER_TAKE_REVIEW_ADD_DATE,
    USER_PASS_REVIEW_ADD_THEME,
    USER_PASS_REVIEW_GET_LIST_REVIEW,
    USER_PASS_REVIEW_ADD_STUDENT_REVIEW,
    USER_CANCEL_REVIEW,
    USER_START_REVIEW_HANGOUTS_LINK,
    USER_START_CHOOSE_REVIEW,
    USER_START_REVIEW_RULES,
    USER_START_REVIEW_CORE,
    USER_FEEDBACK_REVIEW_RATING,
    USER_FEEDBACK_REVIEWER_RATING,
    USER_FEEDBACK_CONFIRMATION,
    USER_FEEDBACK_COMMENT,
    SELECTING_REVIEW_TO_DELETE,
    REVIEWER_DELETE_REVIEW
}
