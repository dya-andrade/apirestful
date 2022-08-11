package br.com.api.restful.integrationtests.vo.pagedmodels;

import java.util.List;

import br.com.api.restful.integrationtests.vo.BookVO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PagedModelBookVO") //JSON2CSHARP -> converte JSON, XML E YML e gera objeto Java
public class PagedModelBookVO {
	
	@XmlElement(name = "content")
	private List<BookVO> content;
	
	public PagedModelBookVO() {}

	public List<BookVO> getContent() {
		return content;
	}

	public void setContent(List<BookVO> content) {
		this.content = content;
	}
}
