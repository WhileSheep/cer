package com.example.cer.config;

import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : liji
 * @date : 2020-03-22 14:16
 */
@Data
public class PdfConfig {
    private String comeBackTemplatePath;
    private String suspendTemplatePath;
    private String outTemplatePath;
    private String keepTemplatePath;
    private String rootPath;
    private String comeBackPdfPath;
    private String suspendPdfPath;
    private String outPdfPath;
    private List<pdfdto> list;

    @Data
    public static class pdfdto{
        private int i;
        private String TemplatePath;
        private String PdfPath;
        private pdfdto(int i,String TemplatePath,String PdfPath){
            setI(i);
            setTemplatePath(TemplatePath);
            setPdfPath(PdfPath);
        }
    }

    public PdfConfig(){
        try {
            rootPath = new File("").getCanonicalPath();
            comeBackTemplatePath = rootPath + "/assets/template/comeBackTemplate.pdf";
            suspendTemplatePath = rootPath + "/assets/template/suspendTemplate.pdf";
            outTemplatePath = rootPath + "/assets/template/outTemplate.pdf";
            comeBackPdfPath = rootPath + "/pdf/comeBackSchool/";
            suspendPdfPath = rootPath + "/pdf/suspendSchool/";
            outPdfPath = rootPath + "/pdf/outSchool/";
            list = new ArrayList<>();
            list.add(new pdfdto(0,comeBackTemplatePath,comeBackPdfPath));
            list.add(new pdfdto(1,suspendTemplatePath,suspendPdfPath));
            list.add(new pdfdto(2,outTemplatePath,outPdfPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public pdfdto getDto(int i){
        return list.get(i);
    }
}
