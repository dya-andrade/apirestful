package br.com.api.restful.integrationtests.controllers.withyaml.mapper;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

public class YMLMapper implements ObjectMapper {

	private com.fasterxml.jackson.databind.ObjectMapper objectMapper;
	protected TypeFactory typeFactory;
	
	private Logger logger = Logger.getLogger(YMLMapper.class.getName());

	public YMLMapper() {
		objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory());
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// quando ocorrer falhas por propriedades desconhecidas

		typeFactory = TypeFactory.defaultInstance(); // classe usada para criar instâncias JavaType concretas
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object deserialize(ObjectMapperDeserializationContext context) {
		try {

			String dataToDeserialize = context.getDataToDeserialize().asString();
			Class type = (Class) context.getType();
			
            logger.info("Trying deserialize object of type" + type);

			return objectMapper.readValue(dataToDeserialize, typeFactory.constructType(type)); // converte para o tipo
																								// necessário
		} catch (JsonProcessingException e) {
            logger.severe("Error deserializing object");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object serialize(ObjectMapperSerializationContext context) {
		try {

			return objectMapper.writeValueAsString(context.getObjectToSerialize());

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
