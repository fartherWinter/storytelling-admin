//package com.chennian.storytelling.common.factory;
//
//
//import com.chennian.storytelling.common.bean.MoviePayResult;
//import com.chennian.storytelling.common.enums.PayApiType;
//import com.chennian.storytelling.common.utils.pay.MovieCardIPayUtil;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class PayApiCreate {
//
////    public static MoviePayResult getPayResult(String payType) {
////        try {
////            String path = PayApiType.getPathByMark(payType);
////            return (MoviePayResult) Class.forName(path).getDeclaredAnnotations(MovieCardIPayUtil.class).newInstance();
////        } catch (Exception e) {
////            log.info(e.toString());
////            return null;
////        }
////
////    }
//}
