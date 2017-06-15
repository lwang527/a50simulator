package common;

import com.ib.controller.NewContract;
import com.ib.controller.Types.SecType;
/**
 * 
* @ClassName: ContractMake 
* @Description: 合约生成工具
* @author wangling
* @email ling.wang@hidimension.com
* @date 2016年1月13日 下午5:21:50 
*
 */
public class ContractMake {
	public static NewContract SGX_A50(String expire_yyyyMM) {
		NewContract contract = new NewContract();
		contract.symbol("XINA50");
		contract.secType(SecType.FUT);
		contract.expiry(expire_yyyyMM);
		contract.exchange("SGX");
		contract.currency("USD");
		return contract;
	}

	public static NewContract CASH_EUR(String expire_yyyyMM) {
		NewContract contract = new NewContract();
		contract.localSymbol("EUR.USD");
		contract.secType(SecType.CASH);
		contract.exchange("IDEALPRO");
		contract.currency("USD");
		return contract;
	}
}
