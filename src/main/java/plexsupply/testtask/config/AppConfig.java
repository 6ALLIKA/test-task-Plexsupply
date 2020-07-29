package plexsupply.testtask.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import plexsupply.testtask.utils.FtpClient;

@Configuration
@PropertySource("classpath:application.properties")

public class AppConfig {
    private final Environment environment;

    @Autowired
    public AppConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean(name = "FTPStorage")
    public FtpClient getFtpStorage() {
        String host = environment.getProperty("ftp_storage_host");
        String port = environment.getProperty("ftp_storage_port");
        String username = environment.getProperty("ftp_storage_username");
        String password = environment.getProperty("ftp_storage_password");
        FtpClient ftpClient = new FtpClient(host, Integer.parseInt(port), username, password);
        return ftpClient;
    }

    @Bean(name = "FTPMain")
    public FtpClient getFtpMain() {
        String host = environment.getProperty("ftp_main_host");
        String port = environment.getProperty("ftp_main_port");
        String username = environment.getProperty("ftp_main_username");
        String password = environment.getProperty("ftp_main_password");
        FtpClient ftpClient = new FtpClient(host, Integer.parseInt(port), username, password);
        return ftpClient;
    }
}
