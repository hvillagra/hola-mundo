package Modelo;

import java.awt.Font;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JOptionPane;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Clase de modelo Informe.
 * @author Hugo Villagra G.
 * @author Nicolas Salazar S.
 * @version 1.0
 */
public class Informe {

	private int cod_informe;
	private Mecanico mecanico;
	private Inspector inspector;
	private Reparacion reparacion;
	private Mecanismo mecanismo;

	//instancia a la BD

	public int getCod_informe() {
		return cod_informe;
	}
	public void setCod_informe(int cod_informe) {
		this.cod_informe = cod_informe;
	}
	public Mecanico getMecanico() {
		return mecanico;
	}
	public void setMecanico(Mecanico mecanico) {
		this.mecanico = mecanico;
	}
	public Inspector getInspector() {
		return inspector;
	}
	public void setInspector(Inspector inspector) {
		this.inspector = inspector;
	}
	public Reparacion getReparacion() {
		return reparacion;
	}
	public void setReparacion(Reparacion reparacion) {
		this.reparacion = reparacion;
	}
	public Mecanismo getMecanismo() {
		return mecanismo;
	}
	public void setMecanismo(Mecanismo mecanismo) {
		this.mecanismo = mecanismo;
	}
	
	/**
	 * Funcionalidad de Exportar Informe.
	 * @param id_rep int ID Reparación.
	 * @param id_mec int ID Mecanismo.
	 * @param ruta String Ruta de destino del archivo.
	 * @param persona Persona que gatilla la funcionalidad.
	 * @param inspector Persona que encuentra la falla.
	 * @param mecanico Persona quien reparará la falla.
	 * @param material String Material.
	 * @param maquina String Maquina.
	 * @param inspeccion String Inspección.
	 */
	public void imprimir(int id_reparacion,int id_mec, String ruta,Persona persona,Persona inspector, Persona mecanico,String material, String maquina, String inspeccion){
		
		String consulta1 = "SELECT M.ID_MECANISMO,M.MECANISMO, M.CAUSA, M.EFECTO, M.EFECTO_MOLECULAR, R.ID_REPARACION, R.DESC_REPARACION FROM MECANISMO M , REPARACION R ,FALLA F WHERE M.ID_MECANISMO = F.ID_MECANISMO AND R.ID_REPARACION = F.ID_REPARACION AND M.ID_MECANISMO = "+id_mec+" AND R.ID_REPARACION = "+id_reparacion;

		//conexion a la BD
		Connection conex = getConexion();
		this.estadoConexion();


		Mecanismo mec = new Mecanismo(); ;
		Reparacion rep =  new Reparacion(); ; 

		try {

			Statement instruccion = conex.createStatement();
			ResultSet rs = instruccion.executeQuery(consulta1);

			while (rs.next()) {

				mec.setId_mecanismo(rs.getInt("ID_MECANISMO"));
				mec.setMecanismo(rs.getString("MECANISMO"));
				mec.setCausa(rs.getString("CAUSA"));
				mec.setEfecto(rs.getString("EFECTO"));
				mec.setEfecto_molecular(rs.getString("EFECTO_MOLECULAR"));
				rep.setId_reparacion(rs.getInt("ID_REPARACION"));
				rep.setReparacion(rs.getString("DESC_REPARACION"));

			}
			
			conex.close();
			instruccion.close();

		} catch (SQLException e) {
			System.out.println("Consulta '" + consulta1 + "' fallida.\n Error: "
					+ e);
		}

		try{
			
			FileOutputStream archivo = new FileOutputStream(ruta+".pdf");
			Document doc = new Document();
			PdfWriter.getInstance(doc, archivo);
			doc.open();
			Image foto = Image.getInstance(getClass().getResource("/Recursos/logo_enap1.png"));
			foto.scaleToFit(150, 150);
			foto.setAlignment(Chunk.ALIGN_RIGHT);
			doc.add(foto);
			
			Calendar c1 = Calendar.getInstance();
			String dia = Integer.toString(c1.get(Calendar.DATE));
			String mes = Integer.toString(c1.get(Calendar.MONTH)+1);
			String annio = Integer.toString(c1.get(Calendar.YEAR));
			
			Paragraph fecha = new Paragraph(dia+"/"+mes+"/"+annio,FontFactory.getFont("arial",13,Font.BOLD));	
			fecha.setAlignment(Element.ALIGN_RIGHT);
			doc.add(fecha);
			
			Paragraph informe = new Paragraph("\nInforme de Reparación\n\n",FontFactory.getFont("arial",18,Font.BOLD));	
			informe.setAlignment(Element.ALIGN_CENTER);
			doc.add(informe);
			
			Paragraph mecanismoTit = new Paragraph("Mecanismo de Falla:\n\n",FontFactory.getFont("arial",13,Font.BOLD));
			doc.add(mecanismoTit);
			
			Paragraph mecanismo1 = new Paragraph(mec.getMecanismo()+"\n\n",FontFactory.getFont("arial",12));
			doc.add(mecanismo1);
			
			Paragraph causaTit = new Paragraph("Causa de la Falla:\n\n",FontFactory.getFont("arial",13,Font.BOLD));
			doc.add(causaTit);
			
			Paragraph causa = new Paragraph(mec.getCausa()+"\n\n",FontFactory.getFont("arial",12));
			doc.add(causa);
			
			Paragraph efectoTit = new Paragraph("Efecto:\n\n",FontFactory.getFont("arial",13,Font.BOLD));
			doc.add(efectoTit);
			
			Paragraph efecto = new Paragraph(mec.getEfecto()+"\n\n",FontFactory.getFont("arial",12));
			doc.add(efecto);
			
			Paragraph efectoMolTit = new Paragraph("Efecto Molecular:\n\n",FontFactory.getFont("arial",13,Font.BOLD));
			doc.add(efectoMolTit);
			
			Paragraph efectoMol = new Paragraph(mec.getEfecto_molecular()+"\n\n",FontFactory.getFont("arial",12));
			doc.add(efectoMol);
			
			Paragraph reparacionTit = new Paragraph("Reparación:\n\n",FontFactory.getFont("arial",13,Font.BOLD));
			doc.add(reparacionTit);
			
			Paragraph reparacion = new Paragraph(rep.getReparacion()+"\n\n",FontFactory.getFont("arial",12));
			doc.add(reparacion);
			//Quien lo detecto
			Paragraph inspDetecto = new Paragraph("Inspector que detecto la falla:",FontFactory.getFont("arial",13,Font.BOLD));
			doc.add(inspDetecto);
			
			Paragraph nombreInsp = new Paragraph(inspector.getNombre()+" "+inspector.getApellido_pat()+" "+ inspector.getApellido_mat()+"",FontFactory.getFont("arial",13,Font.BOLD));
			doc.add(nombreInsp);
			
			Paragraph correoInsp = new Paragraph(inspector.getMail()+"\n\n",FontFactory.getFont("arial",12));
			doc.add(correoInsp);
			
			//Quien lo Reparara
			Paragraph mecRepara = new Paragraph("Mecanico que Reparará la máquina:",FontFactory.getFont("arial",13,Font.BOLD));
			doc.add(mecRepara);
			
			Paragraph nombreMec = new Paragraph(mecanico.getNombre()+" "+mecanico.getApellido_pat()+" "+ mecanico.getApellido_mat()+"",FontFactory.getFont("arial",13,Font.BOLD));
			doc.add(nombreMec);
			
			Paragraph correoMec = new Paragraph(mecanico.getMail()+"\n\n",FontFactory.getFont("arial",12));
			doc.add(correoMec);
			
			Paragraph correoTit = new Paragraph("Contacto encargado del Informe:",FontFactory.getFont("arial",13,Font.BOLD));
			doc.add(correoTit);
			
			Paragraph nombreTit = new Paragraph(persona.getNombre()+" "+persona.getApellido_pat()+" "+ persona.getApellido_mat()+"",FontFactory.getFont("arial",13,Font.BOLD));
			doc.add(nombreTit);
			
			Paragraph correo = new Paragraph(persona.getMail()+"",FontFactory.getFont("arial",12));
			doc.add(correo);
			
			//Paragraph rutTit = new Paragraph(persona.getId_rut()+"",FontFactory.getFont("arial",13,Font.BOLD));
			//doc.add(rutTit);
		
			
			doc.close();
		
			JOptionPane.showMessageDialog(null, "Documento creado con exito");
			
			
			this.nuevoInforme(persona, mecanico,inspector,id_mec, id_reparacion,material,maquina,inspeccion);
			

		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error "+e);
		}
	



	}
	
