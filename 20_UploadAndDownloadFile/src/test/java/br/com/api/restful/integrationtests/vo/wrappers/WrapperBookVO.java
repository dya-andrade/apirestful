package br.com.api.restful.integrationtests.vo.wrappers;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

//JSON2CSHARP -> converte JSON, XML E YML e gera objeto Java
public class WrapperBookVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("_embedded")
	private BookEmbeddedVO embedded;
	
	public WrapperBookVO() {}


	public BookEmbeddedVO getEmbedded() {
		return embedded;
	}

	public void setEmbedded(BookEmbeddedVO embedded) {
		this.embedded = embedded;
	}

	@Override
	public int hashCode() {
		return Objects.hash(embedded);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WrapperBookVO other = (WrapperBookVO) obj;
		return Objects.equals(embedded, other.embedded);
	}
}
