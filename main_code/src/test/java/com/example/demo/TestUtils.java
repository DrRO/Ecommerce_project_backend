package com.example.demo;

import java.lang.reflect.Field;


// TestUtils as in Udacity course video
public class TestUtils {

    public static void injectObject(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
// check if field accessible
            if(!f.isAccessible()){
                f.setAccessible(true);
                wasPrivate = true;
            }

            f.set(target, toInject);
// check uf wasPrivate
            if(wasPrivate){
                f.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
