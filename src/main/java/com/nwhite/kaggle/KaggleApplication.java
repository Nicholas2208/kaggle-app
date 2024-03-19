package com.nwhite.kaggle;

import com.nwhite.kaggle.dto.BookDto;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@SpringBootApplication
public class KaggleApplication implements CommandLineRunner {
	List<BookDto> bookList = new ArrayList<>();

	@Value("${kaggle.key}")
	String kaggleKey;
	@Value("${kaggle.username}")
	String kaggleUsername;
	@Value("${kaggle.dataset.slug}")
	String datasetSlug;
	@Value("${kaggle.url}")
	String url;

	public static void main(String[] args) {
		SpringApplication.run(KaggleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		HttpClient httpClient = HttpClients.createDefault();
		String apiUrl = url + datasetSlug;

		HttpGet request = new HttpGet(apiUrl);
		request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((kaggleUsername + ":" + kaggleKey).getBytes()));

		HttpResponse response = httpClient.execute(request);
		ZipInputStream zipInputStream = new ZipInputStream(response.getEntity().getContent());

		bookList = parseCSV(zipInputStream);

	}

	private static List<BookDto> parseCSV(ZipInputStream zipInputStream) {
		List<BookDto> books = new ArrayList<>();

		try {
			ZipEntry entry = zipInputStream.getNextEntry();
			Reader reader = new InputStreamReader(zipInputStream);
			CSVReader csvReader = new CSVReaderBuilder(reader).build();

			String[] line;
			while ((line = csvReader.readNext()) != null) {
				BookDto book = new BookDto();

				book.setId(safeParseLong(line[0]));
				book.setTitle(line[2]);
				book.setSeries(line[3]);
				book.setReleaseNumber(safeParseInt(line[4]));
				book.setAuthor(line[5]);
				book.setDescription(line[8]);
				book.setNumPages(safeParseInt(line[9]));
				book.setFormat(line[10]);

				List<String> genres = Arrays.stream(line[11].split(",")).toList();
				book.setGenres(genres);

				if (!line[12].isEmpty()) {
					LocalDate date = convertStringToDate(line[12]);
					book.setPublicationDate(date);
				}

				book.setRating(safeParseFloat(line[13]));
				book.setNumberOfVoters(safeParseFloat(line[15]));

				books.add(book);
			}
			csvReader.close();
		} catch (IOException e) {
			throw new RuntimeException("Ошибка парсинга датасета");
		} catch (CsvValidationException e) {
            throw new RuntimeException("Ошибка парсинга CSV");
        }

        return books;
	}

	private static Integer safeParseInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static Long safeParseLong(String value) {
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static Float safeParseFloat(String value) {
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static LocalDate convertStringToDate(String dateString) {
		try {
			String[] parts = dateString.split(" ");
			int day = Integer.parseInt(parts[1].replace(",", ""));
			int year = Integer.parseInt(parts[2]);
			Month month = monthMap.get(parts[0]);

			return LocalDate.of(year, month, day);
		} catch (Exception e) {
			return null;
		}
	}

	private static final Map<String, Month> monthMap = new HashMap<>();

	static {
		monthMap.put("January", Month.JANUARY);
		monthMap.put("February", Month.FEBRUARY);
		monthMap.put("March", Month.MARCH);
		monthMap.put("April", Month.APRIL);
		monthMap.put("May", Month.MAY);
		monthMap.put("June", Month.JUNE);
		monthMap.put("July", Month.JULY);
		monthMap.put("August", Month.AUGUST);
		monthMap.put("September", Month.SEPTEMBER);
		monthMap.put("October", Month.OCTOBER);
		monthMap.put("November", Month.NOVEMBER);
		monthMap.put("December", Month.DECEMBER);
	}

	public List<BookDto> getBookList() {
		return bookList;
	}
}
