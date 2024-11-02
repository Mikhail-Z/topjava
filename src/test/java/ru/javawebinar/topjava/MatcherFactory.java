package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Factory for creating test matchers.
 * <p>
 * Comparing actual and expected objects via AssertJ
 * Support converting json MvcResult to objects for comparation.
 */
public class MatcherFactory {

    public static <T> Matcher<T> usingIgnoringFieldsComparator(Class<T> clazz, String... fieldsToIgnore) {
        return usingAssertions(clazz,
                (a, b) -> assertThat(a).usingRecursiveComparison().ignoringFields(fieldsToIgnore).isEqualTo(b),
                (a, b) -> assertThat(a).usingRecursiveFieldByFieldElementComparatorIgnoringFields(fieldsToIgnore).isEqualTo(b));
    }

    public static <T> Matcher<T> usingAssertions(Class<T> clazz, BiConsumer<T, T> assertionForSingle, BiConsumer<Iterable<T>, Iterable<T>> assertionForIterable) {
        return new Matcher<>(clazz, assertionForSingle, assertionForIterable);
    }

    public static class Matcher<T> {
        private final Class<T> clazz;
        private final BiConsumer<T, T> assertionForSingle;
        private final BiConsumer<Iterable<T>, Iterable<T>> assertionForIterable;

        private Matcher(Class<T> clazz, BiConsumer<T, T> assertionForSingle, BiConsumer<Iterable<T>, Iterable<T>> assertionForIterable) {
            this.clazz = clazz;
            this.assertionForSingle = assertionForSingle;
            this.assertionForIterable = assertionForIterable;
        }

        public void assertMatch(T actual, T expected) {
            assertionForSingle.accept(actual, expected);
        }

        @SafeVarargs
        public final void assertMatch(Iterable<T> actual, T... expected) {
            assertionForIterable.accept(actual, List.of(expected));
        }

        public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
            assertionForIterable.accept(actual, expected);
        }

        public ResultMatcher contentJson(T expected) {
            return result -> assertMatch(JsonUtil.readValue(getContent(result), clazz), expected);
        }

        @SafeVarargs
        public final ResultMatcher contentJson(T... expected) {
            return contentJson(List.of(expected));
        }

        public ResultMatcher contentJson(Iterable<T> expected) {
            return result -> assertMatch(JsonUtil.readValues(getContent(result), clazz), expected);
        }

        public T readFromJson(ResultActions action) throws UnsupportedEncodingException {
            return JsonUtil.readValue(getContent(action.andReturn()), clazz);
        }

        private static String getContent(MvcResult result) throws UnsupportedEncodingException {
            return result.getResponse().getContentAsString();
        }
    }
}
