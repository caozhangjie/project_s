package com.edu.thss.smartdental.model.tooth;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import java.util.regex.*;

public class Tooth {
	
	   /**
	    * 提供牙齿的几种编码方式
	    * */
	    //普通编码: 1~32 恒牙 ,A~T乳牙
		public static String[] labelsUniversal = new String[]{ "1",  "2",  "3",  "4",  "5",  "6",  "7",  "8",  "9", "10", "11", "12", "13", "14", "15", "16", 
															   "32", "31", "30", "29", "28", "27", "26", "25", "24", "23", "22", "21", "20", "19", "18", "17",
															   "A",  "B",  "C",  "D",  "E",  "F",  "G",  "H",  "I",  "J",
															   "T",  "S",  "R",  "Q",  "P",  "O",  "N",  "M",  "L",  "K"};
		//FDI编码
		private static String[] labelsFDI = new String[] {"18", "17", "16", "15", "14", "13", "12", "11", "21", "22", "23", "24", "25", "26", "27", "28", 
														  "48", "47", "46", "45", "44", "43", "42", "41", "31", "32", "33", "34", "35", "36", "37", "38",
														  "55", "54", "53", "52", "51", "61", "62", "63", "64", "65",
														  "85", "84", "83", "82", "81", "71", "72", "73", "74", "75"};
		
		//haderup编码：1~8恒牙，乳牙用1～5加D或罗马数字I～V表示，
		//＋表示上颌牙，－表示下颌牙，
		//＋在牙号之后表示右上象限牙，＋在牙号之前表示左上象限牙，
		//同理，用－号在牙号前、后分别表示左、右象限
		private  static String[] labelsHaderup = new String[] { "8+",  "7+",  "6+",  "5+",  "4+",  "3+",  "2+",  "1+",  "+1", "+2", "+3", "+4", "+5", "+6", "+7", "+8", 
															   "8-",  "7-",  "6-",  "5-",  "4-",  "3-",  "2-",  "1-",  "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", 
															   "A",  "B",  "C",  "D",  "E",  "F",  "G",  "H",  "I",  "J",
															   "T",  "S",  "R",  "Q",  "P",  "O",  "N",  "M",  "L",  "K"};
	   //帕尔默牙位表示法
		private  static String[] labelsPalmer = new String[] {
								"UR8","UR7","UR6","UR5","UR4","UR3","UR2","UR1","UL1","UL2","UL3","UL4","UL5","UL6","UL7","UL8",
								"LR8","LR7","LR6","LR5","LR4","LR3","LR2","LR1","LL1","LL2","LL3","LL4","LL5","LL6","LL7","LL8",
								"URE","URD","URC","URB","URA","ULA","ULB","ULC","ULD","ULE",
								"LRE","LRD","LRC","LRB","LRA","LLA","LLB","LLC","LLD","LLE"};
		//帕尔默牙位表示法【简化】
		public static String[] labelsPalmerSimple = new String[] {
								"8","7","6","5","4","3","2","1","1","2","3","4","5","6","7","8",
								"8","7","6","5","4","3","2","1","1","2","3","4","5","6","7","8",
								"E","D","C","B","A","A","B","C","D","E",
								"E","D","C","B","A","A","B","C","D","E"};
		
