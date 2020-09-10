package com.storyhasyou.kratos.idworker;

/**
 * The type Default identifier generator.
 *
 * @author fangxi
 * @date 2020 /3/5
 */
public class DefaultIdentifierGenerator implements IdentifierGenerator {

    private final Sequence sequence;

    /**
     * Instantiates a new Default identifier generator.
     */
    public DefaultIdentifierGenerator() {
        this.sequence = new Sequence();
    }

    /**
     * Instantiates a new Default identifier generator.
     *
     * @param workerId     the worker id
     * @param dataCenterId the data center id
     */
    public DefaultIdentifierGenerator(long workerId, long dataCenterId) {
        this.sequence = new Sequence(workerId, dataCenterId);
    }

    /**
     * Instantiates a new Default identifier generator.
     *
     * @param sequence the sequence
     */
    public DefaultIdentifierGenerator(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public Long nextId(Object entity) {
        return sequence.nextId();
    }
}
