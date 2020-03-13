package com.example.cer;

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



    @Test
    void contextLoads() {
//        String a = FileUtil.getFileNameNoEx("http://mt.qiniu.timkj.cn/1_o5pPPjg7st2h2yQ3DtbfT2HF9fUE.jpg");
//        System.out.println(a);
//        pdfBuilderService.SvgToPng.apply("http://mt.qiniu.timkj.cn/1_o5pPPjg7st2h2yQ3DtbfT2HF9fUE.jpg");
    }

    @Test
    void getTemplate(){

    }
}
