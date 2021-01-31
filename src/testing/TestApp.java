package testing;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestApp {
    public TestApp() {
    }

    public static void start(Class aClass) {
        List<Method> testMethods = findMethodWithAnnotation(aClass, Test.class);

        for (Method method : testMethods) {
            if (method.getAnnotation(Test.class).priority() < 1 || method.getAnnotation(Test.class).priority() > 10) {
                throw new RuntimeException("Priority must be between 1 and 10");
            }
        }

        testMethods.sort((o1, o2) -> o1.getAnnotation(Test.class).priority() - o2.getAnnotation(Test.class).priority());

        if (testMethods.isEmpty()) {
            System.out.printf("%s has no any test methods\n", aClass.getName());
        }
        Object obj = init(aClass);

        List<Method> beforeMethod = findMethodWithAnnotation(aClass, BeforeSuite.class);
        if (!beforeMethod.isEmpty() && beforeMethod.size() > 1) {
            throw new RuntimeException("BeforeSuite must be only once!");
        }
        List<Method> afterMethod = findMethodWithAnnotation(aClass, AfterSuite.class);
        if (!afterMethod.isEmpty() && afterMethod.size() > 1) {
            throw new RuntimeException("AfterSuite must be only once!");
        }
        if (!beforeMethod.isEmpty()) {
            executeAnnotations(beforeMethod.get(0), obj);
        }
        for (Method method : testMethods) {
            executeAnnotations(method, obj);
        }
        if (!afterMethod.isEmpty()) {
            executeAnnotations(afterMethod.get(0), obj);
        }
    }

    public static void start(String className) {
        try {
            start(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public static List<Method> findMethodWithAnnotation(Class aClass, Class<? extends Annotation> annotation) {
        List<Method> testMethods = new ArrayList<>();
        for (Method method : aClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                testMethods.add(method);
            }
        }
        return testMethods;
    }

    public static void executeAnnotations(Method method, Object obj, Object... args) {
        try {
            method.setAccessible(true);
            method.invoke(obj, args);
            method.setAccessible(false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public static Object init(Class aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException
                | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException("SWW", e);
        }
    }
}
