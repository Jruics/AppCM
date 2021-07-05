package ipvc.estg.appcm

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
    @FormUrlEncoded
    @POST("/myslim/api/users_login")
    fun loginPage(@Field("name") name: String?, @Field("password") password: String?): Call<OutputLoginPage>

    @FormUrlEncoded
    @POST("/myslim/api/notes")
    fun create(@Field("title") title: String?,
                 @Field("latitude") latitude: String?,
                 @Field("longitude") longitude: String?,
                 @Field("description") description: String?,
                 @Field("category") category: String?,
                 @Field("photo") photo: String?,
                 @Field("user_id") user_id: Int?): Call<OutputCreateNote>

    @FormUrlEncoded
    @POST("/myslim/api/notes_edit/{id}")
    fun editBugged(@Path("id") id: String?,
               @Field("title") title: String?,
               @Field("latitude") latitude: String?,
               @Field("longitude") longitude: String?,
               @Field("description") description: String?,
               @Field("category") category: String?,
               @Field("photo") photo: String?,
               @Field("user_id") user_id: Int?): Call<OutputEditNote>

    @FormUrlEncoded
    @POST("/myslim/api/notes_edit_test/{id}")
    fun edit(@Path("id") id: String?,
             @Field("title") title: String?,
             @Field("latitude") latitude: String?,
             @Field("longitude") longitude: String?,
             @Field("description") description: String?,
             @Field("category") category: String?,
             @Field("photo") photo: String?,
             @Field("user_id") user_id: Int?): Call<OutputEditNote>


    @POST("/myslim/api/notes_delete/{id}")
    fun delete(@Path("id") id: String?): Call<OutputDeleteNote>

    @GET("/myslim/api/notes")
    fun getNotes(): Call<List<Note>>

    @GET ("/myslim/api/notes/{id}")
    fun getNotesById(@Path("id") id: String?): Call<List<Note>>
}