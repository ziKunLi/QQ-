package server;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import dto.GroupInfo;
import enity.Group;
import message.InitMessage;
import message.LoginReplyMessage;
import message.RegistReplyMessage;
import service.GroupService;
import service.UserFriendService;
import service.UserGroupService;
import service.UserService;
import utils.JsonUtil;

public class ServerHandler {
	
	private UserService userService = new UserService();
	
	private UserFriendService userFriendService = new UserFriendService();
	
	private UserGroupService userGroupService = new UserGroupService();
	
	private GroupService groupService = new GroupService();
	
	
	public ServerHandler(){
		
	}
	
	/**
	 * 存放所有登录上来的用户
	 */
	private HashMap<String, SocketChannel> clients = new HashMap<String, SocketChannel>();
	
	/**
	 * 读取客户端发来消息的buffer
	 */
	private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	
	/**
	 * 添加已上线用户
	 * @param userId
	 * @param channel
	 */
	private void onlineMessage(String userId,SocketChannel channel){
		 clients.put(userId, channel); 
	}
	
	/**
	 * 有客户进行连接
	 * @param selector
	 * @param key
	 */
	public void online(Selector selector,SelectionKey key){
		try{
			if(key.isAcceptable()){  
	            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();  
	            //接受连接请求  
	            SocketChannel channel = serverChannel.accept();
	            channel.configureBlocking(false);  
	            channel.register(selector, SelectionKey.OP_READ);  
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 向指定的一个或多个用户发送信息
	 */
	public void sendMessage(List<String> clientList,String message){
		try{
			for(int i = 0; i < clientList.size(); i++){
				if(!clients.containsKey(clientList.get(i))){
					System.out.println("该信道不存在");
					continue;
				}
				clients.get(clientList.get(i)).write(CharsetHelper.encode(CharBuffer.wrap(message)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 从客户端接收到信息
	 * @param key
	 */
	public void getMessage(SelectionKey key){
		try {
			if(key.isReadable()){  
				SocketChannel channel = (SocketChannel) key.channel();  
	            readBuffer.clear();  
	            /*当客户端channel关闭后，会不断收到read事件，但没有消息，即read方法返回-1 
	             * 所以这时服务器端也需要关闭channel，避免无限无效的处理*/                
	            int count = channel.read(readBuffer);  
	            if(count > 0){  
	            	//读写反转
	            	readBuffer.flip();           
	            	CharBuffer charBuffer = CharsetHelper.decode(readBuffer);  
	            	//处理接收到的消息
	            	handleMessage(charBuffer.toString(),channel);
	            }  
	            else{  
	                //这里关闭channel，因为客户端已经关闭channel或者异常了  
	                channel.close();                 
	            }                        
	        }  
		} 
		catch (Exception e) {
			
		}
	}
	
	private void handleMessage(String message,SocketChannel channel) throws Exception{
		List<String> clientList = new ArrayList<String>();
		JSONObject jsonObject = new JSONObject(message);
		System.out.println(message);
		String type = (String)jsonObject.get("type");
		switch(type){
			//处理注册信息
			case "0":
				clientList.clear();
				String registId = jsonObject.getString("userId");
				clientList.add(registId);
				onlineMessage(registId,channel);
				RegistReplyMessage registReplyMessage = new RegistReplyMessage();
				registReplyMessage.setType("1");
				if(userService.addUser(registId, jsonObject.getString("nickName"), jsonObject.getString("password"))){
					registReplyMessage.setStatus(1);
				}
				else{
					registReplyMessage.setStatus(0);
				}
				//回复注册
				sendMessage(clientList, JsonUtil.objectToJson(registReplyMessage));
				//注册失败，移除ID
				if(registReplyMessage.getStatus() == 0){
					clients.remove(registId);
				}
				break;
			//处理登录信息
			case "2":
				String userId = jsonObject.getString("userId");
				onlineMessage(userId,channel);
				LoginReplyMessage loginReplyMessage = new LoginReplyMessage();
				loginReplyMessage.setType("4");
				int status = userService.isRightPassword(jsonObject.getString("id"),jsonObject.getString("password"));
				loginReplyMessage.setStatus(status);
				clientList.clear();
				clientList.add(userId);
				sendMessage(clientList, JsonUtil.objectToJson(loginReplyMessage));
				//如果密码输入不正确或账号不正确，那么移除这个连接
				if(status != 1){
					clients.remove(userId);
				}
				break;
			//转发消息
			case "3":
				clientList.clear();
				JSONArray jsonArray = jsonObject.getJSONArray("receivers");
				for(int i = 0 ; i < jsonArray.length(); i++){
					clientList.add(jsonArray.getString(i));
				}
				sendMessage(clientList, message);
				break;
			//处理初始化信息
			case "6":
				//初始化信息接收者
				clientList.clear();
				String initId = jsonObject.getString("id");
				clientList.add(initId);
				InitMessage initMessage = new InitMessage();
				initMessage.setType("5");
				//添加好友信息
				initMessage.setFriends(userFriendService.getFriendList(initId));
				//获取用户所有的群
				List<Group> groups = userGroupService.getGroups(initId);
				List<GroupInfo> groupInfos = new ArrayList<>();
				//添加群的信息
				for(int i = 0; i < groups.size(); i++){
					GroupInfo groupInfo = new GroupInfo();
					groupInfo.setGroupId(groups.get(i).getId());
					groupInfo.setGroupName(groups.get(i).getName());
					//获得群成员ID
					groupInfo.setMembers(groupService.getMemberId(groups.get(i).getId()));
					groupInfos.add(groupInfo);
				}
				//添加群信息
				initMessage.setGroups(groupInfos);
				sendMessage(clientList, JsonUtil.objectToJson(initMessage));
				break;
		}
	}
}
