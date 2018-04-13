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
        types.put(Byte.class, new PrimitiveAndWrapperFormatter());
        types.put(Short.class, new PrimitiveAndWrapperFormatter());
        types.put(Integer.class, new PrimitiveAndWrapperFormatter());
        types.put(Long.class, new PrimitiveAndWrapperFormatter());
        types.put(Float.class, new PrimitiveAndWrapperFormatter());
        types.put(Double.class, new PrimitiveAndWrapperFormatter());
        types.put(Character.class, new PrimitiveAndWrapperFormatter());
        types.put(Boolean.class, new PrimitiveAndWrapperFormatter());

        // строки
        types.put(String.class, new PrimitiveAndWrapperFormatter());

        // массивы
        types.put(Byte[].class, new ArrayFormatter());
        types.put(Short[].class, new ArrayFormatter());
        types.put(Integer[].class, new ArrayFormatter());
        types.put(Long[].class, new ArrayFormatter());
        types.put(Float[].class, new ArrayFormatter());
        types.put(Double[].class, new ArrayFormatter());
        types.put(Character[].class, new ArrayFormatter());
        types.put(Boolean[].class, new ArrayFormatter());
        types.put(Object[].class, new ArrayFormatter());

        // списки
        types.put(ArrayList.class, new CollectionFormatter());
        types.put(LinkedList.class, new CollectionFormatter());
        // множества
        types.put(HashSet.class, new CollectionFormatter());

        // объекты
        types.put(Object.class, new ObjectFormatter());


    }


    @Override public String marshall(Object obj) {
        if (obj == null)
            return "";

        StringBuilder sb = new StringBuilder("{\n");

        Map<String, Object> ctx = new HashMap<>();

        ctx.put("indent", " ");
        ctx.put("numberOfIndents", 1);

        if (!types.containsKey(obj.getClass()))
            return types.get(Object.class).format(obj, this, ctx);

        return types.get(obj.getClass()).format(obj, this, ctx);
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
        String name = formatter.marshall(((Object)i).getClass().getName());


        System.out.println(name);

        String str = formatter.marshall(i);
        System.out.println(str);

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = new GregorianCalendar(1999, 4, 30);

        System.out.println(formatter.marshall(calendar1));
        System.out.println(formatter.marshall(calendar2));

        Date date = new Date();
        date.getTime();

        System.out.println(formatter.marshall(date));
        Integer[] arrayOfInteger = {1, 3, 5, 7};

        List<Integer> listInt = new ArrayList<>(asList(arrayOfInteger));

        Set<Integer> setInt = new HashSet<>(asList(arrayOfInteger));

        System.out.println(formatter.marshall(arrayOfInteger));
        System.out.println(listInt instanceof Collection);
        System.out.println(formatter.marshall(listInt));

        System.out.println(formatter.marshall(setInt));

        Animal animal = new Animal("La kato");
        Cat cat = new Cat("Vaska");
        cat.setAge(3);

        System.out.println(formatter.marshall(animal));
        System.out.println(formatter.marshall(cat));


    }
}
