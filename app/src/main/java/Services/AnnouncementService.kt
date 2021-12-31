package Services


import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AnnouncementService {
    @GET("dict/userdict/group/tool")
    fun getRecitedGroup(@Query("uid") uid:String,@Query("token") token:String): Call<ResponseBody>

    @GET("dict/userdict/group")
    fun getSingleCard(@Query("uid") uid:String,@Query("group") group:String,@Query("token") token:String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("sign/info")
    fun sendInformationUpdate(@Field("email") email:String,
                              @Field("password") password:String
    ): Call<ResponseBody>

    @PUT("dict/userdict/group/tool")
    fun getNewGroup(@Query("uid") uid:String,@Query("token") token:String): Call<ResponseBody>

    @PUT("sign/info")
    fun getCheck(@Query("email") email:String,@Query("vcode") vcode:String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("log")
    fun login(@Field("email") email:String,
                              @Field("password") password:String
    ): Call<ResponseBody>

//@Multipart
//    @FormUrlEncoded
    @POST("dict/userdict/group")
    fun renew(@Query("uid") uid:String,
              @Query("token") token:String,
              @Body body: RequestBody
    ): Call<ResponseBody>



    @PATCH("user/info")
    fun modifyName(@Query("uid") uid:String,
                   @Query("token") token:String,
                   @Query("nickname") nickname:String
    ): Call<ResponseBody>
    @PATCH("user/info")
    fun modifyCountry(@Query("uid") uid:String,
                   @Query("token") token:String,
                   @Query("country") country:String
    ): Call<ResponseBody>
    @PATCH("user/info")
    fun modifyPassword(@Query("uid") uid:String,
                   @Query("token") token:String,
                   @Query("password") pwd:String
    ): Call<ResponseBody>

    @GET("user/profile")
    fun getUserInfo(@Query("uid") uid:String,@Query("token") token:String): Call<ResponseBody>

    @PATCH("sign/info")
    fun createuserinfo(@Query("uid") uid:String,
                       @Query("password") pwd:String,
                       @Query("nickname") nickname:String,
                       @Query("country") country:String
    ): Call<ResponseBody>


}