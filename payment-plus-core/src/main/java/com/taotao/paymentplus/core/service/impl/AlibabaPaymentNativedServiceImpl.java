package com.taotao.paymentplus.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.taotao.paymentplus.core.config.AlibabaConfiguration;
import com.taotao.paymentplus.core.entity.Param;
import com.taotao.paymentplus.core.service.AlibabaPaymentSupport;
import com.taotao.paymentplus.core.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlibabaPaymentNativedServiceImpl extends AlibabaPaymentSupport implements PaymentService {
	
	private AlibabaConfiguration configuration;
	
	public AlibabaPaymentNativedServiceImpl(AlibabaConfiguration configuration) {
		super(configuration);
		this.configuration = configuration;
	}

	@Override
	public Map<String, String> callable(HttpServletRequest request) throws Exception {
		Map<String,String> parse = super.parse(request.getParameterMap());
		boolean check = AlipaySignature.rsaCheckV1(parse, configuration.getAlipayPublicKey(), configuration.getCharset(), configuration.getSignType());
		if(check) {
			return parse;
		}
		return null;
	}

	@Override
	public Map<String, String> prepay(Param param) {
		AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
		if(StringUtils.isBlank(param.getAttach())) {
			model.setBody(param.getOrderId());
		}else {
			model.setBody(param.getAttach());
		}
		model.setOutTradeNo(param.getOrderId());
		model.setTimeoutExpress("90m");
		model.setSubject(configuration.getSubject());
		model.setTotalAmount(param.getAmount().toPlainString());
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(configuration.getGlobalNotifyUrl() + param.getNotifyURLSuffix());
		Map<String, String> map = new HashMap<>();
		try {
			AlipayTradePrecreateResponse response = alipayClient.execute(request);
			map.put("response", response.getBody());
		} catch (AlipayApiException e) {
			log.error("支付宝扫码下单异常", e);
		}
		return map;
	}

}
