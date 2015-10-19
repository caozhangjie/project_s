package com.edu.thss.smartdental.model.tooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.graphics.Color;

public class ToothGraphic {
	
	public String toothID;
	//version1.1
	public float[][] vertices;
	public float[][] normals;
	public ArrayList<ToothGroup> groups;
	public boolean visible;
	private static float[] DefaultOrthoXpos;
	///<summary>The rotation about the Y axis in degrees. Performed before any other rotation or translation.  Positive numbers are clockwise as viewed from occlusal.</summary>
	public float Rotate;
	///<summary>Rotation to the buccal or lingual about the X axis in degrees.  Positive numbers are to buccal. Happens 2nd, after rotateY.  Not common. Usually 0.</summary>
	public float TipB;
	///<summary>Rotation about the Z axis in degrees.  Aka tipping.  Positive numbers are mesial.  Happens last after all other rotations.</summary>
	public float TipM;
	///<summary>Shifting mesially or distally in millimeters.  Mesial is always positive, distal is negative.  Happens after all three rotations.</summary>
	public float ShiftM;
	///<summary>Shifting vertically in millimeters.  Aka supereruption.  Happens after all three rotations.  A positive number is super eruption, whether on the maxillary or mandibular.</summary>
	public float ShiftO;
	///<summary>Shifting buccolingually in millimeters.  Rare.  Usually 0.</summary>
	public float ShiftB;
	///<summary>This gets set to true if this tooth has an RCT.  The purpose of this flag is to cause the root to be painted transparently on top of the RCT.</summary>
	public boolean IsRCT;
	///<summary>Set true to hide the number of the tooth from displaying.  Visible should also be set to false in this case.</summary>
	public boolean hideNumber;
	///<summary>The X indicated that an extraction is planned.</summary>
	public boolean DrawBigX;
	///<summary>If a big X is to be drawn, this will contain the color.</summary>
	public int colorX;
	///<summary>If RCT, then this will contain the color.</summary>
	public int colorRCT;
	///<summary>True if this tooth is set to show the primary letter in addition to the perm number.  This can only be true for 20 of the 32 perm teeth.  A primary tooth would never have this value set.  </summary>
	public boolean ShowPrimaryLetter;
	///<summary>This gets set to true if tooth has an implant.</summary>
	public boolean IsImplant;
	///<summary>If implant, then this will contain the color.</summary>
	public int colorImplant;
	///<summary>This will be true if the tooth is a crown or retainer.  The reason it's needed is so that crowns will show on missing teeth with implants.</summary>
	public boolean IsCrown;
	///<summary>True if the tooth is a bridge pontic, or a tooth on a denture or RPD.  This makes the crown visible from the F view, but not the occlusal.  It also makes the root invisible.</summary>
	public boolean IsPontic;
	///<summary>This gets set to true if tooth has a sealant.</summary>
	public boolean IsSealant;
	///<summary>If sealant, then this will contain the color.</summary>
	public int colorSealant;
	///<summary>This gets set to true if tooth has a watch on it.</summary>
	public boolean Watch;
	public int colorWatch=Color.RED;
	///<summary>For perio, 1, 2, or 3.  It's a string because we'll later allow +.  The text gets drawn directly on the front of the tooth.</summary>
	public String Mobility;
	///<summary>If a mobility is set, the is the color.</summary>
	public int colorMobility;


	public InputStreamReader inputReader;
	
	public ToothGraphic(String tooth_id)  {
		if( !IsValidToothID(tooth_id)) {
			//throw new Exception("Invalid tooth ID");
		}
		toothID=tooth_id;
		//SetDefaultColors();
	}

	public String ToString() {
		String retVal=this.toothID;
		if(IsRCT) {
			retVal+=", RCT";
		}
		return retVal;
	}



	///<summary>Resets this tooth graphic to original location, visiblity, and no restorations.  If primary tooth, then Visible=false.  </summary>
	public void Reset() {
		if(Tooth.IsPrimary(toothID)) {
			visible=false;
		}
		else {
			visible=true;
		}
		TipM=0;
		TipB=0;
		Rotate=0;
		ShiftB=0;
		ShiftM=0;
		ShiftO=0;
		SetDefaultColors();
		IsRCT=false;
		hideNumber=false;
		DrawBigX=false;
		ShowPrimaryLetter=false;
		IsImplant=false;
		IsCrown=false;
		IsPontic=false;
		IsSealant=false;
		Watch=false;
	}

