package com.wtkj.oa.utils.ObjectPool;


import com.wtkj.oa.service.impl.ContractManageServiceImpl;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ObjectFactory implements PooledObjectFactory<Object> {


    @Override
    public PooledObject<Object> makeObject() throws Exception {
        ContractManageServiceImpl obj = new ContractManageServiceImpl();
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void destroyObject(PooledObject<Object> pooledObject) throws Exception {
        pooledObject.getObject();
    }

    @Override
    public boolean validateObject(PooledObject<Object> pooledObject) {
        return pooledObject.allocate();
    }

    @Override
    public void activateObject(PooledObject<Object> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<Object> pooledObject) throws Exception {

    }

    public void saveObject(PooledObject<Object> pooledObject) throws Exception {
        ObjectFactory factory = new ObjectFactory();
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(1);
        GenericObjectPool<Object> objectPool = new GenericObjectPool<>(factory, poolConfig);

    }
}