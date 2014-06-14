package com.ly.fund.vo;

import java.util.Date;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.View;

@Table("product")
public class Product{

	@Id
	@Column
	private Long id;

	@Column
	private String name;

	@Column
	private String author;

	@Column
	private String phone;

	@Column
	private String qq;

	@Column
	private String email;

	@Column
	private String sales;

	@Column
	private String des;

	@Column
	private String memo;

	@Column
	private Double price;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
