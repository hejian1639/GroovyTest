class LoopTest {
    static forEach(Closure closure) {
        def data = [1, 2, 3, 4]
        data.forEach({ closure(it) })
    }

    static void main(String[] args) {
        forEach({ println(it) })
    }


}