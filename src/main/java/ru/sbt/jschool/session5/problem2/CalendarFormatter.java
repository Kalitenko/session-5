package ru.sbt.jschool.session5.problem2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import static ru.sbt.jschool.session5.problem2.JSONFormatterImpl.repeatString;

/**
 * Created by 1 on 13.04.2018.
 */
public class CalendarFormatter implements JSONTypeFormatter<Calendar> {
    @Override
    public String format(Calendar calendar, JSONFormatter formatter, Map<String, Object> ctx) {

        int numberOfIndents = (int)ctx.get("numberOfIndents");
        StringBuilder sb = new StringBuilder(repeatString(" ", numberOfIndents));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        sb.append(dateFormat.format(calendar.getTime()));

        return sb.toString();
    }
}