		private static ToothNumberingNomenclature nomenclature; //当前编码格式，应该是有个专门的类来存储的！
		private static boolean isCanadian; //是否为canada编码
		/**
		 * 判断是否是门牙
		 * */
		public static boolean IsAnterior(String toothNum) {
			if(!IsValidDB(toothNum)) {
				return false;
			}
			int intTooth=Integer.parseInt(toothNum);
			return IsAnterior(intTooth);
		}
		/**
		 * 判断是否是门牙
		 * */
		public static boolean IsAnterior(int intTooth){
			if(intTooth>=6 && intTooth<=11) {
				return true;
			}
			if(intTooth>=22 && intTooth<=27) {
				return true;
			}
			return false;
		}
		/**
		 * 判断是否是后牙
		 * */
		public static boolean IsPosterior(String toothNum){
			if(!IsValidDB(toothNum))
				return false;
			int intTooth=Integer.parseInt(toothNum);
			return IsPosterior(intTooth);
		}
		/**
		 * 判断是否是后牙
		 * */
		public static boolean IsPosterior(int intTooth){
			if(intTooth>=1 && intTooth<=5)
				return true;
			if(intTooth>=12 && intTooth<=21)
				return true;
			if(intTooth>=28 && intTooth<=32)
				return true;
			return false;
		}
		/**
		 * 是否是臼齿
		 * */
		public static boolean IsMolar(String toothNum){
			if(!IsValidDB(toothNum)) {
				return false;
			}
			int intTooth=ToInt(toothNum);
			if(IsPrimary(toothNum)) {
			  if(intTooth<=5 || intTooth>=28) {//AB, ST
			    return true;
			  }
			  if(intTooth>=12 && intTooth<=21) {//IJ, KL 
			    return true;
			  }
			  return false;
			}
			if(intTooth>=1 && intTooth<=3) {
				return true;
			}
			if(intTooth>=14 && intTooth<=19) {
				return true;
			}
			if(intTooth>=30 && intTooth<=32) {
				return true;
			}
			return false;
			
		}
		/**
		 * 是否是臼齿
		 * */
		public static boolean IsMolar(int intTooth){
			String toothNum=FromInt(intTooth);
			return IsMolar(toothNum);
		}
		/**
		 * 是否是后臼齿
		 * */
		public static boolean IsSecondMolar(String toothNum) {
			if(!IsValidDB(toothNum))
				return false;
			int intTooth=Integer.parseInt(toothNum);
			return IsSecondMolar(intTooth);
		}
		public static boolean IsSecondMolar(int intTooth){
			if(intTooth==2 || intTooth==15 || intTooth==18 || intTooth==31){
				return true;
			}
			return false;
		}
		/**
		 * 是否是前臼齿
		 * */
		public static boolean IsPreMolar(String toothNum){
			if(!IsValidDB(toothNum))
				return false;
			int intTooth=Integer.parseInt(toothNum);
			return IsPreMolar(intTooth);
		}
		/**
		 * 是否是前臼齿
		 * */
		public static boolean IsPreMolar(int intTooth){
			if(intTooth==4 
					|| intTooth==5
					|| intTooth==12
					|| intTooth==13
					|| intTooth==20
					|| intTooth==21
					|| intTooth==28
					|| intTooth==29)
					return true;
				return false;
		}
		/**
		 * 获取牙齿标签
		 * */
		public static String GetToothLabel(String tooth_id,ToothNumberingNomenclature nomenclature) {
			if(tooth_id==null || tooth_id==""){
				return ""; 
			}
			if(nomenclature == ToothNumberingNomenclature.Universal) {
				return tooth_id; 
			}
			int index = Arrays.asList(labelsUniversal).indexOf(tooth_id);
			if(index==-1){
				if(nomenclature == ToothNumberingNomenclature.FDI
					// && CultureInfo.CurrentCulture.Name.EndsWith("CA") //此处？？？？？
					&& tooth_id=="51")
				{
					return "99";//多生牙
				}
				return "-";
			}
			if(nomenclature == ToothNumberingNomenclature.FDI) {
				return labelsFDI[index];
			}
			else if(nomenclature == ToothNumberingNomenclature.Haderup) { 
				return labelsHaderup[index];
			}
			else if(nomenclature == ToothNumberingNomenclature.Palmer) { 
				return labelsPalmer[index];
			}
			return "-"; // Should never happen
		}
		/**
		 * 获取牙齿标签
		 * */
		public static String GetToothLabel(String tooth_id) {
			if(tooth_id==null || tooth_id==""){
				return ""; 
			}
			if(nomenclature == ToothNumberingNomenclature.Universal) {
				return tooth_id; 
			}
			int index = Arrays.asList(labelsUniversal).indexOf(tooth_id);
			if(index==-1){
				if(nomenclature == ToothNumberingNomenclature.FDI
					// && CultureInfo.CurrentCulture.Name.EndsWith("CA") //此处？？？？？
					&& tooth_id=="51")
				{
					return "99";//多生牙
				}
				return "-";
			}
			if(nomenclature == ToothNumberingNomenclature.FDI) {
				return labelsFDI[index];
			}
			else if(nomenclature == ToothNumberingNomenclature.Haderup) { 
				return labelsHaderup[index];
			}
			else if(nomenclature == ToothNumberingNomenclature.Palmer) { 
				return labelsPalmer[index];
			}
			return "-"; // Should never happen
		}

		/**
		 * 和牙齿标签相同，但是用在3d显示中
		 */
		public static String GetToothLabelGraphic(String tooth_id,ToothNumberingNomenclature nomenclature){
			if(tooth_id==null || tooth_id==""){
				return ""; 
			}
			
			if(nomenclature==ToothNumberingNomenclature.Universal) {
				return tooth_id;
			}
			int index = Arrays.asList(labelsUniversal).indexOf(tooth_id);
			if(index==-1){
				return "-";
			}
			if(nomenclature == ToothNumberingNomenclature.FDI) {
				return labelsFDI[index];
			}
			else if(nomenclature == ToothNumberingNomenclature.Haderup) { 
				return labelsHaderup[index];
			}
			else if(nomenclature == ToothNumberingNomenclature.Palmer) {
				return labelsPalmerSimple[index];
			}
			return "-"; // Should never happen
		}


