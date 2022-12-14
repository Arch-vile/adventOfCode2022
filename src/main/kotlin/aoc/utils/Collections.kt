package aoc.utils


fun <T> List<T>.splitMiddle(): Pair<List<T>,List<T>> {
    if(size % 2 != 0)
        throw Error("Cannot split middle list of size $size")
   return Pair(subList(0,size/2),subList(size/2,size))
}
fun <E> List<E>.getLooping(index: Int): E {
    if(index < 0) {
        return this[(index+(-1 * index / size + 1)*size)%size]
    } else {
        return this[index % size]
    }
}

/**
 * Ceaser cipher i.e. simple substition mapping
 * A,B  (this list, i.e the plain values)
 * X,Y  (cipher list)
 * listOf("A","B").decode("X", listOf("X","Y")) -> "A"
 */
fun <E,C> List<E>.decode(element: C, cipher: List<C>): E = this[cipher.indexOf(element)]
fun <E,C> List<E>.encode(element: E, cipher: List<C>): C = cipher[this.indexOf(element)]

/**
 * Optimization tool
 * Container storing key/value pairs providing fast value access and ordered by value
 */
class SortedLookup<K, V : Comparable<V>> {
    private val valueLookup = mutableMapOf<K, V>()
    private val sortedByValue =
        sortedSetOf<Pair<K, V>>(Comparator<Pair<K, V>>
        { o1, o2 -> o1.second.compareTo(o2.second) }.thenComparing { o1, o2 -> o1.first.hashCode().compareTo(o2.first.hashCode())  })

    fun add(key: K, value: V) {
        valueLookup[key] = value
        sortedByValue.add(Pair(key, value))
    }

    fun drop(key: K): Pair<K, V> {
        valueLookup.remove(key)
        val toRemove = sortedByValue.first { it.first == key }
        sortedByValue.remove(toRemove)
        return toRemove
    }

    fun containsKey(key: K) = valueLookup.contains(key)

    fun get(key: K) = valueLookup[key]

    fun sortedByValue() = sortedByValue.asSequence()

    fun size() = valueLookup.size


}
