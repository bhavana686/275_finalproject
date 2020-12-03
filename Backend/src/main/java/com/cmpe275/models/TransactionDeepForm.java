package com.cmpe275.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.cmpe275.entity.Enum;
import com.cmpe275.entity.TransferRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

public class TransactionDeepForm {
	
	private long id;
	private List<TransferRequestDeepForm> requests;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public  List<TransferRequestDeepForm> getRequests() {
		return requests;
	}

	public void setRequests(List<TransferRequestDeepForm>  requests) {
		this.requests= requests;
	}

	
	


}
