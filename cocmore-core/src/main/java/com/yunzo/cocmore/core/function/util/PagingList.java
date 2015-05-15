package com.yunzo.cocmore.core.function.util;


import java.util.List;

public class PagingList <T> {
	private Integer currentPage;
	private Integer count;
	private Integer lastPage;
	private List<T> list;
	
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getLastPage() {
		return lastPage;
	}
	public void setLastPage(Integer lastPage) {
		this.lastPage = lastPage;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
}
