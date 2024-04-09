package com.api.Application.Constant;

public class Constant {
	
	public static final String X_REQUESTED_WITH = "X-Requested-With";
	
	public static String PhotoDirectory()
	{
		return System.getProperty("user.home") + "/Downloads/uploads/";
	}
}
