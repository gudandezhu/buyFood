package com.ps.view;

import java.io.Serializable;

public class MessageVO<T> implements Serializable {
	/**
	 * 消息类
	 */
	private static final long serialVersionUID = 1L;
	private boolean error = false;
	private String stic;
	private String msg="";
	
	 //消息主体
	private T body;
	
	
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	public boolean getError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getStic() {
		return stic;
	}
	public void setStic(String stic) {
		this.stic = stic;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	public MessageVO(boolean error, String stic, String msg){
		super();
		this.error = error;
		this.stic = stic;
		this.msg = msg;
	}
	public MessageVO() {
		super();
	}
	public MessageVO(boolean error, String stic, String msg, T body) {
		super();
		this.error = error;
		this.stic = stic;
		this.msg = msg;
		this.body = body;
	}
	
}
