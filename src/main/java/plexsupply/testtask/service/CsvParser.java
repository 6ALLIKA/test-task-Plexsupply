package plexsupply.testtask.service;

import java.util.List;
import org.apache.commons.csv.CSVRecord;

public interface CsvParser {

    List<CSVRecord> parseCsvFile(String fileName);

    void printCsv(List<CSVRecord> records);
}
