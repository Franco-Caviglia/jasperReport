package com.jasper.reports;

import net.sf.jasperreports.engine.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootApplication
public class ReportsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportsApplication.class, args);
	}

	@Bean
	CommandLineRunner init () {
		return args -> {
			//Ruta donde se guarda el archivo;
			String destinationPath = "src" + File.separator +
					"main" + File.separator +
					"resources" + File.separator +
					"static" + File.separator +
					"ReportGenerated.pdf";

			//Ruta del archivo a compilar;
			String filePath = "src" + File.separator +
					"main" + File.separator +
					"resources" + File.separator +
					"templates" + File.separator +
					"report" + File.separator +
					"Report.jrxml";

			LocalDateTime localDateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

			//Valores para las variables;
			Map<String, Object> parameters = new HashMap<>();

			parameters.put("voucher_id", "00001234");
			parameters.put("currentDate", formatter.format(localDateTime));
			parameters.put("amount", 15706.0);
			parameters.put("method_payment", "Cash");
			parameters.put("receptor", "Caviglia, Franco");
			parameters.put("customer", "Downes, Josefina");
			parameters.put("imageDir", "classpath:/static/images/");


			//Compilamos el reporte
			JasperReport jasperReport = JasperCompileManager.compileReport(filePath);
			//Imprimimos el reporte, indicando los parametros y la base de datos;
			JasperPrint jasperPrint = JasperFillManager.
					fillReport(jasperReport, parameters, new JREmptyDataSource());

			//Exportamos el reporte
			JasperExportManager.exportReportToPdfFile(jasperPrint, destinationPath);

			System.out.println("Report created successfully!!!");
		};
	}
}
