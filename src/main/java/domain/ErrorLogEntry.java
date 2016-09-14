package domain;

import java.io.File;

/**
 * Created by jayamalk on 9/14/2016.
 */
public class ErrorLogEntry {

    private File file;
    private String reason;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
