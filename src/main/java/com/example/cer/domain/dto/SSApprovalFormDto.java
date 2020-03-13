package com.example.cer.domain.dto;

import com.example.cer.domain.entity.*;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 休学审批表
 */
@Data
@Component
public class SSApprovalFormDto {
    private SSBasicInformationEntity basicInformation;
    private applyEntity apply;
    private parentOpinionEntity parentOpinion;
    private instructorOpinionEntity instructorOpinion;
    private collegeOpinionEntity collegeOpinion;
    private schoolOpinionEntity schoolOpinion;
    private String studentSignature;
}
