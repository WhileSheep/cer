package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 休学审批表 和 退学审批表  家长意见
 * @author : liji
 * @date : 2020-03-22 13:47
 */
@Data
@Component
public class SSparentOpinionEntity {
    private String parentTelephone;
    private String parentSignature;
    private String studentRelation;
    private String parentIdCard_Zheng;
    private String parentIdCard_Fan;
    private String parentDate;
    private String parentDateYear;
    private String parentDateMonth;
    private String parentDateDay;
}
