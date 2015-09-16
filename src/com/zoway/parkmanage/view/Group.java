package com.zoway.parkmanage.view;

import java.util.ArrayList;
import java.util.List;

public class Group {
	public String string;
	public int drawableresid;
	public final List<String> children = new ArrayList<String>();

	public Group(String string, int drawableresid) {
		this.string = string;
		this.drawableresid = drawableresid;
	}
}
