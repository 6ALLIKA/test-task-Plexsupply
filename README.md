# Test Task

---

# Table of Contents
* [Project structure](#structure)
* [For developer](#developer-start)
* [Author](#author)

---

# <a name="structure"></a>Project Structure
* Java 11
* Maven 3.6.0
* Spring Boot 
  * context, webmvc, swagger
* Maven checkstyle plugin 3.1.1
* Lombok 1.18.12
* Log4j 1.2.17
* Commons csv 1.8
* JUnit 5v
* MockFtpServer 2.7.1
* Commons net 3.6
<hr>

---

# <a name="developer-start"></a>For developer

1. Open the project in your IDE.

2. Add Java SDK 11 or above in Project Structure.

3. Change a path to log file in src.main.resources.log4j.properties. (Optional)

4. Configure FTP server [FileZilla](https://sonikelf.ru/sozdaem-svoj-sobstvennyj-ftp-server-na-baze-filezilla-server/).
 - in Passive mode settings use following IP 127.0.0.1
 - configure FTP over TLS settings
 - add users

5. Configure in application.properties parameters for FtpClient

6. Run the project.

7. For testing this API open URL [Swagger](http://localhost:8080/swagger-ui.html#).

---

# <a name="author"></a>Author

[Skorobohatyi Dmytro](https://github.com/6ALLIKA)
