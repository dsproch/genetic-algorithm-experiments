package extras;
import java.io.IOException;


public class Notifier {

	public static void main(String[] args)
	{
		
		
		try {
			Process p = new ProcessBuilder("/bin/bash", "/home/dakota/notifyScripts/eeSelfGradeNotify").start();
			
			System.out.println(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Notifier.notifySend("Test Title", "This is the main content of the message.  Is it working properly?");
		
	}
	
	public static void notifySend(String title, String message)
	{
		String[] cmd = { "/usr/bin/notify-send",
                "-t",
                "10000",
                title,
                message};
		System.out.println("Notifier::notifySend::" + title + "::" + message);
	    try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
