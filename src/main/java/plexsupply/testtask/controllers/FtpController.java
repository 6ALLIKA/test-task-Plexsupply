package plexsupply.testtask.controllers;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plexsupply.testtask.model.FtpData;
import plexsupply.testtask.service.CsvParser;
import plexsupply.testtask.service.FtpManager;

@AllArgsConstructor
@RestController
@RequestMapping("/test")
public class FtpController {
    private final FtpManager ftpManager;
    private final CsvParser csvParser;

    @GetMapping
    @ApiOperation(value = "Enter folder with files where they contains like '/folder/ or root '/'"
            + " and file names like file.csv. Endpoint give boolean that all files "
            + "are saved to another server or not",
            response = boolean.class)
    public boolean checkFilesFromStorageAndSaveToMainServer(FtpData notification) {
        boolean fileDownloaded =
                ftpManager.getFileFromFtpServerAndCheck(notification.getFolderName(),
                        notification.getFileNames());
        for (String fileName : notification.getFileNames()) {
            List<CSVRecord> csvRecords = csvParser.parseCsvFile(fileName);
            csvParser.printCsv(csvRecords);
        }

        if (fileDownloaded) {
            return ftpManager.uploadCheckedFileToStpServer(notification.getFolderName(),
                    notification.getFileNames());
        } else {
            return false;
        }
    }
}
