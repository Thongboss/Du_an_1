/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import java.util.List;
/**
 *
 * @author ASUS
 */
public abstract class MilKyway<EntityType, KeyType> {

    abstract public void insert(EntityType Entity);

    abstract public void update(EntityType Entity);

    abstract public void delete(KeyType id);

    abstract public EntityType selectByid(KeyType id);

    abstract public List<EntityType> selectAll();

    abstract protected List<EntityType> selectBySql(String sql, Object... args);
}