	public void SetSurfaceColors(String surfaces,int color){
		for(int i=0;i<surfaces.length();i++){
			if(surfaces.charAt(i)=='M'){
				SetGroupColor(ToothGroupType.M,color);
				//SetGroupColor(ToothGroupType.MF,color);
			}
			else if(surfaces.charAt(i)=='O') {
				SetGroupColor(ToothGroupType.O,color);
			}
			else if(surfaces.charAt(i)=='D') {
				SetGroupColor(ToothGroupType.D,color);
				//SetGroupColor(ToothGroupType.DF,color);
			}
			else if(surfaces.charAt(i)=='B') {
				SetGroupColor(ToothGroupType.B,color);
			}
			else if(surfaces.charAt(i)=='L') {
				SetGroupColor(ToothGroupType.L,color);
			}
			else if(surfaces.charAt(i)=='V') {
				SetGroupColor(ToothGroupType.V,color);
			}
			else if(surfaces.charAt(i)=='I') {
				SetGroupColor(ToothGroupType.I,color);
				//SetGroupColor(ToothGroupType.IF,color);
			}
			else if(surfaces.charAt(i)=='F') {
				SetGroupColor(ToothGroupType.F,color);
			}
		}
	}

	public void SetGroupColor(ToothGroupType groupType,int paintColor){
		for(int i=0;i<groups.size();i++){
			if(groups.get(i).groupType != groupType)
			continue;
			groups.get(i).paintColor = paintColor;
		}
	}


	public void SetDefaultColors(){
		for(int i=0;i<groups.size();i++){
			if(groups.get(i).groupType==ToothGroupType.Cementum) {
				groups.get(i).paintColor=Color.rgb(255, 250, 230);//(255,250,245,223);//255,250,230);
			}
			else {//enamel
				groups.get(i).paintColor=Color.rgb(255, 255, 245);//(255,250,250,240);//255,255,245);
			}
			if(groups.get(i).groupType==ToothGroupType.Canals
				|| groups.get(i).groupType==ToothGroupType.Buildup)
			{
				groups.get(i).visible=false;
			}
			else{
				groups.get(i).visible=true;
			}
		}
	}

	public void SetGroupVisibility(ToothGroupType groupType,boolean setVisible) {
		for(int i=0;i<groups.size();i++) {
			if(groups.get(i).groupType!=groupType) {
				continue;
			}
			groups.get(i).visible = setVisible;
		}
	}

	public ToothGroup GetGroupForDisplayList(int index){
		ToothGroupType groupType =ToothGroupType.values()[index];
		
		for(int i=0;i<groups.size();i++) {
			if(groups.get(i).groupType==groupType) {
				return groups.get(i);
			}
		}
		return null;
	}
	public int GetIndexForDisplayList(ToothGroup group) {
		int toothInt=Tooth.ToOrdinal(toothID);
		return (toothInt*10)+group.groupType.ordinal();
	}
	
	public static boolean IsValidToothID(String tooth_id) {
		if(!Tooth.IsValidDB(tooth_id)) {
			return false;
		}
		if(Tooth.IsSuperNum(tooth_id)){
			return false;
		}
		return true;
	}

	public static int IdToInt(String tooth_id) {
		if(!IsValidToothID(tooth_id)) {
			return -1;
		}
		return Tooth.ToInt(tooth_id);
	}

	/***
	 * <summary>The actual specific width of the tooth in mm.
	 */
	
	public static float GetWidth(String tooth_id) {
		if(tooth_id.equals("1")||tooth_id.equals("16")) return 8.5f;
		else if(tooth_id.equals("2")||tooth_id.equals("15")) return 9f;
		else if(tooth_id.equals("3")||tooth_id.equals("14")) return 10f;
		else if(tooth_id.equals("4")||tooth_id.equals("5")
				||tooth_id.equals("13")||tooth_id.equals("12")) return 7f;
		else if(tooth_id.equals("6")||tooth_id.equals("11")) return 7.5f;
		else if(tooth_id.equals("7")||tooth_id.equals("10")) return 6.5f;
		else if(tooth_id.equals("8")||tooth_id.equals("9")) return 8.5f;
		else if(tooth_id.equals("17")||tooth_id.equals("32")) return 10f;
		else if(tooth_id.equals("18")||tooth_id.equals("31")) return 10.5f;
		else if(tooth_id.equals("19")||tooth_id.equals("30")) return 11f;
		else if(tooth_id.equals("20")||tooth_id.equals("21")
				||tooth_id.equals("29")||tooth_id.equals("28")) return 7f;
		else if(tooth_id.equals("22")||tooth_id.equals("27")) return 7f;
		else if(tooth_id.equals("23")||tooth_id.equals("26")) return 5f;
		else if(tooth_id.equals("24")||tooth_id.equals("25")) return 5f;
		return 0;
	}

