package com.edu.thss.smartdental.model.tooth;

import java.util.Comparator;

public class ToothComparer implements Comparator<String> {

	public ToothComparer()
	{
	}
	

	@Override
	public int compare(String toothA, String toothB) {
		//return Tooth.ToOrdinal(toothA).CompareTo(Tooth.ToOrdinal(toothB));
		return 0;
	}
}
