package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 退学审批表 申请信息
 * @author : liji
 * @date : 2020-03-22 13:52
 */
@Data
@Component
public class OSapplyEntity {
    private String applyInformation;
    private String applicantTelephone;
    private String applicantSignature;
    private String studentIdCard_Zheng;
    private String studentIdCard_Fan;
    private String applyDate;
    private String applyDateYear;
    private String applyDateMonth;
    private String applyDateDay;
}
