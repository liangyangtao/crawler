package com.kf.data.fetcher.tools;

import java.util.UUID;

public class UUIDTools {

	  /**
	   * 随机生成UUID
	   * @return
	   */
	  public static String getUUID(){
	    UUID uuid=UUID.randomUUID();
	    String str = uuid.toString(); 
	    String uuidStr=str.replace("-", "");
	    return uuidStr;
	  }
	  
	  
	  /**
	   * 根据字符串生成固定UUID
	   * @param name
	   * @return
	   */
	  public static String getUUID(String name){
	    UUID uuid=UUID.nameUUIDFromBytes(name.getBytes());
	    String str = uuid.toString(); 
	    String uuidStr=str.replace("-", "");
	    return uuidStr;
	  }
}
