package messenger.client.controller.Test;

import junit.framework.Assert;
import messenger.client.controller.Client;
import messenger.client.controller.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

public class LoggerTest{

	private static Logger logger;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		logger = new Logger("127.0.0.1", 5555);
		System.out.println("Starting test..");
	}
	
	
	@Test
	public void testLogIn() throws InterruptedException {
		System.out.println("Started");
		logger.logIn("Rafi", "1234", true);
		Thread.sleep(500);
		Assert.assertEquals(Client.clientID, 1);
		
		logger.logIn("Sami", "qwerty", true);
		Thread.sleep(500);
		Assert.assertEquals(Client.clientID, 15);
	}

}
