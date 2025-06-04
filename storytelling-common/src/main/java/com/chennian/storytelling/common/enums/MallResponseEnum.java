package com.chennian.storytelling.common.enums;

import lombok.Getter;

/**
 * 商城响应消息枚举
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Getter
public enum MallResponseEnum {
    
    // 订单相关
    ORDER_CREATE_SUCCESS(1001, "订单创建成功"),
    ORDER_CREATE_FAIL(1002, "创建订单失败"),
    ORDER_PAY_SUCCESS(1003, "订单支付成功"),
    ORDER_PAY_FAIL(1004, "支付失败"),
    ORDER_CANCEL_SUCCESS(1005, "订单取消成功"),
    ORDER_CANCEL_FAIL(1006, "取消订单失败"),
    ORDER_DELIVER_SUCCESS(1007, "发货成功"),
    ORDER_DELIVER_FAIL(1008, "发货失败"),
    ORDER_RECEIVE_SUCCESS(1009, "确认收货成功"),
    ORDER_RECEIVE_FAIL(1010, "确认收货失败"),
    
    // 商品相关
    PRODUCT_CREATE_SUCCESS(2001, "商品创建成功"),
    PRODUCT_CREATE_FAIL(2002, "商品创建失败"),
    PRODUCT_UPDATE_SUCCESS(2003, "商品更新成功"),
    PRODUCT_UPDATE_FAIL(2004, "商品更新失败"),
    PRODUCT_DELETE_SUCCESS(2005, "商品删除成功"),
    PRODUCT_DELETE_FAIL(2006, "商品删除失败"),
    PRODUCT_ON_SHELF_SUCCESS(2007, "商品上架成功"),
    PRODUCT_ON_SHELF_FAIL(2008, "商品上架失败"),
    PRODUCT_OFF_SHELF_SUCCESS(2009, "商品下架成功"),
    PRODUCT_OFF_SHELF_FAIL(2010, "商品下架失败"),
    PRODUCT_STOCK_UPDATE_SUCCESS(2011, "库存更新成功"),
    PRODUCT_STOCK_UPDATE_FAIL(2012, "库存更新失败"),
    
    // 分类相关
    CATEGORY_CREATE_SUCCESS(3001, "分类创建成功"),
    CATEGORY_CREATE_FAIL(3002, "分类创建失败"),
    CATEGORY_UPDATE_SUCCESS(3003, "分类更新成功"),
    CATEGORY_UPDATE_FAIL(3004, "分类更新失败"),
    CATEGORY_DELETE_SUCCESS(3005, "分类删除成功"),
    CATEGORY_DELETE_FAIL(3006, "分类删除失败"),
    CATEGORY_ENABLE_SUCCESS(3007, "分类启用成功"),
    CATEGORY_ENABLE_FAIL(3008, "分类启用失败"),
    CATEGORY_DISABLE_SUCCESS(3009, "分类禁用成功"),
    CATEGORY_DISABLE_FAIL(3010, "分类禁用失败"),
    CATEGORY_NAME_EXISTS(3011, "分类名称已存在"),
    
    // 购物车相关
    CART_ADD_SUCCESS(4001, "添加到购物车成功"),
    CART_ADD_FAIL(4002, "添加到购物车失败"),
    CART_UPDATE_QUANTITY_SUCCESS(4003, "更新数量成功"),
    CART_UPDATE_QUANTITY_FAIL(4004, "更新数量失败"),
    CART_UPDATE_SELECTED_SUCCESS(4005, "更新选中状态成功"),
    CART_UPDATE_SELECTED_FAIL(4006, "更新选中状态失败"),
    CART_BATCH_UPDATE_SELECTED_SUCCESS(4007, "批量更新选中状态成功"),
    CART_BATCH_UPDATE_SELECTED_FAIL(4008, "批量更新选中状态失败"),
    CART_SELECT_ALL_SUCCESS(4009, "全选操作成功"),
    CART_SELECT_ALL_FAIL(4010, "全选操作失败"),
    CART_DELETE_SUCCESS(4011, "删除成功"),
    CART_DELETE_FAIL(4012, "删除失败"),
    CART_BATCH_DELETE_SUCCESS(4013, "批量删除成功"),
    CART_BATCH_DELETE_FAIL(4014, "批量删除失败"),
    CART_CLEAR_SUCCESS(4015, "清空购物车成功"),
    CART_CLEAR_FAIL(4016, "清空购物车失败"),
    CART_CLEAR_SELECTED_SUCCESS(4017, "清空选中商品成功"),
    CART_CLEAR_SELECTED_FAIL(4018, "清空选中商品失败"),
    
    // 优惠券相关
    COUPON_CREATE_SUCCESS(6001, "优惠券创建成功"),
    COUPON_CREATE_FAIL(6002, "优惠券创建失败"),
    COUPON_UPDATE_SUCCESS(6003, "优惠券更新成功"),
    COUPON_UPDATE_FAIL(6004, "优惠券更新失败"),
    COUPON_DELETE_SUCCESS(6005, "优惠券删除成功"),
    COUPON_DELETE_FAIL(6006, "优惠券删除失败"),
    COUPON_PUBLISH_SUCCESS(6007, "优惠券发布成功"),
    COUPON_PUBLISH_FAIL(6008, "优惠券发布失败"),
    COUPON_DISABLE_SUCCESS(6009, "优惠券停用成功"),
    COUPON_DISABLE_FAIL(6010, "优惠券停用失败"),
    COUPON_RECEIVE_SUCCESS(6011, "优惠券领取成功"),
    COUPON_RECEIVE_FAIL(6012, "优惠券领取失败"),
    COUPON_USE_SUCCESS(6013, "优惠券使用成功"),
    COUPON_USE_FAIL(6014, "优惠券使用失败"),
    COUPON_NOT_EXIST(6015, "优惠券不存在"),
    COUPON_EXPIRED(6016, "优惠券已过期"),
    COUPON_USED(6017, "优惠券已使用"),
    COUPON_NOT_STARTED(6018, "优惠券未开始"),
    COUPON_STOCK_EMPTY(6019, "优惠券库存不足"),
    COUPON_LIMIT_EXCEEDED(6020, "超出领取限制"),
    COUPON_AMOUNT_NOT_ENOUGH(6021, "订单金额不满足优惠券使用条件"),
    
    // 通用验证
    QUANTITY_INVALID(5001, "商品数量必须大于0");
    
    private final int code;
    private final String message;
    
    MallResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}