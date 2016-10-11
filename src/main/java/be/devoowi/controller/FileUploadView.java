package be.devoowi.controller;

import be.devoowi.xsdgenerator.GenerateXsdFromBiSampleCode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@ManagedBean
public class FileUploadView {
    private static final Logger logger = Logger.getLogger(FileUploadView.class.toString());
    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getUpload() {
        StreamedContent download = null;
        if (file != null) {

            try {
                // Creating the directory to store file

                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();
                // keeping the folder clean
                logger.info("Cleaning directory");
                FileUtils.cleanDirectory(dir);
                //download = new DefaultStreamedContent(file.getInputstream());
                InputStream myInputStream = file.getInputstream();


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

                //xsdgen = new ByteArrayInputStream(gen.run(serverFile.getAbsolutePath()).getBytes(StandardCharsets.UTF_8));
                gen.run(serverFile.getAbsolutePath()).getBytes(StandardCharsets.UTF_8);
                download = new DefaultStreamedContent();
                File file = new File("C:\\Users\\cleysst\\workspace\\xsdgen\\null\\tmpFiles\\XXKPMG_EMPLOYEES_PART_2_CODE.xml.xsd");
                InputStream input = new FileInputStream(file);
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                download = new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());
                System.out.println("PREP = " + download.getName());
                //return download;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return download;
    }

    public void showMessage() {
        logger.info("showmessage");
        FacesContext.getCurrentInstance().addMessage("message1", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "PrimeFaces Rocks."));
    }
}