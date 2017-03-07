package case1;

import java.util.ArrayList;

public class Buffer {
	
	private int length;
	private int nClients;
	private boolean finished;
	private ArrayList<Message> messages;
	private boolean waiting;
	
	public Buffer(int length, int nClients){
		this.length=length;
		this.waiting = false;
		this.finished=false;
		this.nClients=nClients;
		messages=new ArrayList<Message>();
	}
	
	public boolean finished(){
		return this.finished;
	}
	//metodo que sume var counter
	public synchronized boolean send(Message m) throws InterruptedException
	{
		boolean sent=false;
		
		if(messages.size() == length)
		{
			setWaiting();
			wait();
			setWaiting();
			messages.add(m);
			sent = true;
			System.out.println("Buffer: Added the message " + m.getMessage()+". There is "+ messages.size()+" messages.");
		}
		else
		{
			messages.add(m);
			sent = true;
			System.out.println("Buffer: Added the message " + m.getMessage()+". There is "+ messages.size()+" messages.");
		}
		return sent;
	}
	
	public synchronized void pop()
	{
		if(isFull() && isWaiting())
		{
			Message m = messages.remove(0);
			m.modify();	
			notify();
		}
		else if(!isEmpty() && !isWaiting())
		{
			Message m = messages.remove(0);
			m.modify();
		}
	}
	
	public boolean hasMessages()
	{
		return messages.size() != 0;
	}
	
	public void setWaiting()
	{
		waiting = !waiting;
	}
	
	public boolean isFull()
	{
		return messages.size() == length;
	}
	
	public boolean isEmpty()
	{
		return messages.size() == 0;
	}
	
	public boolean isWaiting()
	{
		return waiting;
	}
	
	public void finishMessage(){
		nClients--;
		if(nClients==0){
			finished=true;
		}
		
		System.out.println("Quedan " + nClients + " clientes por atender.");
	}
	
}
