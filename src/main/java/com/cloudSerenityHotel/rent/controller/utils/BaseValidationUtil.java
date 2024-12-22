package com.cloudSerenityHotel.rent.controller.utils;

import java.math.BigDecimal;

import jakarta.servlet.http.HttpServletRequest;

public class BaseValidationUtil {

	public static String checkValueIfNullFillDefault(HttpServletRequest request, Object parameterName) {
		return (String) checkValueIfNullFillDefault(request, parameterName, String.class);
	}
	
	public static String checkValueIfNullFillDefault(HttpServletRequest request, Object parameterName, Class<?> clazz) {
		String parameter = request.getParameter((String) parameterName);
		if ("".equals(parameter)) {
			System.out.println("傳入obj 為空, parameter為:" + parameterName);
			return fillDefaultValue(clazz);
		}

		return (String) parameter;
	}

	public static String fillDefaultValue(Class<?> clazz) {
		if (clazz.isAssignableFrom(BigDecimal.class)) {
			return String.valueOf(0.0);
		}

		if (clazz.isAssignableFrom(int.class)) {
			return String.valueOf(0);
		}

		return "";
	}
}
