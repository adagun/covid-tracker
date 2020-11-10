package tech.adagun.covidtracker.services;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tech.adagun.covidtracker.models.RegionStats;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CovidDataService
{
    private static final String DATA_URL = "https://www.arcgis.com/sharing/rest/content/items/b5e7488e117749c19881cce45db13f7e/data";

    public List<RegionStats> getStatsList()
    {
        return statsList;
    }

    private List<RegionStats> statsList = new ArrayList<>();

    @PostConstruct
    // run at 15:00 every weekday
    @Scheduled(cron = "0 0 15 * * 1-5", zone = "Europe/Stockholm")
    public void fetchData() throws IOException, InterruptedException
    {
        downloadData();
        String previousDateString;
        LocalDate currentDate = LocalDate.now();
        String currentDateString = currentDate.toString();

        if (currentDate.getDayOfWeek().toString().equals("MONDAY"))
        {
            // get Friday's date
            previousDateString = currentDate.minusDays(3).toString();
        } else
        {
            previousDateString = currentDate.minusDays(1).toString();
        }

        XSSFSheet currentDaySheet = getDataSheet(currentDateString);
        XSSFSheet previousDaySheet;
        try
        {
            previousDaySheet = getDataSheet(previousDateString);
        } catch (IOException e)
        {
            previousDaySheet = currentDaySheet;
        }

        List<RegionStats> newStatsList = new ArrayList<>();

        for (int i = 1; i < currentDaySheet.getPhysicalNumberOfRows(); i++)
        {
            XSSFRow currentDayRow = currentDaySheet.getRow(i);
            XSSFRow previousDayRow = previousDaySheet.getRow(i);
            RegionStats regionStat = new RegionStats();

            regionStat.setRegion(currentDayRow.getCell(0).getStringCellValue());
            int latestTotalCases = (int) currentDayRow.getCell(1).getNumericCellValue();
            int previousTotalCases = (int) previousDayRow.getCell(1).getNumericCellValue();
            int latestTotalDeaths = (int) currentDayRow.getCell(4).getNumericCellValue();
            int previousTotalDeaths = (int) previousDayRow.getCell(4).getNumericCellValue();
            regionStat.setLatestTotalCases(latestTotalCases);
            regionStat.setLatestTotalDeaths(latestTotalDeaths);
            regionStat.setCasesDiffPreviousDay(latestTotalCases - previousTotalCases);
            regionStat.setDeathsDiffPreviousDay(latestTotalDeaths - previousTotalDeaths);
            newStatsList.add(regionStat);
        }
        this.statsList = newStatsList;
    }

    private void downloadData() throws IOException
    {
        String saveFilePath = "data/" + LocalDate.now() + ".xlsx";
        InputStream in = new URL(DATA_URL).openStream();
        Files.copy(in, Paths.get(saveFilePath), StandardCopyOption.REPLACE_EXISTING);
    }

    private XSSFSheet getDataSheet(String filename) throws IOException
    {
        File myFile = new File("data/" + filename + ".xlsx");
        FileInputStream fis = new FileInputStream(myFile);
        XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
        return myWorkBook.getSheetAt(3);
    }
}