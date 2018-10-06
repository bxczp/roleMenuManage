package com.bxczp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bxczp.entity.UserRole;

/**
 * 用户角色关联Repository接口
 * 
 * @author Administrator
 *
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Integer>, JpaSpecificationExecutor<UserRole> {

    /**
     * 根据用户id删除所有关联信息
     * 
     * @param userId
     */
    @Query(value = "delete from t_user_role where user_id=?1", nativeQuery = true)
    @Modifying
    // UPDATE或者DELETE操作需要使用事务，此时需要 定义Service层，在Service层的方法上添加事务操作。
    public void deleteByUserId(Integer userId);
    
    
    /**
     * 根据角色id删除所有关联信息
     * @param userId
     */
    @Query(value="delete from t_user_role where role_id=?1",nativeQuery=true)
    @Modifying
    public void deleteByRoleId(Integer roleId);
    
}
