package com.example.cer;

import com.example.cer.domain.service.redisService;
import com.example.cer.utils.PdfUtil;
import com.example.cer.domain.service.PdfBuilderService;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
class CerApplicationTests {

    @Autowired
    private PdfBuilderService pdfBuilderService;
    @Autowired
    private PdfUtil pdfUtil;
    @Autowired
    private redisService redisService;



//    @Test
//    void contextLoads() {
//        redisService.set("cer:txsp:0565656:sqrqz",456);
//    }
//
//    @Test
//    void getTemplate(){
//        String pdfLocalPath = "E:/JavaCode/SY/cer/pdf/comeBackSchool/06.pdf.pdf";
//        String a = pdfLocalPath.substring(pdfLocalPath.lastIndexOf("/")+1);
//        System.out.println(a);

}
