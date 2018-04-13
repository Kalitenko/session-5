package ru.sbt.jschool.session5.problem2;

import java.util.*;

import static ru.sbt.jschool.session5.problem2.JSONFormatterImpl.repeatString;

/**
 * Created by 1 on 13.04.2018.
 */
public class CollectionFormatter implements JSONTypeFormatter<Collection<Object>> {
    @Override
    public String format(Collection<Object> collection, JSONFormatter formatter, Map<String, Object> ctx) {

        int numberOfIndents = (int)ctx.get("numberOfIndents");
        StringBuilder sb = new StringBuilder();

        Object[] objects = collection.toArray();
        sb.append(formatter.marshall(objects));

        return sb.toString();
    }
}
