package com.yunzo.cocmore.utils.base;
/** 
 * 字符串主键自增，条件当传进来的Str是空时，自动创建初始化主键 
 * 后面的依次自增 
 * @author david 
 * 
 */
public class StringPrimaryKey {
		
		// 数字串最小值
		private static final char MIN_DATA = '0';
		// 数字串最大值
		private static final char MAX_DATA = '9';
		// 数字串默认从1开始
		private static final char START_DATA = '1';
		// 默认长度
		private static final int DEFAULT_SIZE = 8;
		// KeySize的最大数
		// Long的最大长度是19位，防止溢出
		private static final int MAX_KEYSIZE_VALUE = 18;
		// 默认字符串Head
		private static final String DEFAULT_HEAD = "KEY";
		// 主键字符串头部
		private String keyHead;
		// 字符串数字位数，不足补0
		private Integer keySize = 8;
		// 是否允许数字最大之后自增，默认false
		private boolean keyInc = false;
		// 程序执行开始系统时间
		private Long startExecute = 0L;
		// 程序执行结束系统时间
		private Long finishExecute = 0L;
		
		/**
		 * 初始化主键字符串格式，默认达到KeySize后不可自增
		 * @param keyHead 字符串开头部分
		 * @param keySize 字符串数组长度
		 */
		public StringPrimaryKey(String keyHead, Integer keySize) {
			super();
			/**
			 * 设置不可自增
			 */
			if(this.checkSize(keySize))
				this.keySize = keySize;
			else
				this.keySize = this.DEFAULT_SIZE;
			if(this.checkHead(keyHead))
				this.keyHead = keyHead;
			else
				this.keyHead = this.DEFAULT_HEAD;
		}

		/**
		 * 初始化主键字符串的格式
		 * @param keyHead 字符串开头部分
		 * @param keySize 字符串数组长度
		 * @param keyInc 数值最大值之后是否允许自增
		 */
		public StringPrimaryKey(String keyHead, Integer keySize, boolean keyInc) {
			super();
			if(this.checkSize(keySize))
				this.keySize = keySize;
			else
				this.keySize = this.DEFAULT_SIZE;
			if(this.checkHead(keyHead))
				this.keyHead = keyHead;
			else
				this.keyHead = this.DEFAULT_HEAD;
			this.keyInc = keyInc;
		}
		
		/**
		 * 返回下一个字符串
		 * @param currentKey 当前主键
		 * @return 正常：下一个主键值 = 当前主键 + 1;
		 * 		   当字符串数字达到KeySize的最大数时
		 * 		   KeyInc为true时， 下一个主键字符串返回最大数 + 1
		 * 		   KeyInc为false时， 返回空值
		 */
		public synchronized String nextKey(String currentKey) {
			
			// 记录开始执行程序系统时间
			this.startExecute = System.currentTimeMillis();
			try {
				/**
				 * 去掉首尾空字符
				 */
				currentKey = currentKey.trim();
				if(!this.check(currentKey)) {
					System.out.println(StringPrimaryKey.class.getSimpleName() + 
							" Error: Input CurrentKey Str Type Illegal, Check '" + currentKey +"' is Right!");
					return null;
				}
				StringBuilder sb = new StringBuilder();
				sb.append(this.keyHead);
				int charIndex = 0;
				for(int i = 0; i < currentKey.length(); i++) {
					char symbol = currentKey.charAt(i);
					if(symbol >= this.MIN_DATA && symbol <= this.MAX_DATA) {
						charIndex = i;
						break;
					}
				}
				String dataStr = currentKey.substring(charIndex, currentKey.length());
				Long dataNum = Long.valueOf(dataStr);
				dataNum++;
				if(dataNum < this.splitDataPosition()) {
					for(int i = 0; i <= this.keySize - String.valueOf(dataNum).length() - 1; i++) {
						sb.append(this.MIN_DATA);
					}
					sb.append(dataNum);
				}else if(dataNum >= this.splitDataPosition() && 
						dataNum < this.maxDateNumber()) {
					sb.append(dataNum);
				}else{
					// 超过大小最大数时
					if(this.keyInc) {
						sb.append(dataNum);
					}else{
						// 允许自增标志位false的时候返回空值
						return null;
					}
				}
				return sb.toString();
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			} finally {
				this.finishExecute = System.currentTimeMillis();
//				System.out.println(PrimaryKey.class.getSimpleName() + " nextKey() Execute: "
//						+ (this.finishExecute - this.startExecute) +"ms.");
			}
		}
		
		/**
		 * 获取初始化字符串
		 * @return
		 */
		public synchronized String initStartKey() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.keyHead);
			for(int i = 0; i < this.keySize - 1; i++) {
				sb.append(this.MIN_DATA);
			}
			sb.append(this.START_DATA);
			return sb.toString();
		}
		
		/**
		 * 获取需要补零的最大数字
		 * @return
		 */
		private Long splitDataPosition() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.START_DATA);
			for(int i = 0; i < this.keySize - 1; i++) {
				sb.append(this.MIN_DATA);
			}
			return Long.valueOf(sb.toString());
		}
		
		/**
		 * 获取最大数
		 * @return
		 */
		private Long maxDateNumber() {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < this.keySize; i++) {
				sb.append(this.MAX_DATA);
			}
			return Long.valueOf(sb.toString());
		}
		
		/**
		 * 简单的验证空值
		 * @param key
		 * @return
		 * @throws Exception 
		 */
		private boolean check(String key) throws Exception {	
			try {
				// 空值验证
				if(key == null || key.equals("")) 
					return false;
				// key字符串长度验证
				if(key.length() <= this.keyHead.length()) 
					return false;
				// 是否符合初始化串开头验证
				String head = key.substring(0, this.keyHead.length());
				if(!head.equals(this.keyHead)) 
					return false;
				/**
				 * 串数字长度验证，当允许最大熟自增时候不检测
				 * 当不允许达到最大数字时验证长度合法性
				 */
				String data = key.substring(this.keyHead.length(), key.length());
				if(data.length() != this.keySize && !this.keyInc)
					return false;
				// 验证是否是数字串，通过一个转换变量
				for(int i = 0; i < data.length(); i++) {
					char symbol = data.charAt(i);
					if(symbol > this.MAX_DATA || symbol < this.MIN_DATA) {
						return false;
					}
				}
				return true;			
			} catch (Exception e) {	
				throw e;
			}
		}
		
		/**
		 * 验证输入的KeySize合法性
		 * @param keySize
		 * @return
		 */
		private synchronized boolean checkSize(Integer keySize) {
			if(keySize != null && keySize > 0 
					&& keySize <= this.MAX_KEYSIZE_VALUE)
				return true;
			return false;
		}
		
		/**
		 * 验证输入的KeyHead，条件全部要求是字母
		 * @param keyHead
		 * @return
		 */
		private synchronized boolean checkHead(String keyHead) {
			if(keyHead != null && !keyHead.equals("")) {
				for(int i = 0; i < keyHead.length(); i++) {
					char symbol = keyHead.charAt(i);
					if(symbol >= this.MIN_DATA && symbol <= this.MAX_DATA) {
						return false;
					}
				}
				return true;
			}
			return false;
		}


}
