package com.taotao.paymentplus.autoconfigure;

import com.taotao.paymentplus.autoconfigure.PaymentPlusAutoConfiguration.AutoConfiguredServiceRegistrar;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import com.taotao.paymentplus.core.config.AlibabaConfiguration;
import com.taotao.paymentplus.core.config.WechatGlobalConfiguration;
import com.taotao.paymentplus.core.facade.PaymentFacadePattern;
import com.taotao.paymentplus.core.facade.PaymentFacadePatternImpl;

/**
 * 支付增强自动组件入口
 * 如不用Spring容器作为为Bean工厂,需要额外单独实现
 * 如不依赖Spring容器 PaymentFacadePattern对外接口需要构建PaymentPlusProperties相应的配置
 * @author eden
 *
 */
@Configuration
@Import(AutoConfiguredServiceRegistrar.class)
@EnableConfigurationProperties(PaymentPlusProperties.class)
public class PaymentPlusAutoConfiguration {
	
	private PaymentPlusProperties properties;
	
	public PaymentPlusAutoConfiguration(PaymentPlusProperties properties) {
		this.properties = properties;
	}
	
	public static class AutoConfiguredServiceRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
		
		private ResourceLoader resourceLoader;

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
			PaymentPathBeanDefinitionScanner scanner = new PaymentPathBeanDefinitionScanner(registry, false);
			scanner.setResourceLoader(resourceLoader);
			scanner.addIncludeFilter(new AnnotationTypeFilter(Service.class));
			scanner.doScan("com.taotao.paymentplus.core.service");
		}

		@Override
		public void setResourceLoader(ResourceLoader resourceLoader) {
			this.resourceLoader = resourceLoader;
		}
		
	}
	
	@Bean
	public PaymentFacadePattern paymentFacadePattern() {
		return new PaymentFacadePatternImpl();
		
	}
	
	@Bean
	public WechatGlobalConfiguration wechatGlobalConfiguration() {
		return properties.getWechat();
	}
	
	@Bean
	public AlibabaConfiguration alibabaConfiguration() {
		return properties.getAlibaba();
	}
	
}
