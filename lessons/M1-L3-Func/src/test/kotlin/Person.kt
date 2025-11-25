class Person (val name: String, var location: String ="", var age: Int =0) {

    override fun toString(): String {
        return "Person(name=${name}, age=${age}, city=${location})"
    }

}

