# payment-plus
支付组件

# 直接将payment-plus-boot-stater组件依赖到Spring boot项目中
<dependency>
    <groupId>com.taotao</groupId>
    <artifactId>payment-plus-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>

#使用对外的接口PaymentFacadePattern直接注入到service业务层即可
@RestController
@RequestMapping("/alipay/app")
public class AlibabaPaymentAppController {
	
	@Autowired
	private PaymentFacadePattern paymentFacadePattern;

	@GetMapping("/pay")
	public ResponseEntity<Map<String, String>> pay(){
		FacadeParam param = Selector.A_APP.getBuilder().setOrderId("1").setNotifyURLSuffix("/alipay/app/pay").setAmount(new BigDecimal("1.02")).build();
		Map<String,String> prepay = paymentFacadePattern.prepay(param);
		return new ResponseEntity<>(prepay, HttpStatus.OK);
	}
	
}

#相关支付实现类(可以直接将单独的支付实现接口直接注入到service业务层中)
#AlibabaPaymentAppServiceImpl
#AlibabaPaymentNativedServiceImpl
#AlibabaPaymentWebServiceImpl
#WechatPaymentAppletServiceImpl
#WechatPaymentAppServiceImpl
#WechatPaymentH5ServiceImpl
#WechatPaymentNativedServiceImpl
#WechatPaymentPublicNumServiceImpl
