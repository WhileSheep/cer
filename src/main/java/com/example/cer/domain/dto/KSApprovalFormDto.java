package com.example.cer.domain.dto;

import com.example.cer.domain.entity.*;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 保留学籍审批表
 */
@Data
@Component
public class KSApprovalFormDto {
    private KSBasicInformationEntity basicInformation;
    private keepSchoolApplyEntity apply;
    private parentOpinionEntity parentOpinion;
    private instructorOpinionEntity instructorOpinion;
    private collegeOpinionEntity collegeOpinion;
    private eduOpinionEntity eduOpinion;
    private stuOpinionEntity stuOpinion;
    private String studentSignature;
}
