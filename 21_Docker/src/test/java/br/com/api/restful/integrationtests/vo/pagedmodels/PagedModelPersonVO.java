package br.com.api.restful.integrationtests.vo.pagedmodels;

import java.util.List;

import br.com.api.restful.integrationtests.vo.PersonVO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PagedModelPersonVO") //JSON2CSHARP -> converte JSON, XML E YML e gera objeto Java
public class PagedModelPersonVO {
	
	@XmlElement(name = "content")
	private List<PersonVO> content;
	
	public PagedModelPersonVO() {}

	public List<PersonVO> getContent() {
		return content;
	}

	public void setContent(List<PersonVO> content) {
		this.content = content;
	}
}
