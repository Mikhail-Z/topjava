package ru.javawebinar.topjava;

import org.hamcrest.Matchers;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.ErrorType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TestUtil {

    public static void mockAuthorize(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new AuthorizedUser(user), null, user.getRoles()));
    }

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    public static RequestPostProcessor userAuth(User user) {
        return SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
    }

    public static ResultMatcher errorType(ErrorType errorType) {
        return jsonPath("$.type", Matchers.is(errorType.name()));
    }
}
