package com.taotao.paymentplus.core.service;

import java.util.Map;

import com.taotao.paymentplus.core.entity.Param;

/**
 * 同一支付接口
 * @author eden
 *
 */
public interface PaymentService extends PaymentCallableService {

	/**
	 * 获取预支付
	 * @param param
	 * @return
	 */
	Map<String, String> prepay(Param param);
}
