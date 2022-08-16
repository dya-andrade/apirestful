package br.com.api.restful.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.api.restful.configs.FileStorageConfig;
import br.com.api.restful.exceptions.FileNotFoundException;
import br.com.api.restful.exceptions.FileStorageException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class FileStorageService {
	
	private Logger logger = Logger.getLogger(FileStorageService.class.getName());
	
	private final Path fileStorageLocation;

	@Autowired //inject via construtor
	public FileStorageService(FileStorageConfig fileStorageConfig) {
		
		Path path = Paths.get(fileStorageConfig.getUploadDir())
				.toAbsolutePath().normalize(); 
		
		//setar o caminho absoluto, temos apenas o caminha relativo
		//nomalize - nome redundantes são eliminados
		
		this.fileStorageLocation = path;
		
		//se encontrar o dir ele ler, se não ele cria
		try {
			
			Files.createDirectories(this.fileStorageLocation);
			
		} catch (Exception e) {
			throw new FileStorageException(
					"Could not create the directory where the uploaded files will be stored!", e);
		}
	}

	
	//grava os arquivos em disco
	public String storeFile(MultipartFile file) {
		
		logger.info("Storing file(s) to disk!");
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename()); //normalize o caminho e pega o fileName
		
		try {
			
			//validação do tipo -> ..txt
			if(fileName.contains(".."))
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			
			Path targetLocation = this.fileStorageLocation.resolve(fileName); //criando um arquivo vazio no dir
			
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING); 
			//grava os bytes no dir, se existir um arquivo com mesmo nome ele substitui
			
		} catch (Exception e) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
		}
		
		return fileName;
	}
	
	public Resource loadFileAsResource(String fileName) {
		
		logger.info("Reading a file on disk!");
		
		try {
			
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize(); //pega o dir do arquivo

			Resource resource = new UrlResource(filePath.toUri()); //cria um resource do arquivo, bytes e dir
			
			if(resource.exists()) return resource;
			else throw new FileNotFoundException("File not found!");

		} catch (Exception e) {
			throw new FileNotFoundException("File not found!", e);
		}
	}
	
	public String downloadUri(String fileName) {
		String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/file/v1/download/")
				.path(fileName)
				.toUriString();
		
		return downloadUri;
	}
	
	public String contentType(Resource resource, HttpServletRequest request) {
		
		//define o tipo de contentType, se não devolve genérico
		
		String contentType = "";
		
		try {
			
			contentType = request.getServletContext()
					.getMimeType(resource.getFile().getAbsolutePath());
						
		} catch (Exception e) {
			throw new FileNotFoundException("Could not determine file type!", e);
		}
		
		if(contentType.isBlank()) contentType = "application/octet-stream";

		return contentType;
	}
	
}
