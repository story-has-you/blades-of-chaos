package com.storyhasyou.kratos.idworker;

/**
 * @author fangxi
 * @date 2020/3/5
 */
public class DefaultIdentifierGenerator implements IdentifierGenerator {

    private final Sequence sequence;

    public DefaultIdentifierGenerator() {
        this.sequence = new Sequence();
    }

    public DefaultIdentifierGenerator(long workerId, long dataCenterId) {
        this.sequence = new Sequence(workerId, dataCenterId);
    }

    public DefaultIdentifierGenerator(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public Long nextId(Object entity) {
        return sequence.nextId();
    }
}