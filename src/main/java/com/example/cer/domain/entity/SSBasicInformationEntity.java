package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;


/**
 * 休学审批表 基本信息
 */
@Data
@Component
public class SSBasicInformationEntity {
    private String studentName;
    private String sex;
    private String nation;
    private String birthday;
    private String studentNumber;
    private String college;
    private String major;
    private String enterSchoolTime;
    private String xxTime;
    private String comeBackSchoolTime;
    private String limitTime;
    private String provinceAndCity;
}
