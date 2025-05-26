package com.chennian.storytelling.common.constant;

import java.util.HashMap;
import java.util.Map;

public class ConstantOrder {
    public static final int ORDER_STATUS_UNLOCK = -1001;
    public static final int ORDER_STATUS_TEST = -12;
    public static final int ORDER_STATUS_CANCEL = -11;
    public static final int ORDER_STATUS_REFUND = -10;
    public static final int ORDER_STATUS_EXCEPTION = -9;
    public static final int ORDER_STATUS_OUT_FAIL = -8;
    public static final int ORDER_STATUS_REFUSE = -6;
    public static final int ORDER_STATUS_OUTING = -5;
    public static final int ORDER_STATUS_PAY_FAIL_WX = -4;
    public static final int ORDER_STATUS_PAY_FAIL_KQ = -3;
    public static final int ORDER_STATUS_PARTNER_LOCK_FAIL = -2;
    public static final int ORDER_STATUS_OVERTIME = -1;
    public static final int ORDER_STATUS_NEW = 1;
    public static final int ORDER_STATUS_PARTNER_LOCKED = 2;
    public static final int ORDER_STATUS_PAY_SUCESS_KQ = 3;
    public static final int ORDER_STATUS_PAY_SUCESS_WX = 4;
    public static final int ORDER_STATUS_PAY_OVER = 5;
    public static final int ORDER_STATUS_TAKING = 6;
    public static final int ORDER_STATUS_SUCCESS = 8;
    public static final int ORDER_STATUS_FINISH = 10;
    public static final int ORDER_STATUS_ADVANCE = 11;
    /**
     * 等待人工出票
     */
    public static final int ORDER_STATUS_WAIT_ARTIFICIAL = 100;

    public static final int ORDER_STATUS_ARTIFICIAL_FAIL = 101;
    public static final Map<Integer, String> statusMapPiao = new HashMap();
    public static final Map<Integer, String> statusMapFslUser;
    public static final Map<Integer, String> statusMapFslAdmin;
    public static final Map<Integer, String> statusCodeMap;
    public static final String PAY_TYPE_WX = "WXPAY";
    /**
     * 出票状态
     */
    public static final String FUSILING_ORDER_NEW = "new";
    public static final String FUSILING_ORDER_PAY_OVER = "pay_over";
    public static final String FUSILING_ORDER_TAKING = "taking";
    public static final String FUSILING_ORDER_SUCCESS = "success";
    public static final String FUSILING_ORDER_REFUND = "refund";
    public static final String FUSILING_ORDER_FAIL = "fail";
    public static final String TEST_ORDER_TICKETCODE = "66666666668888888888";

    public ConstantOrder() {
    }

    static {
        statusMapPiao.put(-1001, "主动释放座位");
        statusMapPiao.put(-9, "异常订单");
        statusMapPiao.put(-10, "已退款");
        statusMapPiao.put(-8, "出票失败");
        statusMapPiao.put(-5, "出票中");
        statusMapPiao.put(-2, "第三方锁票失败");
        statusMapPiao.put(-11, "手动取消订单");
        statusMapPiao.put(-1, "过期未支付");
        statusMapPiao.put(1, "待支付");
        statusMapPiao.put(2, "第三方锁票成功");
        statusMapPiao.put(4, "微信支付成功");
        statusMapPiao.put(-4, "微信支付失败");
        statusMapPiao.put(3, "卡券支付成功");
        statusMapPiao.put(11, "微信预支付");
        statusMapPiao.put(-3, "卡券支付失败");
        statusMapPiao.put(10, "订单已完成");
        statusMapPiao.put(100, "等待人工出票");
        statusMapPiao.put(101, "人工出票失败");
//        statusMapPiao.put(102,"人工出票成功");
        statusMapPiao.put(5, "支付完成");
        statusMapPiao.put(6, "出票中");
        statusMapPiao.put(8, "已经出票");
        statusMapFslUser = new HashMap();
        statusMapFslUser.put(-12, "测试订单");
        statusMapFslUser.put(-1001, "主动释放订单");
        statusMapFslUser.put(-9, "异常订单");
        statusMapFslUser.put(-10, "已退款");
        statusMapFslUser.put(-8, "交易失败");
        statusMapFslUser.put(-5, "出票中");
        statusMapFslUser.put(-2, "第三方锁定失败");
        statusMapFslUser.put(-11, "订单取消");
        statusMapFslUser.put(-1, "过期未支付");
        statusMapFslUser.put(1, "订单新创建（待支付）");
        statusMapFslUser.put(2, "第三方锁定订单");
        statusMapFslUser.put(4, "微信支付成功");
        statusMapFslUser.put(-4, "微信支付失败");
        statusMapFslUser.put(3, "卡券支付成功");
        statusMapFslUser.put(-3, "卡券支付失败");
        statusMapFslUser.put(5, "支付完成（待收货）");
        statusMapFslUser.put(6, "已经受理");
        statusMapFslUser.put(-6, "拒绝接单");
        statusMapFslUser.put(8, "交易成功");
        statusMapFslAdmin = new HashMap();
        statusMapFslAdmin.put(-12, "测试订单");
        statusMapFslAdmin.put(-1001, "主动释放订单");
        statusMapFslAdmin.put(-9, "异常订单");
        statusMapFslAdmin.put(-10, "已退款");
        statusMapFslAdmin.put(-8, "交易失败");
        statusMapFslAdmin.put(-5, "出票中");
        statusMapFslAdmin.put(-2, "第三方锁定失败");
        statusMapFslAdmin.put(-11, "订单取消");
        statusMapFslAdmin.put(-1, "过期未支付");
        statusMapFslAdmin.put(1, "订单新创建（待支付）");
        statusMapFslAdmin.put(2, "第三方锁定订单");
        statusMapFslAdmin.put(4, "微信支付成功");
        statusMapFslAdmin.put(-4, "微信支付失败");
        statusMapFslAdmin.put(3, "卡券支付成功");
        statusMapFslAdmin.put(-3, "卡券支付失败");
        statusMapFslAdmin.put(5, "待发货");
        statusMapFslAdmin.put(6, "已受理（待发货）");
        statusMapFslAdmin.put(-6, "拒绝接单");
        statusMapFslAdmin.put(8, "交易成功");
        statusCodeMap = new HashMap();
        statusCodeMap.put(-12, "test");
        statusCodeMap.put(-1001, "unlock");
        statusCodeMap.put(-9, "exception");
        statusCodeMap.put(-10, "refund");
        statusCodeMap.put(-8, "fail");
        statusCodeMap.put(-6, "refuse");
        statusCodeMap.put(-5, "outting");
        statusCodeMap.put(-4, "pay-fail-wx");
        statusCodeMap.put(-3, "pay-fail-code");
        statusCodeMap.put(-2, "lock-fail");
        statusCodeMap.put(-11, "cancel");
        statusCodeMap.put(-1, "overtime");
        statusCodeMap.put(1, "new");
        statusCodeMap.put(2, "locked");
        statusCodeMap.put(3, "pay-success-code");
        statusCodeMap.put(4, "pay-success-wx");
        statusCodeMap.put(5, "pay-over");
        statusCodeMap.put(6, "taking");
        statusCodeMap.put(8, "success");
    }

    public interface Status {
        String PAY_SUCESS_CODE = "pay-success-code";
        String SUCCESS = "success";
    }
}