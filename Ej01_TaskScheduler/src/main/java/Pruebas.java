import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


//Pruebas de TaskScheduler con configuracion XML
public class Pruebas {

	public static void main(String[] args) {		
		
		System.out.println("====================================");
		ApplicationContext appCtx = 
			new ClassPathXmlApplicationContext("beans.xml");
		
	}
	
	
}
