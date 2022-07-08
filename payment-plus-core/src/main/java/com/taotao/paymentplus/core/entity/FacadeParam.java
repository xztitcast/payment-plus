package com.taotao.paymentplus.core.entity;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import com.taotao.paymentplus.core.service.PaymentService;

public class FacadeParam extends Param {
	
	private String name;
	
	private Class<? extends PaymentService> cls;

	protected FacadeParam(String orderId, BigDecimal amount, String notifyURLSuffix, String ipAddress, String openId,
			String attach, String name, Class<? extends PaymentService> cls, HttpServletRequest request) {
		super(orderId, amount, notifyURLSuffix, ipAddress, openId, attach, request);
		this.name = name;
		this.cls = cls;
	}

	public String getName() {
		return name;
	}

	public Class<? extends PaymentService> getCls() {
		return cls;
	}

}
