package MySql;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
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
				int menu=0;
				
				try {
					do{
						System.out.println("Inserta la opción:");
						menu=teclado.nextInt();
					}while(menu==0);
				}
				catch(InputMismatchException ex) {
					System.out.println("Error no has insertado un número...");
					ex.printStackTrace();
				}
				
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
						try {
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
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
	public static boolean login(String login,Connection conn,Scanner teclado) {
		 boolean resultado = false;
		 ResultSet rs = null;
		 try {
			 rs=getUser(login,conn);
		        
			 if (rs.next() && rs != null) { 
		         System.out.println("Inserta la contraseña:");
		         String contrasena=getPassWd(teclado.nextLine(),conn);
		         if(rs.getString("contrasena").equals(contrasena)) {
		        	 insertarEntrada(rs.getInt("idUsuario"), conn);
			         resultado = true;
			     }
		         else {
		        	 System.out.println("Contraseña incorrecta...");
		        	 log(login,LocalDateTime.now());
		         }
		         
		     } 
			 else {
		    	 System.out.println("Usuario no encontrado.");
		     }
		        
			 rs.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}

		    return resultado;	
	}
	
	public static void log(String login, LocalDateTime momento) {
		File fichero=new File("errores.txt");
		
		FileOutputStream fil;
		try {
			fil = new FileOutputStream(fichero,true);
			DataOutputStream escribir= new DataOutputStream(fil);
			
			escribir.writeBytes(String.format("Login:%s, Momento:%s;\n",login,momento));
			escribir.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Usuario crearUsuario(Scanner teclado,Connection conn) {
		Usuario user=null;
		String login=null;
		
		boolean loginOk=false;
		
		System.out.println("Inserta el nombre completo:");
		String nombre=teclado.nextLine();
		
		
		System.out.println("Inserta el login:");
		login=teclado.nextLine();
		
		System.out.println("Inserta la contraseña:");
		String contrasena=teclado.nextLine();
		
		user=new Usuario(login,contrasena,nombre);
		return user;
	}
	
	public static void insertarUsurio(Usuario usuario, Connection conn) {
	    try {
			PreparedStatement insertUser=conn.prepareStatement("INSERT INTO usuarios(nombreLogin, contrasena, nombreCompleto) VALUES(?,?,?);");
			insertUser.setString(1, usuario.getNombreLogin());
			insertUser.setString(2, usuario.getContrasena());
			insertUser.setString(3, usuario.getNombreCompleto());
	    
	    	insertUser.executeUpdate();
	        System.out.println("Se ha creado el usuario con éxito....");
	    } catch (SQLException e) {
			System.out.println("Ya existe dicho login name...");
	        e.printStackTrace();
	    }
	}
	
	public static void mostrarEntradas(String login,Connection conn) {		
		ResultSet rs=null;

		Date id;
		try {
			rs=getUser(login,conn);
			if(rs.next() && rs!=null) {
				rs.close();
				PreparedStatement mostrar=conn.prepareStatement("select * from entradas where idUsuario in (select idUsuario from usuarios where nombreLogin=?)");
				mostrar.setString(1, login);
				rs=mostrar.executeQuery();
				
				
				while(rs.next()) {
					System.out.println(String.format("idUsuario=%d, idEntrada:%s.",rs.getInt("idUsuario"),rs.getTimestamp("idEntrada")));
				}
			}
			else {
				System.out.println("El usuario insertado no existe...");
			}
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertarEntrada(int id, Connection conn) {	    
	    try {
		    PreparedStatement insert=conn.prepareStatement("INSERT INTO entradas(idEntrada, idUsuario) VALUES(?, ?);");
		    insert.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
		    insert.setInt(2, id);
	    
	        insert.executeUpdate();
	        System.out.println("Se ha iniciado sesión con éxito....");
	    } catch (SQLException e) {
	        System.out.println("Error de sesión....");
	        e.printStackTrace();
	    }
	}
	
	public static String getPassWd(String passWd,Connection conn) {
		String resultado=null;
		
		try {
			PreparedStatement contrasena=conn.prepareStatement("select sha2(?,224);");
			contrasena.setString(1, passWd);
			
			ResultSet rs=contrasena.executeQuery();
			rs.next();
			resultado=rs.getString(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return resultado;
	}
	
	public static ResultSet getUser(String logIn,Connection conn) {
		ResultSet rs=null;
		
		try {
			PreparedStatement id=conn.prepareStatement("select * from usuarios where nombreLogin=?;");
			id.setString(1, logIn);
			
			rs=id.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	
	//This functions is not used in this version of project, in this version password is encrypted in Mysql
	/**public static String hashMD5(String contrasena) {
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
	}*/
	
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
			//char passwordArray[]= console.readPassword("Inserta la contraseña: ");
			
			String password=/**new String(passwordArray);*/teclado.nextLine();
		
			conn=DriverManager.getConnection(url, usuario, password);
		} catch (SQLException e) {
			System.out.println("Error de conexión....");
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return conn;
	}

}