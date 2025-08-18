package com.ocbg.xyz.apps.data.service

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * XXX接口
 * User: Tom
 * Date: 2025/2/24 16:17
 */
interface XXXService {

    /**
     * 从远程获取数据
     * @param id
     * @return 获取数据
     */
    @GET("xxx/yyy/getData.do")
    suspend fun fetchData(
        @Query("id") id: String
    ): Result<String>

}