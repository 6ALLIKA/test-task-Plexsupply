package plexsupply.testtask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import plexsupply.testtask.service.FtpManager;
import plexsupply.testtask.utils.FtpClient;

@Service
public class FtpManagerImpl implements FtpManager {
    private FtpClient ftpClientStorage;
    private FtpClient ftpClientMain;

    @Autowired
    public FtpManagerImpl(@Qualifier("FTPStorage") FtpClient ftpClientStorage,
                          @Qualifier("FTPMain") FtpClient ftpClientMain) {
        this.ftpClientStorage = ftpClientStorage;
        this.ftpClientMain = ftpClientMain;
    }

    @Override
    public boolean getFileFromFtpServerAndCheck(String folderPath, String... fileNames) {
        ftpClientStorage.open();
        boolean success = ftpClientStorage.findFileAtServerAndCopy(folderPath, fileNames);
        ftpClientStorage.close();
        return success;
    }

    @Override
    public boolean uploadCheckedFileToStpServer(String folderPath, String... fileNames) {
        ftpClientMain.open();
        boolean success = ftpClientMain.uploadFileToFtpServer(folderPath, fileNames);
        ftpClientMain.close();
        return success;
    }
}
