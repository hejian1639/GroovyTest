
class Example {
    static void main(String[] args) {
        // Using a simple println statement to print output to the console
        println('Hello World');

        def clos = {println "Hello World"};
        clos.call();

        EmailDsl.make {
            to "Nirav Assar"
            from "Barack Obama"
            body "How are things? We are doing well. Take care"
        }


    }
}