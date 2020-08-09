package spring.app.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import spring.app.exceptions.NoNumbersEnteredException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
        String text = "https://vk.com/romanevseev";
        Assert.assertEquals(StringParser.toVkId(text), "romanevseev");
    }
}