	///<summary></summary>
	public static float GetWidth(int tooth_num) {
		return GetWidth(Integer.toString(tooth_num));
	}

	/**
	 * <summary>The x position of the center of the given tooth, with midline being 0. 
	 *  Calculated once, then used to quickly calculate mouse positions and tooth positions.
	 *  All values are in mm.</summary>
	 * @param intTooth
	 * @return
	 * @throws Exception 
	 */
	
	public static float GetDefaultOrthoXpos(int intTooth) throws Exception {
		if(DefaultOrthoXpos==null) {
			DefaultOrthoXpos=new float[33];//0-32, 0 not used
			float spacing=2;//the distance between each adjacent tooth.
			DefaultOrthoXpos[8]=-spacing/2f-GetWidth(8)/2f;
			DefaultOrthoXpos[7]=DefaultOrthoXpos[8]-spacing-GetWidth(8)/2f-GetWidth(7)/2f;
			DefaultOrthoXpos[6]=DefaultOrthoXpos[7]-spacing-GetWidth(7)/2f-GetWidth(6)/2f;
			DefaultOrthoXpos[5]=DefaultOrthoXpos[6]-spacing-GetWidth(6)/2f-GetWidth(5)/2f;
			DefaultOrthoXpos[4]=DefaultOrthoXpos[5]-spacing-GetWidth(5)/2f-GetWidth(4)/2f;
			DefaultOrthoXpos[3]=DefaultOrthoXpos[4]-spacing-GetWidth(4)/2f-GetWidth(3)/2f;
			DefaultOrthoXpos[2]=DefaultOrthoXpos[3]-spacing-GetWidth(3)/2f-GetWidth(2)/2f;
			DefaultOrthoXpos[1]=DefaultOrthoXpos[2]-spacing-GetWidth(2)/2f-GetWidth(1)/2f;
			DefaultOrthoXpos[9]=-DefaultOrthoXpos[8];
			DefaultOrthoXpos[10]=-DefaultOrthoXpos[7];
			DefaultOrthoXpos[11]=-DefaultOrthoXpos[6];
			DefaultOrthoXpos[12]=-DefaultOrthoXpos[5];
			DefaultOrthoXpos[13]=-DefaultOrthoXpos[4];
			DefaultOrthoXpos[14]=-DefaultOrthoXpos[3];
			DefaultOrthoXpos[15]=-DefaultOrthoXpos[2];
			DefaultOrthoXpos[16]=-DefaultOrthoXpos[1];
			DefaultOrthoXpos[24]=spacing/2f+GetWidth(24)/2f;
			DefaultOrthoXpos[23]=DefaultOrthoXpos[24]+spacing+GetWidth(24)/2f+GetWidth(23)/2f;
			DefaultOrthoXpos[22]=DefaultOrthoXpos[23]+spacing+GetWidth(23)/2f+GetWidth(22)/2f;
			DefaultOrthoXpos[21]=DefaultOrthoXpos[22]+spacing+GetWidth(22)/2f+GetWidth(21)/2f;
			DefaultOrthoXpos[20]=DefaultOrthoXpos[21]+spacing+GetWidth(21)/2f+GetWidth(20)/2f;
			DefaultOrthoXpos[19]=DefaultOrthoXpos[20]+spacing+GetWidth(20)/2f+GetWidth(19)/2f;
			DefaultOrthoXpos[18]=DefaultOrthoXpos[19]+spacing+GetWidth(19)/2f+GetWidth(18)/2f;
			DefaultOrthoXpos[17]=DefaultOrthoXpos[18]+spacing+GetWidth(18)/2f+GetWidth(17)/2f;
			DefaultOrthoXpos[25]=-DefaultOrthoXpos[24];
			DefaultOrthoXpos[26]=-DefaultOrthoXpos[23];
			DefaultOrthoXpos[27]=-DefaultOrthoXpos[22];
			DefaultOrthoXpos[28]=-DefaultOrthoXpos[21];
			DefaultOrthoXpos[29]=-DefaultOrthoXpos[20];
			DefaultOrthoXpos[30]=-DefaultOrthoXpos[19];
			DefaultOrthoXpos[31]=-DefaultOrthoXpos[18];
			DefaultOrthoXpos[32]=-DefaultOrthoXpos[17];
		}
		if(intTooth<1 || intTooth>32) {
			throw new Exception("Invalid tooth_num: "+Integer.toString(intTooth));//just for debugging
		}
		return DefaultOrthoXpos[intTooth];
	}

