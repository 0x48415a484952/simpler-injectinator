package com.ox48415a484952;

public class TestIfTenarWorks {
    private Logger logger;

    @Teyne(InjectionType.SINGLETON)
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void log(String toBeLogged) {
        this.logger.log(toBeLogged);
    }
}
