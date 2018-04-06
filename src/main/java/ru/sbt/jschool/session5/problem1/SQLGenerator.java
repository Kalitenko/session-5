package ru.sbt.jschool.session5.problem1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 */


public class SQLGenerator {

    public static final String space = " ";
    public static final String commaAndSpace = ", ";

    public <T> String insert(Class<T> clazz) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO").append(space);

        // название таблицы
        sb.append(writeTableName(clazz)).append("(");

        // названия столбцов
        StringBuilder columnNames = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        int fieldsLength = fields.length;
        int count = 0;
        for(Field field : fields){
            fieldsLength--;
            if(field.isAnnotationPresent(Column.class)){
                count++;
                if(!field.getAnnotation(Column.class).name().isEmpty()){
                    columnNames.append(field.getAnnotation(Column.class).name().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(commaAndSpace);
                } else {
                    columnNames.append(field.getName().toLowerCase().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(commaAndSpace);
                }
            } else if(field.isAnnotationPresent(PrimaryKey.class)){
                count++;
                if(!field.getAnnotation(PrimaryKey.class).name().isEmpty()){
                    columnNames.append(field.getAnnotation(PrimaryKey.class).name().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(commaAndSpace);
                } else {
                    columnNames.append(field.getName().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(commaAndSpace);
                }
            }
        }

        sb.append(columnNames).append(") VALUES (").append(repeatString("?, ", count - 1)).append("?)");

        return sb.toString();
    }

    public <T> String update(Class<T> clazz) {

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE").append(space);

        sb.append(writeTableName(clazz)).append(" SET ");


        // названия столбцов
        StringBuilder columnNames = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        int fieldsLength = fields.length;

        for(Field field : fields){
            fieldsLength--;
            if(field.isAnnotationPresent(Column.class)){
                if(!field.getAnnotation(Column.class).name().isEmpty()){
                    columnNames.append(field.getAnnotation(Column.class).name().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(" = ?, ");
                } else {
                    columnNames.append(field.getName().toLowerCase().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(" = ?, ");
                }
            }
        }

        sb.append(columnNames).append(" = ?").append(" WHERE ");
        columnNames.setLength(0);

        fieldsLength = fields.length;
        int count = 0;
        for(Field field : fields){
            fieldsLength--;
            if(field.isAnnotationPresent(PrimaryKey.class)){
                if(fieldsLength > 0 && count > 0)
                    columnNames.append(" AND ");
                count++;
                if(!field.getAnnotation(PrimaryKey.class).name().isEmpty()){
                    columnNames.append(field.getAnnotation(PrimaryKey.class).name().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(" = ?");
                } else {
                    columnNames.append(field.getName().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(" = ?");
                }
            }
        }
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
        Field[] fields = clazz.getDeclaredFields();
        int fieldsLength = fields.length;
        int count = 0;
        for(Field field : fields){
            fieldsLength--;
            if(field.isAnnotationPresent(PrimaryKey.class)){
                if(fieldsLength > 0 && count > 0)
                    columnNames.append(" AND ");
                count++;
                if(!field.getAnnotation(PrimaryKey.class).name().isEmpty()){
                    columnNames.append(field.getAnnotation(PrimaryKey.class).name().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(" = ?");
                } else {
                    columnNames.append(field.getName().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(" = ?");
                }
            }
        }
        sb.append(columnNames);

        return sb.toString();
    }

    public <T> String select(Class<T> clazz) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT").append(space);

        // названия столбцов
        StringBuilder columnNames = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        int fieldsLength = fields.length;

        for(Field field : fields){
            fieldsLength--;
            if(field.isAnnotationPresent(Column.class)){
                if(!field.getAnnotation(Column.class).name().isEmpty()){
                    columnNames.append(field.getAnnotation(Column.class).name().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(", ");
                } else {
                    columnNames.append(field.getName().toLowerCase().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(", ");
                }
            }
        }

        sb.append(columnNames).append(" FROM ").append(writeTableName(clazz)).append(" WHERE ");
        columnNames.setLength(0);

        // названия столбцов
        fieldsLength = fields.length;
        int count = 0;
        for(Field field : fields){
            fieldsLength--;
            if(field.isAnnotationPresent(PrimaryKey.class)){
                if(fieldsLength > 0 && count > 0)
                    columnNames.append(" AND ");
                count++;
                if(!field.getAnnotation(PrimaryKey.class).name().isEmpty()){
                    columnNames.append(field.getAnnotation(PrimaryKey.class).name().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(" = ?");
                } else {
                    columnNames.append(field.getName().toLowerCase());
                    if(fieldsLength != 0)
                        columnNames.append(" = ?");
                }
            }
        }
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

}
