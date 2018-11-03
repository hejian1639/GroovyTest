class Student implements GroovyInterceptable {
    protected dynamicProps=[:]

    void setProperty(String pName,val) {
        dynamicProps[pName] = val
    }

    def getProperty(String pName) {
        dynamicProps[pName]
    }

    def invokeMethod(String name, Object args) {
        System.out.println ("called invokeMethod $name $args")
    }
}