package com.iregados.deckarr.api.common

sealed class ApiError(val message: String) {
    class Network(message: String) : ApiError(message)
    class Unauthorized(message: String = "Unauthorized") : ApiError(message)
    class NotFound(message: String = "Not found") : ApiError(message)
    class Server(val code: Int, message: String) : ApiError(message)
    class Serialization(message: String) : ApiError(message)
    class Unknown(message: String) : ApiError(message)
}