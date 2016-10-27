import java.io.*;
import java.net.*;
import java.util.logging.*;

public abstract class UDPServer implements Runnable {
	private final int bufferSize;
	private final int port; 
	private final Logger logger =
		Logger.getLogger(UDPServer.class.getCanonicalName());
	private volatile boolean isShutdown = false;

	public UDPServer(int port, int bufferSize) {
		this.bufferSize = bufferSize;
		this.port = port;
	}

	public UDPServer(int port) {
		this(port, 8192);
	}

	@Override
	public void run() {
		byte[] buffer = new byte[bufferSize];
		try (DatagramSocket socket = new DatagramSocket(port)) {
			socket.setSoTimeout(10000);
			while (true) {
				if (isShutdown) return;
				DatagramPacket incoming = 
					new DatagramPacket(buffer, buffer.length);
				try {
					socket.receive(incoming);
					this.respond(socket, incoming);
				} catch (SocketTimeoutException ex) {
					if (isShutdown) return;
				} catch (IOException ex) {
					logger.log(Level.WARNING, ex.getMessage(), ex);
				}
			}
		} catch (SocketException ex) {
			logger.log(Level.SEVERE, "Could not bind port: " + port, ex);
		}
	}

	public abstract void respond(DatagramSocket socket,
			DatagramPacket request) throws IOException;

	public void shutdown() {
		this.isShutdown = true;
	}
}
