/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.util.*;

/**
 *
 * @author drbf1
 */
abstract public class StoreDAO<E, K>{
    abstract public void insert(E entity);
    abstract public void update(E entity);
    abstract public void delete(K id);
    abstract public E selectById(K id);
    abstract public List<E> selectAll();
    abstract protected List<E> selectBySql(String sql, Object...args);
}
