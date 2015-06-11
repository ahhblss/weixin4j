package com.foxinmy.weixin4j.qy.token;

import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.http.weixin.WeixinHttpClient;
import com.foxinmy.weixin4j.http.weixin.WeixinResponse;
import com.foxinmy.weixin4j.model.Consts;
import com.foxinmy.weixin4j.model.Token;
import com.foxinmy.weixin4j.token.TokenCreator;
import com.foxinmy.weixin4j.token.TokenHolder;
import com.foxinmy.weixin4j.util.ConfigUtil;

/**
 * 微信企业号JSTICKET创建
 * 
 * @className WeixinJSTicketCreator
 * @author jy
 * @date 2015年1月10日
 * @since JDK 1.7
 * @see <a
 *      href="http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E9.99.84.E5.BD.951-JS-SDK.E4.BD.BF.E7.94.A8.E6.9D.83.E9.99.90.E7.AD.BE.E5.90.8D.E7.AE.97.E6.B3.95">JS
 *      TICKET</a>
 */
public class WeixinJSTicketCreator implements TokenCreator {

	private final String corpid;
	private final TokenHolder weixinTokenHolder;
	private final WeixinHttpClient httpClient;

	/**
	 * jssdk
	 * 
	 * @param weixinTokenHolder
	 *            <font color="red">公众平台的access_token</font>
	 */
	public WeixinJSTicketCreator(TokenHolder weixinTokenHolder) {
		this(ConfigUtil.getWeixinAccount().getId(), weixinTokenHolder);
	}

	/**
	 * <font color="red">企业号的的access_token</font>
	 * 
	 * @param weixinTokenHolder
	 */
	public WeixinJSTicketCreator(String corpid, TokenHolder weixinTokenHolder) {
		this.corpid = corpid;
		this.weixinTokenHolder = weixinTokenHolder;
		this.httpClient = new WeixinHttpClient();
	}

	@Override
	public String getCacheKey() {
		return String.format("qy_jsticket_%s", corpid);
	}

	@Override
	public Token createToken() throws WeixinException {
		WeixinResponse response = httpClient.get(String.format(
				Consts.QY_JS_TICKET_URL, weixinTokenHolder.getToken()
						.getAccessToken()));
		JSONObject result = response.getAsJson();
		Token token = new Token(result.getString("ticket"));
		token.setExpiresIn(result.getIntValue("expires_in"));
		token.setTime(System.currentTimeMillis());
		return token;
	}
}
