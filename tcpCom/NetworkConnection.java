package tcpCom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class NetworkConnection {

	protected ConnectionThread connThread = new ConnectionThread();
	private Consumer<Serializable> onRecieveCallback;
	private static int count = 0;
	private static List<ConnectionThread> clientList = new ArrayList<>();
	
	public NetworkConnection(Consumer<Serializable> onRecieveCallback) {
		this.onRecieveCallback = onRecieveCallback;
		connThread.setDaemon(true);
	}
	
	public void startConnection() throws Exception{
		count += 1;
		connThread.start();
	}
	
	public void send(Serializable data) throws Exception{
		connThread.out.writeObject(data);
	}
	
	public void sendAll(Serializable data) throws Exception{
		for(ConnectionThread conn : clientList) {
			conn.out.writeObject(data);
		}
	}
	
	public void closeConnection() throws Exception{
		connThread.socket.close();
	}
	
	protected boolean isServer() {
		return false;
	}
	
	protected NetworkConnection getConn() {
		return this;
	}
	
	public static List<ConnectionThread> getConns(){
		return clientList;
	}
	
	private NetworkConnection getThis() {
		return this;
	}
	
	protected abstract String getIP();
	protected abstract int getPort();
	protected abstract ServerSocket getServerSocket();
	
	
	protected class ConnectionThread extends Thread{
		
		protected Socket socket;
		protected ServerSocket server;
		protected ObjectOutputStream out;
		protected List<Socket> sockets;
		protected List<ObjectOutputStream> outs;
		
		@Override
		public void run() {
			System.out.println("running");
			try(Socket socket = isServer() ? getServerSocket().accept() : new Socket(getIP(), getPort());
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
				this.socket = socket;
				this.out = out;
				System.out.println("Connection made");
				socket.setTcpNoDelay(true);
				clientList.add(this);
				if(isServer()) {
					Thread thread = new Thread("wait thread") {
						public void run() {
							while(true) {
								System.out.println("Listening...");
								connThread.run();
							}
						};
					};thread.start();
				}
				while(true) {
					Serializable data = (Serializable) in.readObject();
					onRecieveCallback.accept(data);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