	public static boolean IsPrimary(String tooth_id) {
		/*if(Regex.IsMatch(tooth_id,"^[A-T]$")) {
			return true;
		}
		if(Regex.IsMatch(tooth_id,"^[A-T]S$")) {
			return true;
		}*/
		return false;
	}
	public static boolean IsMaxillary(String tooth_id) {
		if(!IsValidToothID(tooth_id)) {
			return false;
		}
		return Tooth.IsMaxillary(tooth_id);
	}

	public static boolean IsAnterior(String tooth_id) {
		if(!IsValidToothID(tooth_id)) {
			return false;
		}
		return Tooth.IsAnterior(tooth_id);
	}
	

	public static boolean IsRight(String tooth_id) {
		if(!IsValidToothID(tooth_id)) {
			return false;
		}
		int intTooth=IdToInt(tooth_id);
		if(intTooth>=1 && intTooth<=8) {
			return true;
		}
		if(intTooth>=25 && intTooth<=32) {
			return true;
		}
		return false;
	}
	public void importObjSimple(){
		//¼òÒ×°æµÄÑÀ³ÝÍ¼
		this.groups = new ArrayList<ToothGroup>();
		ToothGroup group = null;
		if(toothID.equals("1")||toothID.equals("16")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.B;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.O;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("2")||toothID.equals("15")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.B;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.O;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
			
		}
		else if(toothID.equals("3")||toothID.equals("14")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.B;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.O;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("4")||toothID.equals("13")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Canals;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.B;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.O;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("5")||toothID.equals("12")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Canals;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.B;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.O;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("6")||toothID.equals("11")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.F;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.I;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("7")||toothID.equals("10")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.F;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.I;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("8")||toothID.equals("9")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.F;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("25")||toothID.equals("23")||toothID.equals("24")||toothID.equals("26")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.F;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("27")||toothID.equals("22")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.F;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.I;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("28")||toothID.equals("21")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.B;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.O;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("29")||toothID.equals("20")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.B;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.O;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("30")||toothID.equals("19")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.B;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.O;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("31")||toothID.equals("18")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.B;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.O;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
		else if(toothID.equals("32")||toothID.equals("17")){
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.B;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Cementum;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.D;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.Enamel;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.L;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.M;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.O;
			groups.add(group);
			
			group = new ToothGroup();
			group.visible = true;
			group.groupType = ToothGroupType.V;
			groups.add(group);
		}
	}
	@SuppressWarnings("unused")
	public void importObj() throws NumberFormatException, IOException {
		
		boolean flipHorizontally=false;
		if(toothID!="implant" && IdToInt(toothID)>=9 && IdToInt(toothID)<=24) {
			flipHorizontally=true;
		}
		ArrayList<float[]> ALv=new ArrayList<float[]>();//vertices
		ArrayList<float[]> ALvn=new ArrayList<float[]>();//vertex normals
		ArrayList<ToothGroup> Groups=new ArrayList<ToothGroup>();//type=ToothGroup
		ArrayList<int[][]> ALf=new ArrayList<int[][]>();//faces always part of a group
		BufferedReader stream = new BufferedReader(inputReader);
		String line;
		float[] vertex;
		String[] items;
		String[] subitems;
		int[][] face;//dim 1=the vertex. dim 2 has length=2, with 1st vertex, and 2nd normal.
		ToothGroup group=null;
		while((line = stream.readLine()) != null) {
			if(line.startsWith("#")//comment
				|| line.startsWith("mtllib")//material library.  We build our own.
				|| line.startsWith("usemtl")//use material
				|| line.startsWith("o")) {//object. There's only one object 
				continue;
			}
			if(line.startsWith("v ")) {//vertex
				items=line.split(" ");
				vertex=new float[3];
				if(flipHorizontally) {
					vertex[0]=-Float.parseFloat(items[1]);
				}
				else {
					vertex[0]=Float.parseFloat(items[1]);
				}
				vertex[1]=Float.parseFloat(items[2]);
				vertex[2]=Float.parseFloat(items[3]);
				ALv.add(vertex);
				continue;
		}
			if(line.startsWith("vn")) {//vertex normal
				items=line.split(" ");
				vertex=new float[3];
				if(flipHorizontally) {
					vertex[0]=-Float.parseFloat(items[1]);
				}
				else {
					vertex[0]=Float.parseFloat(items[1]);
				}
				vertex[1]=Float.parseFloat(items[2]);;
				vertex[2]=Float.parseFloat(items[2]);;
				ALvn.add(vertex);
				continue;
			}
			if(line.startsWith("g")) {//group
				if(group != null) {
					//move all faces into the existing group
					group.faces=new int[ALf.size()][][];
					for(int i=0;i<group.faces.length;i++) {
						group.faces[i]=new int[((int[][])ALf.get(i)).length][];
						for(int j=0;j<group.faces[i].length;j++) {//loop through vertices for the face
							group.faces[i][j]=new int[2];
							group.faces[i][j][0]=((int[][])ALf.get(i))[j][0];//vertex
							group.faces[i][j][1]=((int[][])ALf.get(i))[j][1];//normal
						}
					}
					Groups.add(group);
				}
				group=new ToothGroup();
				ALf=new ArrayList<int[][]>();//reset faces
			    if(line.equals("g cube1_Cementum")){
			    	group.groupType =ToothGroupType.Cementum;
			    }
			    else if(line.equals("g cube1_Enamel2")){
			    	group.groupType =ToothGroupType.Enamel;
			    }
			    else if(line.equals("g cube1_M")){
			    	group.groupType =ToothGroupType.M;
			    }
			    else if(line.equals("g cube1_D")){
			    	group.groupType =ToothGroupType.D;
			    }
			    else if(line.equals("g cube1_F")){
			    	group.groupType =ToothGroupType.F;
			    }
			    else if(line.equals("g cube1_I")){
			    	group.groupType =ToothGroupType.I;
			    }
			    else if(line.equals("g cube1_L")){
			    	group.groupType =ToothGroupType.L;
			    }
			    else if(line.equals("g cube1_V")){
			    	group.groupType =ToothGroupType.V;
			    }
			    else if(line.equals("g cube1_B")){
			    	group.groupType =ToothGroupType.B;
			    }
			    else if(line.equals("g cube1_O")){
			    	group.groupType =ToothGroupType.O;
			    }
			    else if(line.equals("g cube2_Canals")){
			    	group.groupType =ToothGroupType.Canals;
			    }
			    else if(line.equals("g cube2_Buildup")){
			    	group.groupType =ToothGroupType.Buildup;
			    }
			    else if(line.equals("g cube1_Implant")){
			    	group.groupType =ToothGroupType.Implant;
			    }
			}
			if(line.startsWith("f")) {//face. Usually 4 vertices, but not always.
				items=line.split(" ");
				face=new int[items.length-1][];
				for(int i=0;i<face.length;i++) {
					subitems=items[i+1].split("//");// eg: 9//9
					face[i]=new int[2];
					face[i][0] = Integer.parseInt(subitems[0])-1;
					face[i][1] = Integer.parseInt(subitems[1])-1;
				}
				ALf.add(face);
				continue;
			}
		}
		group.faces=new int[ALf.size()][][];
		for(int i=0;i<group.faces.length;i++) {
			group.faces[i]=new int[((int[][])ALf.get(i)).length][];
			for(int j=0;j<group.faces[i].length;j++) {//loop through vertices for the face
				group.faces[i][j]=new int[2];
				group.faces[i][j][0]=((int[][])ALf.get(i))[j][0];//vertex
				group.faces[i][j][1]=((int[][])ALf.get(i))[j][1];//normal
			}
		}
		Groups.add(group);
	
	this.vertices=new float[ALv.size()][];
	for(int i=0;i<this.vertices.length;i++) {
		this.vertices[i]=(float[])ALv.get(i);
	}
	this.normals=new float[ALvn.size()][];
	for(int i=0;i<this.normals.length;i++) {
		this.normals[i]=(float[])ALvn.get(i);
	}
}
	

}
