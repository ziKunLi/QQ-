package utils;

import java.util.List;

public class StringUtil {
	
	/**
	 * �ж�һ���ַ����Ƿ�Ϊ��
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		if(str == null||str.trim().equals("")){
			return true;
		}
		return false;
	}
	
	public static boolean isRightId(String id){
		if(isNull(id)){
			return false;
		}
		//�жϳ���
		else if(id.length() != 11){
			return false;
		}
		else{
			//�ж��Ƿ�ȫΪ����
			for(int i = 0; i < id.length(); i++){
				try {
					Integer.parseInt(id.charAt(i) + "");
				} catch (Exception e) {
					return false;
				}
			}
			return true;
		}
	}
	
//	/**
//	 * �ж��Ƿ���һ����ȷ��sql��ѯ���
//	 * @param sql
//	 * @return
//	 */
//	public static boolean isRightQueryString(String sql){
//		String[] words = sql.split("[ /n,]");
//		for(int i =0 ; i < words.length; i++)){
//			if(words[i].toLowerCase().equals("update")||words[i].toLowerCase().equals("update")){
//				
//			}
//		}
//	}
}
