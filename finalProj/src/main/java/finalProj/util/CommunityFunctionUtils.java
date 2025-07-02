package finalProj.util;

import java.util.ArrayList;
import java.util.List;

import finalProj.enumCommunity.CommunityFunction;

public class CommunityFunctionUtils {

	// 是否有指定功能
	public static boolean hasFunction(Long currentValue, CommunityFunction function) {
		if (currentValue == null) {
			return false;
		} else {
			return (currentValue & function.getValue()) != 0;
		}
	}

	// 增加功能
	public static long addFunction(Long currentValue, CommunityFunction functionToAdd) {
	    if (currentValue == null) {
	        return functionToAdd.getValue(); // 回傳 long
	    } else {
	        return currentValue | functionToAdd.getValue(); // 位元 OR
	    }
	}

	// 移除功能
	public static long removeFunction(Long currentValue, CommunityFunction functionToRemove) {
	    if (currentValue == null) {
	        return 0L; // long 型別
	    } else {
	        return currentValue & ~functionToRemove.getValue(); // 位元 AND NOT
	    }
	}
	// 顯示當前的功能
	public static List<CommunityFunction> showAllEnableFunction(Long currentValue) {
		List<CommunityFunction> functionList = new ArrayList<>();

		// 如果 currentValue 是 null，回傳空清單（沒有啟用任何功能
		if (currentValue == null) {
			return functionList;
		} else {
			// 如果 currentValue 有值，回傳空清單（沒有啟用任何功能
			for (CommunityFunction f : CommunityFunction.values()) {
				if (hasFunction(currentValue, f)) {
					functionList.add(f);
				}
			}
			return functionList;
		}
	}
}
