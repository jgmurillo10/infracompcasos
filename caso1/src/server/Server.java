package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import case1.Buffer;
import case1.Message;
import client.Client;

public class Server extends Thread{
	private long id;
	private Buffer buffer;
	public Server(long d, Buffer b){
		super();
		this.id=d;
		this.buffer=b;
	}
	
	public long getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public void run(){
		while(!buffer.finished())
		{
			if(buffer.isFull() && buffer.waiting())
			{
				buffer.pop();
				notify();
			}
			else if(!buffer.isEmpty() && !buffer.waiting())
			{
				buffer.pop();
			}
			else
			{
				Thread.yield();
			}
		}
		System.out.println("Server " + id + " finished execution.");
	}
	
	
	
	public static void main(String[] args) {
		Properties p = new Properties();
		try 
		{
			FileInputStream fis = new FileInputStream("data/data.properties");
			p.load(fis);
			fis.close();
			
			int nServers = Integer.parseInt(p.getProperty("nServers"));
			int bufferLen = Integer.parseInt(p.getProperty("bufferLength"));
			String[] nMpC = p.getProperty("nMessagesPerClient").split(",");
			int nClients = Integer.parseInt(p.getProperty("nClients"));
			int[] nMessagesPerClient = new int[nClients];
			for (int i = 0; i < nClients; i++)
			{
				nMessagesPerClient[i] = Integer.parseInt(nMpC[i]);
			}
			
			System.out.println("nServers: "+nServers+", nClients: "+nClients+", bufferLen: "+bufferLen);
			
			Buffer buf = new Buffer(bufferLen, nClients);
			//Crear los clientes
			Client[] clients = new Client[nClients];
			for (int i=0; i<nClients; i++) {
				System.out.println("cree " + (i+1) +" clientes");
				Message[] msgs = new Message[nMessagesPerClient[i]];
				for(int j = 0; j < nMessagesPerClient[i]; j++)
				{
					msgs[j] = new Message(buf, 0);
				}
				clients[i] = new Client(nMessagesPerClient[i],i, msgs);
				clients[i].start();
			}
			
			//Crear los threads del servidor
			Server[] servers = new Server[nServers];
			
			for (int i=0; i<nServers; i++) {
				System.out.println("cree servers" + (i+1));
				servers[i] = new Server(i,buf);
				servers[i].start();
			}
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
	}

}
