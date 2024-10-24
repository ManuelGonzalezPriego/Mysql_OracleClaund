package Mysql;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.io.Console;

public class Main {

	public static void main(String[] args) {
		Scanner teclado=new Scanner(System.in);
		Connection conn=conexion(teclado);
		
		if(conn!=null) {
			boolean control=false;
			do {
				System.out.println("1.- Alta de usuario. \n2.- Entrada de usuario.\n3.- Listado entradas. \n4.- Salir\n");
				System.out.println("Inserta la opción:");
				int menu=teclado.nextInt();
				teclado.nextLine();
				
				switch(menu) {
					case 1:{
						Usuario user=crearUsuario(teclado,conn);
						
						insertarUsurio(user,conn);
						
						break;
					}
					case 2:{
						System.out.println("Inserta el Login:");
						String login=teclado.nextLine();
						
						login(login,conn,teclado);
						
						break;
					}
					case 3:{
						System.out.println("Inserta el Login:");
						String login=teclado.nextLine();
						
						mostrarEntradas(login,conn);
						
						break;
					}
					case 4:{
						control=true;
						System.out.println("Adioossss");
						break;
					}
					default:{
						System.out.println(String.format("Inserta un argumento valido, la opción:%d no existe.",menu));
					}
				}
			}while(!control);
		}
		teclado.close();

	}
	public static void log(String texto) {
		File fichero=new File("errores.txt");
		
		FileOutputStream fil;
		try {
			fil = new FileOutputStream(fichero,true);
			DataOutputStream escribir= new DataOutputStream(fil);
			
			escribir.writeUTF(texto);
			escribir.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static boolean login(String login,Connection conn,Scanner teclado) {
		 boolean resultado = false;
		 ResultSet rs = null;
		 try {
			 rs = buscarUsuario(login, conn);
		        
			 if (rs != null && rs.next()) { 
		         int id = rs.getInt("idUsuario");
		         System.out.println("Inserta la contraseña:");
		         
		         if(rs.getString("contrasena").equalsIgnoreCase(hashMD5(teclado.nextLine()))) {
		        	 insertarEntrada(id, conn);
			         resultado = true;
			     }
		         else {
		        	 System.out.println("Contraseña incorrecta...");
		        	 log(String.format("Login:%s, Momento:%s.\n",login,LocalDateTime.now()));
		         }
		         
		     } 
			 else {
		    	 System.out.println("Usuario no encontrado.");
		     }
		        
			 if (rs != null) {
				 rs.close();
		     }

		}catch (SQLException e) {
			e.printStackTrace();
		}

		    return resultado;	
	}
	
	public static Usuario crearUsuario(Scanner teclado,Connection conn) {
		Usuario user=null;
		String login=null;
		
		boolean loginOk=false;
		
		System.out.println("Inserta el nombre completo:");
		String nombre=teclado.nextLine();
		
		while(!loginOk) {
			System.out.println("Inserta el login:");
			login=teclado.nextLine();
			
			try {
				if(!buscarUsuario(login,conn).next()) {
					loginOk=true;
				}
				else {
					System.out.println("Ya existe dicho login name...");

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Inserta la contraseña:");
		String contrasena=hashMD5(teclado.nextLine());
		
		user=new Usuario(login,contrasena,nombre);
		return user;
	}
	
	public static void insertarUsurio(Usuario usuario, Connection conn) {
		String nombre=usuario.getNombreCompleto();
		String login=usuario.getNombreLogin();
		String contrasena=usuario.getContrasena();
	    
	    String in = String.format("INSERT INTO usuarios(nombreLogin, contrasena, nombreCompleto) VALUES('%s', '%s', '%s');",login, contrasena ,nombre);
	    
	    try {
	        conn.createStatement().executeUpdate(in);
	        System.out.println("Se ha creado el usuario con éxito....");
	    } catch (SQLException e) {
	        System.out.println("Error no se pudo insertar el usuario....");
	        e.printStackTrace();
	    }
	}
	
	public static void mostrarEntradas(String login,Connection conn) {		
		ResultSet rs=null;

		Date id;
		try {		
			String consulta=String.format("select * from entradas where idUsuario in (select idUsuario from usuarios where nombreLogin='%s')",login);
			rs=conn.createStatement().executeQuery(consulta);
			
			while(rs.next()) {
				System.out.println(String.format("idUsuario=%d, idEntrada:%s.",rs.getInt("idUsuario"),rs.getTimestamp("idEntrada")));
			}
			
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ResultSet buscarUsuario(String login,Connection conn) {
		ResultSet rs=null;

		String consulta=String.format("Select * from usuarios where nombreLogin='%s';",login);
		try {
			rs=conn.createStatement().executeQuery(consulta);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public static void insertarEntrada(int id, Connection conn) {
	    LocalDateTime fechaEntrada = LocalDateTime.now();
	    
	    // Formatea la fecha y hora para SQL (YYYY-MM-DD HH:MM:SS)
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String fechaFormateada = fechaEntrada.format(formatter);
	    
	    String in = String.format("INSERT INTO entradas(idEntrada, idUsuario) VALUES('%s', %d);", fechaFormateada, id);
	    
	    try {
	        conn.createStatement().executeUpdate(in);
	        System.out.println("Se ha iniciado sesión con éxito....");
	    } catch (SQLException e) {
	        System.out.println("Error de sesión....");
	        e.printStackTrace();
	    }
	}
	
	public static String hashMD5(String contrasena) {
		StringBuilder hash = new StringBuilder();

		try {
		    MessageDigest md = MessageDigest.getInstance("MD5");
		    byte[] arrayContrasena = md.digest(contrasena.getBytes("UTF-8"));
		    for (byte b : arrayContrasena) {
		        hash.append(String.format("%02X", b));
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}

		return hash.toString();
	}
	
	public static Connection conexion(Scanner teclado) {
		Connection conn=null;
		Console console=System.console();

		System.out.println("Inserte el host:");
		String hostName=teclado.nextLine();
		
		System.out.println("Inserte el nombre de la base de datos:");
		String dataBase=teclado.nextLine();
		
		String url = String.format("jdbc:mysql://%s/%s", hostName, dataBase);
		
		System.out.println("Inserte el nombre de usuaio:");
		String usuario=teclado.nextLine();
		
		
		try {
			char passwordArray[]= console.readPassword("Inserta la contraseña: ");
			
			String password=new String(passwordArray);
		
			conn=DriverManager.getConnection(url, usuario, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return conn;
	}

}