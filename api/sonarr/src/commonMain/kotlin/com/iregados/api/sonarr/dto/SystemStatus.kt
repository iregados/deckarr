package com.iregados.api.sonarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class SystemStatus(
    val appName: String?,
    val instanceName: String?,
    val version: String?,
    val buildTime: String?,
    val isDebug: Boolean?,
    val isProduction: Boolean?,
    val isAdmin: Boolean?,
    val isUserInteractive: Boolean?,
    val startupPath: String?,
    val appData: String?,
    val osName: String?,
    val osVersion: String?,
    val isNetCore: Boolean?,
    val isLinux: Boolean?,
    val isOsx: Boolean?,
    val isWindows: Boolean?,
    val isDocker: Boolean?,
    val mode: String?,
    val branch: String?,
    val databaseType: String?,
    val databaseVersion: String?,
    val authentication: String?,
    val migrationVersion: Long?,
    val urlBase: String?,
    val runtimeVersion: String?,
    val runtimeName: String?,
    val startTime: String?,
    val packageVersion: String?,
    val packageAuthor: String?,
)
