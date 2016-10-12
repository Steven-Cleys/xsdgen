package be.devoowi.controller;

import be.devoowi.xsdgenerator.GenerateXsdFromBiSampleCode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.*;



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

                String rootPath = System.getProperty("user.dir");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();
                // keeping the folder clean
                logger.info("Cleaning directory");
                FileUtils.cleanDirectory(dir);
                InputStream myInputStream = file.getInputstream();


                // Create the file on server
                String fileName = file.getFileName().substring(file.getFileName().lastIndexOf('\\') + 1); // Shit Explorer fix.
                logger.info("filename: " + fileName);
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + fileName);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(IOUtils.toByteArray(myInputStream));

                stream.close();

                logger.info("Server File Location="
                        + serverFile.getAbsolutePath());

                GenerateXsdFromBiSampleCode gen = new GenerateXsdFromBiSampleCode();

                String xsdstring = gen.run(serverFile.getAbsolutePath());


                download = new DefaultStreamedContent();
                InputStream input = new ByteArrayInputStream(xsdstring.getBytes());
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                download = new DefaultStreamedContent(input, externalContext.getMimeType(file.getFileName()+".xsd"), file.getFileName()+".xsd");

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Info", "PrimeFaces Rocks."));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return download;
    }
}