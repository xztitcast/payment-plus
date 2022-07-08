package com.taotao.paymentplus.core.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.taotao.paymentplus.core.config.WechatAbstractConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class WeChatPaymentSupport {
	
	private WechatAbstractConfiguration configuration;
	
	public WeChatPaymentSupport(WechatAbstractConfiguration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * 预支付请求
	 * @param sort
	 * @param str
	 * @return
	 */
	protected Map<String, String> prepay(String requestURL, SortedMap<String, String> requestParam) {
		PrintWriter writer = null;
        BufferedReader reader = null;
        HttpURLConnection conn = null;
		try{
			URL url = new URL(requestURL);
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            writer = new PrintWriter(conn.getOutputStream());
            writer.write(createXML(requestParam));
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            log.info("预支付请求成功解析XML : {}", sb.toString());
            return parseXML(sb.toString());
		}catch(ParserConfigurationException| IOException| SAXException e){
			log.error("预支付请求获取失败!", e);
			return null;
		}finally {
			IOUtils.close(conn);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
		}
	}
	
	/**
	 * 构建基础请求参数
	 * @return
	 */
	protected SortedMap<String, String> constructBaseParamters(){
		SortedMap<String, String> sort = new TreeMap<>();
		sort.put("body", configuration.getSubject());
		sort.put("appid", configuration.getAppid());
		sort.put("mch_id", configuration.getMchId());
		sort.put("trade_type", configuration.getTradeType());
		sort.put("nonce_str", RandomString(18));
		return sort;
	}
	
	protected SortedMap<String, String> callback(BufferedReader reader) throws ParserConfigurationException, IOException, SAXException{
		StringBuilder sb = new StringBuilder();
    	String line = "";
    	while ((line = reader.readLine()) != null) {
    		sb.append(line);
    	}
    	log.info("callback : {}", sb.toString());
    	return parseXML(sb.toString());
    	
	}
	
	/**
	 * 验签
	 * @param sort
	 * @return
	 */
	protected String sign(SortedMap<String, String> sort) {
		Assert.notEmpty(sort, "this param map must not be empty; it must contain at least oen entry");
		StringBuilder sb = new StringBuilder();
		sort.forEach((k,v) -> {
			 if(!"sign".equals(k) && StringUtils.isNotBlank(v)){
	                sb.append(k).append("=").append(v.trim()).append("&");
	            }
		});
		sb.append("key").append("=").append(configuration.getApiKey());
		String sign = DigestUtils.md5DigestAsHex(sb.toString().getBytes(Charset.forName("UTF-8")));
	    return sign.toUpperCase();
	}
	
	/**
	 * 创建XML
	 * @param sort
	 * @return
	 */
	private String createXML(SortedMap<String, String> sort){
		String sign = sign(sort);
		StringBuilder sb = new StringBuilder("<xml>");
		sort.forEach((k, v) -> {
			sb.append("<").append(k).append(">").append(v).append("</").append(k).append(">");
		});
		sb.append("<sign>").append(sign).append("</sign>").append("</xml>");
		log.info("createXML: {}", sb.toString());
		return sb.toString();
	}
	
	/**
	 * 解析XML
	 * @param xml
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	private SortedMap<String, String> parseXML(String xml) throws ParserConfigurationException, IOException, SAXException{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
        Document document = builder.parse(inputStream);
        SortedMap<String, String> map = new TreeMap<>();
        NodeList nodeList = document.getFirstChild().getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            Node node = nodeList.item(i);
            if (node instanceof Element) map.put(node.getNodeName(),node.getTextContent());
        }
        return map;
	}
	
	private String RandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
	}
	
}
