package mx.com.paquetexpress.dto;

public class SiteChangeDTO {
	private String clienteConvenio;
	private String clienteMiembro;
	private String miembroNombre;
	private String site;
	private String siteName;
	private String direccion;
	private String sucursal;
	private String sucursalNombre;
	
	public SiteChangeDTO(){
		super();
	}
	
	public String getClienteConvenio() {
		return clienteConvenio;
	}
	public void setClienteConvenio(String clienteOrigen) {
		this.clienteConvenio = clienteOrigen;
	}
	public String getClienteMiembro() {
		return clienteMiembro;
	}
	public void setClienteMiembro(String usuario) {
		this.clienteMiembro = usuario;
	}
	public String getMiembroNombre() {
		return miembroNombre;
	}

	public void setMiembroNombre(String miembroNombre) {
		this.miembroNombre = miembroNombre;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getSucursalNombre() {
		return sucursalNombre;
	}

	public void setSucursalNombre(String sucursalNombre) {
		this.sucursalNombre = sucursalNombre;
	}
	
}
