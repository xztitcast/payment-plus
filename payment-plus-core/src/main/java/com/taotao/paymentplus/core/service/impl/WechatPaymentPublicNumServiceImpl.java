package com.taotao.paymentplus.core.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.taotao.paymentplus.core.config.WechatGlobalConfiguration;
import com.taotao.paymentplus.core.entity.Param;
import com.taotao.paymentplus.core.service.PaymentService;
import com.taotao.paymentplus.core.service.WeChatPaymentSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WechatPaymentPublicNumServiceImpl extends WeChatPaymentSupport implements PaymentService {
	
	private WechatGlobalConfiguration configuration;

	public WechatPaymentPublicNumServiceImpl(WechatGlobalConfiguration configuration) {
		super(configuration.getPublicNum());
		this.configuration = configuration;
	}

	@Override
	public Map<String, String> callable(HttpServletRequest request) {
		try(BufferedReader reader = request.getReader()){
			SortedMap<String, String> result = super.callback(reader);
			if (!"SUCCESS".equals(result.get("return_code")) || !"SUCCESS".equals(result.get("result_code"))){
	            return null;
	        }
			String sign = super.sign(result);
			if(!sign.equals(result.get("sign"))) {
				return null;
			}
			return result;
		}catch(ParserConfigurationException| SAXException| IOException e) {
			log.error("微信公众号回调异常", e);
			return null;
		}
	}

	@Override
	public Map<String, String> prepay(Param param) {
		SortedMap<String, String> sort = super.constructBaseParamters();
		if(StringUtils.isNotBlank(param.getAttach())) {
			sort.put("attach", param.getAttach());
		}
		sort.put("openId", param.getOpenId());
		sort.put("out_trade_no", param.getOrderId());
		sort.put("total_fee", param.getAmount().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
		sort.put("spbill_create_ip", param.getIpAddress());
		sort.put("notify_url", configuration.getGlobalNotifyUrl() + param.getNotifyURLSuffix());
		Map<String, String> response = super.prepay(configuration.getUnifiedOrderUrl(), sort);
		sort.clear();
		if(response !=null && "SUCCESS".equals(response.get("return_code")) && "SUCCESS".equals(response.get("result_code"))) {
			sort.put("appid", response.get("appid"));
			sort.put("package", "prepay_id" + "=" + response.get("prepay_id"));
			sort.put("signType", "MD5");
			sort.put("noncestr", response.get("nonce_str"));
			sort.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
            sort.put("sign", sign(sort));
		}
		return sort;
	}

}
