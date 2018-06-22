package server;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import enity.UserFriend;
import utils.JsonUtil;

public class Test {
	public static void main(String[] args) throws JSONException{
		String info = "{\"type\":\"2\",\"id\":\"1471281\",\"password\":\"123456\"}";
		String message = "{\"message\":\"ÄãºÃ\",\"receivers\":[\"1\",\"2\",\"3\"],\"type\":\"3\",\"sender\":\"111\"}";
		String test = "{\"receivers\":[{\"id\":\"1\"}]}";
	
		List<UserFriend> userFriends = new ArrayList<UserFriend>();
		for(int i = 0; i < 5; i++){
			UserFriend userFriend = new UserFriend();
			userFriend.setFriendId(i + "");
			userFriends.add(userFriend);
		}
		JSONObject jsonObject = new JSONObject(userFriends);
	
		JSONArray jsonArray = new JSONArray(userFriends);
		System.out.println(jsonArray);
	}
}
