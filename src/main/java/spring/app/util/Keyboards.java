package spring.app.util;

public class Keyboards {
    public final static String defaultKeyboard = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Пройти опрос\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Посмотреть результаты\"\n" +
            "        },\n" +
            "        \"color\": \"default\"\n" +
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "} ";

    public final static String start = "{\n" +
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

    public final static String noKeyboard = "{\"buttons\":[],\"one_time\":true}";

    public final static String userStart = "{\n" +
            "  \"one_time\": true,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
            "          \"label\": \"Пройти опрос\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
            "          \"label\": \"Посмотреть результаты\"\n" +
            "        },\n" +
            "        \"color\": \"default\"\n" +
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "} ";

    public final static String adminMenu = "{\n" +
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
