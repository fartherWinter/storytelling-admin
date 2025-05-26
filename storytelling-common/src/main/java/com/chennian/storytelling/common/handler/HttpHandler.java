package com.chennian.storytelling.common.handler;//package com.chennian.storytelling.common.handler;
//
//import cn.hutool.core.util.CharsetUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.chennian.storytelling.common.exception.StorytellingBindException;
//import com.chennian.storytelling.common.response.ServerResponseEntity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Objects;
//
///**
// * HTTP处理工具
// *
// * @author by chennian
// * @date 2023/3/14 15:46
// */
//@Component
//public class HttpHandler {
//    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    public <T> void printServerResponseToWeb(ServerResponseEntity<T> serverResponseEntity) {
//        if (serverResponseEntity == null) {
//            logger.info("print obj is null");
//            return;
//        }
//
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
//                .getRequestAttributes();
//        if (requestAttributes == null) {
//            logger.error("requestAttributes is null, can not print to web");
//            return;
//        }
//        HttpServletResponse response = requestAttributes.getResponse();
//        if (response == null) {
//            logger.error("httpServletResponse is null, can not print to web");
//            return;
//        }
//        logger.error("response error: " + serverResponseEntity.getMsg());
//        response.setCharacterEncoding(CharsetUtil.UTF_8);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        PrintWriter printWriter = null;
//        try {
//            printWriter = response.getWriter();
//            printWriter.write(objectMapper.writeValueAsString(serverResponseEntity));
//        } catch (IOException e) {
//            throw new StorytellingBindException("io 异常", e);
//        }
//    }
//
//    public <T> void printServerResponseToWeb(StorytellingBindException StorytellingBindException) {
//        if (StorytellingBindException == null) {
//            logger.info("print obj is null");
//            return;
//        }
//
//        if (Objects.nonNull(StorytellingBindException.getServerResponseEntity())) {
//            printServerResponseToWeb(StorytellingBindException.getServerResponseEntity());
//            return;
//        }
//
//        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
//        serverResponseEntity.setCode(StorytellingBindException.getCode());
//        serverResponseEntity.setMsg(StorytellingBindException.getMessage());
//        printServerResponseToWeb(serverResponseEntity);
//    }
//}
