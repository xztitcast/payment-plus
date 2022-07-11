package com.taotao.paymentplus.core.facade;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import com.taotao.paymentplus.core.entity.FacadeParam;
import com.taotao.paymentplus.core.service.PaymentService;

/**
 * 服务器接口实现类
 * @author eden
 *
 */
public class PaymentFacadePatternImpl implements PaymentFacadePattern, ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public Map<String, String> prepay(FacadeParam param) {
		Assert.hasLength(param.getOrderId(), "订单ID不能为空!");
		Assert.hasLength(param.getNotifyURLSuffix(), "回调地址不能为空!");
		Assert.notNull(param.getAmount(), "订单金额不能为空");
		Assert.isTrue(BigDecimal.ZERO.compareTo(param.getAmount()) < 0, "订单金额不能<=0");
		PaymentService bean = applicationContext.getBean(param.getName(), param.getCls());
		return bean.prepay(param);
	}

	@Override
	public Map<String, String> callback(FacadeParam param) {
		Assert.notNull(param.getRequest(), "HttpServletRequest is not null");
		PaymentService bean = applicationContext.getBean(param.getName(), param.getCls());
		bean.callable(param.getRequest());
		return null;
	}
}
