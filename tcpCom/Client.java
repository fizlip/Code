package tcpCom;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.function.Consumer;

public class Client extends NetworkConnection{

	private String ip;
	private int port;
	
	public Client(String ip, int port, Consumer<Serializable> onReciveCallback) {
		super(onReciveCallback);
		this.ip = ip;
		this.port = port;
	}

	@Override
	protected boolean isServer() {
		return false;
	}

	@Override
	protected ServerSocket getServerSocket() {
		return null;
	}
	
	@Override
	protected String getIP() {
		return ip;
	}

	@Override
	protected int getPort() {
		return port;
	}

}