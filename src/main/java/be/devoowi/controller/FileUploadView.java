package be.devoowi.controller;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import java.io.IOException;

@ManagedBean
public class FileUploadView {

    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getUpload() {
        StreamedContent download = null;
        if(file != null) {

            try {
                 download = new DefaultStreamedContent(file.getInputstream());
            
            } catch (IOException e) {
                e.printStackTrace();
            }
           
            
        }
        return download;
    }
}