package ru.sbt.jschool.session5.problem2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.sbt.jschool.session5.problem2.JSONFormatterImpl.repeatString;

/**
 * Created by 1 on 13.04.2018.
 */
public class ObjectFormatter implements JSONTypeFormatter<Object> {
    @Override
    public String format(Object object, JSONFormatter formatter, Map<String, Object> ctx) {

        StringBuilder sb = new StringBuilder("{\n");

        Class clazz = object.getClass();
        List<Field> fields = new ArrayList<>();
        // возьмем поля класса и его родительского класса, если это не класс Object
        // и не null
        Collections.addAll(fields, clazz.getDeclaredFields());
        Class parentClass = clazz.getSuperclass();

        if(parentClass instanceof Object && parentClass != null)
            Collections.addAll(fields, parentClass.getDeclaredFields());

        int numberOfIndents = (int)ctx.get("numberOfIndents");

        String indent = repeatString(" ", numberOfIndents + 1);
        String delimeter = ",\n" + repeatString(" ", numberOfIndents + 1);

        List<String> strList = new ArrayList<>();
        StringBuilder fieldsToString = new StringBuilder();

        for(Field x : fields) {
            x.setAccessible(true);
            try {
                fieldsToString.append(x.getName()).append(": ")
                        .append(formatter.marshall(x.get(object)));
                strList.add(fieldsToString.toString());
                fieldsToString.setLength(0);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        sb.append(indent).append(String.join(delimeter, strList));

        return sb.append("\n}").toString();
    }
}
