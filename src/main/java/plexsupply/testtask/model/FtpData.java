package plexsupply.testtask.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FtpData {
    private String folderName;
    private String[] fileNames;
}
