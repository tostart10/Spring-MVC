package com.icia.web.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.icia.web.model.KakaoPayApprove;
import com.icia.web.model.KakaoPayOrder;
import com.icia.web.model.KakaoPayReady;


@Service("kakaoPayService")
public class KakaoPayService
{
	private static Logger logger = LoggerFactory.getLogger(KakaoPayService.class);
	
	//env.xml 에 있는 <!-- ########## 카카오페이 시작 ########## -->의 key 들과 짝맞춰줘야함
	//Ctrl + Shift + x -> 모두 대문자로 바꿔줌
	
	//카카오페이 호스트
	@Value("#{env['kakao.pay.host']}")
	private String KAKAO_PAY_HOST;
	
	//카카오페이 관리자 키
	@Value("#{env['kakao.pay.admin.key']}")  
	private String KAKAO_PAY_ADMIN_KEY;
	
	//카카오페이 가맹점 코드, 10자(이건 정해져있는 거임)
	@Value("#{env['kakao.pay.cid']}")
	private String KAKAO_PAY_CID;
	
	//카카오페이 결제 URL
	@Value("#{env['kakao.pay.ready.url']}")
	private String KAKAO_PAY_READY_URL;
	
	//카카오페이 결제 요청 URL
	@Value("#{env['kakao.pay.approve.url']}")
	private String KAKAO_PAY_APPROVE_URL;
	

	//카카오페이 결제 성공 URL
	@Value("#{env['kakao.pay.success.url']}")
	private String KAKAO_PAY_SUCCESS_URL;
	
	//카카오페이 결제 취소 URL
	@Value("#{env['kakao.pay.cancel.url']}")
	private String KAKAO_PAY_CANCEL_URL;
	
	//카카오페이 결제 실패 URL
	@Value("#{env['kakao.pay.fail.url']}")
	private String KAKAO_PAY_FAIL_URL;
	
	
	//필요한값 모두 여기에 정의 (컨트롤러를 제외하고서)
	//결제 준비
	public KakaoPayReady kakaoPayReady(KakaoPayOrder kakaoPayOrder)
	{
		//객체 선언
		KakaoPayReady kakaoPayReady = null;
		
		
		if(kakaoPayOrder != null)
		{
			//spring에서 지원하는 객체로 간편하게 rest방식 API를 호출할 수 있는 spring 내장 클래스
			RestTemplate restTemplate = new RestTemplate();
			
			//서버로 요청할 header
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "KakaoAK " + KAKAO_PAY_ADMIN_KEY); //(변수명, 변수명으로오는 값(여기에 공백한칸 무조건있어야함, )
			//KAKAO_PAY_ADMIN_KEY는 env.xml 에 있는 key=kakao.pay.admin.key의 값
			headers.add("Content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=utf-8");
			//Content-type: application/x-www-form-urlencoded;charset=utf-8
			//              application/x-www-form-urlencoded  (MediaType.class에 들어가면 여기까지만 써져있어서 + ";cha rset=utf-8" 해준거임
			
			//서버로 요청할 body
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			//api의 요청-본문 에 있는 것들
			//여기에 잇는이름들은 무조건 api랑 똑같이 맞춰야함
			params.add("cid", KAKAO_PAY_CID);	//(변수명, 위의 @Value에 있는 변수명)
			params.add("partner_order_id", kakaoPayOrder.getPartnerOrderId());
			params.add("partner_user_id", kakaoPayOrder.getPartnerUserId());
			params.add("item_name", kakaoPayOrder.getItemName());
			params.add("item_code", kakaoPayOrder.getItemCode());
			params.add("quantity", String.valueOf(kakaoPayOrder.getQuantity()));
			params.add("total_amount",  String.valueOf(kakaoPayOrder.getTotalAmount()));
			params.add("tax_free_amount",  String.valueOf(kakaoPayOrder.getTaxFreeAmount()));
		    params.add("approval_url", KAKAO_PAY_SUCCESS_URL);  //결제 성공시
			params.add("cancel_url", KAKAO_PAY_CANCEL_URL);		//결제 취소시
			params.add("fail_url", KAKAO_PAY_FAIL_URL);			//결제 실패시
			
		
			
			//요청하기 위해 header와 body를 합치기
			//spring framework 에서 제공해주는 HttpEntity 클래스의 header와 body를 합치기
			HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers); //생성자에 매개변수로 parms, headers을 넘김
			