		/**
		 * 通过标签找到牙齿序号
		 * */
		public static String GetToothId(String tooth_label,ToothNumberingNomenclature nomenclature){
			//ToothNumberingNomenclature nomenclature = (ToothNumberingNomenclature)PrefC.GetInt(PrefName.UseInternationalToothNumbers);
			if(nomenclature == ToothNumberingNomenclature.Universal) {
				return tooth_label;
			}
			int index = 0;
			if(nomenclature == ToothNumberingNomenclature.FDI) {
				/*if(CultureInfo.CurrentCulture.Name.EndsWith("CA") && tooth_label=="99") {
					return "51";//supernumerary tooth: It is documented in the cdha website that 99 is the only valid number for supernumerary teeth.
				}*/
				index = Arrays.asList(labelsFDI).indexOf(tooth_label);
			}
			else if(nomenclature == ToothNumberingNomenclature.Haderup) { 
				index = Arrays.asList(labelsHaderup).indexOf(tooth_label);
			}
			else if(nomenclature == ToothNumberingNomenclature.Palmer) { 
				index = Arrays.asList(labelsPalmer).indexOf(tooth_label);
			}
			return labelsUniversal[index];
		}
		/**
		 * 通过标签找到牙齿序号
		 * */
		public static String GetToothId(String tooth_label){
			//ToothNumberingNomenclature nomenclature = (ToothNumberingNomenclature)PrefC.GetInt(PrefName.UseInternationalToothNumbers);
			if(nomenclature == ToothNumberingNomenclature.Universal) {
				return tooth_label;
			}
			int index = 0;
			if(nomenclature == ToothNumberingNomenclature.FDI) {
				/*if(CultureInfo.CurrentCulture.Name.EndsWith("CA") && tooth_label=="99") {
					return "51";//supernumerary tooth: It is documented in the cdha website that 99 is the only valid number for supernumerary teeth.
				}*/
				index = Arrays.asList(labelsFDI).indexOf(tooth_label);
			}
			else if(nomenclature == ToothNumberingNomenclature.Haderup) { 
				index = Arrays.asList(labelsHaderup).indexOf(tooth_label);
			}
			else if(nomenclature == ToothNumberingNomenclature.Palmer) { 
				index = Arrays.asList(labelsPalmer).indexOf(tooth_label);
			}
			return labelsUniversal[index];
		}
		
		/**
		 * Get quadrant returns "UR" for teeth 1-8, "LR" for 25-32, "UL" for 9-16, and "LL" for 17-24.
		 */
		public static String GetQuadrant(String toothNum){
			if(!IsValidDB(toothNum)){
				return "";
			}
			int intTooth=Integer.parseInt(toothNum);
			if(intTooth>=1 && intTooth<=8)
				return "UR";
			if(intTooth>=9 && intTooth<=16)
				return "UL";
			if(intTooth>=17 && intTooth<=24)
				return "LL";
			if(intTooth>=25 && intTooth<=32)
				return "LR";
			return "";
		}

		//
		public static String ToInternat(String toothNum,ToothNumberingNomenclature nomenclature ){ // CWI: Left for compatibility
			return GetToothLabel(toothNum,nomenclature);
		}
		public static String ToInternat(String toothNum ){ // CWI: Left for compatibility
			return GetToothLabel(toothNum,nomenclature);
		}
		// 
		public static String FromInternat(String toothNum,ToothNumberingNomenclature nomenclature){ // CWI: Left for compatibility
			return GetToothId(toothNum,nomenclature);
		}
		public  static String FromInternat(String toothNum){ // CWI: Left for compatibility
			return GetToothId(toothNum,nomenclature);
		}
		///牙齿序号，用逗号隔开. 
		public static String FormatRangeForDisplay(String toothNumbers) {
			if(toothNumbers==null) {
				return "";
			}
			toothNumbers=toothNumbers.replace(" ","");//remove all spaces
			if(toothNumbers=="") {
				return "";
			}
			String[] toothArray=toothNumbers.split(",");
			if(toothArray.length==1) {
				return GetToothLabel(toothArray[0]);
			}
			else if(toothArray.length==2) {
				return GetToothLabel(toothArray[0])+","+GetToothLabel(toothArray[1]);
			}
			
			Arrays.sort(toothArray, new ToothComparer());
			StringBuilder strbuild=new StringBuilder();
			//List<string> toothList=new List<string>();
			//strbuild.Append(Tooth.ToInternat(toothArray[0]));//always show the first number
			int currentNum;
			int nextNum;
			int numberInaRow=1;//must have 3 in a row to trigger dash
			for(int i=0;i<toothArray.length-1;i++) {
				//in each loop, we are comparing the current number with the next number
				currentNum=ToOrdinal(toothArray[i]);
				nextNum=ToOrdinal(toothArray[i+1]);
				if(nextNum-currentNum==1 && currentNum!=16 && currentNum!=32) {//if sequential (sequences always break at end of arch)
					numberInaRow++;
				}
				else {
					numberInaRow=1;
				}
				if(numberInaRow<3) {//the next number is not sequential,or if it was a sequence, and it's now broken
					if(strbuild.length()>0 && strbuild.charAt(strbuild.length()-1)!='-') {
						strbuild.append(",");
					}
					strbuild.append(Tooth.GetToothLabel(toothArray[i]));
				}
				else if(numberInaRow==3) {//this way, the dash only gets added exactly once
					strbuild.append("-");
				}
				//else do nothing
			}
			if(strbuild.length()>0 && strbuild.charAt(strbuild.length()-1)!='-') {
				strbuild.append(",");
			}
			strbuild.append(Tooth.GetToothLabel(toothArray[toothArray.length-1]));//always show the last number
			return strbuild.toString();
		}

