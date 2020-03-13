package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 已修课程
 */
@Data
@Component
public class courseEntity {
    private String cGrade;
    private String semester;
}