			//RestTemplet에서 예외가 발생하기 때문에 여기서 try-catch을 해줘야함
			try
			{
				kakaoPayReady = restTemplate.postForObject(new URI(KAKAO_PAY_HOST + KAKAO_PAY_READY_URL), body, KakaoPayReady.class); //안에서 객체생성
				//(url, body, 우리가 넣은 값이 있는 클래스를 보냄(이 클래스(객체)에 담아서 보내줘)
				if(kakaoPayReady != null)
				{
					kakaoPayOrder.settId(kakaoPayReady.getTid());  //tId값을 리턴받음
					logger.debug("+++++++++++++++++++++");
					logger.debug("kakaoPayReady.getTid() : " + kakaoPayReady.getTid());
					logger.error("[KakaoPayService] kakaoPayReady : " + kakaoPayReady);
					logger.debug("+++++++++++++++++++++");
				}
			}
			catch(RestClientException e)
			{
				logger.error("[KakaoPayService] kakaoPayReady RestClientException", e);
			}
			catch(URISyntaxException e)  //URI는 java에서 제공
			{
				logger.error("[KakaoPayService] kakaoPayReady URISyntaxException", e);
			}
		}
		else
		{
			logger.error("[KakaoPayService] KakaoPayReady kakaoPayOrder is null");
		}
			
		return kakaoPayReady;
	}
	
	
	
	//2차 리퀘스트
	public KakaoPayApprove kakaoPayApprove(KakaoPayOrder kakaoPayOrder)
	{
		KakaoPayApprove kakaoPayApprove = null;
		
		if(kakaoPayOrder != null)
		{
			RestTemplate restTemplate = new RestTemplate();
			
			//서버로 요청할 header
			//Authorization: KakaoAK ${SERVICE_APP_ADMIN_KEY}
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "KakaoAK " + KAKAO_PAY_ADMIN_KEY);  //KakaoAK 다음에 꼭 공백하나 넣어줘야함!! 안그러면 오류!!
			headers.add("Content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=utf-8" ); 
			//Content-type: application/x-www-form-urlencoded;charset=utf-8 의 형식과 같아하기 때문에 ";charset=utf-8"을 꼭 + 해줘야함 ;도 꼭 포함해야함!!
			
			
			//서버로 요청할 body(body 조합해서 만들어주는것)
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			
			params.add("cid", KAKAO_PAY_CID);
			params.add("tid", kakaoPayOrder.gettId());	
			params.add("partner_order_id", kakaoPayOrder.getPartnerOrderId());
			params.add("partner_user_id", kakaoPayOrder.getPartnerUserId());
			params.add("pg_token", kakaoPayOrder.getPgToken());
			
			HttpEntity<MultiValueMap<String, String>> body= new HttpEntity<MultiValueMap<String, String>>(params, headers);
			
			try
			{
				kakaoPayApprove = restTemplate.postForObject(new URI(KAKAO_PAY_HOST + KAKAO_PAY_APPROVE_URL), body, KakaoPayApprove.class);
				
				if(kakaoPayApprove != null)
				{
					logger.debug("+++++++++++++++++++++");
					logger.error("[KakaoPayService] kakaoPayApprove : " + kakaoPayApprove);
					logger.debug("+++++++++++++++++++++");
					//리턴은 jsp만 할꺼기 뗴문에 여기엔 로그만 찍어주면됨
				}
			}
			catch(RestClientException e)
			{
				logger.error("[KakaoPayService] kakaoPayApprove RestClientException", e);
			}
			catch(URISyntaxException e)  //URI는 java에서 제공
			{
				logger.error("[KakaoPayService] kakaoPayApprove URISyntaxException", e);
			}
			
		}
		else
		{
			logger.error("[KakaoPayService] kakaoPayApprove kakaoPayOrder is null");
		}
		
		
		return kakaoPayApprove;
	}
	
}
