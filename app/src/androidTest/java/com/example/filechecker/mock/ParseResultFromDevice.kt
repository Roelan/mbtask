package com.example.filechecker.mock

import com.example.filechecker.logger.L
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import junit.framework.Assert
import okhttp3.mockwebserver.RecordedRequest

class ParseResultFromDevice(
    private val recordedRequest: RecordedRequest,
    private val rtpData: EventData?
) {

    fun parseRequestFromDevice() {
        L.i("MockTest", "========== Request from device ===========================")
        L.i("MockTest", "Path: ${recordedRequest.path}")
        L.i("MockTest", "Body: ${recordedRequest.body.readUtf8()}")
    }

    /**
     * Pars result from device
     */
    fun parseResultFromDevice() {
        L.i("MockTest", "========== Request from device: RESULT  =================")
        val resultJson: String = recordedRequest.body.readUtf8()
        var parsedJson: JsonObject = JsonParser.parseString(resultJson).asJsonObject
        val data: String = parsedJson.get("data").asString
        val type: String = parsedJson.get("type").asString
        parsedJson = JsonParser.parseString(data).asJsonObject
        L.i("MockTest", "Type: $type")

        when (type) {
            AGENT_INFO_TYPE -> {
                val hostName = parsedJson.get("host_name").asString
                val fullyQualifiedHostName = parsedJson.get("fully_qualified_host_name").asString
                val osInfo: JsonObject = parsedJson.get("os_info") as JsonObject

                val osPlatform: String = osInfo.get("os_platform").asString
                val osVersion: String = osInfo.get("os_version").asString
                L.i("MockTest", "Agent information body:\n" +
                        "Host name: $hostName\n" +
                        "Fully qualified host name: $fullyQualifiedHostName\n" +
                        "OS Platform: $osPlatform\n" +
                        "OS Version: $osVersion")
            }
            RTP_STREAM_EVENT_TYPE -> {
                val name = parsedJson.get("threat_name").asString
                val status = parsedJson.get("status").asString
                val type = parsedJson.get("type").asString
                L.i("MockTest", "RTP stream event body:\n" +
                        "Threat name: $name\n" +
                        "Threat status: $status\n" +
                        "Threat type: $type")

                Assert.assertEquals(rtpData?.dataName, name)
                Assert.assertEquals(rtpData?.dataStatus, status)
                Assert.assertEquals(rtpData?.dataType, type)
            }
            BOOMERANG_EVENT_TYPE -> {
                val errorCode = parsedJson.get("error_code").asString
                val eventName = parsedJson.get("event_name").asString
                L.i("MockTest", "Boomerang event body:\n" +
                        "error_code: $errorCode\n" +
                        "event_name: $eventName")

            }
            THREAT_SCAN_RESULT_TYPE -> {
                val sourceDetails: JsonObject = parsedJson.get("metadata").asJsonObject.get("sourceDetails") as JsonObject

                val scanResult: String = sourceDetails.get("scanResult").asString
                val type: String = sourceDetails.get("type").asString
                val scanType = sourceDetails.get("scanOptions").asJsonObject.get("scanType").asString
                L.i("MockTest", "Threat scan body:\n" +
                        "scanResult: $scanResult\n" +
                        "type: $type\n" +
                        "scanType: $scanType")
            }
            ASSET_INFORMATION_TYPE -> {

                val osInfo: JsonObject = parsedJson.get("os_info") as JsonObject
                val computerInfo: JsonObject = parsedJson.get("computer_info") as JsonObject

                val hostName: String = parsedJson.get("host_name").asString
                val osPlatform: String = osInfo.get("os_platform").asString
                val osVersion: String = osInfo.get("os_version").asString
                val manufacturer: String = computerInfo.get("manufacturer").asString
                val model: String = computerInfo.get("model").asString

                L.i("MockTest", "Asset information body:\n" +
                        "Host Name: $hostName\n" +
                        "OS Platform: $osPlatform\n" +
                        "Manufacturer: $manufacturer\n" +
                        "Model: $model\n" +
                        "OS Version: $osVersion")
            }
            else -> {
                L.i("MockTest", "========== Unknown result type =====================")
            }
        }
    }

    companion object {
        const val AGENT_INFO_TYPE = "AGENT_INFORMATION"
        const val RTP_STREAM_EVENT_TYPE = "RTP_STREAM_EVENT"
        const val BOOMERANG_EVENT_TYPE = "BOOMERANG_EVENT"
        const val THREAT_SCAN_RESULT_TYPE = "THREAT_SCAN_RESULT"
        const val ASSET_INFORMATION_TYPE = "ASSET_INFORMATION"
    }
}