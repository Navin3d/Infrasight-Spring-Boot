package gmc.project.infrasight.presentationservice.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTimeUtil {
	
	public DateTimeUtil() {}
	
	public static LocalDate dateTimeToDate(LocalDateTime dateTime) {
        return dateTime.toLocalDate();
	}
	
	public static LocalDate parseDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);
           return localDate;
        } catch (Exception e) {
           log.error("Invalid date format. Please use yyyy-MM-dd.");
           throw e;
        }
	}
	
	public static LocalDateTime parseDateTime(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
        	LocalDateTime localDate = LocalDateTime.parse(dateString, formatter);
           return localDate;
        } catch (Exception e) {
           log.error("Invalid date format. Please use yyyy-MM-dd'T'HH:mm.");
           throw e;
        }
	}

}
