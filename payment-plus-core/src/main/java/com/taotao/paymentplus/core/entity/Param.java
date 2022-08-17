package com.taotao.paymentplus.core.entity;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import com.taotao.paymentplus.core.service.PaymentService;
import com.taotao.paymentplus.core.service.impl.AlibabaPaymentAppServiceImpl;
import com.taotao.paymentplus.core.service.impl.AlibabaPaymentNativedServiceImpl;
import com.taotao.paymentplus.core.service.impl.AlibabaPaymentWebServiceImpl;
import com.taotao.paymentplus.core.service.impl.WechatPaymentAppServiceImpl;
import com.taotao.paymentplus.core.service.impl.WechatPaymentAppletServiceImpl;
import com.taotao.paymentplus.core.service.impl.WechatPaymentH5ServiceImpl;
import com.taotao.paymentplus.core.service.impl.WechatPaymentNativedServiceImpl;
import com.taotao.paymentplus.core.service.impl.WechatPaymentPublicNumServiceImpl;

public class Param {

	/**
	 * orderId 订单id(必填参数)
	 */
	private String orderId;
	
	/**
	 * amount 金额(必填参数)
	 * BigDecimal 金额不要使用double构建否则会出现意想不到的结果
	 * new BigDecimal(double)
	 */
	private BigDecimal amount;
	
	/**
	 * notifyURLSuffix 支付回调后缀(必填参数)
	 * 回调参数可以在申请开通支付流程中直接定义
	 * (例如: "/taotao/app"  --> 自动补全http:https://demo.com/taotao/app) 
	 */
	private String notifyURLSuffix;
	
	/**
	 * ipAddress ip地址(可选参数)
	 */
	private String ipAddress;
	
	/**
	 * openId (可选参数)
	 * 小程序支付、公众号支付必传openId
	 */
	private String openId;
	
	/**
	 * attch (可选参数)
	 * 附加参数,相当于remark会,传此参数会被回传
	 */
	private String attach;
	
	/**
	 * 回调参数request
	 */
	private HttpServletRequest request;
	
	protected Param(String orderId, BigDecimal amount, String notifyURLSuffix, String ipAddress, String openId, String attach, HttpServletRequest request) {
		this.orderId = orderId;
		this.amount = amount;
		this.openId = openId;
		this.attach = attach;
		this.ipAddress = ipAddress;
		this.notifyURLSuffix = notifyURLSuffix;
		this.request = request;
	}
	
	public String getOrderId() {
		return orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getNotifyURLSuffix() {
		return notifyURLSuffix;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getOpenId() {
		return openId;
	}

	public String getAttach() {
		return attach;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public static enum Selector {
		/**
		 * 支付宝APP
		 */
		A_APP(new Builder("alibabaPaymentAppServiceImpl", AlibabaPaymentAppServiceImpl.class)),
		/**
		 * 支付宝扫码
		 */
		A_NATIVED(new Builder("alibabaPaymentNativedServiceImpl", AlibabaPaymentNativedServiceImpl.class)),
		/**
		 * 支付宝浏览器页面
		 */
		A_WEB(new Builder("alibabaPaymentWebServiceImpl", AlibabaPaymentWebServiceImpl.class)),
		
		/**
		 * 微信H5
		 */
		W_H5(new Builder("wechatPaymentH5ServiceImpl", WechatPaymentH5ServiceImpl.class)),
		/**
		 * 微信APP
		 */
		W_APP(new Builder("wechatPaymentAppServiceImpl", WechatPaymentAppServiceImpl.class)),
		/**
		 * 微信小程序
		 */
		W_APPLET(new Builder("wechatPaymentAppletServiceImpl", WechatPaymentAppletServiceImpl.class)),
		/**
		 * 微信扫码
		 */
		W_NATIVED(new Builder("wechatPaymentNativedServiceImpl", WechatPaymentNativedServiceImpl.class)),
		/**
		 * 微信公众号
		 */
		W_PUBLIC(new Builder("wechatPaymentPublicNumServiceImpl", WechatPaymentPublicNumServiceImpl.class));
		
		private Builder builder;

		private Selector(Builder builder) {
			this.builder = builder;
		}

		public Builder getBuilder() {
			return builder;
		}
		
		public static Builder select(String args) {
			return Selector.valueOf(args).getBuilder();
		}
		
	}
	
	public static class Builder {
		
		private String orderId;
		
		private BigDecimal amount;
		
		private String notifyURLSuffix;
		
		private String ipAddress;
		
		private String openId;
		
		private String attach;
		
		private String name;
		
		private HttpServletRequest request;
		
		private Class<? extends PaymentService> cls;
		
		public Builder(String name, Class<? extends PaymentService> cls) {
			this.name = name;
			this.cls = cls;
		}
		
		public Builder setOrderId(String orderId) {
			this.orderId = orderId;
			return this;
		}
		
		public Builder setAmount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}
		
		public Builder setNotifyURLSuffix(String notifyURLSuffix) {
			this.notifyURLSuffix = notifyURLSuffix;
			return this;
		}
		
		public Builder setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
			return this;
		}
		
		public Builder setOpenId(String openId) {
			this.openId = openId;
			return this;
		}
		
		public Builder setAttach(String attach) {
			this.attach = attach;
			return this;
		}
		
		public Builder setRequest(HttpServletRequest request) {
			this.request = request;
			return this;
		}
		
		public FacadeParam build() {
			return new FacadeParam(orderId, amount, notifyURLSuffix, ipAddress, openId, attach, name, cls, request);
		}
		
	}
}
