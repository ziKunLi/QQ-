package utils;

import org.json.JSONObject;

public class JsonUtil {
	
	public static String objectToJson(Object object){
		  	
		try {
			JSONObject jsonObject = new JSONObject(object);
			return jsonObject.toString();			
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
