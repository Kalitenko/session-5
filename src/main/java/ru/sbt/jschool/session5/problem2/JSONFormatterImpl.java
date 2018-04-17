package ru.sbt.jschool.session5.problem2;

import ru.sbt.jschool.session5.problem2.data.Animal;
import ru.sbt.jschool.session5.problem2.data.Cat;

import java.util.*;

import static java.util.Arrays.asList;

/**
 * @author NIzhikov
 */
public class JSONFormatterImpl implements JSONFormatter {
    private Map<Class, JSONTypeFormatter> types = new HashMap<>();

    public JSONFormatterImpl() {
        // даты
        types.put(Date.class, new DateFormatter());
        types.put(GregorianCalendar.class, new CalendarFormatter());

        // классы-обертки
        types.put(Number.class, new PrimitiveAndWrapperFormatter());
        types.put(Character.class, new PrimitiveAndWrapperFormatter());
        types.put(Boolean.class, new PrimitiveAndWrapperFormatter());

        // строки
        types.put(String.class, new PrimitiveAndWrapperFormatter());

        // массивы
        types.put(Number[].class, new ArrayFormatter());
        types.put(Character[].class, new ArrayFormatter());
        types.put(Boolean[].class, new ArrayFormatter());
        types.put(Object[].class, new ArrayFormatter());

        // коллекции
        types.put(Collection.class, new CollectionFormatter());

        // объекты
        types.put(Object.class, new ObjectFormatter());
    }

    @Override public String marshall(Object obj) {
        if (obj == null)
            return "";

        StringBuilder sb = new StringBuilder("");

        Map<String, Object> ctx = new HashMap<>();

        ctx.put("indent", " ");

        if(ctx.get("numberOfIndents") == null)
            ctx.put("numberOfIndents", 1);
        else{
            int tmp = (int) ctx.get("numberOfIndents") + 1;
            ctx.put("numberOfIndents", tmp);
        }

        return marshall(obj, ctx);
    }

    public String marshall(Object obj, Map<String, Object> ctx) {
        if (types.containsKey(obj.getClass()))
            return types.get(obj.getClass()).format(obj, this, ctx);

        if (obj instanceof Collection) {
            return types.get(Collection.class).format(obj, this, ctx);
        } else if (obj instanceof Map) {
            return types.get(Map.class).format(obj, this, ctx);
        } else if (obj instanceof Number) {
            return types.get(Number.class).format(obj, this, ctx);
        }else if (obj instanceof Number[]) {
            return types.get(Number[].class).format(obj, this, ctx);
        }

        return types.get(Object.class).format(obj, this, ctx);
    }


    @Override public <T> boolean addType(Class<T> clazz, JSONTypeFormatter<T> format) {

        boolean result = false;

        if (!types.containsKey(clazz))
            result = types.put(clazz, format) != null;

        return result;
    }
    // повторение строки
    static public String repeatString(String str, int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++)
            sb.append(str);

        return sb.toString();
    }

    public static void main(String[] args) {
        JSONFormatterImpl formatter = new JSONFormatterImpl();

        int i = 9;
        double d =55.5;
        String name = formatter.marshall(((Object)i).getClass().getName());
        System.out.println(name);

        System.out.println(formatter.marshall(i));
        System.out.println(formatter.marshall(d));

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = new GregorianCalendar(1999, 4, 30);
        Date date = new Date();
        date.getTime();

        System.out.println(formatter.marshall(calendar1));
        System.out.println(formatter.marshall(calendar2));
        System.out.println(formatter.marshall(date));

        Integer[] arrayOfInteger = {1, 3, 5, 7};
        List<Integer> listInt = new ArrayList<>(asList(arrayOfInteger));
        Set<Integer> setInt = new HashSet<>(asList(arrayOfInteger));

        System.out.println(formatter.marshall(arrayOfInteger));
        System.out.println(formatter.marshall(listInt));
        System.out.println(formatter.marshall(setInt));

        Animal animal = new Animal("La kato");
        Cat cat = new Cat("Vaska");
        cat.setAge(3);

        System.out.println(formatter.marshall(animal));
        System.out.println(formatter.marshall(cat));


    }
}
