package be.devoowi.controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import be.devoowi.xsdgenerator.GenerateXsdFromBiSampleCode;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
@Controller
@WebServlet
public class FileUploadController {

	private static final Logger logger = Logger.getLogger(FileUploadController.class.toString());
	private InputStream xsdgen;

	//private MultipartFile file;
	/**
	 * Upload single file using Spring Controller
	 */

	public void uploadFileHandler(FileUploadEvent event) {

			try {
				UploadedFile file = event.getFile();
				InputStream myInputStream = file.getInputstream();

				// Creating the directory to store file
				String rootPath = System.getProperty("catalina.home");
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + file.getFileName());
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(IOUtils.toByteArray(myInputStream));

				stream.close();

				logger.info("Server File Location="
						+ serverFile.getAbsolutePath());

				GenerateXsdFromBiSampleCode gen = new GenerateXsdFromBiSampleCode();

				xsdgen = new ByteArrayInputStream(gen.run(serverFile.getAbsolutePath()).getBytes(StandardCharsets.UTF_8));
				HttpServletResponse response =
						(HttpServletResponse) FacesContext.getCurrentInstance()
								.getExternalContext().getResponse();

				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=autogen.xsd");
				//OutputStream out = response.getOutputStream();
				byte [] buffer = new byte[1024];
				int bytesRead = 0;
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				while((bytesRead = xsdgen.read(buffer)) != -1)
				{

					out.write(buffer, 0, bytesRead);
				}
				out.close();
				response.getOutputStream().write(out.toByteArray());
				response.getOutputStream().flush();
				response.getOutputStream().close();
				getDownload();
				FacesContext.getCurrentInstance().responseComplete();


			} catch (Exception e) {
				//return "You failed to upload " + name + " => " + e.getMessage();
			}
	}

	public StreamedContent getDownload() throws Exception {
		StreamedContent download=new DefaultStreamedContent();
		File file = new File("C:\\Users\\cleysst\\workspace\\xsdgen\\null\\tmpFiles\\XXKPMG_EMPLOYEES_PART_2_CODE.xml.xsd");
		InputStream input = new FileInputStream(file);
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		download = new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());
		System.out.println("PREP = " + download.getName());
		return download;
	}

	/**
	 * Upload multiple file using Spring Controller
	 */
	@RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
	public @ResponseBody
	String uploadMultipleFileHandler(@RequestParam("name") String[] names,
			@RequestParam("file") MultipartFile[] files) {

		if (files.length != names.length)
			return "Mandatory information missing";

		String message = "";
		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			String name = names[i];
			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("catalina.home");
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.info("Server File Location="
						+ serverFile.getAbsolutePath());

				message = message + "You successfully uploaded file=" + name
						+ "<br />";
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		}
		return message;
	}
}
