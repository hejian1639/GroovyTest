

static def email(Closure cl) {

    def email = new EmailSpec()
    cl.delegate = email
    cl()
//    def code = cl.rehydrate(email, this, this)
//    code.resolveStrategy = Closure.DELEGATE_ONLY
//    code()
}