		/***
		 * <summary>Takes a user entered string and validates/formats it for the database.
		 * Throws an ApplicationException if any formatting errors.  User string can contain spaces, dashes, and commas,
		 * @throws Exception 
		 */
		public static String FormatRangeForDb(String toothNumbers) throws Exception{
			if(toothNumbers==null) {
				return "";
			}
			toothNumbers=toothNumbers.replace(" ","");//remove all spaces
			if(toothNumbers=="") {
				return "";
			}
			String[] toothArray=toothNumbers.split(",");
			ArrayList<String> toothList=new ArrayList<String>();
			String rangebegin;
			String rangeend;
			int beginint;
			int endint;
			
			for(int i=0;i<toothArray.length;i++){
				if(toothArray[i].contains("-")){
					rangebegin=toothArray[i].split("-")[0];
					rangeend=toothArray[i].split("-")[1];
					if(!IsValidEntry(rangebegin)) {
						throw new Exception(rangebegin+" "+" Tooth: is not a valid tooth number.");
					}
					if(!IsValidEntry(rangeend)) {
						throw new Exception(rangeend+" "+"Tooth: is not a valid tooth number.");
					}
					beginint=Tooth.ToOrdinal(GetToothId(rangebegin));
					endint=Tooth.ToInt(GetToothId(rangeend));
					if(endint<beginint){
						throw new Exception("Range specified is impossible.");
					}
					while(beginint<=endint){
						toothList.add(Tooth.FromOrdinal(beginint));
						beginint++;
					}
				}
				else{
					if(!IsValidEntry(toothArray[i])){
						throw new Exception(toothArray[i]+" "+"Tooth: is not a valid tooth number.");
					}
					toothList.add(Tooth.GetToothId(toothArray[i]));
				}
			}
			//toothList.sort(new ToothComparer());
			 Collections.sort(toothList,new ToothComparer());
			String retVal="";
			for(int i=0;i<toothList.size();i++){
				if(i>0){
					retVal+=",";
				}
				retVal+=toothList.get(i);
			}
			return retVal;
		}

		///<summary>Used every time user enters toothNum in procedure box. Must be followed with FromInternat. These are the *ONLY* methods that are designed to accept user input.  Can also handle international toothnum</summary>
		public static boolean IsValidEntry(String toothNum){
			//int tempnomenclature = Integer.parseInt(nomenclature);
			if(nomenclature==ToothNumberingNomenclature.Universal){//Universal,american
				//tooth numbers validated the same as they are in db.
				return IsValidDB(toothNum);
			}
			else if(nomenclature==ToothNumberingNomenclature.FDI){// FDI
				if(toothNum==null || toothNum==""){
					return false;
				}
				Pattern pattern = Pattern.compile("^[1-4][1-8]$");
				Matcher matcher = pattern.matcher(toothNum);
				
				if(matcher.matches()){//perm teeth: matches firt digit 1-4 and second digit 1-8,9 would be supernumerary?
					return true;
				}
				pattern = Pattern.compile("^[5-8][1-5]$");
				if(matcher.matches()){//pri teeth: matches firt digit 5-8 and second digit 1-5
					return true;
				}
				
				if(isCanadian && toothNum=="99") {//supernumerary tooth: It is documented in the cdha website that 99 is the only valid number for supernumerary teeth.
					return true;
				}
				return false;
			}
			else if(nomenclature==ToothNumberingNomenclature.Haderup) {//Haderup
				if(toothNum==null || toothNum=="") {
					return false;
				}
				for(int i=0;i<labelsHaderup.length;i++) {
					if(labelsHaderup[i]==toothNum) {
						return true;
					}
				}
				return false;
			}
			else{// if(nomenclature==3) {// Palmer
				if(toothNum==null || toothNum=="") {
					return false;
				}
				for(int i=0;i<labelsPalmer.length;i++) {
					if(labelsPalmer[i]==toothNum) {
						return true;
					}
				}
				return false;
			}			
		}

