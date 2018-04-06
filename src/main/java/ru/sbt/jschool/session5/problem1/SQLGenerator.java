package ru.sbt.jschool.session5.problem1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SQLGenerator {

    private static final String space = " ";
    private static final String commaSpace = ", ";
    private static final String questionMark = "?";
    private static final String questionMarkCommaSpace = "?, ";
    private static final String equalSignQuestionMarkSpace = " = ?";
    private static final String equalSignQuestionMarkCommaSpace = " = ?, ";
    private static final String and = " AND ";


    public <T> String insert(Class<T> clazz) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO").append(space);

        // название таблицы
        sb.append(writeTableName(clazz)).append("(");

        // названия столбцов
        StringBuilder columnNames = new StringBuilder();
        Counter count = new Counter(0);

        sb.append(writeFields(clazz, commaSpace, true, true, count, false));
        sb.append(columnNames).append(") VALUES (")
                .append(repeatString(questionMarkCommaSpace, count.getCount() - 1))
                .append(questionMark).append(")");

        return sb.toString();
    }

    public <T> String update(Class<T> clazz) {

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE").append(space);

        sb.append(writeTableName(clazz)).append(" SET ");

        // названия столбцов
        StringBuilder columnNames = new StringBuilder();
        Counter counter = new Counter(0);

        columnNames.append(writeFields(clazz, equalSignQuestionMarkCommaSpace, true,
                false, counter, false));

        sb.append(columnNames).append(equalSignQuestionMarkSpace).append(" WHERE ");
        columnNames.setLength(0);

        columnNames.append(writeFields(clazz, equalSignQuestionMarkSpace, false,
                true, counter, true));

        sb.append(columnNames);
        return sb.toString();
    }

    public <T> String delete(Class<T> clazz) {

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM").append(space);

        // название таблицы
        sb.append(writeTableName(clazz)).append(" WHERE ");

        // названия столбцов
        StringBuilder columnNames = new StringBuilder();
        Counter counter = new Counter(0);

        columnNames.append(writeFields(clazz, equalSignQuestionMarkSpace, false,
                true, counter, true));

        sb.append(columnNames);
        return sb.toString();
    }

    public <T> String select(Class<T> clazz) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT").append(space);

        // названия столбцов
        StringBuilder columnNames = new StringBuilder();
        Counter counter = new Counter(0);

        columnNames.append(writeFields(clazz, commaSpace, true,
                false, counter, false));

        sb.append(columnNames).append(" FROM ").append(writeTableName(clazz)).append(" WHERE ");
        columnNames.setLength(0);

        columnNames.append(writeFields(clazz, equalSignQuestionMarkSpace, false,
                true, counter, true));

        sb.append(columnNames);
        return sb.toString();
    }

    // повторение строки
    static private String repeatString(String str, int num){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < num; i++)
            sb.append(str);

        return sb.toString();
    }

    // название таблицы
    String writeTableName(Class clazz){

        StringBuilder sb = new StringBuilder();
        Annotation[] annotations = clazz.getAnnotations();
        for(Annotation x : annotations)
            if(x instanceof Table)
                sb.append(((Table) x).name());

        return sb.toString();
    }

    // класс счетчик
    class Counter{
        private int count;

        Counter(int count){
            this.count = count;
        }
        public void setCount(int count) {
            this.count = count;
        }
        public int getCount() {
            return count;
        }
    }

    // поля в строку
    String writeFields(Class clazz, String separator, boolean columnClass, boolean primaryKeyClass,
                       Counter counter, boolean flag){

        StringBuilder sb = new StringBuilder();

        List<String> columnsNames = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        int count = 0;
        for(Field field : fields){
            if(columnClass) {
                if (field.isAnnotationPresent(Column.class)) {
                    count++;
                    if (!field.getAnnotation(Column.class).name().isEmpty())
                        columnsNames.add(field.getAnnotation(Column.class).name().toLowerCase());
                    else
                        columnsNames.add(field.getName().toLowerCase().toLowerCase());
                }
            }
            if(primaryKeyClass){
                if(field.isAnnotationPresent(PrimaryKey.class)){
                    count++;
                    if(!field.getAnnotation(PrimaryKey.class).name().isEmpty())
                        columnsNames.add(field.getAnnotation(PrimaryKey.class).name().toLowerCase());
                    else
                        columnsNames.add(field.getName().toLowerCase().toLowerCase());
                }
            }
        }

        counter.setCount(count);
        if(count > 1 && flag) {
            for(int i = 0; i < columnsNames.size(); i++){
                String tmpString = columnsNames.get(i) + separator;
                columnsNames.set(i, tmpString);
            }
            sb.append(String.join(and, columnsNames));
        }
        else
            sb.append(String.join(separator, columnsNames));

        return sb.toString();
    }

}
