package spring.app.util;

public class Keyboards {

    public final static String HEADER_FR = "{\"one_time\": false,\n\"buttons\": [\n";

    public final static String FOOTER_FR = "]\n}";

    public final static String ROW_DELIMETER_FR = "],[";

    public final static String DELETE_STUDENT_REVIEW = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Отмена записи\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      }\n";

    public final static String REVIEW_START_FR = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Начать ревью\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      }\n";

    public final static String REVIEW_CANCEL_FR = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Отменить ревью\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      }\n";

    public final static String DEF_USER_MENU_KB = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Сдать ревью\"\n" +
            "        },\n" +
            "        \"color\": \"primary\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Принять ревью\"\n" +
            "        },\n" +
            "        \"color\": \"primary\"\n" +
            "      }\n";


    public final static String START_KB = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Начать\"\n" +
            "        },\n" +
            "        \"color\": \"default\"\n" +
            "      }\n";

    public final static String DEF_BACK_KB = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Назад\"\n" +
            "        },\n" +
            "        \"color\": \"default\"\n" +
            "      }\n";

    public final static String BACK_AND_EDIT_STATUS_KB = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Назад\"\n" +
            "        },\n" +
            "        \"color\": \"default\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Изменить статус\"\n" +
            "        },\n" +
            "        \"color\": \"primary\"\n" +
            "      }\n";

    public final static String NO_KB = "{\"buttons\":[],\"one_time\":false}";

    public final static String ADMIN_START_KB = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"/admin\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Начать\"\n" +
            "        },\n" +
            "        \"color\": \"default\"\n" +
            "      }\n";

    public final static String YES_NO_KB = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Да\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Нет\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      }\n";

    public final static String DEF_ADMIN_MENU_KB = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Добавить пользователя\"\n" +
            "        },\n" +
            "        \"color\": \"primary\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Изменить пользователя\"\n" +
            "        },\n" +
            "        \"color\": \"primary\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Удалить пользователя\"\n" +
            "        },\n" +
            "        \"color\": \"primary\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Ревью\"\n" +
            "        },\n" +
            "        \"color\": \"primary\"\n" +
            "      }\n" +
            "     ],[" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"3\\\"}\",\n" +
            "          \"label\": \"Главное меню\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      }\n";

    public final static String USER_MENU_DELETE_STUDENT_REVIEW = "{\n" +
            "  \"one_time\": true,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Отмена записи\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Назад\"\n" +
            "        },\n" +
            "        \"color\": \"default\"\n" +
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "} ";

    //TODO:удалить, раз класс в котором он использовался не соответствовал тз и был удален
    public final static String USER_TAKE_REVIEW_CONFIRMATION_KB = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Добавить\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Отменить\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Назад\"\n" +
            "        },\n" +
            "        \"color\": \"default\"\n" +
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "} ";

    public final static String USER_MENU_KB = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Главное меню\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      }";

    public final static String CHANGE_OR_NOT_ADDED_USER_FULLNAME = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"оставить имя как есть\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"ввести новое имя фамилию\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      }\n";

    public final static String CHANGE_FULLNAME_VKID_EDITING_USER_OR_BACK = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"изменить имя\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"изменить вкИд\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      }\n";

    public final static String YES_OR_CANCEL = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"да\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"отмена\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      }\n";

    public final static String RIGHT_WRONG_ANSWER = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Ответ принят\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Ответ не принят\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      }";

    public final static String PASS_OR_NOT_PASS_OR_BACK = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Пройдено\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Не пройдено\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      }\n"+
            "       ],[" +
            "           {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Назад\"\n" +
            "        },\n" +
            "        \"color\": \"default\"\n" +
            "      }\n";
}
