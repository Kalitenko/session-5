package ru.sbt.jschool.session5.problem2;

import java.util.Map;

/**
 */
@FunctionalInterface
public interface JSONTypeFormatter<T> {
    String format(T t, JSONFormatter formatter, Map<String, Object> ctx);
}