	/**
	 * Funcionalidad de Registrar Informe.
	 * @param persona Persona que gatilla la funcionalidad.
	 * @param mecanico Persona quien reparará la falla.
	 * @param inspector Persona que encuentra la falla.
	 * @param id_mec int ID Mecanismo.
	 * @param id_rep int ID Reparación.
	 * @param material String Material.
	 * @param maquina String Maquina.
	 * @param inspeccion String Inspección.
	 */
	public void nuevoInforme(Persona persona,Persona mecanico,Persona inspector, int id_mec, int id_rep,String material, String maquina, String inspeccion ){
		
		//Query para la BD
		String consulta1 = "SELECT * FROM INFORME";
		//
		//Conexion con la BD
		Connection conex = getConexion();
		this.estadoConexion();
		//
		//Variables para mantenedor de datos
		ArrayList<Informe> array = new ArrayList<Informe>();
		Informe c = new Informe();
		
		int idMaterial=0;
		int idMaquina=0;
		int idInspeccion=0;
		//
		try {

			//Realizar Query
			Statement instruccion = conex.createStatement();
			ResultSet rs = instruccion.executeQuery(consulta1);
			//
		
			
			String idMatQ = ("SELECT ID_MATERIAL FROM MATERIAL WHERE DESC_MATERIAL= ? AND IND_VIG='S'");
			PreparedStatement prueba1 = conex.prepareStatement(idMatQ);
			prueba1.setString(1, material);
			String idMaqQ = ("SELECT ID_MAQUINA FROM MAQUINA WHERE DESC_MAQUINA= ? AND IND_VIG='S'");
			PreparedStatement prueba2 = conex.prepareStatement(idMaqQ);
			prueba2.setString(1, maquina);
			String idInsQ = ("SELECT ID_INSPECCION FROM INSPECCION WHERE DESC_INSPECCION= ? AND IND_VIG='S'");
			PreparedStatement prueba3 = conex.prepareStatement(idInsQ);
			prueba3.setString(1, inspeccion);
			//ResultSet idMat = instruccion2.executeQuery(idMatQ);
			ResultSet idMat = prueba1.executeQuery();
			//ResultSet idMaq = instruccion3.executeQuery(idMaqQ);
			ResultSet idMaq = prueba2.executeQuery();
			//ResultSet idIns = instruccion4.executeQuery(idInsQ);
			ResultSet idIns = prueba3.executeQuery();
			
			//Guardar Datos en Contenedores
			
			
			while (rs.next()) {

				c = new Informe();
				c.setCod_informe(rs.getInt("ID_INFORME"));
				array.add(c);

			}
			if(idMat.next()){
				idMaterial=idMat.getInt("ID_MATERIAL");
			}
			if(idMaq.next()){
				idMaquina=idMaq.getInt("ID_MAQUINA");	
			}
			if(idIns.next()){
				idInspeccion=idIns.getInt("ID_INSPECCION");
			}
						//
			
			instruccion.close();

		} catch (SQLException e) {
			System.out.println("Consulta '" + consulta1 + "' fallida.\n Error: "
					+ e);
		}



		//Buscar el ultimo ID_MAQUINA
		int numFilas = array.size();
		int ultimoNumero = 1;

		if(numFilas != 0){

			ultimoNumero = array.get(numFilas-1).getCod_informe();
			ultimoNumero++;

		}
		
		
		String consulta2 = "INSERT INTO INFORME VALUES("+ultimoNumero+","+persona.getId_rut()+","+persona.getId_tipo_persona()+","+id_mec+","+id_rep+",GETDATE(),'S',"+idMaterial+","+idMaquina+","+idInspeccion+")";
		String consulta3 = "INSERT INTO INFORME VALUES("+ultimoNumero+","+mecanico.getId_rut()+","+mecanico.getId_tipo_persona()+","+id_mec+","+id_rep+",GETDATE(),'N',"+idMaterial+","+idMaquina+","+idInspeccion+")";
		String consulta4 = "INSERT INTO INFORME VALUES("+ultimoNumero+","+inspector.getId_rut()+","+inspector.getId_tipo_persona()+","+id_mec+","+id_rep+",GETDATE(),'N',"+idMaterial+","+idMaquina+","+idInspeccion+")";
		
		
		try {

			//Realizar Query
			Statement instruccion = conex.createStatement();
			int rs = instruccion.executeUpdate(consulta2);
			System.out.println(rs);
			int rs2 = instruccion.executeUpdate(consulta3);
			System.out.println(rs2);
			int rs3 = instruccion.executeUpdate(consulta4);
			System.out.println(rs3);
			
			instruccion.close();
			conex.close();

		} catch (SQLException e) {
			System.out.println("Consulta '" + consulta1 + "' fallida.\n Error: "
					+ e);
		}
		
		
	}

	/*---------------------------------------------------------------------*/
	/*-------------------Conexion a la BD----------------------------------*/

	/**
	 * Conexión a la Base de Datos.
	 * @return Connection.
	 */
	@SuppressWarnings("finally")
	public static Connection getConexion()
	{
		Connection conexion=null;

		try
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = "jdbc:sqlserver://localhost;databaseName=OLTP_ACONCAGUA;user=refineria;password=refineria;";
			//String url = "jdbc:sqlserver://refineria.redirectme.net;databaseName=OLTP_ACONCAGUA;user=refineria;password=refineria;";
			conexion= DriverManager.getConnection(url);
		}
		catch(ClassNotFoundException ex)
		{

		}
		catch(SQLException ex)
		{

		}
		catch(Exception ex)
		{

		}
		finally
		{
			return conexion;
		}
	}

	/**
	 * Revisar Estado de Conexión.
	 */
	public void estadoConexion(){
		if( getConexion() != null )
			System.out.println("Conexion exitosa");
		else
			System.out.println("Conexion no lograda");
	}

	/*---------------------------------------------------------------------*/
}
