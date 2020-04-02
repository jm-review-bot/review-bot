package spring.app.service;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.app.exceptions.IncorrectVkIdsException;
import spring.app.model.User;
import spring.app.service.abstraction.VkService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class VkServiceTest {

    @Autowired
    private VkService vkService;


    @Test
    void testGetUserInfo() throws ClientException, ApiException {
        List<String> incorrectVkIds = Arrays.asList("87583", "revseeeeev");
        List<String> correctVkIds = Arrays.asList("3808497", "vednn");

        assertThrows(IncorrectVkIdsException.class, () -> {
            List<User> resultUsers = vkService.newUsersFromVk(incorrectVkIds);
            resultUsers.forEach(System.out::println);
        });

        assertDoesNotThrow(() -> {
            List<User> resultUsers = vkService.newUsersFromVk(correctVkIds);
            resultUsers.forEach(System.out::println);
        });
    }
}