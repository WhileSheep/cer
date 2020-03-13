package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * 学院意见
 */
@Data
@Component
public class collegeOpinionEntity {
    private String collegeSignature;
    private String collegeDate;
    private String collegeDateYear;
    private String collegeDateMonth;
    private String collegeDateDay;
}
