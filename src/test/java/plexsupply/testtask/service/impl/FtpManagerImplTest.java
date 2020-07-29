package plexsupply.testtask.service.impl;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import plexsupply.testtask.utils.FtpClient;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FtpManagerImplTest {
    private FakeFtpServer fakeFtpServer;

    private FtpClient ftpClient;

    @BeforeEach
    public void setup() {
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.addUserAccount(new UserAccount("user", "password", "/data"));

        UnixFakeFileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/data"));
        fileSystem.add(new FileEntry("/data/foobar.txt", "abcdef 1234567890"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.setServerControlPort(0);

        fakeFtpServer.start();

        ftpClient = new FtpClient("localhost", fakeFtpServer.getServerControlPort(), "user", "password");
        ftpClient.open();
    }

    @AfterEach
    public void teardown() {
        ftpClient.close();
        fakeFtpServer.stop();
    }

    @Test
    void getFileFromFtpServerAndCheck() {
        ftpClient.findFileAtServerAndCopy("/data/", "foobar.txt");
        assertTrue((new File("src/main/resources/tmp/foobar.txt")).exists());
    }

    @Test
    void uploadCheckedFileToStpServer() {
        ftpClient.uploadFileToFtpServer("/data/", "foobar.txt");
        assertTrue(fakeFtpServer.getFileSystem().exists("/data/foobar.txt"));
    }
}