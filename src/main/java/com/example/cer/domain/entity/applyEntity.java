package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * 申请人信息
 */
@Data
@Component
public class applyEntity {
    private String applyInformation;
    private String applicantTelephone;
    private String applicantSignature;
    private String applyDate;
    private String applyDateYear;
    private String applyDateMonth;
    private String applyDateDay;
}
