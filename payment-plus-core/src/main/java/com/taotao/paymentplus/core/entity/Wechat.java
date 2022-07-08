package com.taotao.paymentplus.core.entity;


import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 微信回调成功或者失败的数据映射
 * @author eden
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "xml")
public class Wechat {

	private String return_code;

    private String return_msg;
}
