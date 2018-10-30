EmailDsl.make {
    to "Nirav Assar"
    from "Barack Obama"
    body "How are things? We are doing well. Take care"
}

MemoDsl.make {
    to "Nirav Assar"
    from "Barack Obama"
    body "How are things? We are doing well. Take care"
    idea "The economy is key"
    request "Please vote for me"
    xml
}

email {
    from 'dsl-guru@mycompany.com'
    to 'john.doe@waitaminute.com'
    subject 'The pope has resigned!'
    body {
        p 'Really, the pope has resigned!'
    }


}

def email(Closure cl) {
    def email = new EmailSpec()
    cl.delegate=email
    cl()
//    def code = cl.rehydrate(email, this, this)
//    code.resolveStrategy = Closure.DELEGATE_ONLY
//    code()
}

//def methodMissing(String methodName, args) {
//}
