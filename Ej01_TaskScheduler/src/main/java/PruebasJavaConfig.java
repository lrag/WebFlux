import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.curso.cfg.Configuracion;


//Pruebas de TaskScheduler con configuracion XM
public class PruebasJavaConfig {

	public static void main(String[] args) {		
		
		ApplicationContext appCtx = new AnnotationConfigApplicationContext(Configuracion.class);
		System.out.println("Ejemplo con anotacions");
		System.out.println("====================================");
		
	}
	
	
}
