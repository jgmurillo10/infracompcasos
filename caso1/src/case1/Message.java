package case1;

public class Message {
	
	private Buffer buffer;
	private int message;
	
	public Message(Buffer buffer, int message){
		this.buffer=buffer;
		this.message=message;
	}
	public int getMessage(){
		return this.message;
	}
	public void finishMessage(){
		buffer.finishMessage();
	}
	public void setMessage(int message){
		this.message=message;
	}
	
	public synchronized boolean send(){
		try {
			boolean resp = buffer.send(this);
			wait();
			return resp;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			return false;
		}
		//sends the message to the buffer
	}
	public synchronized void modify() {
		message++;
		notify();
		System.out.println("Message: Notifying that the value has changed to " + message);
	}
}
