package com.yunzo.cocmore.core.function.util;

import java.util.ArrayList;
import java.util.List;


public class Tree1 {
		private String cls = "folder";
		private String id;

		private boolean leaf = false;
		//private boolean checked=false;
		private String text = "";
		private List<Tree1> children = new ArrayList<Tree1>();
		private int level;
		private String pid;

		public String getCls() {
			return cls;
		}

		public void setCls(String cls) {
			this.cls = cls;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public boolean getLeaf() {
			return leaf;
		}

		public void setLeaf(boolean leaf) {
			this.leaf = leaf;
		}
		
//		public boolean isChecked() {
//			return checked;
//		}

//		public void setChecked(boolean checked) {
//			this.checked = checked;
//		}
//		
//		public boolean getChecked(){
//			return checked;
//		}
		
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public List<Tree1> getChildren() {
			return children;
		}

		public void setChildren(List<Tree1> children) {
			this.children = children;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public String getPid() {
			return pid;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}
}
