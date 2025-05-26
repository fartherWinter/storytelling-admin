package com.chennian.storytelling.common.bean;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author chennian
 * @Date 2024/4/23 10:25
 */
@NoArgsConstructor
@Data
public class WeChatPayComment implements Serializable {

    /**
     * errcode
     */
    @SerializedName("errcode")
    private Integer errcode;
    /**
     * success
     */
    @SerializedName("success")
    private Boolean success;
    /**
     * commentList
     */
    @SerializedName("commentList")
    private List<CommentListDTO> commentList;
    /**
     * total
     */
    @SerializedName("total")
    private Integer total;
    /**
     * offset
     */
    @SerializedName("offset")
    private Integer offset;

    /**
     * CommentListDTO
     */
    @NoArgsConstructor
    @Data
    public static class CommentListDTO {
        /**
         * commentId
         */
        @SerializedName("commentId")
        private String commentId;
        /**
         * amount
         */
        @SerializedName("amount")
        private Integer amount;
        /**
         * orderId
         */
        @SerializedName("orderId")
        private String orderId;
        /**
         * payTime
         */
        @SerializedName("payTime")
        private String payTime;
        /**
         * wxPayId
         */
        @SerializedName("wxPayId")
        private String wxPayId;
        /**
         * orderInfo
         */
        @SerializedName("orderInfo")
        private OrderInfoDTO orderInfo;
        /**
         * userInfo
         */
        @SerializedName("userInfo")
        private UserInfoDTO userInfo;
        /**
         * bizInfo
         */
        @SerializedName("bizInfo")
        private BizInfoDTO bizInfo;
        /**
         * score
         */
        @SerializedName("score")
        private Integer score;
        /**
         * createTime
         */
        @SerializedName("createTime")
        private String createTime;
        /**
         * content
         */
        @SerializedName("content")
        private ContentDTO content;
        /**
         * extInfo
         */
        @SerializedName("extInfo")
        private ExtInfoDTO extInfo;
        /**
         * productInfo
         */
        @SerializedName("productInfo")
        private ProductInfoDTO productInfo;

        /**
         * OrderInfoDTO
         */
        @NoArgsConstructor
        @Data
        public static class OrderInfoDTO {
            /**
             * busiOrderId
             */
            @SerializedName("busiOrderId")
            private String busiOrderId;
        }

        /**
         * UserInfoDTO
         */
        @NoArgsConstructor
        @Data
        public static class UserInfoDTO {
            /**
             * openid
             */
            @SerializedName("openid")
            private String openid;
            /**
             * headImg
             */
            @SerializedName("headImg")
            private String headImg;
            /**
             * nickName
             */
            @SerializedName("nickName")
            private String nickName;
        }

        /**
         * BizInfoDTO
         */
        @NoArgsConstructor
        @Data
        public static class BizInfoDTO {
            /**
             * appid
             */
            @SerializedName("appid")
            private String appid;
            /**
             * headImg
             */
            @SerializedName("headImg")
            private String headImg;
            /**
             * nickName
             */
            @SerializedName("nickName")
            private String nickName;
        }

        /**
         * ContentDTO
         */
        @NoArgsConstructor
        @Data
        public static class ContentDTO {
            /**
             * media
             */
            @SerializedName("media")
            private List<MediaDTO> media;
            /**
             * txt
             */
            @SerializedName("txt")
            private String txt;

            /**
             * MediaDTO
             */
            @NoArgsConstructor
            @Data
            public static class MediaDTO {
                /**
                 * img
                 */
                @SerializedName("img")
                private String img;
                /**
                 * thumbImg
                 */
                @SerializedName("thumbImg")
                private String thumbImg;
            }
        }

        /**
         * ExtInfoDTO
         */
        @NoArgsConstructor
        @Data
        public static class ExtInfoDTO {
            /**
             * isAlreadySendTmpl
             */
            @SerializedName("isAlreadySendTmpl")
            private Boolean isAlreadySendTmpl;
        }

        /**
         * ProductInfoDTO
         */
        @NoArgsConstructor
        @Data
        public static class ProductInfoDTO {
            /**
             * productList
             */
            @SerializedName("productList")
            private List<ProductListDTO> productList;

            /**
             * ProductListDTO
             */
            @NoArgsConstructor
            @Data
            public static class ProductListDTO {
                /**
                 * name
                 */
                @SerializedName("name")
                private String name;
                /**
                 * picUrl
                 */
                @SerializedName("picUrl")
                private String picUrl;
            }
        }
    }
}
