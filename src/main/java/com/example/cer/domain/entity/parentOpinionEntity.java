package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * 复学审批表 家长意见
 */
@Data
@Component
public class parentOpinionEntity {
    private String parentTelephone;
    private String parentSignature;
    private String parentDate;
    private String parentDateYear;
    private String parentDateMonth;
    private String parentDateDay;
}
