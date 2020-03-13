package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 保留学籍  申请人信息
 */
@Data
@Component
public class keepSchoolApplyEntity {
    private String applyInformation;
    private String applicantSignature;
    private String applyDate;
}
