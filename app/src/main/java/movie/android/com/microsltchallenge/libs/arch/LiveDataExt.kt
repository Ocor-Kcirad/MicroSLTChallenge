package movie.android.com.microsltchallenge.libs.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <A, B, C> LiveData<A>.combine(
    other: LiveData<B>,
    combiner: ((A?, B?) -> C)
): LiveData<C> {
    return MediatorLiveData<C>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if (localLastA != null && localLastB != null) {
                this.value = combiner.invoke(localLastA, localLastB)
            }
        }

        addSource(this@combine) {
            lastA = it
            update()
        }
        addSource(other) {
            lastB = it
            update()
        }
    }
}