		///<summary>Intended to validate toothNum coming in from database. Will not handle any international tooth nums since all database teeth are in US format.</summary>
		public static boolean IsValidDB(String toothNum){
			if(toothNum==null || toothNum.equals(""))
				return false;
			Pattern pattern = Pattern.compile("^[A-T]$");
			Matcher matcher = pattern.matcher(toothNum);
			if(matcher.matches())
				return true;
			pattern = Pattern.compile("^[A-T]S$");
			if(matcher.matches())//supernumerary
				return true;
			/*pattern = Pattern.compile("^[1-9]\\d?$");
			if(!matcher.matches()){//matches 1 or 2 digits, leading 0 not allowed
				return false;
			}*/
			int intTooth=Integer.parseInt(toothNum);
			if(intTooth<=32) {
				return true;
			}
			if(intTooth>=51 && intTooth<=82) {//supernumerary
				return true;
			}
			return false;
		}

		///<summary></summary>
		public static boolean IsSuperNum(String toothNum){
			if(toothNum==null || toothNum=="")
				return false;
			Pattern pattern = Pattern.compile("^[A-T]$");
			Matcher matcher = pattern.matcher(toothNum);
			if(matcher.matches())
				return false;
			pattern = Pattern.compile("^[A-T]S$");
			if(matcher.matches())//supernumerary
				return true;
			pattern = Pattern.compile("^[1-9]\\d?$");
			if(!matcher.matches()){//matches 1 or 2 digits, leading 0 not allowed
				return false;
			}
			int intTooth=Integer.parseInt(toothNum);
			if(intTooth<=32)
				return false;
			if(intTooth>=51 && intTooth<=82)//supernumerary
				return true;	
			return false;
		}

		///<summary>Returns 1-32, or -1.  The toothNum should be validated before coming here, but it won't crash if invalid.  Primary or perm are ok.  Empty and null are also ok.  Supernumerary are also ok.</summary>
		public static int ToInt(String tooth_id){
			if(tooth_id==null || tooth_id=="")
				return -1;
			
			if(IsPrimary(tooth_id)) {
				return Integer.parseInt(PriToPerm(tooth_id));
			}
			else if(IsSuperNum(tooth_id)) {
				return Integer.parseInt(SupToPerm(tooth_id));
			}
			else {
				return Integer.parseInt(tooth_id);
			}
			
		}

		///<summary></summary>
		public static String FromInt(int intTooth){
			return Integer.toString(intTooth);
		}

		/**
		 * <summary>Returns true if A-T or AS-TS.  Otherwise, returns false.</summary>
		 *判断是否为乳牙
		 */
		public static boolean IsPrimary(String tooth_id){
			Pattern pattern = Pattern.compile("^[A-T]$");
			Matcher matcher = pattern.matcher(tooth_id);
			if(matcher.matches()) {
				return true;
			}
			pattern = Pattern.compile("^[A-T]S$");
			if(matcher.matches()) {
				return true;
			}
			return false;
		}

		/**
		 * 恒牙映射到乳牙
		 * */
		public static String PermToPri(String tooth_id) {
			int temptooth_id = Integer.parseInt(tooth_id);
			switch(temptooth_id) {
				default: return "";
				case 4: return "A";
				case 5: return "B";
				case 6: return "C";
				case 7: return "D";
				case 8: return "E";
				case 9: return "F";
				case 10: return "G";
				case 11: return "H";
				case 12: return "I";
				case 13: return "J";
				case 14: return "K";
				case 21: return "L";
				case 22: return "M";
				case 23: return "N";
				case 24: return "O";
				case 25: return "P";
				case 26: return "Q";
				case 27: return "R";
				case 28: return "S";
				case 29: return "T";
				case 54: return "AS";
				case 55: return "BS";
				case 56: return "CS";
				case 57: return "DS";
				case 58: return "ES";
				case 59: return "FS";
				case 60: return "GS";
				case 61: return "HS";
				case 62: return "IS";
				case 63: return "JS";
				case 70: return "KS";
				case 71: return "LS";
				case 72: return "MS";
				case 73: return "NS";
				case 74: return "OS";
				case 75: return "PS";
				case 76: return "QS";
				case 77: return "RS";
				case 78: return "SS";
				case 79: return "TS";
			}
		}

