package com.mdbank.util;

import com.mdbank.dao.AbstractDao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public final class ReflectionUtils {
    public static Map<String, Field> getAnnotatedFieldsMap(Class clazz, Class... annotations) {
        Map<String, Field> result = new HashMap<>();

        while (clazz != null && clazz != Object.class) {

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                for (Class annotation : annotations) {

                    //noinspection unchecked
                    if (field.getAnnotation(annotation) != null) {
                        result.put(field.getName(), field);
                    }
                }
                field.setAccessible(false);
            }
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    public static <E extends AbstractDao> Class getFirstTypeParameterClass(Class<E> clazz) {
        ParameterizedType paramType = (ParameterizedType) clazz.getGenericSuperclass();
        return (Class) paramType.getActualTypeArguments()[0];
    }

    public static <E extends AbstractDao> Class getSecondTypeParameterClass(Class<E> clazz) {
        ParameterizedType paramType = (ParameterizedType) clazz.getGenericSuperclass();
        return (Class) paramType.getActualTypeArguments()[1];
    }

    private ReflectionUtils() {

    }
}
