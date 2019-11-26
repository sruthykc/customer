
  package com.diviso.graeshoppe.service.mapper.avro; 
  import com.diviso.graeshoppe.domain.Customer;

import java.nio.ByteBuffer;

import org.mapstruct.*;
  
  @Mapper(componentModel = "spring") 
  public interface CustomerAvroMapper {
	  
	 
  
	  Customer  avroToModel( com.diviso.graeshoppe.avro.Customer customer);
	 // @Mapping(source = "photo", target ="image" , qualifiedByName = "arrayToByteBuffer")
	  com.diviso.graeshoppe.avro.Customer modelToAvro(Customer customer);
	 /*@Named("arrayToByteBuffer")
	  public static  java.nio.ByteBuffer  arrayToByteBuffer(byte[] photo){
		  return ByteBuffer.wrap(photo);*/
	  
	  
  }
 