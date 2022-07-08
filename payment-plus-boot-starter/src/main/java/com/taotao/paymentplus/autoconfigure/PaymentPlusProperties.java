package com.taotao.paymentplus.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import com.taotao.paymentplus.core.config.AlibabaConfiguration;
import com.taotao.paymentplus.core.config.WechatGlobalConfiguration;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "payment-plus")
public class PaymentPlusProperties {
	
	@NestedConfigurationProperty
	private WechatGlobalConfiguration wechat;
	
	/**
	 * 支付宝
	 */
	@NestedConfigurationProperty
	private AlibabaConfiguration alibaba;
}
