package com.example.cer.controller;

import com.example.cer.domain.dto.CBSApprovalFormDto;
import com.example.cer.domain.dto.KSApprovalFormDto;
import com.example.cer.domain.dto.OSApprovalFormDto;
import com.example.cer.domain.dto.SSApprovalFormDto;
import com.example.cer.domain.service.ApprovalFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;



@RestController
@RequestMapping("/apply")
public class ApprovalFormController {

    private ApprovalFormService ApprovalFormService;


    @Autowired
    public ApprovalFormController(ApprovalFormService ApprovalFormService) {
        this.ApprovalFormService = ApprovalFormService;
    }


    /**
     * 复学
     */
    @RequestMapping(value = "/comeBackSchool",method = RequestMethod.POST)
    public void comeBackSchool(@RequestBody List<CBSApprovalFormDto> cBSApprovalFormDtos){
        ApprovalFormService.cBSDataProcessing(cBSApprovalFormDtos);
    }

    /**
     * 休学
     */
    @RequestMapping(value = "/suspendSchool",method = RequestMethod.POST)
    public void suspendSchool(@RequestBody List<SSApprovalFormDto> sSApprovalFormDtos){
        ApprovalFormService.sSDataProcessing(sSApprovalFormDtos);
    }

    /**
     * 退学
     */
    @RequestMapping(value = "/outSchool",method = RequestMethod.POST)
    public void outSchool(@RequestBody List<OSApprovalFormDto> oSApprovalFormDtos){
        ApprovalFormService.outDataProcessing(oSApprovalFormDtos);
    }

    /**
     * 保留学籍
     */
    @RequestMapping(value = "/keepSchool",method = RequestMethod.POST)
    public ResponseEntity keepSchool(@RequestBody List<KSApprovalFormDto> ksApprovalFormDtos) {
        ApprovalFormService.keepDataProcessing(ksApprovalFormDtos);
        return null;
    }
}