		/**
		 * 恒牙到乳牙的映射
		 * */
		public static String PermToPri(int intTooth){
			String tooth_id=FromInt(intTooth);
			return PermToPri(tooth_id);
		}

		/**
		 * 乳牙到恒牙的映射
		 * */
		public static String PriToPerm(String tooth_id) {
			if(tooth_id.equals("A")) return "4";
			else if(tooth_id.equals("B")) return "5";
			else if(tooth_id.equals("C")) return "6";
			else if(tooth_id.equals("D")) return "7";
			else if(tooth_id.equals("E")) return "8";
			else if(tooth_id.equals("F")) return "9";
			else if(tooth_id.equals("G")) return "10";
			else if(tooth_id.equals("H")) return "11";
			else if(tooth_id.equals("I")) return "12";
			else if(tooth_id.equals("J")) return "13";
			else if(tooth_id.equals("K")) return "20";
			else if(tooth_id.equals("L")) return "21";
			else if(tooth_id.equals("M")) return "22";
			else if(tooth_id.equals("N")) return "23";
			else if(tooth_id.equals("O")) return "24";
			else if(tooth_id.equals("P")) return "25";
			else if(tooth_id.equals("Q")) return "26";
			else if(tooth_id.equals("R")) return "27";
			else if(tooth_id.equals("S")) return "28";
			else if(tooth_id.equals("T")) return "29";
			else if(tooth_id.equals("AS")) return "4";
			else if(tooth_id.equals("BS")) return "5";
			else if(tooth_id.equals("CS")) return "6";
			else if(tooth_id.equals("DS")) return "7";
			else if(tooth_id.equals("ES")) return "8";
			else if(tooth_id.equals("FS")) return "9";
			else if(tooth_id.equals("GS")) return "10";
			else if(tooth_id.equals("HS")) return "11";
			else if(tooth_id.equals("IS")) return "12";
			else if(tooth_id.equals("JS")) return "13";
			else if(tooth_id.equals("KS")) return "20";
			else if(tooth_id.equals("LS")) return "21";
			else if(tooth_id.equals("MS")) return "22";
			else if(tooth_id.equals("NS")) return "23";
			else if(tooth_id.equals("OS")) return "24";
			else if(tooth_id.equals("PS")) return "25";
			else if(tooth_id.equals("QS")) return "26";
			else if(tooth_id.equals("RS")) return "27";
			else if(tooth_id.equals("SS")) return "28";
			else if(tooth_id.equals("TS")) return "29";
		    return "";
		}

		/**
		 * <summary>Converts supernumerary teeth to permanent.</summary>
		 */
		public static String SupToPerm(String tooth_id) {
			if(tooth_id.equals("51")) return "1";
			else if(tooth_id.equals("52")) return "2";
			else if(tooth_id.equals("53")) return "3";
			else if(tooth_id.equals("54")) return "4";
			else if(tooth_id.equals("55")) return "5";
			else if(tooth_id.equals("56")) return "6";
			else if(tooth_id.equals("57")) return "7";
			else if(tooth_id.equals("58")) return "8";
			else if(tooth_id.equals("59")) return "9";
			else if(tooth_id.equals("60")) return "10";
			else if(tooth_id.equals("61")) return "11";
			else if(tooth_id.equals("62")) return "12";
			else if(tooth_id.equals("63")) return "13";
			else if(tooth_id.equals("64")) return "14";
			else if(tooth_id.equals("65")) return "15";
			else if(tooth_id.equals("66")) return "16";
			else if(tooth_id.equals("67")) return "17";
			else if(tooth_id.equals("68")) return "18";
			else if(tooth_id.equals("69")) return "19";
			else if(tooth_id.equals("70")) return "20";
			else if(tooth_id.equals("71")) return "21";
			else if(tooth_id.equals("72")) return "22";
			else if(tooth_id.equals("73")) return "23";
			else if(tooth_id.equals("74")) return "24";
			else if(tooth_id.equals("75")) return "25";
			else if(tooth_id.equals("76")) return "26";
			else if(tooth_id.equals("77")) return "27";
			else if(tooth_id.equals("78")) return "28";
			else if(tooth_id.equals("79")) return "29";
			else if(tooth_id.equals("80")) return "30";
			else if(tooth_id.equals("81")) return "31";
			else if(tooth_id.equals("82")) return "32";
			else if(tooth_id.equals("AS")) return "4";
			else if(tooth_id.equals("BS")) return "5";
			else if(tooth_id.equals("CS")) return "6";
			else if(tooth_id.equals("DS")) return "7";
			else if(tooth_id.equals("ES")) return "8";
			else if(tooth_id.equals("FS")) return "9";
			else if(tooth_id.equals("GS")) return "10";
			else if(tooth_id.equals("HS")) return "11";
			else if(tooth_id.equals("IS")) return "12";
			else if(tooth_id.equals("JS")) return "13";
			else if(tooth_id.equals("KS")) return "20";
			else if(tooth_id.equals("LS")) return "21";
			else if(tooth_id.equals("MS")) return "22";
			else if(tooth_id.equals("NS")) return "23";
			else if(tooth_id.equals("OS")) return "24";
			else if(tooth_id.equals("PS")) return "25";
			else if(tooth_id.equals("QS")) return "26";
			else if(tooth_id.equals("RS")) return "27";
			else if(tooth_id.equals("SS")) return "28";
			else if(tooth_id.equals("TS")) return "29";
			return "";
		}
				
