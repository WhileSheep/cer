package com.example.cer.controller;

import com.example.cer.domain.dto.CBSApprovalFormDto;
import com.example.cer.domain.dto.KSApprovalFormDto;
import com.example.cer.domain.dto.OSApprovalFormDto;
import com.example.cer.domain.dto.SSApprovalFormDto;
import com.example.cer.domain.service.ApprovalFormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Slf4j
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
    @RequestMapping(value = "/fxsp",method = RequestMethod.POST)
    public void comeBackSchool(@RequestBody List<CBSApprovalFormDto> cBSApprovalFormDtos){
        log.info(Thread.currentThread().getName());
        ApprovalFormService.cBSDataProcessing(cBSApprovalFormDtos);
    }

    /**
     * 休学
     */
    @RequestMapping(value = "/xxsp",method = RequestMethod.POST)
    public void suspendSchool(@RequestBody List<SSApprovalFormDto> sSApprovalFormDtos){
        log.info(Thread.currentThread().getName());
        ApprovalFormService.sSDataProcessing(sSApprovalFormDtos);
    }

    /**
     * 退学
     */
    @RequestMapping(value = "/txsp",method = RequestMethod.POST)
    public void outSchool(@RequestBody List<OSApprovalFormDto> oSApprovalFormDtos){
        log.info(Thread.currentThread().getName());
        ApprovalFormService.outDataProcessing(oSApprovalFormDtos);
    }

    /**
     * 保留学籍
     */
    @RequestMapping(value = "/blxj",method = RequestMethod.POST)
    public ResponseEntity keepSchool(@RequestBody List<KSApprovalFormDto> ksApprovalFormDtos) {
        log.info(Thread.currentThread().getName());
        ApprovalFormService.keepDataProcessing(ksApprovalFormDtos);
        return null;
    }
}
