package com.yunzo.cocmore.core.function.util;

public enum Status {
	//	状态（0、新增，1、保存，2、提交，3、审核，4、反审核5、生效，6、失效，7、启用，8、禁用，9、作废，10、变更,11、已关联,12、开始,13、关闭,14、启动,15、待审核,16、通过,17、拒绝
	// 18、待发送，19、发送成功，20、发送失败,21 、已读、22未读）
	
	ADDNEW(0),SAVE(1),SUBMIT(2),AUDIT(3),UNAUDIT(4),
	EFFECT(5),UNEFFECT(6),USE(7),UNUSE(8),ABOLISH(9),
	CHANGE(10),RELATION(11),START(12),OVER(13),ENABLE(14),PENDING(15),PASS(16),REFUSAL(17),WAITING(18),SUCCESS(19),FAIL(20),READ(21),UNREAD(22);

	//定义自己的构造器
	private int value;//中文名字


	private Status(int value){
		this.value=value;
	}

	public int value() {
		return value;
	}
}
