package com.taotao.paymentplus.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.taotao.paymentplus.core.config.AlibabaConfiguration;
import com.taotao.paymentplus.core.entity.Param;
import com.taotao.paymentplus.core.service.AlibabaPaymentSupport;
import com.taotao.paymentplus.core.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlibabaPaymentWebServiceImpl extends AlibabaPaymentSupport implements PaymentService {
	
	private AlibabaConfiguration configuration;

	public AlibabaPaymentWebServiceImpl(AlibabaConfiguration configuration) {
		super(configuration);
		this.configuration = configuration;
	}

	@Override
	public Map<String, String> callable(HttpServletRequest request) {
		Map<String,String> parse = super.parse(request.getParameterMap());
		boolean check = false;
		try {
			check = AlipaySignature.rsaCheckV1(parse, configuration.getAlipayPublicKey(), configuration.getCharset(), configuration.getSignType());
		} catch (AlipayApiException e) {
			log.error("支付宝浏览器页面支付回调异常", e);
		}
		if(check) {
			return parse;
		}
		return null;
	}

	@Override
	public Map<String, String> prepay(Param param) {
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		if(StringUtils.isBlank(param.getAttach())) {
			model.setBody(param.getOrderId());
		}else {
			model.setBody(param.getAttach());
		}
		model.setOutTradeNo(param.getOrderId());
		model.setTimeoutExpress("90m");
		model.setQuitUrl(configuration.getQuitUrl());
		model.setSubject(configuration.getSubject());
		model.setTotalAmount(param.getAmount().toPlainString());
		model.setProductCode("QUICK_WAP_WAY");
		AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(configuration.getGlobalNotifyUrl() + param.getNotifyURLSuffix());
		Map<String, String> map = new HashMap<>();
		try {
			AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
			map.put("response", response.getBody());
		} catch (AlipayApiException e) {
			log.error("支付宝浏览器页面下单异常", e);
		}
		return map;
	}

}
