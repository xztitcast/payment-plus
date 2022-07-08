package com.taotao.paymentplus.core.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付宝配置
 * @author eden
 *
 */
@Getter
@Setter
public class AlibabaConfiguration {

	/**
	 * 主体
	 */
	private String subject;

	/**
	 * appid
	 */
	private String appId;
	
	/**
	 * 支付宝公钥,在申请开通支付宝支付时由支付宝提供
	 */
	private String alipayPublicKey;
	
	/**
	 * 应用公钥,在申请开通支付宝时使用支付宝提供的工具生成,应用公钥需要上传到支付宝
	 */
	private String appPublicKey;
	
	/**
	 * 应用私钥,在申请开通支付宝时使用支付宝提供的工具生成,应用公钥放在程序用于验签
	 */
	private String appPrivateKey;
	
	/**
	 * 验签类型
	 */
	private String signType = "RSA2";
	
	/**
	 * 格式
	 */
	private String format = "json";
	
	/**
	 * 字符集
	 */
	private String charset = "UTF-8";
	
	/**
	 * web支付时URL参数
	 */
	private String quitUrl;
	
	/**
	 * 全局回调地址
	 */
	private String globalNotifyUrl;
	
	/**
	 * 网关
	 */
	private String gateWay = "https://openapi.alipay.com/gateway.do";
	
}
