package com.taotao.paymentplus.core.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付回调接口
 * @author eden
 *
 */
public interface PaymentCallableService {

	/**
	 * 支付、或退款回调
	 * 支付或者退款回调方法中会自动验签
	 * 验签成功会返回解析的结果
	 * 验签失败返回的空的结果集
	 * @param request
	 * @return
	 */
	Map<String, String> callable(HttpServletRequest request) throws Exception;
}
