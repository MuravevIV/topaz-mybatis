package com.ilyamur.topaz.mybatis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ilyamur.topaz.mybatis.entity.TransactionToken;
import com.ilyamur.topaz.mybatis.mapper.TransactionTokenMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@ActiveProfiles(ApplicationProfile.DEV)
public class TransactionTokenTest {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    PlatformTransactionManager txManager;

    private TransactionTokenMapper mapper;

    @Before
    public void before() {
        mapper = sqlSessionTemplate.getMapper(TransactionTokenMapper.class);
    }

    private TransactionToken tokenFactory(String tokenPrefix, String transactionPrefix) {
        TransactionToken t = new TransactionToken();
        t.setToken(tokenPrefix + System.currentTimeMillis());
        t.setTransaction(transactionPrefix + System.currentTimeMillis());
        return t;
    }

    @Test
    public void testInsert() {
        TransactionToken t = tokenFactory("alpha", "beta");
        mapper.insert(t);
        assertTrue(t.getId() > -1);

        long count = mapper.count();

        TransactionToken t2 = tokenFactory("cappa", "delta");
        mapper.insert(t2);
        assertTrue(t2.getId() > -1);

        assertEquals(count + 1, mapper.count());
    }

    @Test
    public void testUpdate() {
        TransactionToken t = tokenFactory("faraday", "gamma");
        mapper.insert(t);

        TransactionToken t2 = mapper.getById(t.getId());
        assertEquals(t.getToken(), t2.getToken());
        assertEquals(t.getTransaction(), t2.getTransaction());

        t2.setToken("bingo" + System.currentTimeMillis());
        t2.setTransaction("funky" + System.currentTimeMillis());
        mapper.update(t2);

        TransactionToken t3 = mapper.getById(t.getId());
        assertEquals(t2.getToken(), t3.getToken());
        assertEquals(t2.getTransaction(), t3.getTransaction());
    }

    @Test
    public void testDeleteById() {
        long count = mapper.count();

        TransactionToken t = tokenFactory("indigo", "jakarta");
        mapper.insert(t);
        assertEquals(count + 1, mapper.count());

        mapper.deleteById(t);
        assertEquals(count, mapper.count());
    }

    @Test
    public void testDeleteByLongId() {
        long count = mapper.count();

        TransactionToken t = tokenFactory("indigo", "jakarta");
        mapper.insert(t);
        assertEquals(count + 1, mapper.count());

        mapper.deleteByLongId(t.getId());
        assertEquals(count, mapper.count());
    }

    @Test
    public void testDeleteByTransaction() {
        long count = mapper.count();

        TransactionToken t2 = tokenFactory("kava", "lambda");
        mapper.insert(t2);
        assertEquals(count + 1, mapper.count());

        mapper.deleteByTransaction(t2);
        assertEquals(count, mapper.count());
    }

    @Test
    public void testFindByTransaction() {
        TransactionToken t = tokenFactory("manual", "nova");
        mapper.insert(t);
        assertTrue(t.getId() >= 0);

        TransactionToken t2 = mapper.selectByTransaction(t.getTransaction());
        assertEquals(t.getToken(), t2.getToken());
        assertEquals(t.getTransaction(), t2.getTransaction());
    }

    @Test
    public void testRollback() {
        DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
        txDef.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        txDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDef);

        long count = mapper.count();

        TransactionToken t = tokenFactory("omega", "passport");
        mapper.insert(t);
        assertEquals(count + 1, mapper.count());

        txManager.rollback(txStatus);
        assertEquals(count, mapper.count());

        TransactionToken t3 = tokenFactory("quark", "star");
        mapper.insert(t3);
        assertEquals(count + 1, mapper.count());
    }
}
