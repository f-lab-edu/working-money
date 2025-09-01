package org.example.workingmoney.common;

import org.example.workingmoney.service.common.exception.ExceptionDescribable;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.ArrayList;
import java.util.List;

public final class AppErrorRegistry {
    private AppErrorRegistry() {}

    public static List<ExceptionDescribable> all() {
        List<ExceptionDescribable> list = new ArrayList<>();

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(ExceptionDescribable.class));

        String basePackage = "org.example.workingmoney";
        var candidates = scanner.findCandidateComponents(basePackage);
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        for (var bd : candidates) {
            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName(), false, cl);
                if (clazz.isEnum() && ExceptionDescribable.class.isAssignableFrom(clazz)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) clazz;
                    Object[] constants = enumClass.getEnumConstants();
                    for (Object constant : constants) {
                        list.add((ExceptionDescribable) constant);
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to load class: " + bd.getBeanClassName(), e);
            }
        }

        return list;
    }
}
