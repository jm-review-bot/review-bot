package spring.app.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoNumbersEnteredException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class StringParserTest {

    @Test
    void toWordsArray() {
        List<String> list = Arrays.asList(StringParser.toWordsArray("154,253.dfb ks3ad   Йцукен ЁПМЛВЫс тдйиг ё\ndw12 /start"));
        System.out.println(list);
        list.forEach(System.out::println);
        // проверяем что разделение идем по пробелам, переносам и знакам пунктуации
    }

    @Test
    void toNumbersSet() throws NoNumbersEnteredException {
//        Set<Integer> set = StringParser.toNumbersSet("154,253.dfbks3ad   тдйиг ё\ndw12");
        Set<Integer> set = StringParser.toNumbersSet("щшнаечавя89765");
        System.out.println(set);
        set.forEach(System.out::println);
        // проверяем что разделение идем по пробелам, переносам и знакам пунктуации и буквам.
    }

    @Test
    void toVkIdsListTest() {
        String text = "https://vk.com/romanevseev,https://vk.com/bishunmo1 https://vk.com/id3183318,";
        Assert.assertEquals(StringParser.toVkIdsList(text), Arrays.asList("romanevseev", "bishunmo1", "3183318"));
    }
}