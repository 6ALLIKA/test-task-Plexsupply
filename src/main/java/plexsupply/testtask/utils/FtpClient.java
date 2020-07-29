package plexsupply.testtask.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import plexsupply.testtask.exceptions.FtpExeption;

@Log4j
public class FtpClient {

    public static final String TMP_PATH_FOLDER = "src/main/resources/tmp/";
    private final String server;
    private final int port;
    private final String user;
    private final String password;
    private FTPClient ftp;

    public FtpClient(String server, int port, String user, String password) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void open() {
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        try {
            ftp.connect(server, port);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
            }
            log.info("Connecting to server " + server + " success.");
        } catch (IOException e) {
            throw new FtpExeption("Exception in connecting to FTP Server", e);
        }

        try {
            ftp.login(user, password);
            log.info("Successful authentication to server.");
        } catch (IOException e) {
            log.info("Unsuccessful authentication to server.");
        }
    }

    public void close() {
        try {
            ftp.disconnect();
        } catch (IOException e) {
            log.warn("Disconnect from server " + server + " unsuccessful.");
        }
    }

    public boolean findFileAtServerAndCopy(String folderPath, String... fileNames) {
        ftp.enterLocalPassiveMode();
        FTPFile[] ftpFiles;
        boolean ftpFilesExist = false;
        try {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftpFiles = ftp.listFiles(folderPath);
        } catch (IOException e) {
            throw new FtpExeption("Receiving files list in folder is failed!", e);
        }
        for (FTPFile file : ftpFiles) {
            for (String fileName : fileNames) {
                if (file.getName().equals(fileName)) {
                    ftpFilesExist = true;
                    break;
                }
            }
        }

        if (ftpFilesExist) {
            return copyFileFromFtp(folderPath, fileNames);
        }
        return false;
    }

    public boolean uploadFileToFtpServer(String folderPath, String... fileNames) {
        boolean allFlesUploaded = false;
        for (String fileName : fileNames) {
            FileInputStream fileInputStream;
            File downloadedFile = new File(TMP_PATH_FOLDER + fileName);
            try {
                fileInputStream = new FileInputStream(downloadedFile);
                allFlesUploaded = ftp.storeFile(folderPath + fileName,
                        fileInputStream);
                log.info("File uploading to FTP server is successful.");
                fileInputStream.close();
                if (downloadedFile.delete()) {
                    log.info("Temporary file " + fileName + " from project deleted.");
                } else {
                    log.warn("Temporary file " + fileName + " from project wasn't deleted.");
                }
            } catch (FileNotFoundException e) {
                log.error("File " + fileName + " in application not found.");
            } catch (IOException e) {
                throw new FtpExeption("Uploading file is failed!", e);
            }
        }
        return allFlesUploaded;
    }

    private boolean copyFileFromFtp(String folderPath, String... fileNames) {
        boolean allFilesRxists = false;
        for (String fileName : fileNames) {
            File downloadedFile = new File(TMP_PATH_FOLDER + fileName);
            try {
                OutputStream outputStream =
                        new BufferedOutputStream(new FileOutputStream(downloadedFile));
                InputStream inputStream = ftp.retrieveFileStream(folderPath + fileName);
                byte[] bytesArray = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                    outputStream.write(bytesArray, 0, bytesRead);
                }
                boolean success = ftp.completePendingCommand();
                if (success) {
                    log.info("File " + fileName + " has been downloaded successfully.");
                }
                allFilesRxists = downloadedFile.exists();
                outputStream.close();
                inputStream.close();
            } catch (FileNotFoundException e) {
                log.warn("File " + fileName + " can't be created!", e);
            } catch (IOException e) {
                throw new FtpExeption("Downloading file is failed!");
            }
        }
        return allFilesRxists;
    }
}
