package org.example.workingmoney.config.validation;

import org.example.workingmoney.common.AppErrorRegistry;
import org.example.workingmoney.service.common.exception.ExceptionDescribable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UniqueErrorCodeValidatorTest {

    @Test
    void exception_code_중복되지_않는지_테스트() {
        Map<Integer, List<String>> codeToTypes = new HashMap<>();
        for (ExceptionDescribable type : AppErrorRegistry.all()) {
            codeToTypes.computeIfAbsent(type.code(), k -> new ArrayList<>())
                .add(type.getClass().getSimpleName() + "." + String.valueOf(type));
        }

        List<String> duplicates = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry : codeToTypes.entrySet()) {
            if (entry.getValue().size() > 1) {
                duplicates.add(entry.getKey() + " -> " + entry.getValue());
            }
        }

        assertTrue(duplicates.isEmpty(), "Duplicate error codes detected: " + duplicates);
    }
}


