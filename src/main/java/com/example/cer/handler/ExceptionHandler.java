package com.example.cer.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/Exception")
public class ExceptionHandler {

    @RequestMapping("/NullPointer")
    public ResponseEntity<Map<String,Object>> nullExceptionHandler(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("timeStamp",new Date(System.currentTimeMillis()));
        map.put("status",HttpStatus.BAD_REQUEST);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("causeBy",request.getAttribute("data"));
        map.put("data",map1);
        map.put("errorType","NullPointer");
        map.put("message","请求数据字段与dto匹配失败,当前请求已被拦截");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }
}
