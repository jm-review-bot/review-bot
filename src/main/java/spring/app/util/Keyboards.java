package spring.app.util;

public class Keyboards {

    public final static String USER_MENU_KB = "{\n" +
            "  \"one_time\": true,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
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
            "        \"color\": \"positive\"\n" +
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "} ";

    public final static StringBuilder HEADER_FR = new StringBuilder("{\"one_time\": true,\n\"buttons\": [\n[\n");

    public final static StringBuilder FOOTER_FR = new StringBuilder("]\n]\n}");

    public final static StringBuilder ROW_DELIMETER_FR = new StringBuilder("],[");

    public final static StringBuilder REVIEW_START_FR = new StringBuilder("{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Начать прием ревью\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      }\n");

    public final static StringBuilder REVIEW_CANCEL_FR = new StringBuilder("{\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Отменить ревью\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      }\n");

    public final static StringBuilder USER_MENU_FR = new StringBuilder("{\n" +
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
                    "      }\n");

    public final static String START_KB = "{\n" +
            "  \"one_time\": true,\n" +
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

    public final static String NO_KB = "{\"buttons\":[],\"one_time\":true}";

    public final static String ADMIN_START_KB = "{\n" +
            "  \"one_time\": true,\n" +
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
            "  \"one_time\": true,\n" +
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
            "  \"one_time\": true,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Добавить пользователя\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Удалить пользователя\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "} ";

}
