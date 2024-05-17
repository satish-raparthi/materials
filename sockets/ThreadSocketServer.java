import java.io.*;
import java.net.*;
public class ThreadSocketServer {
	private int port;
       FileInputStream fis = null;
       BufferedInputStream bis = null;
       OutputStream os = null;
       ServerSocket serverSocket = null;
       Socket socket = null;
       BufferedReader in;

	public ThreadSocketServer(int p){
		port=p;
	}

	//fn to start the socket and listen for the requests
    public void startListening() {
	  try{

		 //create server socket
        serverSocket = new ServerSocket(port);

		//start listening in a loop and process the request
        while (true) {
          System.out.println("Waiting for client request...");

		   //start listening for input
          socket = serverSocket.accept();
          System.out.println("Accepted connection : " + socket);

	  Thread t=new Thread(new SocketResponder());
	  t.start();

	         }
 
		}catch(Exception ex){
			System.out.println(ex.getMessage());
	   }
    }

    class SocketResponder implements Runnable{
  	  public void run(){
	
	try{

			//create buffered reader to read from socket
          in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		   //read file name from client
			String fn = in.readLine();

          // create file object for the file name
          File file = new File (fn);

			//capture the length of the file
          byte [] mybytearray  = new byte [(int)file.length()];

			//create input stream on the file to read the content
          fis = new FileInputStream(file);
          bis = new BufferedInputStream(fis);
          bis.read(mybytearray,0,mybytearray.length);

			//create output stream on the socket
          os = socket.getOutputStream();
          System.out.println("Sending " + fn + "(" + mybytearray.length + " bytes)");

			//write the file content to socket
          os.write(mybytearray,0,mybytearray.length);

			//flush the content through socket
          os.flush();
          System.out.println("Sent the content of the file" + fn +" to client");

			//close all the streams and sockets
          if (bis != null) bis.close();
          if (os != null) os.close();

    if (socket!=null) socket.close();

	}catch(Exception ex){
		ex.printStackTrace();
	}

	}

    }


    public static void main(String[] args) {
	  try{
	    //accept the port no
		 int port=Integer.parseInt(args[0]);
		
		 //create the server
        ThreadSocketServer ts=new ThreadSocketServer(port);

		 //start listening
        ts.startListening();

		}catch(Exception ex){
			System.out.println(ex.getMessage());
	   }
    }
}
