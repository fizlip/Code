package tcpCom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.function.Consumer;

import application.AdminController;
import application.ChatController;
import javafx.application.Platform;
import tcpCom.NetworkConnection.ConnectionThread;

public class Server extends NetworkConnection{
	protected ConnectionThread connThread;
	private int port;
	private Client client;
	private Socket socket;
	private List<Socket> sockets;
	private List<ObjectOutputStream> outs;
	public boolean keepRunning = true;
	public static ServerSocket serverSocket;
	
	public Server(int port, Consumer<Serializable> onRecieveCallback) {
		super(onRecieveCallback);
		this.port = port;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void waitForConnection() {
		while(keepRunning) {
			try {
				serverSocket.accept();
				System.out.println("CONNECTION");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected boolean isServer() {
		return true;
	}
	
	@Override
	protected ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	@Override
	protected String getIP() {
		return null;
	}
	
	@Override
	protected int getPort() {
		return port;
	}
	
}
