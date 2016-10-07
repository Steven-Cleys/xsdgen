package be.devoowi.controller;


        import javax.faces.application.FacesMessage;
        import javax.faces.bean.ManagedBean;
        import javax.faces.context.FacesContext;

        import org.primefaces.event.FileUploadEvent;
        import org.primefaces.model.UploadedFile;

@ManagedBean
public class FileUploadView {

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}