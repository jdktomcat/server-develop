package com.tmindtech.api.account.db;

import com.tmindtech.api.account.model.Account;
import com.tmindtech.api.account.model.AccountRole;
import com.tmindtech.api.account.model.Role;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by RexQian on 2017/2/22.
 */
public interface AccountRoleMapper extends Mapper<AccountRole> {
    List<Account> getAccountList(@Param("name_like") String nameLike,
                                 @Param("role_id_list") List<Long> roleIdList);

    List<Role> getRoleList(@Param("account_id") Long accountId);
}
