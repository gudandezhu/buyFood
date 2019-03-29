package com.ps.view.menu;

import java.io.Serializable;

public class MenuVO<T>  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean error = false;
	private String msg;
	private int total;
	private T rows;
	
	
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getRows() {
		return rows;
	}
	public void setRows(T rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "MenuVO [error=" + error + ", msg=" + msg + ", rows=" + rows + "]";
	}
	
	
}
