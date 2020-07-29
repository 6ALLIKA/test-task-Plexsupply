package plexsupply.testtask.service.impl;

import dnl.utils.text.table.TextTable;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import lombok.extern.log4j.Log4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import plexsupply.testtask.service.CsvParser;

@Service
@Log4j
public class CsvParserImpl implements CsvParser {

    public static final String TMP_PATH_FOLDER = "src/main/resources/tmp/";

    @Override
    public List<CSVRecord> parseCsvFile(String fileName) {
        List<CSVRecord> records = null;
        try (InputStream inputStream = new FileInputStream(TMP_PATH_FOLDER + fileName);
             BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            records = csvParser.getRecords();
        } catch (FileNotFoundException e) {
            log.error("File in application not found.");
        } catch (IOException e) {
            log.error("CSV parsing was failed", e);
        }
        return records;
    }

    @Override
    public void printCsv(List<CSVRecord> records) {
        String[][] stringMatrix = new String[records.size()][records.get(0).size()];
        for (int i = 0; i < records.size(); i++) {
            Iterator<String> iterator = records.get(i).iterator();
            for (int z = 0; z < records.get(0).size(); z++) {
                stringMatrix[i][z] = iterator.next();
            }
        }
        TextTable textTable = new TextTable(new String[records.get(0).size()], stringMatrix);
        textTable.printTable();
    }
}
