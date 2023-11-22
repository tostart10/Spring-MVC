package com.icia.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.icia.common.util.StringUtil;
import com.icia.web.model.KakaoPayApprove;
import com.icia.web.model.KakaoPayOrder;
import com.icia.web.model.KakaoPayReady;
import com.icia.web.model.Response;
import com.icia.web.service.KakaoPayService;
import com.icia.web.util.CookieUtil;
import com.icia.web.util.HttpUtil;

@Controller("kakaoPayController")
public class KakaoPayController 
{
	private static Logger logger = LoggerFactory.getLogger(KakaoPayController.class);
	   
	
	   @Autowired
	   private KakaoPayService kakaoPayService;
	   
	   //쿠키명
	   @Value("#{env['auth.cookie.name']}")
	   private String AUTH_COOKIE_NAME;
	   
	   @RequestMapping(value="/kakao/pay")
	   public String pay(HttpServletRequest request , HttpServletResponse response)
	   {
		   return "/kakao/pay";
	   }
	   
	   
	   //카카오페이 팝업창
	   @RequestMapping(value="/kakao/payPopUp", method=RequestMethod.POST)
	   public String payPopUp(ModelMap model, HttpServletRequest request, HttpServletResponse response)
	   {
		   String pcUrl = HttpUtil.get(request, "pcUrl" , "");
		   String orderId = HttpUtil.get(request, "orderId", "");
		   String tId = HttpUtil.get(request, "tId" , "");
		   String userId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
		   
		   //.jsp에 뿌려줄 값들 /kakao/payPopUp.jsp에 뿌려주게됨
		   model.addAttribute("pcUrl", pcUrl);
		   model.addAttribute("orderId", orderId);
		   model.addAttribute("tId", tId);
		   model.addAttribute("userId", userId );
		   
		   return "/kakao/payPopUp";
	   }
	   
	   
	   //pay.jsp ajax통신
	   @RequestMapping(value="/kakao/payReady", method=RequestMethod.POST)
	   @ResponseBody
	   public Response<Object> payReady(HttpServletRequest request, HttpServletResponse response)
	   {
			Response<Object> ajaxResponse =  new Response<Object>();
			
			//api 에 나와있는 서버에 필수로 보내야하는 것들
			String orderId = StringUtil.uniqueValue();  // StringUtil.uniqueValue() 이 모듈안에 UUID를 가져오는거 있음
			// StringUtil.uniqueValue() 대신에 시퀀스번호를 여기에 넣어서 주문번호를 할 수 있음
			//시퀀스 번호를 여기에 넣음
			
			//사용자페이지에서 넘어온 값들
			String userId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
			String itemCode = HttpUtil.get(request, "item", ""); //두번쨰에 있는 "item"은 jsp의 ajax 안에 data:{}안에 있는 변수 값이랑 똑같해야함
			String itemName = HttpUtil.get(request, "itemName", "");
			int quantity = HttpUtil.get(request, "quantity", (int)0);
			int totalAmount = HttpUtil.get(request, "totalAmount", (int)0);
			int taxFreeAmount = HttpUtil.get(request, "taxFreeAmount", (int)0);
			int vatAmount = HttpUtil.get(request, "vatAmount", (int)0);
			
			//modle은 reqest받아서 뿌려줄것들을 다 model에 넣을 거임
			//api 사용하기위해 객체생성- 객체생성해서 api사용하려고 (model 에 .java 만들어줘야함)
			//객체생성
			KakaoPayOrder kakaoPayOrder = new KakaoPayOrder();
			
			kakaoPayOrder.setPartnerOrderId(orderId);
			kakaoPayOrder.setPartnerUserId(userId);
			kakaoPayOrder.setItemCode(itemCode);
			kakaoPayOrder.setItemName(itemName);
			kakaoPayOrder.setQuantity(quantity);
			kakaoPayOrder.setTotalAmount(totalAmount);
			kakaoPayOrder.setTaxFreeAmount(taxFreeAmount);
			kakaoPayOrder.setVatAmount(vatAmount);
			
			
			logger.debug("+++++++++++++++가니???+++++++++  userId : " + userId);
			logger.debug("itemCode : " + itemCode);
			logger.debug("itemName : " + itemName);
			logger.debug("quantity : " + quantity);
			logger.debug("totalAmount : " + totalAmount);
			logger.debug(" taxFreeAmount : "+ taxFreeAmount);
			logger.debug("+++++++++++++++가니???+++++++++  vatAmount : " + vatAmount);
			
			
			//서비스 콜 하려면 @Autowired 꼭 해야함
			KakaoPayReady kakaoPayReady = kakaoPayService.kakaoPayReady(kakaoPayOrder);
			
			if(kakaoPayReady != null)
			{
				logger.debug("[KakaoPayController] payReady : " + kakaoPayReady);
				
				kakaoPayOrder.settId(kakaoPayReady.getTid());  //일부러 다시 해줌(서비스에서 한거 또해줌 -> 참조형 변수에 대한 이해위해)
				
				JsonObject json = new JsonObject();
				//인수값으로 json을 보내려고함
				json.addProperty("orderId", orderId);  //UUId값
				json.addProperty("tId", kakaoPayReady.getTid()); //http프로토콜 연결값
				json.addProperty("appUrl", kakaoPayReady.getNext_redirect_app_url()); //전용앱
				json.addProperty("moblieUrl", kakaoPayReady.getNext_redirect_mobile_url()); //모바일웹
				json.addProperty("pcUrl", kakaoPayReady.getNext_redirect_pc_url()); //pc웹
				//이 값을들 가지고 (response객체에 담아서)클라이언트로 넘어갈거임 -> response이기 때문
				
				//리스폰스 객체에 담음
				ajaxResponse.setResponse(0, "success", json);   //json은 데이터임, 데이터를 보냄, T는 모든 것을 받음
				
			}
			else
			{
				ajaxResponse.setResponse(-1, "fail", null);  //(코드, 메세지, 데이터)
			}
			
			return ajaxResponse; //리스폰스 객체를 던져줌
	   }

	   
	   
