package ipvc.estg.appcm

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface EndPoints {
    @FormUrlEncoded
    @POST("/myslim/api/users_login")
    fun loginPage(@Field("name") name: String?, @Field("password") password: String?): Call<OutputLoginPage>

    @FormUrlEncoded
    @POST("/myslim/api/notes")
    fun reportar(@Field("title") title: String?,
                 @Field("latitude") latitude: String?,
                 @Field("longitude") longitude: String?,
                 @Field("description") description: String?,
                 @Field("category") category: String?,
                 @Field("photo") photo: String?,
                 @Field("user_id") user_id: Int?): Call<OutputCreateNote>

    @FormUrlEncoded
    @POST("/myslim/api/notes/{id}")
    fun editar(@Field("title") title: String?,
               @Field("latitude") latitude: String?,
               @Field("longitude") longitude: String?,
               @Field("description") description: String?,
               @Field("category") category: String?,
               @Field("photo") photo: String?,
               @Field("user_id") user_id: Int?): Call<OutputEditNote>


    @POST("/myslim/api/notes/{id}")
    fun apagar(@Path("id") id: String?): Call<OutputDeleteNote>
}