package com.ocbg.xyz.apps.common.network

/**
 * 网络库
 * User: Tom
 * Date: 2024/9/12 11:09
 */
class NetworkRepository {

    private val serviceMap = HashMap<String, Class<Any>>()

    companion object {
        private const val TAG = "网络库"
        const val SUCCESS_CODE = "0"
        private var network: NetworkRepository? = null
        fun getInstance(): NetworkRepository {
            if (network == null) {
                synchronized(NetworkRepository::class.java) {
                    if (network == null) {
                        network = NetworkRepository()
                    }
                }
            }
            return network!!
        }
    }

    fun <T> getApi(serviceClass: Class<T>): T {
        return ServiceCreator.create(serviceClass)

//        val service = serviceMap[serviceClass.name]
//        return if (service == null) {
//            MLog.d(TAG, "service == null 开始新建")
//            val newService = ServiceCreator.create(serviceClass)
//            serviceMap[serviceClass.name] = newService
//            newService
//        } else {
//            MLog.d(TAG, "service != null 复用")
//            service as T
//        }
    }

}
