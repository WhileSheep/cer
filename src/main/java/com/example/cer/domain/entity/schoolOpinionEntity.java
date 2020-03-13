package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * 学校意见
 */
@Data
@Component
public class schoolOpinionEntity {
    private String schoolSignature;
    private String schoolDate;
    private String schoolDateYear;
    private String schoolDateMonth;
    private String schoolDateDay;
}
