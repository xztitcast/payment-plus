package com.taotao.paymentplus.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.taotao.paymentplus.core.config.AlibabaConfiguration;
import com.taotao.paymentplus.core.entity.Param;
import com.taotao.paymentplus.core.service.AlibabaPaymentSupport;
import com.taotao.paymentplus.core.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlibabaPaymentAppServiceImpl extends AlibabaPaymentSupport implements PaymentService {
	
	private AlibabaConfiguration configuration;
	
	public AlibabaPaymentAppServiceImpl(AlibabaConfiguration configuration) {
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
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		if(StringUtils.isBlank(param.getAttach())) {
			model.setBody(param.getOrderId());
		}else {
			model.setBody(param.getAttach());
		}
		model.setOutTradeNo(param.getOrderId());
		model.setTimeoutExpress("90m");
		model.setSubject(configuration.getSubject());
		model.setTotalAmount(param.getAmount().toPlainString());
		model.setProductCode("QUICK_MSECURITY_PAY");
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(configuration.getGlobalNotifyUrl() + param.getNotifyURLSuffix());
		Map<String, String> map = new HashMap<>();
		try {
			AlipayTradeAppPayResponse sdkExecute = alipayClient.sdkExecute(request);
			map.put("response", sdkExecute.getBody());
		} catch (AlipayApiException e) {
			log.error("支付宝App下单异常", e);
		}
		return map;
	}

}
