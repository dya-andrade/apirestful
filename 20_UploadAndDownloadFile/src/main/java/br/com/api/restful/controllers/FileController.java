package br.com.api.restful.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.api.restful.data.vo.v1.UploadFileResponseVO;
import br.com.api.restful.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/file/v1")
@Tag(name = "File", description = "Endpoints.")
public class FileController {
	
	@Autowired
	private FileStorageService service;
	
	@PostMapping("/upload")
	public UploadFileResponseVO upload(@RequestParam("file") MultipartFile file) {
		
		String fileName = service.storeFile(file);
		String downloadUri = service.downloadUri(fileName);
		
		return new UploadFileResponseVO(fileName, downloadUri, file.getContentType(), file.getSize());
	}
	
	@PostMapping("/uploadMultiple")
	public List<UploadFileResponseVO> uploadMultiple(@RequestParam("files") MultipartFile[] files) {
		
		return Arrays.asList(files)
				.stream()
				.map(file -> upload(file)) //chama o primeiro método upload
				.collect(Collectors.toList());
	}
	
	//file.txt
	@GetMapping("download/{filename:.+}")
	public ResponseEntity<Resource> download(@PathVariable String filename, HttpServletRequest request) {
		
		Resource resource = service.loadFileAsResource(filename);
		String contentType = service.contentType(resource, request);

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"") 
				//indica se o conteúdo deve ser exibido como uma página da web
				.body(resource);
	}

}
