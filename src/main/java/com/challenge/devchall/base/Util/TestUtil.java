package com.challenge.devchall.base.Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TestUtil {
    public static boolean setFieldValue(Object o, String fieldName, Object value) {
        Field field = null;

        try {
            field = o.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return false;
        }

        field.setAccessible(true);

        try {
            field.set(o, value);
        } catch (IllegalAccessException e) {
            return false;
        }


        return true;
    }

    public static <T> T getFieldValue(Object o, String fieldName, T defaultValue) {
        Field field = null;

        try {
            field = o.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return defaultValue;
        }

        field.setAccessible(true);

        try {
            return (T) field.get(o);
        } catch (IllegalAccessException e) {
            return defaultValue;
        }
    }

    public static <T> T call(Object obj, String methodName, Object... args) {
        try {
            Class<?>[] argTypes = Arrays.stream(args)
                    .map(Object::getClass)
                    .toArray(Class<?>[]::new);

            Method method = obj.getClass().getDeclaredMethod(methodName, argTypes);
            method.setAccessible(true);
            return (T) method.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T callArr(Object obj, String methodName, Object... args) {
        return call(obj, methodName, new Object[]{args});
    }
}
