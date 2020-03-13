package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * 辅导员意见
 */
@Data
@Component
public class instructorOpinionEntity {
    private String instructorSignature;
    private String instructorDate;
    private String instructorDateYear;
    private String instructorDateMonth;
    private String instructorDateDay;
}
