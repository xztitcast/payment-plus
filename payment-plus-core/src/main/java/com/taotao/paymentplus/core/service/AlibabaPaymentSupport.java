package com.taotao.paymentplus.core.service;

import java.util.HashMap;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.taotao.paymentplus.core.config.AlibabaConfiguration;

public abstract class AlibabaPaymentSupport {
	
	protected AlipayClient alipayClient;

	public AlibabaPaymentSupport(AlibabaConfiguration configuration) {
		this.alipayClient = new DefaultAlipayClient(configuration.getGateWay(), configuration.getAppId(), 
				configuration.getAppPrivateKey(), configuration.getFormat(),configuration.getCharset(),
				configuration.getAlipayPublicKey(), configuration.getSignType());
	}

	/**
	 * 解析
	 * @param map
	 * @return
	 */
	protected Map<String, String> parse(Map<String, String[]> map) {
		Map<String, String> paramMap = new HashMap<>();
    	map.forEach((k,v) -> {
			if(v == null) {
				paramMap.put(k, "");
			}else {
				paramMap.put(k, String.join(",", v));
			}
		});
    	return paramMap;
	}
	
	/**
	 * 退款状态验证
	 * @param reqInfo
	 * @return
	 */
	public Map<String, String> signature(String reqInfo) {
		Map<String, String> paramMap = new HashMap<>();
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizContent(reqInfo);
		try {
			AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
			if(response.isSuccess()) {
				paramMap.put("out_refund_no", response.getOutRequestNo());
				paramMap.put("refund_status", response.getMsg().toUpperCase());
				paramMap.put("refund_recv_accout", null);
				paramMap.put("success_time", null);
			}
		} catch (AlipayApiException e) {
			//log.error("退款查询异常", e);
		}
		return paramMap;
	}
}
