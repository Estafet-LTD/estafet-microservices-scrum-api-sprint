package com.estafet.microservices.api.sprint.messages;

public class Deleted {

	private int deleted;

	public Deleted(int deleted) {
		this.deleted = deleted;
	}

	public Deleted() { }

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
}
