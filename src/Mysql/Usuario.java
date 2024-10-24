package Mysql;

public class Usuario {
	String nombreLogin;
	String contrasena;
	String nombreCompleto;
	
	public Usuario(String nombreLogin, String contrasena, String nombreCompleto) {
		this.nombreLogin = nombreLogin;
		this.contrasena = contrasena;
		this.nombreCompleto = nombreCompleto;
	}

	public String getNombreLogin() {
		return nombreLogin;
	}

	public String getContrasena() {
		return contrasena;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	@Override
	public String toString() {
		return "NombreLogin=" + nombreLogin + ", contrasena=" + contrasena + ", nombreCompleto="
				+ nombreCompleto + ".";
	}
}
