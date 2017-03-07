package client;

import java.util.Random;

import case1.Message;

public class Client extends Thread{

	public int nMessages;
	public Message[] messages;
	public int id;
	public Client (int nMessages, int id, Message[]  messages){
		this.nMessages=nMessages;
		this.messages = messages;
		this.id=id;
	}
	

	public void run(){
		Random random= new Random();
		int aux = nMessages;
		while(0<nMessages){
//			creates messages
			int num = random.nextInt(100);
			messages[nMessages-1].setMessage(num);
			messages[nMessages-1].send();
			System.out.println("Enviado: "+num+" Respuesta: "+messages[nMessages-1].getMessage());
			if(messages[nMessages-1].getMessage()==num+1){
				System.out.println("Message: "+ (aux-nMessages+1) + " of client " + id + " processed succesfully. ");
			}else{
				System.out.println("Message: "+ (aux-nMessages+1) + " of client " + id + " processed unsuccesfully. ");
			}
			nMessages--;
		}
		messages[nMessages].finishMessage();
		System.out.println("Client "+ id + " finished execution.");
	}
}
