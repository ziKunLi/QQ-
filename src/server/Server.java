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
	 * 初始化服务器，绑定端口
	 * @param port
	 */
	public void init(int port){   
        ServerSocketChannel servSocketChannel;  
           
        try {  
        	//打开信道
            servSocketChannel = ServerSocketChannel.open();  
            //设置异步IO
            servSocketChannel.configureBlocking(false);  
            //绑定端口  
            servSocketChannel.socket().bind(new InetSocketAddress(port)); 
            //得到选择器
            selector = Selector.open();  
            //向信道中注册选择器
            servSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }        
    } 
	
	/**
	 * 开始监听连接上的客户
	 * @param serverHandler
	 */
	public void startListen(ServerHandler serverHandler) {  
        while(true){  
            try{  
                selector.select();               
                Iterator<SelectionKey> ite = selector.selectedKeys().iterator();  
                   
                while(ite.hasNext()){  
                    SelectionKey key = (SelectionKey) ite.next();   
                    //确保不重复处理
                    ite.remove();
                    //处理上线信息
                    serverHandler.online(selector, key);
                    //得到客户端发来的信息，并进行转发
                    serverHandler.getMessage(key);
                }  
                
            }  
            catch(Throwable t){  
                t.printStackTrace();  
            }                            
        }                
    }  
}
