package ru.javawebinar.topjava;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Factory for creating test matchers.
 * <p>
 * Comparing actual and expected objects via AssertJ
 */
public class MatcherFactory {
    public static <T> Matcher<T> usingIgnoringFieldsComparator(String[] fieldsToIgnore, String[] fieldsWithOrderToIgnore) {
        return new Matcher<>(fieldsToIgnore, fieldsWithOrderToIgnore);
    }

    public static class Matcher<T> {
        private final String[] fieldsToIgnore;
        private final String[] fieldsWithIgnoringOrder;

        private Matcher(String[] fieldsToIgnore, String[] fieldsWithIgnoringOrder) {
            this.fieldsToIgnore = fieldsToIgnore;
            this.fieldsWithIgnoringOrder = fieldsWithIgnoringOrder;
        }

        public void assertMatch(T actual, T expected) {
            assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore).ignoringCollectionOrderInFields(fieldsWithIgnoringOrder).isEqualTo(expected);
        }

        @SafeVarargs
        public final void assertMatch(Iterable<T> actual, T... expected) {
            assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore).ignoringCollectionOrderInFields(fieldsWithIgnoringOrder).isEqualTo(List.of(expected));
        }

        public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
            assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields(fieldsToIgnore).isEqualTo(expected);
        }
    }
}