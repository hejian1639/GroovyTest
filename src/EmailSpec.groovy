class EmailSpec {

    class BodySpec {
        void p(String p) {
            println "Body: $p"
        }
    }

    BodySpec bodySpec

    void from(String from) { println "From: $from" }

    void to(String... to) { println "To: $to" }

    void subject(String subject) { println "Subject: $subject" }

    void body(Closure code) {
        bodySpec = new BodySpec()
        code.delegate = bodySpec
//        def code = body.rehydrate(bodySpec, this, this)
//        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }

}

