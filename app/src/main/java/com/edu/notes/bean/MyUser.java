package com.edu.notes.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MyUser extends BmobUser {
	
	private static final long serialVersionUID = 1L;

	private BmobFile headPortrait;
	private String nickname;


	public BmobFile getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(BmobFile headPortrait) {
		this.headPortrait = headPortrait;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
