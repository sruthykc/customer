package com.diviso.graeshoppe.domain;

public class Message {
	
	private long num_parts;
	private String sender;
	private String content;
	
	public long getNumParts() {
		return num_parts;
	}
	public void setNumParts(long num_parts) {
		this.num_parts = num_parts;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
