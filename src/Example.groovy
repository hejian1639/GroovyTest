class Example {
    static void main(String[] args) {
        // Using a simple println statement to print output to the console
        println('Hello World')

        def example = new Example()
        def clos = { println  "Hello World" }
        clos=clos.rehydrate(example,example,this)
//        clos.delegate = example
//        clos.owner = example
//        clos.resolveStrategy = Closure.DELEGATE_ONLY
        clos()

        EmailDsl.make {
            to "Nirav Assar"
            from "Barack Obama"
            body "How are things? We are doing well. Take care"
        }


        Student mst = new Student()
        mst.Name = "Joe"
        mst.ID = 1

        println(mst.Name)
        println(mst.ID)
        mst.AddMarks()
    }


    void println(String s) {
        System.out.println("Example print: " + s)
    }
}