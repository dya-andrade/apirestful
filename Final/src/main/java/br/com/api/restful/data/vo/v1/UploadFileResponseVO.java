package br.com.api.restful.data.vo.v1;

import java.io.Serializable;

public class UploadFileResponseVO implements Serializable { //equals e hash apenas para persistir

	private static final long serialVersionUID = 1L;

	private String fileName;
	private String downloadUri;
	private String type;
	private Long size;
	
	public UploadFileResponseVO() {}

	public UploadFileResponseVO(String fileName, String downloadUri, String type, Long size) {
		this.fileName = fileName;
		this.downloadUri = downloadUri;
		this.type = type;
		this.size = size;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDownloadUri() {
		return downloadUri;
	}

	public void setDownloadUri(String downloadUri) {
		this.downloadUri = downloadUri;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

}
