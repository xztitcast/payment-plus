package com.taotao.paymentplus.core.config;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信全局相关配置
 * @author eden
 *
 */
@Getter
@Setter
public class WechatGlobalConfiguration {

	/**
	 * 退款请求地址
	 */
	private String refundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	
	/**
	 * 预支付请求地址
	 */
	private String unifiedOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	/**
	 * 全局回调地址
	 */
	private String globalNotifyUrl;
	
	/**
	 * App
	 */
	@NestedConfigurationProperty
	private AppConfiguration app;
	
	/**
	 * 小程序
	 */
	@NestedConfigurationProperty
	private AppletConfiguration applet;
	
	/**
	 * h5
	 */
	@NestedConfigurationProperty
	private H5Configuration h5;
	
	/**
	 * 扫码
	 */
	@NestedConfigurationProperty
	private NativedConfiguration natived;
	
	/**
	 * 公众号
	 */
	@NestedConfigurationProperty
	private PublicNumConfiguration publicNum;
    
}
