package com.example.cer.domain.dto;

import com.example.cer.domain.entity.*;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 退学审批表
 */
@Data
@Component
public class OSApprovalFormDto implements Dto{
    private int i;
    private String pdfName;
    private OSBasicInformationEntity basicInformation;
    private OSapplyEntity apply;
    private SSparentOpinionEntity parentOpinion;
    private instructorOpinionEntity instructorOpinion;
    private collegeOpinionEntity collegeOpinion;
    private schoolOpinionEntity schoolOpinion;
    //    private String comment;  //备注

    @Override
    public OSApprovalFormDto run() {
        this.i = 3;
        return this;
    }

}
