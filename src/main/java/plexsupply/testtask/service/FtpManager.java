package plexsupply.testtask.service;

public interface FtpManager {

    boolean getFileFromFtpServerAndCheck(String folderPath, String... fileNames);

    boolean uploadCheckedFileToStpServer(String folderPath, String... fileNames);
}
