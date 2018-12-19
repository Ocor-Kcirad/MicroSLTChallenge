package movie.android.com.microsltchallenge.libs.socket

import androidx.lifecycle.MutableLiveData
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MovieEventLiveData : MutableLiveData<MovieEvent>() {

    companion object {
        private const val EVENT_NAME = "movies"
        private const val ENDPOINT = "https://api.movies.deveyesin.com"
    }

    //TODO: Catch error and retry
    private val socket: Socket by lazy<Socket> { IO.socket(ENDPOINT) }

    private val gson = Gson()
    private val type = object : TypeToken<MovieEvent>() {}.type

    private val eventListener = Emitter.Listener {
        postValue(gson.fromJson<MovieEvent>(it[0].toString(), type))
    }

    override fun onInactive() {
        super.onInactive()
        socket.disconnect()
        socket.off(EVENT_NAME, eventListener)
    }

    override fun onActive() {
        super.onActive()
        socket.on(EVENT_NAME, eventListener)
        socket.connect()
    }

}