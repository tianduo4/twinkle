package com.twinkle.common.util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * description: 封装各种生成唯一性ID算法的工具类.
 *
 * @author ：King
 * @date ：2019/1/13 9:23
 */
public class IdGenUtil{

	private static SecureRandom random = new SecureRandom();
	
    private static final int LENGTH = 8;  
    
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	/**
	 * ID 生成
	 */
	public static String getNextId() {
		return IdGenUtil.uuid();
	}
	
	/**
	 * 生成纯英文字母的uuid
	 * @return
	 */
	public static String getUuidOfAbc(){
		StringBuffer result = new StringBuffer();
		for(int i=0;i<32;i++){
			result =  result.append((char)(Math.random()*26+'a'));
		}
		return result.toString();
	}
		
	/** 
     * 这是典型的随机洗牌算法。 
     * 流程是从备选数组中选择一个放入目标数组中，将选取的数组从备选数组移除（放至最后，并缩小选择区域） 
     * 算法时间复杂度O(n) 
     * @return 随机8为不重复数组 
     */ 
    public static String generateNumber() {  
        String no="";  
        //初始化备选数组  
        int[] defaultNums = new int[10];  
        for (int i = 0; i < defaultNums.length; i++) {  
            defaultNums[i] = i;  
        }  
   
        Random random = new Random();  
        int[] nums = new int[LENGTH];  
        //默认数组中可以选择的部分长度  
        int canBeUsed = 10;  
        //填充目标数组  
        for (int i = 0; i < nums.length; i++) {  
            //将随机选取的数字存入目标数组  
            int index = random.nextInt(canBeUsed);  
            nums[i] = defaultNums[index];  
            //将已用过的数字扔到备选数组最后，并减小可选区域  
            swap(index, canBeUsed - 1, defaultNums);  
            canBeUsed--;  
        }  
        if (nums.length>0) {  
            for (int i = 0; i < nums.length; i++) {  
                no+=nums[i];  
            }  
        }  
   
        return no;  
    }  

    
    private static void swap(int i, int j, int[] nums) {  
        int temp = nums[i];  
        nums[i] = nums[j];  
        nums[j] = temp;  
    }  
	
	public static void main(String[] args) {
		System.out.println(IdGenUtil.uuid());
		System.out.println(IdGenUtil.uuid().length());
		System.out.println(IdGenUtil.getNextId());
		System.out.println(randomLong());
		System.out.println(generateNumber());
	}

}
