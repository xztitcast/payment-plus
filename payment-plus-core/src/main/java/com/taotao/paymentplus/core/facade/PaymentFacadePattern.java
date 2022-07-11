package com.taotao.paymentplus.core.facade;

import java.util.Map;

import com.taotao.paymentplus.core.entity.FacadeParam;

/**
 * 暴露对外提供服务调用接口
 * @author eden
 *
 */
public interface PaymentFacadePattern {

	/**
	 * 对外预支付请求
	 * @param param
	 * @return
	 */
	public Map<String, String> prepay(FacadeParam param);
	
	/**
	 * 对外回调接口
	 * @param param
	 * @return
	 */
	public Map<String, String> callback(FacadeParam param)throws Exception;
	
}
