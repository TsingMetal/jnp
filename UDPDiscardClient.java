import java.net.*;
import java.io.*;

public class UDPDiscardClient {
	public final static int PORT = 9;

	public static void main(String[] args) {
		String hostname = args.length > 0 ? args[0] : "localhost";

		try (DatagramSocket sock = new DatagramSocket()) {
			InetAddress server = InetAddress.getByName(hostname);
			BufferedReader userInput = 
				new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String line = userInput.readLine();
				if (line.equals("."))  break;
				byte[] data = line.getBytes();
				DatagramPacket output =
					new DatagramPacket(data, data.length, server, PORT);
				sock.send(output);
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
