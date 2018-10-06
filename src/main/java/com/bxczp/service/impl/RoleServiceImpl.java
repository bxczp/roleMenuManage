package com.bxczp.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bxczp.entity.Role;
import com.bxczp.repository.RoleRepository;
import com.bxczp.service.RoleService;
import com.bxczp.util.StringUtil;

/**
 * 角色Service实现类
 * 
 * @author Administrator
 *
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleRepository roleRepository;

    @Override
    public List<Role> findByUserId(Integer id) {
        return roleRepository.findByUserId(id);
    }

    @Override
    public Role findById(Integer id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public List<Role> listAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> list(Role role, Integer page, Integer pageSize, Direction direction, String... properties) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Role> pageUser = roleRepository.findAll(new Specification<Role>() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (role != null) {
                    if (StringUtil.isNotEmpty(role.getName())) {
                        predicate.getExpressions().add(cb.like(root.get("name"), "%" + role.getName() + "%"));
                    }
                    predicate.getExpressions().add(cb.notEqual(root.get("id"), 1));
                }
                return predicate;
            }
        }, pageable);
        return pageUser.getContent();
    }

    @Override
    public Long getCount(Role role) {
        Long count = roleRepository.count(new Specification<Role>() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (role != null) {
                    if (StringUtil.isNotEmpty(role.getName())) {
                        predicate.getExpressions().add(cb.like(root.get("name"), "%" + role.getName() + "%"));
                    }
                    predicate.getExpressions().add(cb.notEqual(root.get("id"), 1));
                }
                return predicate;
            }

        });
        return count;
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void delete(Integer id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

}
