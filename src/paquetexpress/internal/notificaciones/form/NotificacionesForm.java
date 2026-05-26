package paquetexpress.internal.notificaciones.form;

import org.apache.struts.action.ActionForm;

/**
 * @author Administrador
 *
 */
public class NotificacionesForm extends ActionForm{	
	private static final long serialVersionUID = 1L;
	
	private String currentTask = ""; 
	
	private String nombre = "";
	private String email = "";
	private String guias = "";
	public String getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getGuias() {
		return guias;
	}
	public void setGuias(String guias) {
		this.guias = guias;
	}	
}