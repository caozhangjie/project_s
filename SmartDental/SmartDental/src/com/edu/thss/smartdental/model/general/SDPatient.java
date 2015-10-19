package com.edu.thss.smartdental.model.general;

import java.util.Date;

/**
 * @version 1.0
 * @author anna
 * 描述了一个病人的基本信息
 * */
public class SDPatient {

	/**
	 * 由于时间关系，大部分类采用public，下一版会考虑private
	 * */
	public String name;
	public int gender;
	public Date birth;
	public String idNum; //身份证
	public String medicareNum; //医保号
	
	public String country;
	public String race;
	public String bloodType;
	public String job;
	public String grade;
	
	public String faceImage;//头像路径
	public String homeadress;
	public String homephone;
	public String mobile;
	public String email;
	public String workadress;
	public String workphone;
	
	public String passHistory;
	
	public String familyHistroy;
	
	public String allergyHistroy;
	
	public SDPatient(){
		
	}
}
