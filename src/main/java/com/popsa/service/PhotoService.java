package com.popsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PhotoService {

    public static final String COMMA_DELIMITER = ",";

    private static final Logger logger = Logger.getLogger(PhotoService.class.getSimpleName());

    @Autowired
    ReverseGeocodeService reverseGeocodeService;

    PhotoService (ReverseGeocodeService reverseGeocodeService) {
        this.reverseGeocodeService = reverseGeocodeService;
    }

    public String getTitle(String fileName) {
        Map<String, Integer> months = new HashMap<>();
        Map<String, Integer> dayOfWeekMap = new HashMap<>();
        Map<String, Integer> cityMap = new HashMap<>();
        Map<String, Integer> countryMap = new HashMap<>();

        try(Scanner scanner = new Scanner(new File(fileName));) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(COMMA_DELIMITER);
                LocalDateTime date = getLocalDateTime(values[0]);
                processDate(months, dayOfWeekMap, date);
                processLocation(cityMap, countryMap, values);
            }
            String time = getTime(months, dayOfWeekMap);
            String location = getLocation(cityMap, countryMap);
            return createTitle(time, location);
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, "File could not be found");
            return "";
        }
    }

    private void processLocation(Map<String, Integer> cityMap, Map<String, Integer> countryMap, String[] values) {
        String[] location = reverseGeocodeService.getCity(Double.parseDouble(values[1]), Double.parseDouble(values[2]));
        if (location != null) {
            updateFrequencyMap(cityMap, location[0]);
            updateFrequencyMap(countryMap, location[1]);
        }
    }

    private void processDate(Map<String, Integer> months, Map<String, Integer> dayOfWeekMap, LocalDateTime date) {
        if (date != null) {
            String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            updateFrequencyMap(months, month);
            String day = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            updateFrequencyMap(dayOfWeekMap, day);
        }
    }

    private LocalDateTime getLocalDateTime(String value) {
        try {
            return LocalDateTime.parse(value.replace(" ", "T"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException exception) {
            try {
                Instant instant = Instant.parse(value);
                return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            } catch (DateTimeParseException ex) {
                logger.log(Level.WARNING, "Date-time could not be parsed");
                return null;
            }
        }
    }

    private String createTitle(String time, String location) {
        if (time == null && location == null) {
            return "A collection of old photos from various dates";
        }
        if (time == null)
            return randomAdjective() + " " + location + " " + "in various dates.";
        if (location == null)
            return randomAdjective() + " " + time + " in various locations.";
        return randomAdjective() + " " + location + " " + time;
    }

    private String randomAdjective() {
        String[] adjective = new String[]{"Magnificent", "Terrific", "Wonderful", "Thrilling", "Fantastic", "Super", "Superb"};
        Random ran = new Random();
        return adjective[ran.nextInt(7)];
    }

    private String getLocation(Map<String, Integer> cityMap, Map<String, Integer> countryMap) {
        String location = null;
        Optional<String> frequent = mostFrequent(countryMap);
        if(frequent.isPresent())
            location = frequent.get();
        frequent = mostFrequent(cityMap);
        if(frequent.isPresent())
            location = frequent.get();
        return location;
    }

    private String getTime(Map<String, Integer> months, Map<String, Integer> dayOfWeekMap) {
        Optional<String> frequent = mostFrequent(months);
        String time = null;
        if(frequent.isPresent())
            time = "in " + frequent.get();
        frequent = mostFrequent(dayOfWeekMap);
        if(frequent.isPresent())
            time = "on " + frequent.get();
        return time;
    }

    private void updateFrequencyMap(Map<String, Integer> objectMap, String timeUnit) {
        objectMap.putIfAbsent(timeUnit, 1);
        objectMap.computeIfPresent(timeUnit, (k, v) -> v = v + 1);
    }

    private Optional<String> mostFrequent(Map<String, Integer> objectMap) {
        int objectCount = objectMap.size();
        return objectMap.entrySet().stream()
                .filter(entry -> entry.getValue() > objectCount / 2).map(Map.Entry::getKey).findFirst();
    }
}
