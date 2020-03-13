package com.example.cer.qiNiu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author : liji
 * @date : 2020-03-12 15:49
 */
@Slf4j
@Component
public class qiniuUpload {
    private static String accessKey = "MJJWYQuaq2gM8dm_6YAOrVw-6Os9_67duOgHeQBa";
    private static String secretKey = "CnI5kgMC8ZUnsgYVmjn4zOdwV9YRF6pbveAk68zx";
    static String bucket = "mt-timkj-cn";


    @Async
    public void uploadPdf(String pdfLocalPath){
        log.info(Thread.currentThread().getName() + " upload内异步方法开始" + Thread.currentThread().isAlive());
        Configuration cfg = new Configuration(Region.region1());

        UploadManager uploadManager = new UploadManager(cfg);

        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(pdfLocalPath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info("上传返回结果:" + putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        log.info(Thread.currentThread().getName() + " upload内异步方法结束" + Thread.currentThread().isAlive());
    }
}
