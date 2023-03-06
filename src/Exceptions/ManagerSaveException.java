
package Exceptions;

import Manager.Feedback;

public class ManagerSaveException extends Exception {

    final private String message;
    final private String filePath;

    public ManagerSaveException(final String filePath) {
        this.filePath = "Путь к файлу: "+filePath;
        message = String.valueOf(Feedback.ERROR_WRITING_FILE);
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
