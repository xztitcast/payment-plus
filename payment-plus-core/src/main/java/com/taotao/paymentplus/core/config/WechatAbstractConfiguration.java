package com.taotao.paymentplus.core.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WechatAbstractConfiguration {

	/**
	 * 支付主体
	 */
	protected String subject;

	/**
	 * appid
	 */
    protected String appid;

    /**
     * mch_id
     */
    protected String mchId;

    /**
     * paiKey
     */
    protected String apiKey;

    /**
     * 交易类型
     */
    protected String tradeType;
    
}
