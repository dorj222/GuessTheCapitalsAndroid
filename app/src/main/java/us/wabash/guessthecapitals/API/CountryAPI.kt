package us.wabash.guessthecapitals.API



import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import us.wabash.guessthecapitals.data.countryDataItem

// host: https://api.exchangeratesapi.io
// path: /latest
// query params: ? base=USD

interface CountryAPI {
    @GET("rest/v2/all")
    fun getData() : Call<List<countryDataItem>>
}
