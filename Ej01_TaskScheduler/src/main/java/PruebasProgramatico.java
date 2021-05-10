import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.TaskScheduler;

import com.curso.modelo.negocio.Tarea;


//Pruebas de TaskScheduler con configuracion XML
public class PruebasProgramatico {

	public static void main(String[] args) {		
		
		ApplicationContext appCtx = new ClassPathXmlApplicationContext("beansProgramatico.xml");
		System.out.println("====================================");
		
		TaskScheduler sch = (TaskScheduler) appCtx.getBean("taskScheduler");
		
		sch.scheduleAtFixedRate(new Tarea("T1"), 1000);
		
	}
	
	
}