		/**
		 * 将乳牙和恒牙数字化
		 */
		public static int ToOrdinal(String toothNum){
			//
			if(IsPrimary(toothNum)){
				if(toothNum.equals("A")) return 33;
				else if(toothNum.equals("B")) return 34;
				else if(toothNum.equals("C")) return 35;
				else if(toothNum.equals("D")) return 36;
				else if(toothNum.equals("E")) return 37;
				else if(toothNum.equals("F")) return 38;
				else if(toothNum.equals("G")) return 39;
				else if(toothNum.equals("H")) return 40;
				else if(toothNum.equals("I")) return 41;
				else if(toothNum.equals("J")) return 42;
				else if(toothNum.equals("K")) return 43;
				else if(toothNum.equals("L")) return 44;
				else if(toothNum.equals("M")) return 45;
				else if(toothNum.equals("N")) return 46;
				else if(toothNum.equals("O")) return 47;
				else if(toothNum.equals("P")) return 48;
				else if(toothNum.equals("Q")) return 49;
				else if(toothNum.equals("R")) return 50;
				else if(toothNum.equals("S")) return 51;
				else if(toothNum.equals("T")) return 52;
				return -1;
			}
			else{//perm
				return ToInt(toothNum);
			}
		}

		///<summary>Assumes ordinal is valid.</summary>
		public static String FromOrdinal(int ordinal){
			if(ordinal<1 || ordinal>52){
				return "1";//just so it won't crash.
			}
			if(ordinal<33){
				return Integer.toString(ordinal);
			}
			if(ordinal<43){
				return Tooth.PermToPri(ordinal-29);
			}
			return Tooth.PermToPri(ordinal-23);
		}
					
		/**
		 * 判断是否是上颚
		 * */
		public static boolean IsMaxillary(int intTooth){
			String toothNum=FromInt(intTooth);
			return IsMaxillary(toothNum);
		}

		/**
		 * 判断是否是上颚
		 * */
		public static boolean IsMaxillary(String tooth_id) {
			if(!IsValidDB(tooth_id)) {
				return false;
			}
			int intTooth=ToInt(tooth_id);
			if(intTooth>=1 && intTooth<=16) {
				return true;
			}
			return false;
		}

		/**
		 * <summary>Handles direct user input and tidies according to rules. 
		 *  ToothNum might be empty, and a tidy should still be attempted.  Otherwise, toothNum must be valid.</summary>
		 */
		public static String SurfTidyForDisplay(String surf,String toothNum){
			//Canadian valid=MOIDBLV
			if(surf==null){
				surf="";
			}
			String surfTidy="";
	      ArrayList<String> al=new ArrayList<String>();
	      for(int i=0;i<surf.length();i++){
	        al.add(surf.substring(i,i+1).toUpperCase());
	      }
				//M----------------------------------------
	      if(al.contains("M")){
	        surfTidy+="M";
	      }
				//O-------------------------------------------
				if(toothNum=="" || IsPosterior(toothNum)){
					if(al.contains("O")){
						surfTidy+="O";
					}
				}
				//I---------------------------------
				if(toothNum=="" || IsAnterior(toothNum)){
					if(al.contains("I")) {
						surfTidy+="I";
					}
				}
	      //D---------------------------------------
	      if(al.contains("D")){
	        surfTidy+="D";
	      }
				//B------------------------------------------------
				if(toothNum=="" || IsPosterior(toothNum)) {
					if(al.contains("B")) {
						surfTidy+="B";
					}
				}
				//F-----------------------------------------
				if(isCanadian) {
					if(toothNum=="" || IsAnterior(toothNum)){
						if(al.contains("V")) {//Canadian equivalent of F
							surfTidy+="V";
						}
					}
				}
				else {
					if(toothNum=="" || IsAnterior(toothNum)) {
						if(al.contains("F")) {
							surfTidy+="F";
						}
					}
				}
				//V-----------------------------------------
				if(isCanadian) {
					if(al.contains("5")) {//Canadian equivalent of V
						surfTidy+="5";
					}
				}
				else {
					if(al.contains("V")) {
						surfTidy+="V";
					}
				}
				//L-----------------------------------------
	      if(al.contains("L")){
	        surfTidy+="L";
	      }
	      return surfTidy;      
	    }

