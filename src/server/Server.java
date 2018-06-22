package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class Server {
	
	private Selector selector;
	
	
	private static Server server = new Server();
	
	private Server(){}
	
	public static Server getInstance(){
		return server;
	}
	
	/**
	 * ��ʼ�����������󶨶˿�
	 * @param port
	 */
	public void init(int port){   
        ServerSocketChannel servSocketChannel;  
           
        try {  
        	//���ŵ�
            servSocketChannel = ServerSocketChannel.open();  
            //�����첽IO
            servSocketChannel.configureBlocking(false);  
            //�󶨶˿�  
            servSocketChannel.socket().bind(new InetSocketAddress(port)); 
            //�õ�ѡ����
            selector = Selector.open();  
            //���ŵ���ע��ѡ����
            servSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }        
    } 
	
	/**
	 * ��ʼ���������ϵĿͻ�
	 * @param serverHandler
	 */
	public void startListen(ServerHandler serverHandler) {  
        while(true){  
            try{  
                selector.select();               
                Iterator<SelectionKey> ite = selector.selectedKeys().iterator();  
                   
                while(ite.hasNext()){  
                    SelectionKey key = (SelectionKey) ite.next();   
                    //ȷ�����ظ�����
                    ite.remove();
                    //����������Ϣ
                    serverHandler.online(selector, key);
                    //�õ��ͻ��˷�������Ϣ��������ת��
                    serverHandler.getMessage(key);
                }  
                
            }  
            catch(Throwable t){  
                t.printStackTrace();  
            }                            
        }                
    }  
}
