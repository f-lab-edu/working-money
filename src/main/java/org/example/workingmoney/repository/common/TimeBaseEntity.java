package org.example.workingmoney.repository.common;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class TimeBaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
