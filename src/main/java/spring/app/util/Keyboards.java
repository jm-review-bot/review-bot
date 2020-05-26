package spring.app.util;

public class Keyboards {

    public final static String HEADER_FR = "{\"one_time\": false,\n\"buttons\": [\n[\n";

    public final static String FOOTER_FR = "]\n]\n}";

    public final static String ROW_DELIMETER_FR = "],[";

    public final static String REVIEW_START_FR = "{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Начать прием ревью\"\n" +
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

    public final static String USER_MENU_FR = "{\n" +
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
            "  \"one_time\": false,\n" +
            "  \"buttons\": [[\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Начать\"\n" +
            "        },\n" +
            "        \"color\": \"default\"\n" +
            "      }\n" +
            "  ]]\n" +
            "} ";

    public final static String BACK_KB = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [[\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Назад\"\n" +
            "        },\n" +
            "        \"color\": \"default\"\n" +
            "      }\n" +
            "  ]]\n" +
            "} ";

    public final static String NO_KB = "{\"buttons\":[],\"one_time\":false}";

    public final static String ADMIN_START_KB = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
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
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "} ";

    public final static String YES_NO_KB = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
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
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "} ";

    public final static String ADMIN_MENU_KB = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
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
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Удалить пользователя\"\n" +
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
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "} ";

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
            "  \"one_time\": false,\n" +
            "  \"buttons\": [[\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Главное меню\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      }\n" +
            "  ]]\n" +
            "} ";

    public final static String FEEDBACK_CONFIRM_KB = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Начать\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Отказаться\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "} ";

    public final static String CHANGE_OR_NOT_ADDED_USER_FULLNAME = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
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
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "} ";


}
