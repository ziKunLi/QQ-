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
	 * ������е�¼�������û�
	 */
	private HashMap<String, SocketChannel> clients = new HashMap<String, SocketChannel>();
	
	/**
	 * ��ȡ�ͻ��˷�����Ϣ��buffer
	 */
	private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	
	/**
	 * ����������û�
	 * @param userId
	 * @param channel
	 */
	private void onlineMessage(String userId,SocketChannel channel){
		 clients.put(userId, channel); 
	}
	
	/**
	 * �пͻ���������
	 * @param selector
	 * @param key
	 */
	public void online(Selector selector,SelectionKey key){
		try{
			if(key.isAcceptable()){  
	            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();  
	            //������������  
	            SocketChannel channel = serverChannel.accept();
	            channel.configureBlocking(false);  
	            channel.register(selector, SelectionKey.OP_READ);  
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * ��ָ����һ�������û�������Ϣ
	 */
	public void sendMessage(List<String> clientList,String message){
		try{
			for(int i = 0; i < clientList.size(); i++){
				if(!clients.containsKey(clientList.get(i))){
					System.out.println("���ŵ�������");
					continue;
				}
				clients.get(clientList.get(i)).write(CharsetHelper.encode(CharBuffer.wrap(message)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * �ӿͻ��˽��յ���Ϣ
	 * @param key
	 */
	public void getMessage(SelectionKey key){
		try {
			if(key.isReadable()){  
				SocketChannel channel = (SocketChannel) key.channel();  
	            readBuffer.clear();  
	            /*���ͻ���channel�رպ󣬻᲻���յ�read�¼�����û����Ϣ����read��������-1 
	             * ������ʱ��������Ҳ��Ҫ�ر�channel������������Ч�Ĵ���*/                
	            int count = channel.read(readBuffer);  
	            if(count > 0){  
	            	//��д��ת
	            	readBuffer.flip();           
	            	CharBuffer charBuffer = CharsetHelper.decode(readBuffer);  
	            	//������յ�����Ϣ
	            	handleMessage(charBuffer.toString(),channel);
	            }  
	            else{  
	                //����ر�channel����Ϊ�ͻ����Ѿ��ر�channel�����쳣��  
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
			//����ע����Ϣ
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
				//�ظ�ע��
				sendMessage(clientList, JsonUtil.objectToJson(registReplyMessage));
				//ע��ʧ�ܣ��Ƴ�ID
				if(registReplyMessage.getStatus() == 0){
					clients.remove(registId);
				}
				break;
			//�����¼��Ϣ
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
				//����������벻��ȷ���˺Ų���ȷ����ô�Ƴ��������
				if(status != 1){
					clients.remove(userId);
				}
				break;
			//ת����Ϣ
			case "3":
				clientList.clear();
				JSONArray jsonArray = jsonObject.getJSONArray("receivers");
				for(int i = 0 ; i < jsonArray.length(); i++){
					clientList.add(jsonArray.getString(i));
				}
				sendMessage(clientList, message);
				break;
			//�����ʼ����Ϣ
			case "6":
				//��ʼ����Ϣ������
				clientList.clear();
				String initId = jsonObject.getString("id");
				clientList.add(initId);
				InitMessage initMessage = new InitMessage();
				initMessage.setType("5");
				//��Ӻ�����Ϣ
				initMessage.setFriends(userFriendService.getFriendList(initId));
				//��ȡ�û����е�Ⱥ
				List<Group> groups = userGroupService.getGroups(initId);
				List<GroupInfo> groupInfos = new ArrayList<>();
				//���Ⱥ����Ϣ
				for(int i = 0; i < groups.size(); i++){
					GroupInfo groupInfo = new GroupInfo();
					groupInfo.setGroupId(groups.get(i).getId());
					groupInfo.setGroupName(groups.get(i).getName());
					//���Ⱥ��ԱID
					groupInfo.setMembers(groupService.getMemberId(groups.get(i).getId()));
					groupInfos.add(groupInfo);
				}
				//���Ⱥ��Ϣ
				initMessage.setGroups(groupInfos);
				sendMessage(clientList, JsonUtil.objectToJson(initMessage));
				break;
		}
	}
}
