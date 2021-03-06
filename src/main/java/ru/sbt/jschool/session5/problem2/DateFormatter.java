package ru.sbt.jschool.session5.problem2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static ru.sbt.jschool.session5.problem2.JSONFormatterImpl.repeatString;

/**
 * @author NIzhikov
 */
public class DateFormatter implements JSONTypeFormatter<Date> {
    @Override public String format(Date date, JSONFormatter formatter, Map<String, Object> ctx) {

        int numberOfIndents = (int)ctx.get("numberOfIndents");
        StringBuilder sb = new StringBuilder(repeatString(" ", numberOfIndents));

        SimpleDateFormat dataFormat = new SimpleDateFormat("dd.MM.yyyy");
        sb.append(dataFormat.format(date));

        return sb.toString();

    }
}