		///<summary>Converts the database value to a claim value.  Special handling for V surfaces.  ToothNum must be valid.</summary>
		public static String SurfTidyForClaims(String surf,String toothNum) {
			
			//Canadian valid=MOIDBLV
			if(surf==null) {
				surf="";
			}
			String surfTidy="";
			ArrayList<String> al=new ArrayList<String>();
			for(int i=0;i<surf.length();i++) {
				al.add(surf.substring(i,i+1).toUpperCase());
			}
			//M----------------------------------------
			if(al.contains("M")) {
				surfTidy+="M";
			}
			//O-------------------------------------------
			if(IsPosterior(toothNum)) {
				if(al.contains("O")) {
					surfTidy+="O";
				}
			}
			//I---------------------------------
			if(IsAnterior(toothNum)) {
				if(al.contains("I")) {
					surfTidy+="I";
				}
			}
			//D---------------------------------------
			if(al.contains("D")) {
				surfTidy+="D";
			}
			//B------------------------------------------------
			//if(isCanadian) {//not needed because db to claim behavior is identical.  It's only in the UI where the V would show as 5
			if(IsPosterior(toothNum)) {
				if(al.contains("B") || al.contains("V")) {
					surfTidy+="B";
				}
			}
			//F-----------------------------------------
			if(IsAnterior(toothNum)) {
				if(al.contains("F") || al.contains("V")) {
					if(isCanadian) {
						surfTidy+="V";//Vestibular
					}
					else {
						surfTidy+="F";
					}
				}
			}
			//L-----------------------------------------
			if(al.contains("L")) {
				surfTidy+="L";
			}
			return surfTidy;
		}

		///<summary>Takes display string and converts it into Db string.  ToothNum does not need to be valid.</summary>
		public static String SurfTidyFromDisplayToDb(String surf,String toothNum) {
			
			//Canadian valid=MOIDBLV
			if(surf==null) {
				surf="";
			}
			String surfTidy="";
			ArrayList<String> al=new ArrayList<String>();
			for(int i=0;i<surf.length();i++) {
				al.add(surf.substring(i,i+1).toUpperCase());
			}
			//M----------------------------------------
			if(al.contains("M")) {
				surfTidy+="M";
			}
			//O-------------------------------------------
			if(toothNum=="" || IsPosterior(toothNum)) {
				if(al.contains("O")) {
					surfTidy+="O";
				}
			}
			//I---------------------------------
			if(toothNum=="" || IsAnterior(toothNum)) {
				if(al.contains("I")) {
					surfTidy+="I";
				}
			}
			//D---------------------------------------
			if(al.contains("D")) {
				surfTidy+="D";
			}
			//B------------------------------------------------
			if(toothNum=="" || IsPosterior(toothNum)) {
				if(al.contains("B")) {
					surfTidy+="B";
				}
			}
			//F-----------------------------------------
			if(isCanadian) {
				if(toothNum=="" || IsAnterior(toothNum)) {
					if(al.contains("V")) {//Canadian equivalent of F
						surfTidy+="F";//for db
					}
				}
			}
			else {
				if(toothNum=="" || IsAnterior(toothNum)) {
					if(al.contains("F")) {
						surfTidy+="F";
					}
				}
			}
			//V-----------------------------------------
			if(isCanadian) {
				if(al.contains("5")) {//Canadian equivalent of V
					surfTidy+="V";//for db
				}
			}
			else {
				if(al.contains("V")) {
					surfTidy+="V";
				}
			}
			//L-----------------------------------------
			if(al.contains("L")) {
				surfTidy+="L";
			}
			return surfTidy;
		}

				///<summary>Takes surfaces from Db and converts them to appropriate culture for display.  Only Canada supported so far.  ToothNum does not need to be valid since minimal manipulation here.</summary>
		public static String SurfTidyFromDbToDisplay(String surf,String toothNum) {
			
			//Canadian valid=MOIDBLV
			if(!isCanadian) {
				return surf;
			}
			//Canadian:
			if(surf==null) {
				return "";
			}
			String surfTidy=surf.replace("V","5");//USA classV becomes 5 for Canadian display
			surfTidy=surfTidy.replace("F","V");//USA Facial becomes Vestibular for Canadian display
			return surfTidy;
		}

		/// <summary>This will be deleted as soon as it's no longer in use by DirectX chart.</summary>
		public static float PerioShiftMm(String tooth_id) {
			return 0;
		}


	}
	

