package com.example.cer.domain.service;

import com.example.cer.domain.dto.SSApprovalFormDto;
import com.example.cer.utils.FileUtil;
import com.example.cer.utils.ImageUtil;
import com.example.cer.utils.PdfUtil;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : liji
 * @date : 2020-03-28 13:22
 */
@Slf4j
@Service
public class xxspService {
    private final static float fontSize = 10.6f;
    private redisService redisService;

    @Autowired
    public xxspService(redisService redisService) {
        this.redisService = redisService;
    }

    private static boolean check(String item){
        return !"".equals(item) && item != null;
    }
    private static PdfFont setFont(){
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return font;
    }

    @Data
    static class item{
        /**
         * 1代表电子签名,0代表身份证
         */
        private int isSignature;
        private String key;
        private String value;
        item(String key, String value){
            setKey(key);
            setValue(value);
        }
        item(String key, String value,int isSignature){
            setKey(key);
            setValue(value);
            setIsSignature(isSignature);
        }
    }

    xxspService xxspInfo(Map<String, PdfFormField> formFieldMap, SSApprovalFormDto ssApprovalFormDto){
        List<item> listInfo = new ArrayList<>();
        List<item> date = new ArrayList<>();
        listInfo.add(new item("name",ssApprovalFormDto.getBasicInformation().getStudentName()));
        listInfo.add(new item("xb",ssApprovalFormDto.getBasicInformation().getSex()));
        listInfo.add(new item("mz",ssApprovalFormDto.getBasicInformation().getNation()));
        listInfo.add(new item("csrq",ssApprovalFormDto.getBasicInformation().getBirthday()));
        listInfo.add(new item("number",ssApprovalFormDto.getBasicInformation().getStudentNumber()));
        listInfo.add(new item("college",ssApprovalFormDto.getBasicInformation().getCollege()));
        listInfo.add(new item("major",ssApprovalFormDto.getBasicInformation().getMajor()));
        listInfo.add(new item("rxsj",ssApprovalFormDto.getBasicInformation().getEnterSchoolTime()));
        listInfo.add(new item("xxsj",ssApprovalFormDto.getBasicInformation().getXxTime()));
        listInfo.add(new item("fxsj",ssApprovalFormDto.getBasicInformation().getComeBackSchoolTime()));
        listInfo.add(new item("xxqx",ssApprovalFormDto.getBasicInformation().getLimitTime()));
        listInfo.add(new item("lyss",ssApprovalFormDto.getBasicInformation().getProvinceAndCity()));
        listInfo.add(new item("fxsq",ssApprovalFormDto.getApply().getApplyInformation()));
        listInfo.add(new item("lxdh",ssApprovalFormDto.getApply().getApplicantTelephone()));
        listInfo.add(new item("gx",ssApprovalFormDto.getParentOpinion().getStudentRelation()));
        listInfo.add(new item("jzlxdh",ssApprovalFormDto.getParentOpinion().getParentTelephone()));
        date.add(new item("1",ssApprovalFormDto.getApply().getApplyDate()));
        date.add(new item("2",ssApprovalFormDto.getParentOpinion().getParentDate()));
        date.add(new item("3",ssApprovalFormDto.getInstructorOpinion().getInstructorDate()));
        date.add(new item("4",ssApprovalFormDto.getCollegeOpinion().getCollegeDate()));
        date.add(new item("5",ssApprovalFormDto.getSchoolOpinion().getSchoolDate()));
        for (item it : listInfo){
            if (check(it.getValue()))
                formFieldMap.get(it.getKey()).setFont(setFont()).setFontSize(fontSize).setValue(it.getValue());
        }
        for (item it : date){
            if (check(it.getValue())){
                String[] a = it.getValue().split("-");
                formFieldMap.get("year" + it.getKey()).setFont(setFont()).setFontSize(fontSize).setValue(a[0]);
                formFieldMap.get("month" + it.getKey()).setFont(setFont()).setFontSize(fontSize).setValue(a[1]);
                formFieldMap.get("day" + it.getKey()).setFont(setFont()).setFontSize(fontSize).setValue(a[2]);
            }
        }
        return this;
    }

    void xxspImage(Map<String, PdfFormField> formFieldMap, SSApprovalFormDto ssApprovalFormDto) {
        List<item> listImg = new ArrayList<>();
        listImg.add(new item("sqrqm", ssApprovalFormDto.getApply().getApplicantSignature(), 1));
        listImg.add(new item("jzqm", ssApprovalFormDto.getParentOpinion().getParentSignature(), 1));
        listImg.add(new item("fdyqm", ssApprovalFormDto.getInstructorOpinion().getInstructorSignature(), 1));
        listImg.add(new item("xyqm", ssApprovalFormDto.getCollegeOpinion().getCollegeSignature(), 1));
        listImg.add(new item("xxqm", ssApprovalFormDto.getSchoolOpinion().getSchoolSignature(), 1));
        listImg.add(new item("sfzzm", ssApprovalFormDto.getParentOpinion().getParentIdCard_Zheng(), 0));
        listImg.add(new item("sfzfm", ssApprovalFormDto.getParentOpinion().getParentIdCard_Fan(), 0));
        for (item it : listImg) {
            if (check(it.getValue())) {
                if (it.getIsSignature() == 1) {
                    //生成竖版签字
                    ByteArrayOutputStream out = PdfUtil.RotationImage(PdfUtil.SvgToPng(it.getValue()));//生成横版签字
                    String base64 = FileUtil.byteToBase64(out.toByteArray());
                    redisService.set("cer:xxsp:" + FileUtil.getFileNameNoEx(ssApprovalFormDto.getPdfName()) + ":" + it.getKey(),base64);
                    formFieldMap.get(it.getKey()).setValue(base64);
                } else {
                    InputStream inputStream = ImageUtil.getImageStream(it.getValue());
                    String base64 = null;
                    if (inputStream != null)
                        base64 = FileUtil.getBase64FromInputStream(inputStream);
                    if (base64 != null){
                        redisService.set("cer:xxsp:" + FileUtil.getFileNameNoEx(ssApprovalFormDto.getPdfName()) + ":" + it.getKey(),base64);
                        formFieldMap.get(it.getKey()).setValue(base64);
                    }
                }
            }
        }
    }
}
