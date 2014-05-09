package com.n11.mock;

import java.lang.reflect.Constructor;
import java.util.Properties;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;

public class ReCaptchaMock implements ReCaptcha {
	
	@Override
	public String createRecaptchaHtml(String errorMessage, Properties options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createRecaptchaHtml(String errorMessage, String theme,
			Integer tabindex) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public ReCaptchaResponse checkAnswer(String remoteAddr, String challenge,
			String response) {
		
		ReCaptchaResponse reCatptchaResponse = null;
		
		Constructor<ReCaptchaResponse> ctor = null;
		try {
			ctor = (Constructor<ReCaptchaResponse>)ReCaptchaResponse.class.getDeclaredConstructors()[0];
			ctor.setAccessible(true);
			reCatptchaResponse = (ReCaptchaResponse)ctor.newInstance(true, "");
			
//			Field validField = reCatptchaResponse.getClass().getDeclaredField("valid");
//			validField.setAccessible(true);
//			validField.setBoolean(reCatptchaResponse, true);
//			
//			Field errorMessageField = reCatptchaResponse.getClass().getDeclaredField("errorMessage");
//			errorMessageField.setAccessible(true);
//			errorMessageField.set(reCatptchaResponse, "");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reCatptchaResponse;
	}
}
