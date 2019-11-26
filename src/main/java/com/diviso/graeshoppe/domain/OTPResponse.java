package com.diviso.graeshoppe.domain;

import java.util.HashSet;
import java.util.Set;

public class OTPResponse {
	
	private long balance;
	private long batch_id;
	private long cost;
	private long num_messages;
	
	private Message message;
	
    private Set<MessageRecipient> messages= new HashSet<>();

	private String receipt_url;
	private String custom;
	private String status;
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public long getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(long batch_id) {
		this.batch_id = batch_id;
	}
	public long getCost() {
		return cost;
	}
	public void setCost(long cost) {
		this.cost = cost;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}

	public String getReceipt_url() {
		return receipt_url;
	}
	public void setReceipt_url(String receipt_url) {
		this.receipt_url = receipt_url;
	}
	public String getCustom() {
		return custom;
	}
	public void setCustom(String custom) {
		this.custom = custom;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getNum_messages() {
		return num_messages;
	}
	public void setNum_messages(long num_messages) {
		this.num_messages = num_messages;
	}
	public Set<MessageRecipient> getMessages() {
		return messages;
	}
	public void setMessages(Set<MessageRecipient> messages) {
		this.messages = messages;
	}
	

}
