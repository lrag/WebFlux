import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


//Pruebas de TaskScheduler con configuracion XML y anotaciones
public class PruebasAnotaciones {

	public static void main(String[] args) {		
		
		ApplicationContext appCtx = new ClassPathXmlApplicationContext("beansAnotaciones.xml");
		System.out.println("Ejemplo con anotacions");
		System.out.println("====================================");
		
	}
	
	
}
