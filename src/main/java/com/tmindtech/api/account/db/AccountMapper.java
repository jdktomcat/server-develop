package com.tmindtech.api.account.db;

import com.tmindtech.api.account.model.Account;
import com.tmindtech.api.account.model.AccountResult;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by RexQian on 2017/2/21.
 */
public interface AccountMapper extends Mapper<Account> {

    List<AccountResult> getAccounts(@Param("role_id") Long roleId, @Param("name_like") String nameLike);

    List<Long> getPermissionIds(@Param("account_id") Long accountId);

}
