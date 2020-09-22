import java.io.*;
import java.net.*;
import java.util.Scanner;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

public class ConalitegBookDownloader {
  public static void main(String[] args){
    System.out.println("");
    System.out.println("----------------------------------------");
    System.out.println("DESCARGA DE LIBROS DESDE SITIO CONALITEG");
    System.out.println("----------------------------------------");
    System.out.println("");

    // Pedir datos
    Scanner sc = new Scanner(System.in);

    System.out.print("Nombre del Libro: ");
    String nameBook = sc.nextLine();

    System.out.print("Url Conaliteg: ");
    String urlBook = sc.nextLine();

    System.out.print("Num. paginas: ");
    int numPages = sc.nextInt();
    System.out.println("");

    // Descargar imagenes del libro
    String numPage = "";
    int x;

    for(int i=0; i<=numPages; i++){
      if(i<10){
        numPage = "00" + i;
      }else if(i>9 && i<100){
        numPage = "0" + i;
      }else{
        numPage = "" + i;
      }
      x = (i * 100)/numPages;
      System.out.print("Descargando " + nameBook + " del sitio Conaliteg... " + x + "%\r");

      descargaPaginas(nameBook, numPage, urlBook);
    }

    System.out.println("Descarga del libro " + nameBook + " finalizada.                          ");

    // Crear PDF
    
	  	//Crea el directorio de destino en caso de que no exista
	    File dir = new File("Libros/");
	
	    if (!dir.exists()){
	      if (!dir.mkdir()){
	          return; // no se pudo crear la carpeta de destino
	      }
	    }
    
	try {
		crearPDF(numPages, nameBook);
	} catch (FileNotFoundException | DocumentException e) {
		e.printStackTrace();
	}

    System.out.println("PDF " + nameBook + " creado.                          ");
    System.out.println("");
    
    // Borrar carpeta con imagenes
    numPage = "";
    String folder = "img" + nameBook + "/";
    File dirIMG = new File(folder);
    String name = "";
    
    for(int i=0; i<=numPages; i++){
        if(i<10){
          numPage = "00" + i;
        }else if(i>9 && i<100){
          numPage = "0" + i;
        }else{
          numPage = "" + i;
        }
        
        x = (i * 100)/numPages;
        name = nameBook + numPage + ".jpg"; //nombre del archivo
        File file = new File(folder + name);
        if (file.delete()) {
        	System.out.print("Eliminando archivos no necesarios... " + x + "%\r");
        }
      }
    
    if(dirIMG.delete()) {
    	System.out.println("Los archivos no necesarios fueron eliminados.                          ");
    	System.out.println("");
    }
    
    System.out.println("-- ¡Operacion finalizada con exito! --");
    System.out.println("");
  }

  private static void descargaPaginas(String nameBook, String numPage, String urlBook){

    String url = urlBook + numPage + ".jpg"; //dirección url del recurso a descargar
    String name = nameBook + numPage + ".jpg"; //nombre del archivo destino
    String folder = "img" + nameBook + "/";//Directorio destino para las descargas

    //Crea el directorio de destino en caso de que no exista
    File dir = new File(folder);
    if (!dir.exists()){
      if (!dir.mkdir()){
          return; // no se pudo crear la carpeta de destino
      }
    }


    File file = new File(folder + name);
    try {

        URLConnection conn = new URL(url).openConnection();
        conn.connect();
      //  System.out.println("\nempezando descarga: \n");
      //  System.out.println(">> URL: " + url);
      //  System.out.println(">> Nombre: " + name);
      //  System.out.println(">> tamaño: " + conn.getContentLength() + " bytes");
        InputStream in = conn.getInputStream();
        OutputStream out = new FileOutputStream(file);

        int b = 0;
        while (b != -1) {
          b = in.read();
          if (b != -1)
            out.write(b);
        }

        out.close();
        in.close();

    } catch (MalformedURLException e) {
      System.out.println("la url: " + url + " no es valida!");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  
  public static void crearPDF(int numPages, String nameBook) throws FileNotFoundException, DocumentException {
	  
	  String numPage = "";
	  int x = 0;
  
      // Se crea el documento
      Document documento = new Document();
      Image imagen;
      
      OutputStream ficheroPDF = new FileOutputStream("Libros/" + nameBook + ".pdf"); // El OutPutStream para el fichero donde crearemos el PDF
      PdfWriter.getInstance(documento, ficheroPDF); // Se asocia el documento de OutPutStream
      
      // Se abre el documento
      documento.open();
      float height = documento.getPageSize().getHeight();
      float width = documento.getPageSize().getWidth();
      // Añadimos las imagenes al documento
      for(int i=0; i<=numPages; i++){
          if(i<10){
            numPage = "00" + i;
          }else if(i>9 && i<100){
            numPage = "0" + i;
          }else{
            numPage = "" + i;
          }
          x = (i * 100)/numPages;
          System.out.print("Creando PDF " + nameBook + "... " + x + "%\r");

	        try {
				imagen = Image.getInstance("img" + nameBook + "/" + nameBook + numPage + ".jpg");
				//imagen.scaleToFit(100, 100);
		          imagen.setAlignment(Chunk.ALIGN_MIDDLE);
		          
		          imagen.scaleToFit(width, height);
		          documento.add(imagen);
			} catch (BadElementException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
      
      // Se cierra el documento
      documento.close();
  }
  
}