	   //카카오페이 성공
	   @RequestMapping(value="/kakao/paySuccess")
	   public String paySuccess(ModelMap model, HttpServletRequest request, HttpServletResponse response)
	   {
		   String pgToken = HttpUtil.get(request, "pg_token", "");//이거는 카카오페이에서 보내주는 거임
		   
		   model.addAttribute("pgToken", pgToken);
		   
		   
		   return "/kakao/paySuccess";
	   }
	   
	   
	   //2차 리퀘스트(승인요청)(API에서 승인요청 부분임)
	   @RequestMapping(value="/kakao/payResult") //최종적인 결제 내역을 jsp로 보여줄거임
	   public String payResult(ModelMap model, HttpServletRequest request, HttpServletResponse response)
	   {
		   KakaoPayApprove kakaoPayApprove = null;  //서비스에서 return으로 받아야해서 객체 선언해줌
		   
		   String tId = HttpUtil.get(request, "tId", "");
		   String orderId = HttpUtil.get(request, "orderId", "");
		   String userId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
		   String pgToken = HttpUtil.get(request, "pgToken", "");
		   
		   KakaoPayOrder kakaoPayOrder = new KakaoPayOrder();
		   
		   kakaoPayOrder.setPartnerOrderId(orderId);
		   kakaoPayOrder.setPartnerUserId(userId);
		   kakaoPayOrder.settId(tId);
		   kakaoPayOrder.setPgToken(pgToken);
		   
		   kakaoPayApprove = kakaoPayService.kakaoPayApprove(kakaoPayOrder);
		   
		   //kakaoPayApprove 객체 자체를 담아줌
		   model.addAttribute("kakaoPayApprove",kakaoPayApprove);
		   
		   return "/kakao/payResult";
	   }
	   
	   
	   
	   //결제 취소시
	   @RequestMapping(value="/kakao/payCancel")
	   public String payCancle(HttpServletRequest request, HttpServletResponse response)
	   {
		   //따로 DB작업을 안해놔서 이것만 함 - 현재는 payCancle, payFail은 카카오페이랑 연동할 수 있는 방법없음
		   return "/kakao/payCancel";
	   }
	   
	   
	   
	   //결제 실패시
	   @RequestMapping(value="/kakao/payFail")
	   public String payFail(HttpServletRequest request, HttpServletResponse response)
	   {
		 //따로 DB작업을 안해놔서 이것만 함
		   return "/kakao/payFail";
	   }
	   
	   
	   
}
