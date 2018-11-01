import library


EmailDsl.make {
    to "Nirav Assar"
    from "Barack Obama"
    body "How are things? We are doing well. Take care"
}

def m = MemoDsl.make {
    to "Nirav Assar"
    from "Barack Obama"
    body "How are things? We are doing well. Take care"
    idea "The economy is key"
    request "Please vote for me"
    xml
}

m.getXml()

def emailTemplate(Closure cl) {
    def mail = library.email {
        from 'dsl-guru@mycompany.com'
        to 'john.doe@waitaminute.com'
        subject 'The pope has resigned!'


    }

    cl.delegate = mail
    cl()
    mail
}

library.email {

    emailTemplate {
        body {
            p 'Really, the pope has resigned!'
        }

    }

    emailTemplate {
        body {
            p 'b'
        }

    }

//    for (int i = 0; i < 10; ++i) {
//        from 'dsl-guru@mycompany.com'
//    }
//    to 'john.doe@waitaminute.com'
//    subject 'The pope has resigned!'
//    body {
//        p 'Really, the pope has resigned!'
//    }


}




def iamList = [1, 2, 3, 4, 5]  //定义一个List
iamList.each { //调用它的each，这段代码的格式看不懂了吧？each是个函数，圆括号去哪了？
    println it
}

